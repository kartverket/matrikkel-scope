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
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.IntIndexSelector;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.core.StringIndexSelector;
import org.scopemvc.util.Debug;
import org.scopemvc.model.util.ArraySelectorIterator;
import org.scopemvc.model.util.CompoundSelectorIterator;
import org.scopemvc.model.util.IntIndexSelectorIterator;

/**
 * <P>
 *
 * BeansPropertyManager is a {@link org.scopemvc.core.PropertyManager} that
 * handles the properties of JavaBean model objects. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.12 $ $Date: 2002/11/06 00:30:10 $
 * @created 05 September 2002
 * @todo Use commons-beanutils functionality (ludovicc)
 */
public class BeansPropertyManager extends PropertyManager {

    private static final Log LOG = LogFactory.getLog(BeansPropertyManager.class);


    /**
     * <P>
     *
     * Return the value of the property identified by the passed {@link
     * Selector}. If the passed Selector is null, return the model object
     * itself. </P>
     *
     * @param inModel model to get property from
     * @param inSelector identify the property to be returned or null for the
     *      model object itself.
     * @return value of selected property
     * @throws Exception If the value of the property could not be retrieved
     */
    public Object get(Object inModel, Selector inSelector) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("get: " + inModel + ", " + inSelector);
        }

        if (inModel == null) {
//            throw new IllegalArgumentException("Can't get from a null model.");
            return null;
        }

        if (inSelector == null) {
            return inModel;
        }

        try {
            Accessor accessor = findTerminalAccessor(inModel, inSelector);
            accessor.traverseProperty();
            return accessor.model;
        } catch (NullPropertyException e) {
            LOG.debug("Could not locate the property using selector " + Selector.asString(inSelector) + " in model " + inModel);
            return null;
        } catch (IllegalArgumentException e2){
           LOG.error("Could not locate the property using selector " + Selector.asString(inSelector) + " in model " + inModel);
           throw new RuntimeException("Implementasjonsfeil i bruk av matrikkel-scope.", e2);
        }
    }


    /**
     * <P>
     *
     * Is a property read-only? If the passed Selector is null then is the model
     * object as a whole read-only? </P> <P>
     *
     * Enforcement of the access state must be implemented by the model itself
     * by using the DynamicReadOnly interface. </P>
     *
     * @param inModel model object to test the property on.
     * @param inSelector The property to test or null to test the whole model
     *      object.
     * @return whether the property is read-only or not.
     * @throws Exception If the read-only state of the property could not be
     *      tested
     * @see DynamicReadOnly
     */
    public boolean isReadOnly(Object inModel, Selector inSelector)
             throws Exception {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't ask isReadOnly for a null model, selector is " + Selector.asString(inSelector));
        }

        if (inSelector == null) {
            return true;
        }

        Accessor accessor;
        try {
            accessor = findTerminalAccessor(inModel, inSelector);
        } catch (Exception e) {
            return true;
        }

        if (accessor.descriptor == null) {
            // List elements always mutable by default
            if (accessor.model instanceof DynamicReadOnly) {
                return ((DynamicReadOnly) accessor.model).isPropertyReadOnly(accessor.selector);
            }
            return false;
        } else if (accessor.isGetterIndexed) {
            // Indexed setter
            return (((IndexedPropertyDescriptor) accessor.descriptor).getIndexedWriteMethod() == null);
        } else {
            // Normal setter
            if (accessor.model instanceof DynamicReadOnly) {
                if (((DynamicReadOnly) accessor.model).isPropertyReadOnly(accessor.selector)) {
                    return true;
                }
            }
            return (accessor.descriptor.getWriteMethod() == null);
        }
    }


    /**
     * Return the Class of a property.
     *
     * @param inModel model to test the property for.
     * @param inSelector property to test.
     * @return Class of property. Never null.
     * @throws Exception If the class of the property could not be retrieved
     */
    public Class getPropertyClass(Object inModel, Selector inSelector)
             throws Exception {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't getPropertyClass for a null model, selector is " + Selector.asString(inSelector));
        }

        if (inSelector == null) {
            return inModel.getClass();
        }
        Accessor accessor = findTerminalAccessor(inModel, inSelector);
        if (accessor.descriptor == null) {
            // List
            return Object.class;
            // TODO: should test type of Object[]
        } else if (accessor.isGetterIndexed) {
            // Indexed getter
            Method getter = ((IndexedPropertyDescriptor) accessor.descriptor).getIndexedReadMethod();
            if (getter == null) {
                throw new IllegalArgumentException("No indexed getter for: " + Selector.asString(inSelector) + " in model " + inModel);
            }
            return getter.getReturnType();
        } else {
            // Normal getter
            Method getter = accessor.descriptor.getReadMethod();
            if (getter == null) {
                throw new IllegalArgumentException("No getter for: " + Selector.asString(inSelector) + " in model " + inModel);
            }
            return getter.getReturnType();
        }
    }


    /**
     * <P>
     *
     * Return an Iterator that iterates over Selectors for all properties of the
     * passed model object. </P>
     *
     * @param inModel model to make an Iterator for.
     * @return Iterator that iterates over Selectors for all properties of the
     *      passed model object.
     */
    public Iterator getSelectorIterator(Object inModel) {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't getSelectorIterator for a null model.");
        }

        if (inModel instanceof Object[]) {
            return new IntIndexSelectorIterator(0, ((Object[]) inModel).length - 1);
        }

        PropertyDescriptor[] descriptors = BeanInfos.getBeanInfo(inModel.getClass()).getPropertyDescriptors();
        Selector[] selectors = new Selector[descriptors.length];
        for (int i = 0; i < descriptors.length; ++i) {
            selectors[i] = Selector.fromString(descriptors[i].getName());
            // TODO: cache these!
        }
        Iterator result = new ArraySelectorIterator(selectors);

        if (inModel instanceof List) {
            return new CompoundSelectorIterator(result, new IntIndexSelectorIterator(0, ((List) inModel).size() - 1));
        } else {
            return result;
        }
    }


    /**
     * <P>
     *
     * Tries to find a Selector that would get() a property equals() to the
     * passed Object. If the model is a java.util.List, call indexOf() to search
     * contents first. For Object[] search through contents. Else get an
     * Iterator over all properties and iterate over to find a match. </P> <P>
     *
     * Note: doesn't search through JavaBeans indexed properties for a match.
     * </P>
     *
     * @param inProperty the property Object to find.
     * @param inModel the model to get the property from.
     * @return a Selector that would return an Object equals() to the passed
     *      property, or null if not found.
     */
    public Selector getSelectorFor(Object inModel, Object inProperty) {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't getSelectorFor for a null model.");
        }

        if (inProperty == null) {
            return null;
        }

        if (inModel instanceof java.util.List) {
            int i = ((java.util.List) inModel).indexOf(inProperty);
            if (i < 0) {
                return null;
            }
            return Selector.fromInt(i);
        }

        if (inModel instanceof Object[]) {
            Object[] array = (Object[]) inModel;
            for (int i = array.length - 1; i >= 0; --i) {
                Object o = array[i];
                if (o != null && o.equals(inProperty)) {
                    return Selector.fromInt(i);
                }
            }
            return null;
        }

        Iterator i = getSelectorIterator(inModel);
        if (Debug.ON) {
            Debug.assertTrue(i != null, "null Iterator");
        }
        while (i.hasNext()) {
            Object o = i.next();
            if (Debug.ON) {
                Debug.assertTrue(o == null || o instanceof Selector, "Iterator doesn't contain Selector: " + o);
            }
            Selector s = (Selector) o;
            try {
                o = get(inModel, s);
                if (o != null && o.equals(inProperty)) {
                    return s;
                }
            } catch (Exception e) {
                // ignore that property and carry on
            }
        }
        return null;
    }


    /**
     * <P>
     *
     * Set the value of the property identified by a {@link Selector} in the
     * passed model object to a new value. </P> <P>
     *
     * The implementation should not set the value if the new value has the same
     * Object reference as the original. It could also avoid setting the value
     * if the new value is equivalent to the old value, and the value is of an
     * immutable Class (like Integer, String). Otherwise the property must be
     * set to the new value, even if it equals() the old value. </P> <P>
     *
     * Usually, a {@link org.scopemvc.core.ModelChangeEvent} for {@link
     * org.scopemvc.core.ModelChangeEvent#VALUE_CHANGED} should be broadcast by the model when the
     * property is set so that interested listeners know that the model's state
     * has changed. </P> <P>
     *
     * If the property is a sub-model object then the parent model should be
     * registered as a {@link org.scopemvc.core.ModelChangeListener} to be able to propagate
     * events properly. This propagation is partially implemented in {@link
     * org.scopemvc.model.basic.BasicModel} but it relies on child Models being
     * listened to by their parent. (Note: deregister from the old Model then
     * register with the new one). See the sample code for examples using {@link
     * org.scopemvc.model.basic.BasicModel#listenNewSubmodel} and {@link
     * org.scopemvc.model.basic.BasicModel#unlistenOldSubmodel}. </P>
     *
     * @param inModel model to set the property on.
     * @param inSelector identify the property to be set. Can't be null.
     * @param inValue the value to set the property to.
     * @throws Exception if the value could not be set in the model
     * @todo Could add convenience methods for primitive types? (smefroy)
     */
    public void set(Object inModel, Selector inSelector, Object inValue)
             throws Exception {

        if (inModel == null) {
            throw new IllegalArgumentException("Can't set for a null model, selector is " + Selector.asString(inSelector));
        }

        if (inSelector == null) {
            throw new IllegalArgumentException("Can't set value of model: " + inModel + " because the passed Selector is null");
        }

        Accessor accessor;
        try {
            accessor = findTerminalAccessor(inModel, inSelector);
            if (Debug.ON) {
                Debug.assertTrue(accessor.selector != null, "null accessor.selector");
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("set: accessor: " + accessor + ", value: " + inValue);
            }
        } catch (NullPropertyException e) {
            throw new UnsupportedOperationException("Can't set property with selector: " + Selector.asString(inSelector) + " in model: " + inModel);
        }

        try {

            if (accessor.descriptor == null) {
                // java.util.List or Object[]
                if (LOG.isDebugEnabled()) {
                    LOG.debug("set: int indexed property");
                }
                if (Debug.ON) {
                    Debug.assertTrue(accessor.selector instanceof IntIndexSelector);
                }
                int index = ((IntIndexSelector) accessor.selector).getIndex();
                Object model = accessor.model;
                if (model instanceof List) {
                    ((List) model).set(index, inValue);
                } else if (model instanceof Object[]) {
                    ((Object[]) model)[index] = inValue;
                } else {
                    throw new IllegalArgumentException(
                            "Can't access properties using int index (expected List or Object[]) with selector: "
                            + Selector.asString(inSelector) + " in model: " + inModel);
                }

            } else if (accessor.isGetterIndexed) {
                // Indexed setter
                if (LOG.isDebugEnabled()) {
                    LOG.debug("set: indexed");
                }
                if (Debug.ON) {
                    Debug.assertTrue(accessor.descriptor instanceof IndexedPropertyDescriptor);
                }
                Method setter = ((IndexedPropertyDescriptor) accessor.descriptor).getIndexedWriteMethod();
                if (setter == null) {
                    throw new IllegalArgumentException("Can't find indexed setter for selector: " + Selector.asString(inSelector) + " in model: " + inModel);
                }
                if (Debug.ON) {
                    Debug.assertTrue(accessor.selector instanceof StringIndexSelector);
                }
                if (Debug.ON) {
                    Debug.assertTrue(accessor.selector.getNext() instanceof IntIndexSelector, "not IntIndexSelector: " + accessor.selector);
                }
                Integer index = new Integer(((IntIndexSelector) accessor.selector.getNext()).getIndex());
                Object[] params = {index, inValue};
                setter.invoke(accessor.model, params);

            } else {
                // Normal setter
                Method setter = ((PropertyDescriptor) accessor.descriptor).getWriteMethod();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("set: normal: " + setter);
                }
                if (setter == null) {
                    throw new IllegalArgumentException("Can't find setter for selector: " + Selector.asString(inSelector) + " in model: " + inModel);
                }
                Object[] params = {inValue};
                setter.invoke(accessor.model, params);
            }

        } catch (InvocationTargetException e) {
            LOG.warn("set: can't invoke setter for selector: " + Selector.asString(inSelector) + " in model: " + inModel, e);
            if (e.getTargetException() instanceof Exception) {
                throw (Exception) e.getTargetException();
            } else {
                throw e;
            }
        } catch (IllegalAccessException e1) {
            LOG.warn("set: no accessible setter for selector: " + Selector.asString(inSelector) + " in model: " + inModel, e1);
            throw new IllegalArgumentException("Illegal access to setter for selector: " + Selector.asString(inSelector) + " in model: " + inModel);
        } catch (IndexOutOfBoundsException e2) {
            LOG.warn("set: IndexOutOfBoundsException: " + inSelector, e2);
            throw new IllegalArgumentException("Can't access property for selector: " + Selector.asString(inSelector) + " in model: " + inModel);
        } catch (NullPointerException e3) {
            LOG.warn("set: NullPointerException: " + inSelector, e3);
            throw new IllegalArgumentException("Can't access property for selector: " + Selector.asString(inSelector) + " in model: " + inModel);
        }
    }


    /**
     * <P>
     *
     * Set the value of the property identified by a {@link Selector} in the
     * passed model object to a new value. </P> <P>
     *
     * The implementation should not set the value if the new value has the same
     * Object reference as the original. It could also avoid setting the value
     * if the new value is equivalent to the old value, and the value is of an
     * immutable Class (like Integer, String). Otherwise the property must be
     * set to the new value, even if it equals() the old value. </P> <P>
     *
     * Usually, a {@link org.scopemvc.core.ModelChangeEvent} for {@link
     * org.scopemvc.core.ModelChangeEvent#VALUE_CHANGED} should be broadcast by the model when the
     * property is set so that interested listeners know that the model's state
     * has changed. </P> <P>
     *
     * If the property is a sub-model object then the parent model should be
     * registered as a {@link org.scopemvc.core.ModelChangeListener} to be able to propagate
     * events properly. This propagation is partially implemented in {@link
     * org.scopemvc.model.basic.BasicModel} but it relies on child Models being
     * listened to by their parent. (Note: deregister from the old Model then
     * register with the new one). See the sample code for examples using {@link
     * org.scopemvc.model.basic.BasicModel#listenNewSubmodel} and {@link
     * org.scopemvc.model.basic.BasicModel#unlistenOldSubmodel}. </P>
     *
     * @param inModel model to set the property on.
     * @param inSelector identify the property to be set. Can't be null.
     * @return TODO: Describe the Return Value
     * @throws Exception if the value could not be set in the model
     */
    public boolean hasProperty(Object inModel, Selector inSelector) {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't ask hasProperty for a null model.");
        }

        if (inSelector == null) {
            return true;
        }
        try {
            Accessor accessor = findTerminalAccessor(inModel, inSelector);
            accessor.traverseProperty();
            return true;
        } catch (Exception e) {
            return false;
            // ***** this is a bit nasty
        }
    }


    // ------------------------ Working directly with JavaBeans properties ------------------------

    /**
     * Burrow down through submodels to find the terminal accessor (which could
     * be a JavaBeans indexed property) from the passed model and selector.
     *
     * @param inModel model to get the accessor on.
     * @param inSelector identify the property. Can't be null.
     * @return The terminal accessor
     * @throws Exception if the Accessor could not be found for any reason
     */
    Accessor findTerminalAccessor(Object inModel, Selector inSelector) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("findTerminalAccessor: " + inModel + ", " + inSelector);
        }
        if (Debug.ON) {
            Debug.assertTrue(inModel != null, "null model");
        }
        if (Debug.ON) {
            Debug.assertTrue(inSelector != null, "null Selector");
        }

        Accessor accessor = new Accessor(inModel, inSelector);
        accessor.findProperty();
        while (!accessor.isAtTerminal()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("findTerminalAccessor: not at terminal: " + accessor.selector);
            }
            accessor.traverseProperty();
            accessor.findProperty();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("findTerminalAccessor: found terminal: " + accessor.selector);
        }
        return accessor;
    }

}
