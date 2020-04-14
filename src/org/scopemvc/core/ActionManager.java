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
 * $Id: ActionManager.java,v 1.9 2002/09/16 10:49:17 ludovicc Exp $
 */
package org.scopemvc.core;

/**
 * <P>
 *
 * ActionManager is a {@link ModelManager} that handles the actions (invokable
 * methods) of model objects. The core of Scope doesn't depend on this manager
 * although this abstract class is in the core package for consistency with the
 * PropertyManager. An implementation for JavaBean model objects is provided in
 * {@link org.scopemvc.model.beans.BeansActionManager BeansActionManager}. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.9 $ $Date: 2002/09/16 10:49:17 $
 */
public abstract class ActionManager extends ModelManager {

    private static final String NAME = "ActionManager";


    /**
     * Create an ActionManager for the model class
     *
     * @param inModelClass The class of the model
     * @return The ActionManager instance
     */
    public static ActionManager getInstance(Class inModelClass) {
        return (ActionManager) make(NAME, inModelClass);
    }


    /**
     * Create an ActionManager for the model
     *
     * @param inModel The model
     * @return The ActionManager instance
     */
    public static ActionManager getInstance(Object inModel) {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't create an ActionManager for null");
        }
        return getInstance(inModel.getClass());
    }


    /**
     * Generic interface to perform arbitrary actions on a model object.
     *
     * @param inModel the model object to perform the action.
     * @param inAction the {@link ModelAction} to do.
     * @param inParameters parameters for action.
     * @return the return from the method (wrapped in an object if primitive
     *      type), or null if the method returns void.
     * @throws Exception If the action could not be executed successully
     */
    public abstract Object doAction(Object inModel, ModelAction inAction, Object[] inParameters) throws Exception;


    /**
     * Can this model do the passed {@link ModelAction}?
     *
     * @param inModel The model that may contain the method for executing the
     *      action
     * @param inAction The action
     * @return True if the model contains the method for executing the action
     */
    public abstract boolean canDoAction(Object inModel, ModelAction inAction);
}
