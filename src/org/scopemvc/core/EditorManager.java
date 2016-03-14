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
package org.scopemvc.core;

/**
 * <P>
 *
 * EditorManager is a {@link ModelManager} that handles the creation of
 * appropriate Viewer and Editor Views for the properties of model objects for a
 * specific view type. An implementation for JavaBean model objects is provided
 * in {@link org.scopemvc.model.beans.BeansEditorManager}. </P> <P>
 *
 * The viewtype passed into this manager is arbitrary and identifies the
 * particular type of View an application is interested in. For example, "swing"
 * or "xml" or "awt". See {@link org.scopemvc.view.util.PropertyEditorFactory}.
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.9 $ $Date: 2002/09/12 10:51:03 $
 * @created 05 August 2002
 */
public abstract class EditorManager extends ModelManager {

    private static final String NAME = "EditorManager";


    /**
     * Create an EditorManager for the model class
     *
     * @param inModelClass The model class
     * @return The EditorManager instance
     */
    public static EditorManager getInstance(Class inModelClass) {
        return (EditorManager) make(NAME, inModelClass);
    }


    /**
     * Create an EditorManager for the model
     *
     * @param inModel The model
     * @return The EditorManager instance
     */
    public static EditorManager getInstance(Object inModel) {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't create an EditorManager for null");
        }
        return getInstance(inModel.getClass());
    }


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
    public abstract View getEditor(String inViewType, Object inModel, Selector inSelector);


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
    public abstract View getViewer(String inViewType, Object inModel, Selector inSelector);
}
