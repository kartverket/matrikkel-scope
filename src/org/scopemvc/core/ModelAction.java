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
 * $Id: ModelAction.java,v 1.6 2002/09/11 14:35:07 ludovicc Exp $
 */
package org.scopemvc.core;

import org.scopemvc.util.Debug;

import java.lang.reflect.Method;

/**
 * <P>
 *
 * Actions on model objects can be invoked via {@link ActionManager}'s API,
 * taking a ModelAction to describe the method to invoke. A ModelAction contains
 * a method name and a Class[] to describe the parameters in the method
 * signature. </P> <P>
 *
 * ModelActions are probably not used by application builders who will more
 * likely call a model object's methods directly. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/11 14:35:07 $
 * @see ActionManager#doAction
 * @see ActionManager#canDoAction
 */
public final class ModelAction {

    private String methodName;

    private Class[] parameterClasses;


    /**
     * Constructor for the ModelAction object
     *
     * @param inMethodName The name of the method on the model
     */
    public ModelAction(String inMethodName) {
        this(inMethodName, (Class) null);
    }


    /**
     * Constructor for the ModelAction object
     *
     * @param inMethod The method on the model
     */
    public ModelAction(Method inMethod) {
        this(inMethod.getName(), inMethod.getParameterTypes());
    }


    /**
     * Constructor for the ModelAction object
     *
     * @param inMethodName The name of the method on the model
     * @param inParameterClass The class of the unique method parameter
     */
    public ModelAction(String inMethodName, Class inParameterClass) {
        if (inMethodName == null) {
            throw new IllegalArgumentException("Can't create a ModelAction with a null method name.");
        }
        methodName = inMethodName;

        if (inParameterClass == null) {
            parameterClasses = new Class[0];
        } else {
            parameterClasses = new Class[]{inParameterClass};
        }
        if (Debug.ON) {
            Debug.assertTrue(parameterClasses != null);
        }
    }


    /**
     * Constructor for the ModelAction object
     *
     * @param inMethodName The name of the method on the model
     * @param inParameterClasses The set of classes for the parameters of the
     *      method
     */
    public ModelAction(String inMethodName, Class[] inParameterClasses) {
        if (inMethodName == null) {
            throw new IllegalArgumentException("Can't create a ModelAction with a null method name.");
        }
        methodName = inMethodName;

        if (inParameterClasses == null) {
            parameterClasses = new Class[0];
        } else {
            parameterClasses = inParameterClasses;
        }
        if (Debug.ON) {
            Debug.assertTrue(parameterClasses != null);
        }
    }


    /**
     * Gets the method name
     *
     * @return The methodName value
     */
    public String getMethodName() {
        return methodName;
    }


    /**
     * Gets the array of <code>Class</code> objects that represent the formal
     * parameter types, in declaration order, of the method.
     *
     * @return The parameter classes to describe the method signature. Never
     *      null: if no parameters returns an empty array.
     */
    public Class[] getParameterClasses() {
        return parameterClasses;
    }


    /**
     * Returns a string representation of this object
     *
     * @return a string representation
     */
    public String toString() {
        return "(ModelAction:" + methodName + "(" + parameterClasses + ")" + ")";
    }
}

