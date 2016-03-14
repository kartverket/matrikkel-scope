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
package org.scopemvc.model.beans;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.IntIndexSelector;
import org.scopemvc.core.Selector;
import org.scopemvc.core.StringIndexSelector;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * Describes a single JavaBeans accessor. Since this includes indexed
 * properties, it might represent access into an ordered Collection, consuming
 * two Selectors: a String one to describe the Collection and an int one to
 * fetch the member of the List; eg "customers.0" can be traversed in one step
 * if the parent model JavaBean has a <CODE>getCustomer(int)</CODE> method. <br>
 * Handles java.util.List and Object[] for int indexed properties. </P> <p>
 *
 * Make sure to call findProperty before traverseProperty.</p>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.12 $ $Date: 2002/09/25 13:53:10 $
 * @created 05 September 2002
 */
final class Accessor {

    private static final Log LOG = LogFactory.getLog(Accessor.class);

    /**
     * Current model (changes when traverseProperty is called)
     */
    Object model;

    /**
     * Current selector (changes when traverseProperty is called)
     */
    Selector selector;

    /**
     * Original model, used for error reports
     */
    Object originalModel;

    /**
     * Original selector, used for error reports
     */
    Selector originalSelector;

    /**
     * Descriptor for the current property, either
     * <ul>
     *   <li> IndexedPropertyDescriptor (traverse through indexed),
     *   <li> PropertyDescriptor (normal String),
     *   <li> or null (normal Integer through a List model)
     * </ul>
     * Populated in findProperty
     */
    PropertyDescriptor descriptor;

    /**
     * Populated in findProperty
     */
    boolean isGetterIndexed;

    /**
     * reuse this when invoking indexed properties
     */
    private Object[] params = new Object[1];

    /**
     * Creates a new Accessor for the Selector in the model
     *
     * @param inModel The model object, not null
     * @param inSelector The Selector for a property in the model, not null
     */
    Accessor(Object inModel, Selector inSelector) {
        if (inModel == null) {
            throw new IllegalArgumentException("No model to find property for selector: " + Selector.asString(inSelector));
        }
        if (inSelector == null) {
            throw new IllegalArgumentException("No property selector defined for model: " + inModel);
        }
        originalModel = inModel;
        originalSelector = inSelector;
        model = inModel;
        selector = inSelector;
    }


    /**
     * Returns a string representation of this object
     *
     * @return a string representation of this object
     */
    public String toString() {
        return "Accessor: original model(" + originalModel
                + ") original selector(" + originalSelector
                + ") current model(" + model
                + ") current selector(" + selector
                + ") descriptor(" + descriptor
                + ") isGetterIndexed(" + isGetterIndexed + ")";
    }


    /**
     * Next traversal is over the terminal property? Depends on prior call to
     * {@link #findProperty}.
     *
     * @return The atTerminal value
     */
    boolean isAtTerminal() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("isAtTerminal: " + model + ", " + selector + ", " + isGetterIndexed);
        }

        return (
                selector == null
                || (selector.getNext() == null)
                || (isGetterIndexed && selector instanceof StringIndexSelector
                && selector.getNext() instanceof IntIndexSelector
                && selector.getNext().getNext() == null)
                );
    }


    /**
     * Find the next property and populate the descriptor. <br>
     * descriptor == null if next selector is IntIndexed (ie access property
     * through java.util.List or Object[]), else either a PropertyDescriptor or
     * an IndexedPropertyDescriptor.
     *
     * @throws IllegalArgumentException if the model or the selector is null
     * @throws NullPropertyException if the property could not be found in the
     *      model using the selector.
     */
    void findProperty() throws IllegalArgumentException, NullPropertyException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("findProperty: " + model + ", " + selector);
        }
        if (Debug.ON) {
            Debug.assertTrue(selector != null, "null selector");
        }

        if (model == null) {
            Selector intermediateSelector = originalSelector.deepClone();
            intermediateSelector.removeLast(selector);
            throw new NullPropertyException("Null property in model: " + originalModel + " with selector: " + Selector.asString(intermediateSelector));
        }
        if (selector == null) {
            throw new IllegalArgumentException("No property selector defined for model: " + model);
        }

        if (selector instanceof IntIndexSelector) {
            descriptor = null;
            isGetterIndexed = false;
        } else {
            String property = ((StringIndexSelector) selector).getIndex();
            descriptor = BeanInfos.getPropertyDescriptor(model.getClass(), property);
            if (descriptor == null) {
                throw new IllegalArgumentException("Property for selector: " + Selector.asString(selector) +
                        " could not be found in model: " + model);
            }
            if (descriptor instanceof IndexedPropertyDescriptor
                    && selector.getNext() instanceof IntIndexSelector
                    && ((IndexedPropertyDescriptor) descriptor).getIndexedReadMethod() != null) {
                isGetterIndexed = true;
            } else {
                isGetterIndexed = false;
            }
        }
    }


    /**
     * Burrows down through the next accessor to the next one. If the property
     * we return is an int-indexed property (ie accessed property from
     * java.util.List or Object[]) then the PropertyDescriptor will be null.
     *
     * @throws IllegalArgumentException if the model or the selector is null
     * @throws Exception Any other exception thrown when accessing the model
     */
    void traverseProperty() throws IllegalArgumentException, Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("traverseProperty: " + model + ", " + selector + ", " + descriptor);
        }

        try {
            if (descriptor == null) {
                // Traverse a List or array using int selector
                if (Debug.ON) {
                    Debug.assertTrue(selector instanceof IntIndexSelector, "not IntIndexSelector: " + selector);
                }
                int index = ((IntIndexSelector) selector).getIndex();
                if (model instanceof List) {
                    model = ((List) model).get(index);
                } else if (model instanceof Object[]) {
                    model = ((Object[]) model)[index];
                } else {
                    throw new IllegalArgumentException("Can't access properties using int index (expected List or Object[]) selector: " + Selector.asString(selector));
                }
                selector = selector.getNext();

            } else if (isGetterIndexed) {
                // Traverse a javabeans indexed property using the int selector from selector.getNext()
                if (Debug.ON) {
                    Debug.assertTrue(descriptor instanceof IndexedPropertyDescriptor, "not IndexedPropertyDescriptor: " + descriptor);
                }
                if (Debug.ON) {
                    Debug.assertTrue(selector instanceof StringIndexSelector, "not StringIndexSelector: " + selector);
                }
                if (Debug.ON) {
                    Debug.assertTrue(selector.getNext() instanceof IntIndexSelector, "not IntIndexSelector: " + selector.getNext());
                }
                Method getter = ((IndexedPropertyDescriptor) descriptor).getIndexedReadMethod();
                params[0] = new Integer(((IntIndexSelector) selector.getNext()).getIndex());
                if (getter == null) {
                    throw new IllegalArgumentException("No indexed getter for selector: " + Selector.asString(selector));
                }
                model = getter.invoke(model, params);
                selector = selector.getNext().getNext();

            } else {
                // Traverse a property using the String selector
                if (Debug.ON) {
                    Debug.assertTrue(selector instanceof StringIndexSelector, "not StringIndexSelector: " + selector);
                }
                Method getter = descriptor.getReadMethod();
                if (getter == null) {
                    throw new IllegalArgumentException("No getter for selector: " + Selector.asString(selector));
                }
                model = getter.invoke(model, (Object[]) null);
                selector = selector.getNext();
            }

        } catch (InvocationTargetException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("traverseProperty: " + selector, e);
            }
            if (e.getTargetException() instanceof Exception) {
                throw (Exception) e.getTargetException();
            }
            throw e;
        } catch (IllegalAccessException e1) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("traverseProperty: no accessible getter: " + selector, e1);
            }
            throw e1;
        } catch (IndexOutOfBoundsException e2) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("traverseProperty: IndexOutOfBoundsException: " + selector, e2);
            }
            throw e2;
        } catch (NullPointerException e3) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("traverseProperty: NullPointerException: " + selector, e3);
            }
            throw e3;
        }
    }
}

