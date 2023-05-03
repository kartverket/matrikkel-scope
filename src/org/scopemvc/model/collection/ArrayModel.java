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
 * $Id: ArrayModel.java,v 1.7 2002/09/12 10:51:03 ludovicc Exp $
 * Changes:
 *  - Added generics to class signature (see content field)
 */
package org.scopemvc.model.collection;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <P>
 *
 * A BasicModel wrapper for an <CODE>Object[]</CODE> that can propagate changes
 * to contained Models up the Model hierarchy, and implements the java.util.List
 * interface. The array itself is exposed via the "array" property, however,
 * changes to this array must be made through the public API in order to
 * maintain the model change propagation. </P> <P>
 *
 * By default ArrayModel registers itself as a listener to Models that are added
 * to the array and deregisters when those Models are removed. This behaviour
 * can be changed at creation so that ModelChangeEvent propagation is disabled.
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.7 $ $Date: 2002/09/12 10:51:03 $
 */
public class ArrayModel<E> extends BasicModel implements List<E> {

    /**
     * The selector for the array contained in this wrapper class.
     */
    public static final Selector ARRAY = Selector.fromString("array");

    private static final String TO_STRING_SEPARATOR = ", ";
    private static final int TO_STRING_SEPARATOR_LENGTH = TO_STRING_SEPARATOR.length();

    private static final Log LOG = LogFactory.getLog(ArrayModel.class);

    /**
     * The wrapped array
     */
    private E[] contents;

    /**
     * if true, propagate the changes made in the elements of the array
     */
    private boolean propagateModelChanges;


    /**
     * Constructor for the ArrayModel object
     */
    public ArrayModel() {
        this(true);
    }


    /**
     * Constructor for the ArrayModel object
     *
     * @param inArray The array to wrap
     */
    public ArrayModel(E[] inArray) {
        this(true);
        setArray(inArray);
    }


    /**
     * Constructor for the ArrayModel object
     *
     * @param inSize The initial size of the array
     */
    public ArrayModel(int inSize) {
        this(true, inSize);
    }


    /**
     * Constructor for the ArrayModel object
     *
     * @param inPropagateModelChanges If true, propagate the changes made in the
     *      elements of the array
     */
    public ArrayModel(boolean inPropagateModelChanges) {
        propagateModelChanges = inPropagateModelChanges;
    }


    /**
     * Constructor for the ArrayModel object
     *
     * @param inPropagateModelChanges If true, propagate the changes made in the
     *      elements of the array
     * @param inSize The initial size of the array
     */
    public ArrayModel(boolean inPropagateModelChanges, int inSize) {
        this(inPropagateModelChanges);
        if (inSize < 0) {
            throw new IllegalArgumentException("Can't create ArrayModel with size < 0: " + inSize);
        }
       TypeVariable<? extends Class<? extends ArrayModel>>[] typeParameters = getClass().getTypeParameters();
        Class arrayType = Object.class;
        if (typeParameters.length == 1) {
            Type[] bounds = typeParameters[0].getBounds();
            if (bounds.length == 1) {
                arrayType = Class.class.cast(bounds[0]);
            }
        }
       setArray((E[]) Array.newInstance(arrayType, inSize));
    }


    /**
     * Constructor for the ArrayModel object
     *
     * @param inPropagateModelChanges If true, propagate the changes made in the
     *      elements of the array
     * @param inArray The array to wrap
     */
    public ArrayModel(boolean inPropagateModelChanges, E[] inArray) {
        this(inPropagateModelChanges);
        setArray(inArray);
    }


    /**
     * Gets the array
     *
     * @return The array value
     */
    public Object[] getArray() {
        return contents;
    }


    /**
     * Expose size as a JavaBeans property.
     *
     * @return size of array, 0 if null.
     */
    public int getSize() {
        return size();
    }


    /**
     * @param inValue TODO: Describe the Parameter
     * @return The indexOf value
     * @deprecated see indexOf
     */
    public int getIndexOf(Object inValue) {
        if (inValue == null) {
            return -1;
        }
        for (int i = 0; i < getSize(); ++i) {
            if (inValue.equals(contents[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Gets the empty
     *
     * @return The empty value
     */
    public boolean isEmpty() {
        return (contents == null || contents.length < 1);
    }

    /**
     * TODO: document the method
     *
     * @param index TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public E get(int index) {
        return contents[index];
    }


    /**
     * Set contents to the passed Object array and fire a ModelChangeEvent.
     *
     * @param inContents The new array value
     */
    public void setArray(E[] inContents) {

        if (propagateModelChanges) {
            // Clear up old array if it contained any Models
            if (contents != null) {
                for (int i = 0; i < contents.length; ++i) {
                    Object o = contents[i];
                    if (o instanceof ModelChangeEventSource) {
                        ((ModelChangeEventSource) o).removeModelChangeListener(this);
                    }
                }
            }
        }

        contents = inContents;

        if (propagateModelChanges) {
            // Register as listener to any Models in the new array
            if (contents != null) {
                for (int i = 0; i < contents.length; ++i) {
                    Object o = contents[i];
                    if (o != null && o instanceof ModelChangeEventSource) {
                        ((ModelChangeEventSource) o).addModelChangeListener(this);
                    }
                }
            }
        }

        fireModelChange(ModelChangeEvent.VALUE_CHANGED, ARRAY);
    }

    /**
     * TODO: document the method
     *
     * @param index TODO: Describe the Parameter
     * @param element TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public E set(int index, E element) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("set: " + index + ", " + element);
        }

        E oldValue = get(index);
        contents[index] = element;
        fireModelChange(VALUE_CHANGED, Selector.fromInt(index));

        if (propagateModelChanges) {
            if (oldValue instanceof ModelChangeEventSource) {
                ((ModelChangeEventSource) oldValue).removeModelChangeListener(this);
            }
            if (element instanceof ModelChangeEventSource) {
                ((ModelChangeEventSource) element).addModelChangeListener(this);
            }
        }

        return oldValue;
    }

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public String toString() {

        if (contents == null) {
            return "";
        }

        StringBuffer result = new StringBuffer("(");

        for (int i = 0; i < contents.length; ++i) {
            result.append(contents[i] == null ? "[null]" : contents[i].toString());
            result.append(TO_STRING_SEPARATOR);
        }

        if (contents.length > 0) {
            result.setLength(result.length() - TO_STRING_SEPARATOR_LENGTH);
        }
        result.append(')');

        return result.toString();
    }


    // -------------------------- implement List. This is mostly very lazy -- tidy up sometime. ------------------------------

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public Iterator<E> iterator() {
        return Arrays.asList(contents).iterator();
    }

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public int size() {
        if (contents == null) {
            return 0;
        }
        return contents.length;
    }

    /**
     * TODO: document the method
     *
     * @param a TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public <T> T[] toArray(T[] a) {
        return Arrays.asList(contents).toArray(a);
    }

    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean contains(Object o) {
        return Arrays.asList(contents).contains(o);
    }

    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public Object[] toArray() {
        return contents;
    }

    /**
     * Adds an element to the All attribute of the ArrayModel object
     *
     * @param c The element to be added to the All attribute
     * @return TODO: Describe the Return Value
     */
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean add(E o) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: document the method
     *
     * @param c TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: document the method
     *
     * @param c TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean containsAll(Collection<?> c) {
        return Arrays.asList(contents).containsAll(c);
    }

    /**
     * TODO: document the method
     */
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds an element to the All attribute of the ArrayModel object
     *
     * @param index The element to be added to the All attribute
     * @param c The element to be added to the All attribute
     * @return TODO: Describe the Return Value
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public int hashCode() {
        return contents.hashCode();
    }

    /**
     * TODO: document the method
     *
     * @param c TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean equals(Object o) {
        return contents.equals(o);
    }

    /**
     * TODO: document the method
     *
     * @param index TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public int lastIndexOf(Object o) {
        return Arrays.asList(contents).lastIndexOf(o);
    }

    /**
     * TODO: document the method
     *
     * @param index TODO: Describe the Parameter
     * @param element TODO: Describe the Parameter
     */
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * TODO: document the method
     *
     * @param index TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public ListIterator<E> listIterator(int index) {
        return Arrays.asList(contents).listIterator(index);
    }

    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public int indexOf(Object o) {
        return getIndexOf(o);
    }

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public ListIterator<E> listIterator() {
        return Arrays.asList(contents).listIterator();
    }

    /**
     * TODO: document the method
     *
     * @param fromIndex TODO: Describe the Parameter
     * @param toIndex TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public List<E> subList(int fromIndex, int toIndex) {
        return Arrays.asList(contents).subList(fromIndex, toIndex);
    }
}
