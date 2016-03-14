/*
 * Scope: a generic MVC framework.
 * Copyright (c) 2000-2002, The Scope team
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name "Scope" nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * $Id: pretty.settings,v 1.4 2002/09/19 18:10:27 ludovicc Exp $
 */
package org.scopemvc.view.servlet.xml;


import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.IntIndexSelector;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.model.collection.ArrayModel;
import org.scopemvc.model.collection.ListModel;
import org.scopemvc.util.Debug;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

/**
 * <P>
 *
 * Converts a model into an XML document (as SAX events driving a
 * ContentHandler). Uses a {@link PropertyIDGenerator} to create "path"
 * attributes for all elements, and "id" attributes for model elements. </P> <P>
 *
 * Handles circular references using the "ID" and "IDREF" pattern. </P> <P>
 *
 * <PRE>
 * (data id='_root')
 *     (name path='name')Steve(/name)
 *     (pets path='pets')
 *         (element index='0' path='pets.0')
 *             (data id='pets.0')
 *                 (name path='pets.0.name')Trevor(/name)
 *             (/data)
 *         (/element)
 *     (/pets)
 * (/data)
 * </PRE> </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/09/05 15:41:45 $
 * @created 05 September 2002
 */
public class ModelToXML {

    // Some constants for creating the XML
    /**
     * TODO: describe of the Field
     */
    protected static final String ID_ATTRIBUTE = "id";
    /**
     * TODO: describe of the Field
     */
    protected static final String IDREF_ATTRIBUTE = "idref";
    /**
     * TODO: describe of the Field
     */
    protected static final String CDATA_TYPE = "CDATA";
    /**
     * TODO: describe of the Field
     */
    protected static final String PATH_ATTRIBUTE = "id";
    /**
     * TODO: describe of the Field
     */
    protected static final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();

    /**
     * Element to contain contents of a collection model.
     */
    protected static String COLLECTION_ELEMENT = "element";

    /**
     * Attribute to index the contents of a collection model.
     */
    protected static String COLLECTION_INDEX_ATTRIBUTE = "index";

    /**
     * Element to contain a model object.
     */
    protected static String MODEL_ELEMENT = "data";

    private static final Log LOG = LogFactory.getLog(ModelToXML.class);

    // Reuse these Attributes to avoid object creation (initialised in init()).
    /**
     * TODO: describe of the Field
     */
    protected AttributesImpl idAttributes;
    /**
     * TODO: describe of the Field
     */
    protected AttributesImpl idrefAttributes;
    /**
     * TODO: describe of the Field
     */
    protected AttributesImpl pathIndexAttributes;
    /**
     * TODO: describe of the Field
     */
    protected AttributesImpl indexAttributes;
    /**
     * TODO: describe of the Field
     */
    protected AttributesImpl pathAttributes;


    /**
     * Constructor for the ModelToXML object
     */
    public ModelToXML() {
        init();
    }


    /**
     * @param inContentHandler Drive this ContentHandler with the Model's SAX
     *      events.
     * @param inModel Model object to write. null generates no SAX.
     * @param inIDGenerator TODO: Describe the Parameter
     * @throws Exception TODO: Describe the Exception
     */
    public void modelToXML(Object inModel, ContentHandler inContentHandler, PropertyIDGenerator inIDGenerator)
             throws Exception {

        inContentHandler.startDocument();

        IdRefMap idRefMap = new IdRefMap();
        String id = idRefMap.getNextId();
        idAttributes.setValue(0, id);
        idRefMap.storeModel(id, inModel);

        inContentHandler.startElement("", MODEL_ELEMENT, MODEL_ELEMENT, idAttributes);

        propertiesToXML(inModel, inContentHandler, inIDGenerator, idRefMap);

        inContentHandler.endElement("", MODEL_ELEMENT, MODEL_ELEMENT);

        inContentHandler.endDocument();
    }


    /**
     * Initialise the Attributes that are reused during SAX generation.
     */
    protected void init() {
        idAttributes = new AttributesImpl();
        idAttributes.addAttribute("", ID_ATTRIBUTE, ID_ATTRIBUTE, CDATA_TYPE, "");

        idrefAttributes = new AttributesImpl();
        idrefAttributes.addAttribute("", IDREF_ATTRIBUTE, IDREF_ATTRIBUTE, CDATA_TYPE, "");

        pathIndexAttributes = new AttributesImpl();
        pathIndexAttributes.addAttribute("", PATH_ATTRIBUTE, PATH_ATTRIBUTE, CDATA_TYPE, "");
        pathIndexAttributes.addAttribute("", COLLECTION_INDEX_ATTRIBUTE, COLLECTION_INDEX_ATTRIBUTE, CDATA_TYPE, "");

        indexAttributes = new AttributesImpl();
        indexAttributes.addAttribute("", COLLECTION_INDEX_ATTRIBUTE, COLLECTION_INDEX_ATTRIBUTE, CDATA_TYPE, "");

        pathAttributes = new AttributesImpl();
        pathAttributes.addAttribute("", PATH_ATTRIBUTE, PATH_ATTRIBUTE, CDATA_TYPE, "");
    }


    /**
     * TODO: document the method
     *
     * @param inModel TODO: Describe the Parameter
     * @param inContentHandler TODO: Describe the Parameter
     * @param inIDGenerator TODO: Describe the Parameter
     * @param inIdRefMap TODO: Describe the Parameter
     * @throws Exception TODO: Describe the Exception
     */
    protected void propertiesToXML(Object inModel, ContentHandler inContentHandler, PropertyIDGenerator inIDGenerator, IdRefMap inIdRefMap)
             throws Exception {
        if (Debug.ON) {
            Debug.assertTrue(inContentHandler != null, "null ContentHandler");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("propertiesToXML: idGenerator=" + inIDGenerator);
        }

        // Don't serialise null models
        if (inModel == null) {
            return;
        }

        // Get a PropertyManager for the model
        PropertyManager manager = PropertyManager.getInstance(inModel);
        if (Debug.ON) {
            Debug.assertTrue(manager != null, "null manager");
        }

        // Serialise the properties using a SelectorIterator
        Iterator i = manager.getSelectorIterator(inModel);
        if (Debug.ON) {
            Debug.assertTrue(i != null, "null Iterator");
        }
        while (i.hasNext()) {

            try {
                // selector
                Object o = i.next();
                if (Debug.ON) {
                    Debug.assertTrue(o instanceof Selector, "not a Selector: " + o);
                }
                Selector selector = (Selector) o;
                Object property = manager.get(inModel, selector);

                // Don't serialise null properties
                if (property == null) {
                    continue;
                }

                // Don't serialise the array property of an ArrayModel ***** feels like a hack
                if (inModel instanceof ArrayModel && ArrayModel.ARRAY.equals(selector)) {
                    continue;
                }

                // Don't serialise the list property of a ListModel ***** feels like a hack
                if (inModel instanceof ListModel && ListModel.LIST.equals(selector)) {
                    continue;
                }

                StringConvertor convertor = StringConvertors.forClass(property.getClass());
                inIDGenerator.startProperty(selector);
                String path = inIDGenerator.getPropertyID();

                // If selector is an IntIndexSelector then write the
                // ... property like: <element index='0' path='xxx'>
                // ... else write it like: <{selectorName} path='xxx'>
                if (selector instanceof IntIndexSelector) {
                    if (path != null) {
                        pathIndexAttributes.setValue(0, path);
                        pathIndexAttributes.setValue(1, selector.getName());
                        inContentHandler.startElement("", COLLECTION_ELEMENT, COLLECTION_ELEMENT, pathIndexAttributes);
                    } else {
                        indexAttributes.setValue(0, selector.getName());
                        inContentHandler.startElement("", COLLECTION_ELEMENT, COLLECTION_ELEMENT, indexAttributes);
                    }
                } else {
                    if (path != null) {
                        pathAttributes.setValue(0, path);
                        inContentHandler.startElement("", selector.getName(), selector.getName(), pathAttributes);
                    } else {
                        inContentHandler.startElement("", selector.getName(), selector.getName(), EMPTY_ATTRIBUTES);
                    }
                }

                // value, using StringConvertor
                if (convertor != null) {
                    String value = convertor.valueAsString(property);
                    if (Debug.ON) {
                        Debug.assertTrue(value != null);
                    }
                    char[] valueElement = value.toCharArray();
                    inContentHandler.characters(valueElement, 0, valueElement.length);
                } else {
                    // A Model so check if already serialised (look in the IdRefMap)
                    String previousId = inIdRefMap.findIdFor(property);
                    if (previousId != null) {
                        idrefAttributes.setValue(0, previousId);
                        inContentHandler.startElement("", MODEL_ELEMENT, MODEL_ELEMENT, idrefAttributes);
                    } else {
                        // ... if not previously serialised then do it here
                        String id = inIdRefMap.getNextId();
                        idAttributes.setValue(0, id);
                        inContentHandler.startElement("", MODEL_ELEMENT, MODEL_ELEMENT, idAttributes);
                        inIdRefMap.storeModel(id, property);
                        propertiesToXML(property, inContentHandler, inIDGenerator, inIdRefMap);
                    }
                    inContentHandler.endElement("", MODEL_ELEMENT, MODEL_ELEMENT);
                }

                // end of property
                if (selector instanceof IntIndexSelector) {
                    inContentHandler.endElement("", COLLECTION_ELEMENT, COLLECTION_ELEMENT);
                } else {
                    inContentHandler.endElement("", selector.getName(), selector.getName());
                }

                inIDGenerator.endProperty();

            } catch (Exception e) {
                // ignore: may not be able to get write-only properties
            }
        }
    }
}

class IdRefMap {

    private long nextId = 0;
    private HashMap models = new HashMap();

    /**
     * Gets the next id
     *
     * @return The nextId value
     */
    String getNextId() {
        String result = "_" + Long.toString(nextId);
        nextId++;
        return result;
    }

    /**
     * TODO: document the method
     *
     * @param inModel TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    String findIdFor(Object inModel) {
        return (String) models.get(inModel);
    }

    /**
     * TODO: document the method
     *
     * @param inId TODO: Describe the Parameter
     * @param inModel TODO: Describe the Parameter
     */
    void storeModel(String inId, Object inModel) {
        models.put(inModel, inId);
    }
}
