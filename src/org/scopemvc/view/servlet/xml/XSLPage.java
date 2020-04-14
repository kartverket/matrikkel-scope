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
 * $Id: XSLPage.java,v 1.7 2002/09/05 15:41:45 ludovicc Exp $
 */
package org.scopemvc.view.servlet.xml;


import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.util.Debug;
import org.scopemvc.util.ScopeConfig;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;
import org.xml.sax.ContentHandler;
import org.scopemvc.view.servlet.ValidationFailure;

/**
 * <P>
 *
 * A concrete {@link AbstractXSLPage} that uses Scope's ModelManager
 * implementations to serialise its entire bound model object to an XML
 * document. A better strategy would be to use a more intelligent view that
 * selectively serialises relevant parts of the model object. </P> <P>
 *
 * Handles circular references using the "ID" and "IDREF" pattern. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.7 $ $Date: 2002/09/05 15:41:45 $
 */
public class XSLPage extends AbstractXSLPage {

    /**
     * The prefix on form parameters to identify a property/value pair used to
     * repopulate the View's bound model. A property/value pair looks like:
     * <CODE>
     * &lt;PROPERTY_ID_PREFIX&gt;ViewID&lt;VIEW_ID_SUFFIX&gt;SelectorDescription
     * </CODE>
     *
     * @see #populateModel
     */
    static char PROPERTY_ID_PREFIX;

    private final static Log LOG = LogFactory.getLog(XSLPage.class);

    /**
     * Optional setting for whether the SAX convertor should write all the
     * property "paths" out when this view is streamed. Some views might want
     * this if they send back form parameters like <CODE>{path}=newValue</CODE>
     * for automatic repopulation back into the bound model object.
     */
    protected boolean requiresPropertyDescriptions;

    /**
     * TODO: describe of the Field
     */
    protected ModelToXML xmlGenerator = new ModelToXML();


    /**
     * @param inViewID unique View ID for routing incoming Controls
     * @param inXslURI the XSLT this View uses to transform its model objects
     *      after they convert to XML
     */
    public XSLPage(String inViewID, String inXslURI) {
        this(inViewID, inXslURI, false);
    }


    /**
     * @param inViewID unique View ID for routing incoming Controls
     * @param inXslURI the XSLT this View uses to transform its model objects
     *      after they convert to XML
     * @param inRequiresModelIds Does this view need the SAX convertor to write
     *      out property description attributes for properties? For repopulation
     *      back into the bound model via {@link #populateModel}
     */
    public XSLPage(String inViewID, String inXslURI, boolean inRequiresModelIds) {
        super(inViewID, inXslURI);
        requiresPropertyDescriptions = inRequiresModelIds;
        init();
    }


    /**
     * <P>
     *
     * Interprets any form parameters like <CODE>
     * &lt;PROPERTY_ID_PREFIX&gt;SelectorDescription
     * </CODE> as [property_description, property_value] pairs </P> <P>
     *
     * Extracts the property_description from the form parameter key, then
     * populates its model object using the String value. Any parameters treated
     * this way are removed from the HashMap. </P>
     *
     * @param ioParameters form parameters to parse for [property_description,
     *      property_value] pairs, removing any processed pairs from the
     *      parameters before return
     * @return PopulateModelFailedException that will be handled as a validation
     *      failure.
     */
    public List populateModel(HashMap ioParameters) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("populateModel: " + ioParameters);
        }

        List errors = new LinkedList();
        // collect ValidationFailures in here
        List toRemove = new LinkedList();
        for (Iterator i = ioParameters.keySet().iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (Debug.ON) {
                Debug.assertTrue(o instanceof String);
            }
            String parameterKey = (String) o;
            if (LOG.isDebugEnabled()) {
                LOG.debug("populateModel: " + parameterKey);
            }

            // Recognise prefixed form parameters for model population
            if (parameterKey.charAt(0) == PROPERTY_ID_PREFIX) {

                // get the form parameter value and remove from the parameter list
                o = ioParameters.get(parameterKey);
                // ... if multiple values take the first
                if (o instanceof Object[]) {
                    if (Debug.ON) {
                        Debug.assertTrue(((Object[]) o).length > 0);
                    }
                    o = ((Object[]) o)[0];
                }
                if (Debug.ON) {
                    Debug.assertTrue(o instanceof String);
                }
                String stringValue = (String) o;

                // Mark it for removal (avoid concurrency problem with Iterator)
                toRemove.add(o);

                // find the property description
                String propertyDescription = parameterKey.substring(1);
                try {
                    populateBoundModelProperty(propertyDescription, stringValue);
                } catch (Exception e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("populateModel: got an exception: " + e);
                    }
                    errors.add(new ValidationFailure(propertyDescription, stringValue, e));
                }
            }
        }

        for (Iterator i = toRemove.iterator(); i.hasNext(); ) {
            ioParameters.remove(i.next());
        }

        if (errors.size() == 0) {
            errors = null;
        }
        return errors;
    }


    /**
     * Do it like this so that we can pick up application-specific
     * ScopeConfig... static initializers would happen before user got a chance
     * to setup the custom config properties.
     */
    protected void init() {
        PROPERTY_ID_PREFIX = ScopeConfig.getChar("ServletFormParameter.propertyIDPrefix");
        if (PROPERTY_ID_PREFIX == 0) {
            LOG.fatal("No propertyIDPrefix in config.");
        }
    }


    /**
     * @param inContentHandler Drive this ContentHandler with the Model's SAX
     *      events.
     * @throws Exception TODO: Describe the Exception
     */
    protected void generateXMLDocument(ContentHandler inContentHandler)
             throws Exception {
        if (requiresPropertyDescriptions) {
            xmlGenerator.modelToXML(getBoundModel(), inContentHandler,
                    new FullIDGenerator());
        } else {
            xmlGenerator.modelToXML(getBoundModel(), inContentHandler,
                    new NoIDGenerator());
        }
    }


    /**
     * Use the property_description, property_value pair passed to set a
     * property in the bound model to a new value. Use StringConvertor if
     * available to convert from String to the property's native datatype.
     *
     * @param inPropertyDescription TODO: Describe the Parameter
     * @param inValue TODO: Describe the Parameter
     * @throws Exception on any failure
     */
    protected void populateBoundModelProperty(String inPropertyDescription, String inValue)
             throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("populateBoundModelProperty: " + inPropertyDescription + ", " + inValue);
        }

        if (getBoundModel() == null) {
            LOG.error("No bound model for: " + this);
            return;
        }

        // Get the PropertyManager for the bound model
        Object model = getBoundModel();
        PropertyManager manager = PropertyManager.getInstance(model);
        if (Debug.ON) {
            Debug.assertTrue(manager != null, "null manager");
        }

        // Get the Selector from the property description
        Selector selector = Selector.fromString(inPropertyDescription);

        // Use StringConvertors if available, else just set(Selector, String)
        StringConvertor convertor = StringConvertors.forClass(manager.getPropertyClass(model, selector));
        if (convertor != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("populateProperty: got " + convertor.getClass());
            }
            Object value = convertor.stringAsValue(inValue);
            manager.set(model, selector, value);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("populateProperty: no StringConvertor ");
            }
            manager.set(model, selector, inValue);
        }
    }
}

final class NoIDGenerator extends PropertyIDGenerator {

    /**
     * Gets the property ID
     *
     * @return The propertyID value
     */
    public String getPropertyID() {
        return null;
    }
}

final class FullIDGenerator extends PropertyIDGenerator {

    /**
     * Gets the property ID
     *
     * @return The propertyID value
     */
    public String getPropertyID() {
        if (currentPropertySelector == null) {
            return null;
        }
        return XSLPage.PROPERTY_ID_PREFIX
                + Selector.asString(currentPropertySelector);
    }
}
