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
package org.scopemvc.view.servlet.jsp;


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
import org.scopemvc.view.servlet.Page;
import org.scopemvc.view.servlet.ValidationFailure;

/**
 * <P>
 *
 * A concrete {@link org.scopemvc.view.servlet.Page}. ***** Doesn't do the
 * populateModel stuff yet </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.8 $ $Date: 2002/09/05 15:41:48 $
 * @created 05 September 2002
 */
public class JSPPage extends Page {

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

    private static final Log LOG = LogFactory.getLog(JSPPage.class);

    private String path;


    /**
     * @param inViewID unique View ID for routing incoming Controls
     * @param inJSPPath the path to the JSP this View uses
     */
    public JSPPage(String inViewID, String inJSPPath) {
        super(inViewID);
        path = inJSPPath;
        init();
    }


    /**
     * Gets the path
     *
     * @return The path value
     */
    public final String getPath() {
        return path;
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
