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
 * $Id: ModelChangeEvent.java,v 1.6 2002/09/11 19:12:29 ludovicc Exp $
 */
package org.scopemvc.core;


/**
 * <P>
 *
 * Event that is broadcasted to notify interested {@link ModelChangeListener}s
 * of a change in state of a model object implementing {@link
 * ModelChangeEventSource}. It contains:
 * <UL>
 *   <LI> the source of the event (ie the model object that changed), </LI>
 *   <LI> the {@link Selector} identifying the property that changed, </LI>
 *   <LI> and a 'type' of change (see {@link ModelChangeTypes}). </LI>
 * </UL>
 * </P> <P>
 *
 * Note that the event does not contain the actual data that changed, unlike
 * JavaBeans PropertyChangeEvent. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/11 19:12:29 $
 * @see ModelChangeEventSource
 * @see Selector
 * @see ModelChangeListener
 * @see org.scopemvc.model.basic.BasicModel
 */
public interface ModelChangeEvent extends ModelChangeTypes {

    /**
     * Set the type of change this event notifies of.
     *
     * @param inType type of change.
     * @see ModelChangeTypes
     */
    void setType(int inType);


    /**
     * Return the type of change this event notifies.
     *
     * @return type of change.
     * @see ModelChangeTypes
     */
    int getType();


    /**
     * Set the source of this event.
     *
     * @param inModel source Model of this event.
     */
    void setModel(ModelChangeEventSource inModel);


    /**
     * Get the source Model of this event.
     *
     * @return the source Model of this event.
     */
    ModelChangeEventSource getModel();


    /**
     * Set the Selector representing the contents that changed to cause
     * broadcast of this event.
     *
     * @param inSelector Selector for contents that changed.
     */
    void setSelector(Selector inSelector);


    /**
     * Get the Selector for the contents that changed to cause broadcast of this
     * event.
     *
     * @return the selector representing the contents that changed.
     */
    Selector getSelector();
}

