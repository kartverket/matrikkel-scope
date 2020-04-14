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
 * $Id: PropertyManager.java,v 1.8 2002/09/11 19:12:29 ludovicc Exp $
 */
package org.scopemvc.core;

import java.util.Iterator;

/**
 * <P>
 *
 * PropertyManager is a {@link ModelManager} that provides access to the
 * properties of model objects. An implementation for JavaBean model objects is
 * provided in org.scopemvc.model.beans.BeansPropertyManager. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.8 $ $Date: 2002/09/11 19:12:29 $
 */
public abstract class PropertyManager extends ModelManager {

    private static final String NAME = "PropertyManager";


    /**
     * Create an PropertyManager for the model class
     *
     * @param inModelClass The model class
     * @return The PropertyManager instance
     */
    public static PropertyManager getInstance(Class inModelClass) {
        return (PropertyManager) make(NAME, inModelClass);
    }


    /**
     * Create an PropertyManager for the model
     *
     * @param inModel The model
     * @return The PropertyManager instance
     */
    public static PropertyManager getInstance(Object inModel) {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't create a PropertyManager for null");
        }
        return getInstance(inModel.getClass());
    }


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
    public abstract Object get(Object inModel, Selector inSelector)
             throws Exception;


    /**
     * <P>
     *
     * Is a property read-only? If the passed Selector is null then is the model
     * object as a whole read-only? </P> <P>
     *
     * Enforcement of the access state must be implemented by the model itself.
     * </P>
     *
     * @param inModel model object to test the property on.
     * @param inSelector The property to test or null to test the whole model
     *      object.
     * @return whether the property is read-only or not.
     * @throws Exception If the read-only state of the property could not be
     *      tested
     */
    public abstract boolean isReadOnly(Object inModel, Selector inSelector)
             throws Exception;


    /**
     * Return the Class of a property.
     *
     * @param inModel model to test the property for.
     * @param inSelector property to test.
     * @return Class of property. Never null.
     * @throws Exception If the class of the property could not be retrieved
     */
    public abstract Class getPropertyClass(Object inModel, Selector inSelector)
             throws Exception;


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
    public abstract Iterator getSelectorIterator(Object inModel);


    /**
     * Return a Selector that would get() a property equals() to the passed
     * Object. Guaranteed to work only for non-primitive properties.
     *
     * @param inProperty the property Object to find.
     * @param inModel the model to get the property from.
     * @return a Selector that would return an Object equals() to the passed
     *      property, or null if not found.
     */
    public abstract Selector getSelectorFor(Object inModel, Object inProperty);


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
     * Usually, a {@link ModelChangeEvent} for {@link
     * ModelChangeEvent#VALUE_CHANGED} should be broadcast by the model when the
     * property is set so that interested listeners know that the model's state
     * has changed. </P> <P>
     *
     * If the property is a sub-model object then the parent model should be
     * registered as a {@link ModelChangeListener} to be able to propagate
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
     */
    public abstract void set(Object inModel, Selector inSelector, Object inValue)
             throws Exception;


    /**
     * Does the passed model object contain the property identified by the
     * passed Selector?
     *
     * @param inModel model to test the property for.
     * @param inSelector property to test.
     * @return true if the property exists on the passed model.
     */
    public abstract boolean hasProperty(Object inModel, Selector inSelector);
}

