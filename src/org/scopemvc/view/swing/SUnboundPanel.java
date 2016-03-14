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
package org.scopemvc.view.swing;


/**
 * <P>
 *
 * A JPanel implementation of View that is not bound to a model object for use
 * in Swing-based user interfaces. This can be used as a simple View that is
 * never bound to a model and which can contain subviews bound to their own
 * completely independent models (rather than being bound to submodels of a
 * common parent model that is bound to a parent SwingView). {@link
 * org.scopemvc.controller.swing.SwingContext} uses the following methods from
 * the {@link SwingView} base class that can be overridden in subclasses that
 * can be shown as Window-level views:
 * <UL>
 *   <LI> {@link #getTitle}</LI>
 *   <LI> {@link #getDisplayMode}</LI>
 *   <LI> {@link #getCloseControl}</LI>
 *   <LI> {@link #isResizable}</LI>
 * </UL>
 * </P> <P>
 *
 * Attempts to bind a model to this are silently ignored so it can be used as a
 * subview to insulate deeper subviews from being bound to a model from a parent
 * view. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.8 $ $Date: 2002/10/04 23:30:11 $
 * @created 05 September 2002
 */
public class SUnboundPanel extends SwingView {

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return null;
    }


    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public final void setBoundModel(Object inModel) {
        // noop
    }
}
