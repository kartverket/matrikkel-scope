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

import org.scopemvc.core.View;

/**
 * A swing element that can be contained in a SwingView and that will share the
 * same bound model. <p>
 *
 * Use this interface when your View object in not a subclass of Component, for
 * example Action; or when the View component doesn't belong to the parent
 * SwingView yet have to share the same model or issue controls to the same
 * controller, for example MenuItem. <p>
 *
 * Don't use this interface when your View object is a component contained (in
 * Swing terms) in the parent SwingView. For example a TextField can be
 * discovered automatically by its parent SPanel and have its bound model set
 * automatically by the parent SPanel.
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.3 $ $Date: 2002/09/19 18:08:33 $
 * @created 18 June 2002
 */

public interface SwingSubView extends View {

    /**
     * Gets the owner of this subview element
     *
     * @return The View owning this subview element
     */
    SwingView getOwner();

    /**
     * Sets the owner of this subview element
     *
     * @param inView The new View owning this subview element
     */
    void setOwner(SwingView inView);

    /**
     * Unset the owner of this subview element
     *
     * @param inView The View that was owning this subview element
     */
    void unsetOwner(SwingView inView);
}
