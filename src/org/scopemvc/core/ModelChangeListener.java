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
 * $Id: ModelChangeListener.java,v 1.6 2002/09/11 19:12:29 ludovicc Exp $
 */
package org.scopemvc.core;


/**
 * <P>
 *
 * A listener to the {@link ModelChangeEvent}s fired by a model object
 * implementing ModelChangeEventSource when it changes state. </P> <P>
 *
 * To use:
 * <UL>
 *   <LI> Implement this interface, responding to the change in Model state in
 *   {@link #modelChanged},</LI>
 *   <LI> Register as a listener to a Model using {@link
 *   ModelChangeEventSource#addModelChangeListener},</LI>
 *   <LI> Deregister as a listener when no longer interested in receiving
 *   notification of changes using {@link
 *   ModelChangeEventSource#removeModelChangeListener}.</LI>
 * </UL>
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/11 19:12:29 $
 * @see ModelChangeEventSource
 * @see ModelChangeEvent
 * @see org.scopemvc.model.basic.BasicModel
 */
public interface ModelChangeListener {

    /**
     * <P>
     *
     * Invoked to notify listeners of a change in the state of a {@link
     * ModelChangeEventSource}. </P>
     *
     * @param inEvent the {@link ModelChangeEvent} representing the change in
     *      state of the ModelChangeEventSource.
     */
    void modelChanged(ModelChangeEvent inEvent);
}
