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
 * $Id: Controller.java,v 1.6 2002/09/06 16:11:45 ludovicc Exp $
 */
package org.scopemvc.core;


/**
 * <P>
 *
 * Controllers arranged in a hierarchy of chains of command provide the
 * structure of an application's logic, mirroring independent contexts of
 * discrete {@link View}s and their bound model objects. Each context can be
 * treated as an independent component. Application logic invoked by sending
 * {@link Control}s into the chain executes in the context of a Controller that
 * specifically recognises and responds to the Control by its ID. </P> <P>
 *
 * For example, a Controller might manage a search panel (eg CustomerSearchView
 * and bound CustomerSearchModel) that can be embedded within other arbitrary
 * views. The search panel context has certain Controls that are recognised by
 * the Controller which has no knowledge of anything outside the immediate
 * context. Controls that the Controller doesn't explicitly recognise are simply
 * passed to the parent Controller. </P> <P>
 *
 * A Controller responds to {@link Control}s issued to it via the handleControl
 * method from one of two places:
 * <UL>
 *   <LI> {@link View}s issue Controls to indicate that the user has invoked
 *   some application logic by interacting with the user interface, eg by
 *   pressing a Button,</LI>
 *   <LI> Controllers pass Controls to their parent Controller to create a Chain
 *   of Command.</LI>
 * </UL>
 * </P> <P>
 *
 * To implement a Controller see the basic implementation provided in {@link
 * org.scopemvc.controller.basic.BasicController BasicController}</P> </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/06 16:11:45 $
 */
public interface Controller {

    /**
     * Get the parent of this Controller
     *
     * @return the parent Controller of this Controller in the chain of command
     */
    Controller getParent();


    /**
     * Bind a new model to this Controller. <br>
     * The controller will automatically bind the model and the view together.
     *
     * @param inModel the model object bound to the View that this Controller
     *      maintains
     */
    void setModel(Object inModel);


    /**
     * Return the model bound to this Controller
     *
     * @return the model object bound to the View that this Controller maintains
     */
    Object getModel();


    /**
     * Bind a new view to this Controller. <br>
     * The controller will automatically bind the model and the view together.
     *
     * @param inView set the View that this Controller maintains
     */
    void setView(View inView);


    /**
     * Return the View bound to this Controller.
     *
     * @return the View that this Controller maintains
     */
    View getView();


    /**
     * <P>
     *
     * Respond to a {@link Control} (from either a {@link View} or a child
     * Controller) or pass up to the parent Controller in the chain of command
     * if the Control is not recognised in this context. </P>
     *
     * @param inControl the Control to respond to by examination of its ID: see
     *      {@link Control#matchesID}
     * @see org.scopemvc.controller.basic.BasicController#handleControl
     */
    void handleControl(Control inControl);


    /**
     * Find the top-most parent Controller at the head of the chain of command.
     * This is the application Controller that initialises the rest of the
     * Controllers as children to handle specific areas of functionality.
     *
     * @return The topParent value
     */
    Controller getTopParent();
}

