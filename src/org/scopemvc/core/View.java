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
 * A View implements the presentation of a model object to the user and allows
 * interaction with the model object. Implementations can be bound to a model
 * and change the state of the model as the user interacts with the View. If the
 * View implements ModelChangeListener then it can also update in response to
 * changes in the state of bound models that implement ModelChangeEventSource. A
 * View can also issue Controls to its parent Controller in response to user
 * interaction that influences with application logic. </P> <P>
 *
 * A View is bound to a single model object, i.e. it presents some or all of the
 * data in the bound model. </P> <P>
 *
 * If a View needs to show data from multiple models, it will be bound to a
 * parent model that contains those multiple models as accessible properties.
 * Think of this parent container as an implementation <I>view model</I> rather
 * than a business/domain model. </P> <P>
 *
 * A View is created and displayed by a parent Controller the implements
 * application/presentation logic. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/06 16:11:47 $
 * @created 05 August 2002
 * @see Controller
 * @see org.scopemvc.view.swing
 * @see org.scopemvc.view.servlet.xml
 */
public interface View {

    /**
     * Gets the model object bound to this View.
     *
     * @return the model object this View is bound to.
     */
    Object getBoundModel();


    /**
     * Sets the model object bound to this View.
     *
     * @param inModel a model object that this View will bind to to present to
     *      the user.
     */
    void setBoundModel(Object inModel);


    /**
     * Gets the Controller for this View
     *
     * @return the parent (owner) Controller or null if none.
     */
    Controller getController();


    /**
     * Sets the Controller for this View
     *
     * @param inController assign a parent (owner) Controller that this View
     *      will issue Controls to.
     */
    void setController(Controller inController);


    /**
     * Issue a Control to the View's parent (owner) Controller.
     *
     * @param inControl The Control to issue
     */
    void issueControl(Control inControl);
}

