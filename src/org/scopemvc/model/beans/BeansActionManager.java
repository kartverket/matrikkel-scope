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
 * $Id: BeansActionManager.java,v 1.8 2002/09/12 10:51:03 ludovicc Exp $
 */
package org.scopemvc.model.beans;


import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.ActionManager;
import org.scopemvc.core.ModelAction;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * BeansActionManager is a {@link org.scopemvc.core.ActionManager} that handles
 * the actions (invokable methods) of JavaBean model objects. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.8 $ $Date: 2002/09/12 10:51:03 $
 */
public class BeansActionManager extends ActionManager {

    private static final Log LOG = LogFactory.getLog(BeansActionManager.class);


    /**
     * Utility method to determine if a given parameter class array matches the
     * supplied prototype class array
     *
     * @param inPrototypeClasses the prototype that is used in the comparison
     * @param inParameterClasses these are the parameter classes that must be
     *      validated against the prototype
     * @return whether the parameter classes match the prototype
     */
    protected static boolean matchesPrototype(Class[] inPrototypeClasses, Class[] inParameterClasses) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("matchesPrototype: prototype classes: " + inPrototypeClasses + ", parameter classes: " + inParameterClasses);
            if (inPrototypeClasses != null) {
                LOG.debug("matchesPrototype: prototype classes: " + java.util.Arrays.asList(inPrototypeClasses));
            }
            if (inParameterClasses != null) {
                LOG.debug("matchesPrototype: parameter classes: " + java.util.Arrays.asList(inParameterClasses));
            }
        }

        if (inPrototypeClasses == null || inParameterClasses == null || inPrototypeClasses.length == 0 || inParameterClasses.length == 0) {
            // Equivalent if both are zero length or null
            if (LOG.isDebugEnabled()) {
                LOG.debug("matchesPrototype: nulls or 0 lengths");
            }
            return (inPrototypeClasses == null || inPrototypeClasses.length == 0) && (inParameterClasses == null || inParameterClasses.length == 0);
        } else if (inPrototypeClasses.length == inParameterClasses.length) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("matchesPrototype: lengths: " + inPrototypeClasses.length + ", " + inParameterClasses.length);
            }

            for (int i = 0; i < inPrototypeClasses.length; ++i) {
                if (!matchesItem(inPrototypeClasses[i], inParameterClasses[i])) {
                    return false;
                }
            }
            return true;
        } else {
            // different lengths
            return false;
        }
    }


    /**
     * Utility method to determine if a given class matches another class,
     * taking into account primitive--class mapping and inheritance.
     *
     * @param inPrototypeClass TODO: Describe the Parameter
     * @param inParameterClass TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected static boolean matchesItem(Class inPrototypeClass, Class inParameterClass) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("matchesItem: prototype class: " + inPrototypeClass + ", parameter class: " + inPrototypeClass);
        }

        if (inPrototypeClass.isAssignableFrom(inParameterClass)) {
            return true;
        }

        if (inPrototypeClass.isPrimitive()) {
            if ((inPrototypeClass.equals(int.class) && inParameterClass.equals(Integer.class)) ||
                    (inPrototypeClass.equals(long.class) && inParameterClass.equals(Long.class)) ||
                    (inPrototypeClass.equals(short.class) && inParameterClass.equals(Short.class)) ||
                    (inPrototypeClass.equals(float.class) && inParameterClass.equals(Float.class)) ||
                    (inPrototypeClass.equals(double.class) && inParameterClass.equals(Double.class)) ||
                    (inPrototypeClass.equals(char.class) && inParameterClass.equals(Character.class)) ||
                    (inPrototypeClass.equals(byte.class) && inParameterClass.equals(Byte.class))
                    ) {
                return true;
            }
        }
        return false;
    }


    /**
     * Execute the ModelAction with the given parameters
     *
     * @param inModel The model used for executing the ModelAction
     * @param inAction The action (method) to execute
     * @param inParameters The parameters of the method
     * @return The result of the ModelAction method
     * @throws Exception if the execution of the ModelAction failed for any
     *      reason
     */
    public Object doAction(Object inModel, ModelAction inAction, Object[] inParameters) throws Exception {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't doAction on a null model.");
        }
        if (inAction == null) {
            throw new IllegalArgumentException("Can't doAction with a null action.");
        }

        Method method = getMethod(inModel, inAction);
        if (method == null) {
            throw new IllegalArgumentException("Cannot do action: " + inAction + " on model: " + inModel);
        }

        try {
            return method.invoke(inModel, inParameters);
        } catch (InvocationTargetException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("doAction:" + e);
            }
            if (e.getTargetException() instanceof Exception) {
                throw (Exception) e.getTargetException();
            }
            throw e;
//            throw new ModelException(inModel, "doAction on: " + inModel + ": " + e);
        } catch (Exception e1) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("doAction:" + e1);
            }
            throw e1;
        }
    }


    /**
     * Returns true if the ModelAction can be executed on the model object. <br>
     * If the model implements {@link DynamicInvokable}, then check that the
     * ModelAction can be invoked by calling the {@link
     * DynamicInvokable#isActionInvokable isActionInvokable()} method on the
     * model.
     *
     * @param inModel The model used for executing the ModelAction
     * @param inAction The action (method) to execute
     * @return true if the ModelAction can be executed on the model
     */
    public boolean canDoAction(Object inModel, ModelAction inAction) {
        if (inModel instanceof DynamicInvokable) {
            return ((DynamicInvokable) inModel).isActionInvokable(inAction);
        }
        return (getMethod(inModel, inAction) != null);
    }


    /**
     * Find a Method matching the prototype in the passed ModelAction for the
     * passed model object, using the BeanInfo to introspect the model as a
     * JavaBean.
     *
     * @param inAction find a Method that matches the prototype described in
     *      this ModelAction.
     * @param inModel The model used for executing the ModelAction
     * @return a Method matching the ModelAction for the passed model or null if
     *      no match.
     */
    protected Method getMethod(Object inModel, ModelAction inAction) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getMethod: " + inAction);
        }
        if (inModel == null) {
            throw new IllegalArgumentException("Can't getMethod on a null model.");
        }
        if (inAction == null) {
            throw new IllegalArgumentException("Can't getMethod with a null action.");
        }

        MethodDescriptor[] descriptors = BeanInfos.getBeanInfo(inModel.getClass()).getMethodDescriptors();
        if (Debug.ON) {
            Debug.assertTrue(descriptors != null);
        }

        for (int i = 0; i < descriptors.length; ++i) {
            MethodDescriptor desc = descriptors[i];
            if (Debug.ON) {
                Debug.assertTrue(desc != null);
            }

            if (desc.getName().equals(inAction.getMethodName())) {
                if (matchesPrototype(desc.getMethod().getParameterTypes(), inAction.getParameterClasses())) {
                    return desc.getMethod();
                }
            }
        }
        return null;
    }
}
