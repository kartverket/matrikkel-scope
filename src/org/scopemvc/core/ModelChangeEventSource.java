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
 * Implemented by model objects that fire {@link ModelChangeEvent}s when their
 * state changes: an implementation of the Observer Pattern. The Swing
 * components in {@link org.scopemvc.view.swing} are aware to ModelChangeEvents:
 * model objects that implement this interface are able to automatically refresh
 * any {@link View}s bound to them. </P> <P>
 *
 * If a submodel of a model changes, the parent must also fire a
 * ModelChangeEvent to notify of the change in state of one of its properties.
 * For example, if a <CODE>Person</CODE> has a <CODE>Pet</CODE> as a property,
 * then when the Pet changes its age it fires an event to notify of a change to
 * the "age" property. The Person must then also fire an event to notify that
 * the "pet.age" property changed. The Swing components rely on this behaviour.
 * </P> <P>
 *
 * A useful base implementation of ModelChangeEventSource is provided in {@link
 * org.scopemvc.model.basic.BasicModel}, which provides several convenience
 * methods. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/11 19:12:29 $
 * @created 05 August 2002
 */
public interface ModelChangeEventSource extends ModelChangeListener {

    /**
     * Adds a listener for ModelChangeEvents
     *
     * @param inListener The listener to be added
     */
    void addModelChangeListener(ModelChangeListener inListener);


    /**
     * Removes a listener for ModelChangeEvents
     *
     * @param inListener The listener to be removed
     */
    void removeModelChangeListener(ModelChangeListener inListener);


    /**
     * Fire a ModelChangeEvent to all listeners
     *
     * @param inChangeType The type of the change. One of the {@link
     *      ModelChangeTypes} values
     * @param inSelector The Selector for the property of the model affected by
     *      the change
     */
    void fireModelChange(int inChangeType, Selector inSelector);


    /**
     * Implement ModelChangeListener to respond to a ModelChangeEvent from a
     * contained sub-model that needs changes to be propagated up the model
     * hierarchy.
     *
     * @param inEvent The event
     */
    void modelChanged(ModelChangeEvent inEvent);
}
