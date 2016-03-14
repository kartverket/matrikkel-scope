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
package org.scopemvc.view.util;


import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.View;
import org.scopemvc.util.Debug;
import org.scopemvc.util.ScopeConfig;

/**
 * <P>
 *
 * Factory for Property Editors/Viewers of different Types (eg Swing, AWT etc)
 * loaded from Scope Config. </P> <P>
 *
 * Format of config is this: <PRE>
 *   PropertyEditor.<viewtype>.<property class>
 *
 * =<editor class> PropertyViewer.<viewtype>.<property class>
 *
 * =<viewer class> </PRE> <br>
 * Defaults are provided in DefaultScopeConfig for java primitive types for the
 * "Swing" viewtype. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.8 $ $Date: 2002/09/25 13:53:10 $
 * @created 05 September 2002
 * @todo Revert to . as a delimiter. The parsing can always be fixed...
 *      (ludovicc)
 */
public final class PropertyEditorFactory {

    /**
     * Viewtype of viewers/editors to use in a Swing UI.
     */
    public static final String SWING = "swing";

    private static final Log LOG = LogFactory.getLog(PropertyEditorFactory.class);

    private static final String EDITOR_PROPERTY_PREFIX = "PropertyEditor.";
    private static final String VIEWER_PROPERTY_PREFIX = "PropertyViewer.";

    private static final char DELIMITER = '-';

    /**
     * Map of editor viewtype against HashMap of property class against editor
     * class.
     */
    private static HashMap propertyEditors;

    /**
     * Map of viewer viewtype against HashMap of property class against viewer
     * class.
     */
    private static HashMap propertyViewers;
    // can't be '.' else parsing fails below

    /**
     * Load the defaults from ScopeConfig.
     */
    static {
        propertyEditors = load(EDITOR_PROPERTY_PREFIX);
        propertyViewers = load(VIEWER_PROPERTY_PREFIX);
    }


    private PropertyEditorFactory() { }


    /**
     * <p>
     *
     * Return a newly created editor for a property class for a certain
     * viewtype. Viewtype is arbitrary and could be for instance Swing or XML or
     * AWT: define the editors in ScopeConfig.</p> <p>
     *
     * If the editor cannot be found for the class, tries to find the editor
     * recursively by going up in the class hierarchy. This means that if your
     * editor can support any type of class, you can register it for the <code>java.lang.Object</code>
     * class. </p>
     *
     * @param inViewType find an editor of this type.
     * @param inPropertyClass The class of the property to edit.
     * @return a newly created editor View or null if nothing appropriate can be
     *      found.
     */
    public static View getPropertyEditor(String inViewType, Class inPropertyClass) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getPropertyEditor: " + inViewType + ", " + inPropertyClass);
        }
        try {
            // Get editors of the right type
            HashMap editorsForViewType = (HashMap) propertyEditors.get(inViewType);
            if (editorsForViewType == null) {
                return null;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("getPropertyEditor: editorsForViewType: " + editorsForViewType);
            }

            // Search for matching property class, climbing superclasses but not interfaces
            Class propertyClass = inPropertyClass;
            while (propertyClass != null) {
                Class editorClass = (Class) editorsForViewType.get(propertyClass.getName());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getPropertyEditor: editorClass: " + editorClass);
                }
                if (editorClass != null) {
                    return (View) editorClass.newInstance();
                }
                propertyClass = propertyClass.getSuperclass();
            }
        } catch (Exception e) {
            LOG.warn("getPropertyEditor: default: " + inPropertyClass + ", " + inViewType, e);
            // ignore can't create instance of the editor
        }
        return null;
    }


    /**
     * <p>
     *
     * Return a newly created viewer a property class for a certain viewtype.
     * Viewtype is arbitrary and could be for instance Swing or XML or AWT:
     * define the viewers in ScopeConfig. </p> <p>
     *
     * If the viewer cannot be found for the class, tries to find the viewer
     * recursively by going up in the class hierarchy. This means that if your
     * viewer can support any type of class, you can register it for the <code>java.lang.Object</code>
     * class. </p>
     *
     * @param inViewType find an viewer of this type.
     * @param inPropertyClass The class of the property
     * @return a newly created viewer View or null if nothing appropriate can be
     *      found.
     */
    public static View getPropertyViewer(String inViewType, Class inPropertyClass) {
        try {
            // Get viewers of the right type
            HashMap viewersForViewType = (HashMap) propertyViewers.get(inViewType);
            if (viewersForViewType == null) {
                return null;
            }

            // Search for matching property class, climbing superclasses but not interfaces
            Class propertyClass = inPropertyClass;
            while (propertyClass != null) {
                Class viewerClass = (Class) viewersForViewType.get(propertyClass.getName());
                if (viewerClass != null) {
                    return (View) viewerClass.newInstance();
                }
                propertyClass = propertyClass.getSuperclass();
            }
        } catch (Exception e) {
            LOG.warn("getPropertyViewer: default: " + inPropertyClass + ", " + inViewType, e);
            // ignore can't create instance of the editor
        }
        return null;
    }


    private static HashMap load(String inPrefix) {
        HashMap result = new HashMap();
        for (Iterator iter = ScopeConfig.getKeysMatching(inPrefix); iter.hasNext(); ) {
            String key = (String) iter.next();
            if (key.lastIndexOf(DELIMITER) <= key.indexOf('.') + 1 || key.lastIndexOf(DELIMITER) >= key.length() - 2) {
                LOG.error("Bad " + inPrefix + " configuration: no type or property class: " + key);
            } else {
                String type = key.substring(key.indexOf('.') + 1, key.lastIndexOf(DELIMITER));
                String propertyClass = key.substring(key.lastIndexOf(DELIMITER) + 1);
                Class editorClass = ScopeConfig.getClass(key);
                if (editorClass == null) {
                    LOG.error("Bad class: " + ScopeConfig.getString(key));
                } else {
                    if (result.get(type) == null) {
                        result.put(type, new HashMap());
                    }
                    HashMap editors = (HashMap) result.get(type);
                    if (Debug.ON) {
                        Debug.assertTrue(editors != null, "null editors");
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("load: " + type + ", " + propertyClass + ", " + editorClass);
                    }
                    editors.put(propertyClass, editorClass);
                }
            }
        }
        return result;
    }
}

