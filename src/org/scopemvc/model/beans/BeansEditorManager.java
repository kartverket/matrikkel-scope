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
package org.scopemvc.model.beans;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.EditorManager;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.core.View;
import org.scopemvc.view.util.PropertyEditorFactory;

/**
 * <P>
 *
 * BeansEditorManager is a {@link org.scopemvc.core.EditorManager} that handles
 * creation of editors and viewer for properties of JavaBean model objects. </P>
 * <P>
 *
 * Currently this ignores BeanInfo and uses the defaults in {@link
 * org.scopemvc.view.util.PropertyEditorFactory} since the BeanInfo API has no
 * way to distinguish different View implementations and doesn't have a getter
 * for a viewer, just an editor. We could handle this a little better by
 * type-checking the BeanInfo property editor for instanceof JComponent (and
 * View) to see if it has a Swing impl, however that doesn't really work for
 * arbitrary view types. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/09/12 10:51:03 $
 * @created 05 September 2002
 */
public class BeansEditorManager extends EditorManager {

    private static final Log LOG = LogFactory.getLog(BeansEditorManager.class);


    /**
     * Get the editor for the view type and the property in the model.
     *
     * @param inViewType type of view this editor is needed for. Defaults are
     *      provided for "swing" in DefaultScopeConfig that the
     *      BeansEditorManager uses.
     * @param inModel model for whose property the editor is required.
     * @param inSelector identify the property that the editor is needed for.
     * @return a View used to edit the passed model's property for the passed
     *      view type.
     */
    public View getEditor(String inViewType, Object inModel, Selector inSelector) {
        try {
            Class propertyClass = PropertyManager.getInstance(inModel).getPropertyClass(inModel, inSelector);
            return PropertyEditorFactory.getPropertyEditor(inViewType, propertyClass);
        } catch (Exception e) {
            LOG.debug("getEditor", e);
        }
        return null;
//         PropertyDescriptor propertyDescriptor = BeansPropertyManager.getPropertyDescriptor(model, propertyName);
//         Class editorClass = propertyDescriptor.getPropertyEditorClass();
//         Object editor;
//         if (editorClass == null) {
//             editor = PropertyEditorManager.findEditor();
//         } else {
//             try {
//                 editor = editorClass.newInstance();
//             } catch(Exception e) {
//                 LOG.fatal("Can't create: " + editorClass, e);
//                 editor = null;
//             }
//         }
//         if (Debug.ON) Debug.assert(editor instanceof View, "editor not a View: " + editor);
//         return (View)editor;
    }


    /**
     * Get the viewer for the view type and the property in the model.
     *
     * @param inViewType type of view this viewer is needed for. Defaults are
     *      provided for "swing" in DefaultScopeConfig that the
     *      BeansEditorManager uses.
     * @param inModel model for whose property the viewer is required.
     * @param inSelector identify the property that the viewer is needed for.
     * @return a View used to view the passed model's property for the passed
     *      view type.
     */
    public View getViewer(String inViewType, Object inModel, Selector inSelector) {
        try {
            Class propertyClass = PropertyManager.getInstance(inModel).getPropertyClass(inModel, inSelector);
            return PropertyEditorFactory.getPropertyViewer(inViewType, propertyClass);
        } catch (Exception e) {
            LOG.warn("getViewer", e);
        }
        return null;
    }
}

