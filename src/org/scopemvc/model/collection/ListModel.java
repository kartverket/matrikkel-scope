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
 * $Id: ListModel.java,v 1.14 2002/11/20 01:36:58 ludovicc Exp $
 * Changes:
 *  - Added generic signature to this class
 */
package org.scopemvc.model.collection;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.Selector;
import org.scopemvc.model.BasicModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * <P>
 *
 * A BasicModel wrapper for an <CODE>List</CODE> that can propagate changes to
 * contained Models up the Model hierarchy. The list itself is exposed via the
 * "list" property, however, changes to this list must be made through this
 * class's public API in order to maintain the event propagation. </P> <P>
 *
 * By default ListModel registers itself as a listener to Models that are added
 * to the list and deregisters when those Models are removed. This behaviour can
 * be changed at creation so that ModelChangeEvent propagation is disabled. </P>
 * <P>
 *
 * ListModel implements the List interface, and it exposes some List methods as
 * Javabean-compliant methods (eg. size() is also available as getSize()), so it
 * can be used by Selectors (eg. you can have a selector on the 'size'
 * property). </P>
 *
 * @author <A HREF="mailto:haruki_zaemon@users.sourceforge.net">Simon Harris</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <A HREF="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</A>
 * @version $Revision: 1.14 $
 * @created 05 September 2002
 */
public class ListModel<E> extends BasicModel implements List<E> {
   /** Selector for the list property included in this model */
   public static final Selector LIST = Selector.fromString("list");

   // ----------------- for debug -----------------------

   /** Separator for the elements in the list, used by toString() */
   private static final String TO_STRING_SEPARATOR = ", ";

   private static final Log LOG = LogFactory.getLog(ListModel.class);


   /**
    * If true, listen to changes in the elements of the list (if they implement ModelChangeEventSource), and propagate
    * the changes to the listeners of this list model.
    */
   private boolean propagateModelChanges;
   /** The list wrapped by this model instance */
   private List<E> contents;

   /** Constructor for the ListModel object */
   public ListModel() {
      this(true, new ArrayList<E>(10));
   }


   /**
    * Constructor for the ListModel object
    *
    * @param inList The list to wrap
    */
   public ListModel(List<E> inList) {
      this(true, inList);
   }


   /**
    * Constructor for the ListModel object
    *
    * @param inPropagateModelChanges If true, listen to changes in the elements
    *                                of the list (if they implement ModelChangeEventSource), and
    *                                propagate the changes to the listeners of this list model.
    */
   public ListModel(boolean inPropagateModelChanges) {
      propagateModelChanges = inPropagateModelChanges;
   }


   /**
    * Constructor for the ListModel object
    *
    * @param inPropagateModelChanges If true, listen to changes in the elements
    *                                of the list (if they implement ModelChangeEventSource), and
    *                                propagate the changes to the listeners of this list model.
    * @param inList                  The list to wrap
    */
   public ListModel(boolean inPropagateModelChanges, List<E> inList) {
      this(inPropagateModelChanges);
      setList(inList);
   }


   /**
    * Gets the list wrapped by this model. <br>
    * Warning: the list returned by this method is unmodifiable, so if you need
    * to make changes to the list, you need to use the methods in the ListModel
    * wrapper class instead of the original list.
    *
    * @return The list value
    */
   public List<E> getList() {
      if( contents == null ) {
         return null;
      }
      return Collections.unmodifiableList(contents);
   }


   /**
    * Gets the size of the list
    *
    * @return The size value
    */
   public int getSize() {
      if( contents == null ) {
         return 0;
      }
      return contents.size();
   }


   /**
    * Gets the index of the passed object in the list
    *
    * @param inElement The element of the list to find the index for
    * @return The index of the element in the list, or -1 if the element is not
    *         in the list
    */
   public int getIndexOf(Object inElement) {
      if( inElement == null || contents == null ) {
         return -1;
      }
      return contents.indexOf(inElement);
   }


   /**
    * Returns true if the list is empty
    *
    * @return The empty value
    */
   public boolean isEmpty() {
      if( contents == null ) {
         return true;
      }
      return contents.isEmpty();
   }


   /**
    * Gets the element at the given index in the list
    *
    * @param inIndex The index of the element in the list
    * @return The element in the list at the given index
    */
   public E get(int inIndex) {
      if( contents == null ) {
         return null;
      }
      return contents.get(inIndex);
   }


   /**
    * Defines the list to wrap. <p>
    * If propagateModelChanges is true, then it installs listeners on the
    * elements of the list if they implement ModelChangeEventSource, to be able
    * to detect changes in those elements. </p> <p>
    * Fires a ModelChangeEvent on the 'list' selector at the end of this
    * method. </p>
    *
    * @param inContents The new list value
    */
   public void setList(List<E> inContents) {

      if( propagateModelChanges ) {
         // Clear up old list if it contained any Models
         if( contents != null ) {
            for( Iterator i = contents.iterator(); i.hasNext(); ) {
               Object o = i.next();
               if( o instanceof ModelChangeEventSource ) {
                  ((ModelChangeEventSource) o).removeModelChangeListener(this);
               }
            }
         }
      }

      contents = inContents;

      if( propagateModelChanges ) {
         // Register as listener to any Models in the new list
         if( contents != null ) {
            for( Iterator i = contents.iterator(); i.hasNext(); ) {
               Object o = i.next();
               if( o instanceof ModelChangeEventSource ) {
                  ((ModelChangeEventSource) o).addModelChangeListener(this);
               }
            }
         }
      }

      fireModelChange(ModelChangeEvent.VALUE_CHANGED, LIST);
   }


   /**
    * Replaces the element at the specified position in this list with the
    * specified element.
    *
    * @param inIndex   index of element to replace.
    * @param inElement element to be stored at the specified position.
    * @return the element previously at the specified position.
    */
   public E set(int inIndex, E inElement) {
      if( LOG.isDebugEnabled() ) {
         LOG.debug("set: " + inIndex + ", " + inElement);
      }

      if( contents == null ) {
         throw new IllegalStateException("Cannot set an item to the list model when the wrapped list is null");
      }

      E oldElement = contents.set(inIndex, inElement);
      fireModelChange(VALUE_CHANGED, Selector.fromInt(inIndex));

      if( propagateModelChanges ) {
         if( oldElement instanceof ModelChangeEventSource ) {
            ((ModelChangeEventSource) oldElement).removeModelChangeListener(this);
         }
         if( inElement instanceof ModelChangeEventSource ) {
            ((ModelChangeEventSource) inElement).addModelChangeListener(this);
         }
      }

      return oldElement;
   }

   // -------------------------- implement List ------------------------------

   /**
    * Returns an iterator over the elements in this list in proper sequence.
    *
    * @return an iterator over the elements in this list in proper sequence.
    */
   public Iterator<E> iterator() {
      if( contents == null ) {
         return new ArrayList<E>().iterator();
      }
      return contents.iterator();
   }



   /**
    * Returns the number of elements in this list. If this list contains more
    * than <tt>Integer.MAX_VALUE</tt> elements, returns <tt>Integer.MAX_VALUE
    * </tt>.
    *
    * @return the number of elements in this list.
    */
   public int size() {
      return getSize();
   }

   /**
    * Returns <tt>true</tt> if this list contains the specified element. More
    * formally, returns <tt>true</tt> if and only if this list contains at
    * least one element <tt>e</tt> such that <tt>
    * (o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt> .
    *
    * @param inElement element whose presence in this list is to be tested.
    * @return <tt>true</tt> if this list contains the specified element.
    */
   public boolean contains(Object inElement) {
      if( contents == null ) {
         return false;
      }
      return contents.contains(inElement);
   }

   /**
    * Removes the first occurrence in this list of the specified element. If
    * this list does not contain the element, it is unchanged. More formally,
    * removes the element with the lowest index i such that <tt>(o==null ?
    * get(i)==null : o.equals(get(i)))</tt> (if such an element exists).
    *
    * @param inElement element to be removed from this list, if present.
    * @return <tt>true</tt> if this list contained the specified element.
    */
   public boolean remove(Object inElement) {
      int i = indexOf(inElement);
      if( i < 0 ) {
         return false;
      }
      remove(i);
      return true;
   }


   /**
    * Appends all of the elements in the specified collection to the end of
    * this list, in the order that they are returned by the specified
    * collection's iterator. <br>
    * The behavior of this operation is unspecified if the specified collection
    * is modified while the operation is in progress. (Note that this will
    * occur if the specified collection is this list, and it's nonempty.)
    *
    * @param inCollection collection whose elements are to be added to this
    *                     list.
    * @return <tt>true</tt> if this list changed as a result of the call.
    */
   public boolean addAll(Collection<? extends E> inCollection) {
      boolean changed = false;
      makeActive(false);
      try {

         for( Iterator<? extends E> it = inCollection.iterator(); it.hasNext(); ) {
            changed |= add(it.next());
         }
      } finally {
         makeActive(true);
      }
      if( changed ) {
         fireModelChange(VALUE_CHANGED, null);
      }
      return changed;
   }


   /**
    * Appends the specified element to the end of this list.
    *
    * @param inElement element to be appended to this list.
    * @return <tt>true</tt> (as per the general contract of the <tt>
    *         Collection.add</tt> method).
    */
   public boolean add(E inElement) {
      if( LOG.isDebugEnabled() ) {
         LOG.debug("add: " + inElement);
      }

      if( contents == null ) {
         throw new IllegalStateException("Cannot add an item to the list model when the wrapped list is null");
      }

      if( !contents.add(inElement) ) {
         return false;
      }

      if( propagateModelChanges && inElement instanceof ModelChangeEventSource ) {
         ((ModelChangeEventSource) inElement).addModelChangeListener(this);
      }
      fireModelChange(VALUE_ADDED, Selector.fromInt(contents.size() - 1));
      return true;
   }


   /**
    * Removes from this list all the elements that are contained in the
    * specified collection.
    *
    * @param inCollection collection that defines which elements will be
    *                     removed from this list.
    * @return <tt>true</tt> if this list changed as a result of the call.
    */
   public boolean removeAll(Collection<?> inCollection) {
      boolean result = false;
      makeActive(false);

      try {
         for( Iterator<?> it = inCollection.iterator(); it.hasNext(); ) {
            result = result | remove(it.next());
         }
      } finally {
         makeActive(true);
      }
      fireModelChange(VALUE_CHANGED, null);
      return result;
   }


   /**
    * Returns <tt>true</tt> if this list contains all of the elements of the
    * specified collection.
    *
    * @param inCollection collection to be checked for containment in this
    *                     list.
    * @return <tt>true</tt> if this list contains all of the elements of the
    *         specified collection.
    */
   public boolean containsAll(Collection<?> inCollection) {
      if( contents == null ) {
         return false;
      }
      return getList().containsAll(inCollection);
   }


   /**
    * Removes all of the elements from this list (optional operation). This
    * list will be empty after this call returns (unless it throws an
    * exception).
    */
   public void clear() {
      if( contents == null || contents.size() == 0 ) {
         return;
      }

      makeActive(false);
      try {
         if( propagateModelChanges ) {
            for( Iterator i = contents.iterator(); i.hasNext(); ) {
               Object o = i.next();
               if( o instanceof ModelChangeEventSource ) {
                  ((ModelChangeEventSource) o).removeModelChangeListener(this);
               }
            }
         }
         contents.clear();
      } finally {
         makeActive(true);
      }
      fireModelChange(VALUE_CHANGED, null);
   }


   /**
    * Inserts all of the elements in the specified collection into this list at
    * the specified position. <br>
    * Shifts the element currently at that position (if any) and any subsequent
    * elements to the right (increases their indices). The new elements will
    * appear in this list in the order that they are returned by the specified
    * collection's iterator. The behavior of this operation is unspecified if
    * the specified collection is modified while the operation is in progress.
    * (Note that this will occur if the specified collection is this list, and
    * it's nonempty.)
    *
    * @param inIndex      index at which to insert first element from the specified
    *                     collection.
    * @param inCollection elements to be inserted into this list.
    * @return true if the list has changed
    */
   public boolean addAll(int inIndex, Collection<? extends E> inCollection) {
      boolean changed = false;
      makeActive(false);
      try {
         for( Iterator<? extends E> it = inCollection.iterator(); it.hasNext(); ) {
            changed = changed | add(it.next());
         }
      } finally {
         makeActive(true);
      }
      if( changed ) {
         fireModelChange(VALUE_CHANGED, null);
      }
      return changed;
   }


   /**
    * Retains only the elements in this list that are contained in the
    * specified collection (optional operation). In other words, removes from
    * this list all the elements that are not contained in the specified
    * collection.
    *
    * @param inCollection collection that defines which elements this set will
    *                     retain.
    * @return <tt>true</tt> if this list changed as a result of the call.
    */
   public boolean retainAll(Collection<?> inCollection) {
      boolean changed = false;
      makeActive(false);
      try {
         for( Iterator it = iterator(); it.hasNext(); ) {
            Object o = it.next();
            if( !inCollection.contains(o) ) {
               it.remove();
               changed = true;
            }
         }
      } finally {
         makeActive(true);
      }
      if( changed ) {
         fireModelChange(VALUE_CHANGED, null);
      }
      return changed;
   }


   /**
    * Removes the element at the specified position in this list (optional
    * operation). Shifts any subsequent elements to the left (subtracts one
    * from their indices). Returns the element that was removed from the list.
    *
    * @param inIndex the index of the element to removed.
    * @return the element previously at the specified position.
    */
   public E remove(int inIndex) {
      if( contents == null ) {
         return null;
      }

      E oldValue = contents.remove(inIndex);
      if( propagateModelChanges && oldValue instanceof ModelChangeEventSource ) {
         ((ModelChangeEventSource) oldValue).removeModelChangeListener(this);
      }
      if( oldValue != null ) {
         fireModelChange(VALUE_REMOVED, Selector.fromInt(inIndex));
      }
      return oldValue;
   }


   /**
    * Returns the index in this list of the last occurrence of the specified
    * element, or -1 if this list does not contain this element. More formally,
    * returns the highest index <tt>i</tt> such that <tt>(o==null ?
    * get(i)==null : o.equals(get(i)))</tt> , or -1 if there is no such index.
    *
    * @param inElement element to search for.
    * @return the index in this list of the last occurrence of the specified
    *         element, or -1 if this list does not contain this element.
    */
   public int lastIndexOf(Object inElement) {
      if( contents == null ) {
         return -1;
      }
      return contents.lastIndexOf(inElement);
   }


   /**
    * Inserts the specified element at the specified position in this list
    * (optional operation). Shifts the element currently at that position (if
    * any) and any subsequent elements to the right (adds one to their
    * indices).
    *
    * @param inIndex   index at which the specified element is to be inserted.
    * @param inElement element to be inserted.
    */
   public void add(int inIndex, E inElement) {
      if( contents == null ) {
         throw new IllegalStateException("Cannot add an item to the list model when the wrapped list is null");
      }

      contents.add(inIndex, inElement);
      if( propagateModelChanges && inElement instanceof ModelChangeEventSource ) {
         ((ModelChangeEventSource) inElement).addModelChangeListener(this);
      }
      fireModelChange(VALUE_ADDED, Selector.fromInt(inIndex));
   }


   /**
    * Returns a list iterator of the elements in this list (in proper
    * sequence), starting at the specified position in this list. The specified
    * index indicates the first element that would be returned by an initial
    * call to the <tt>next</tt> method. An initial call to the <tt>previous
    * </tt> method would return the element with the specified index minus one.
    *
    * @param inIndex index of first element to be returned from the list
    *                iterator (by a call to the <tt>next</tt> method).
    * @return a list iterator of the elements in this list (in proper
    *         sequence), starting at the specified position in this list.
    */
   public ListIterator<E> listIterator(int inIndex) {
      if( contents == null ) {
         return new ArrayList<E>().listIterator();
      }
      return contents.listIterator(inIndex);
   }


   /**
    * Returns the index in this list of the first occurrence of the specified
    * element, or -1 if this list does not contain this element. More formally,
    * returns the lowest index <tt>i</tt> such that <tt>(o==null ? get(i)==null
    * : o.equals(get(i)))</tt> , or -1 if there is no such index.
    *
    * @param inElement element to search for.
    * @return the index in this list of the first occurrence of the specified
    *         element, or -1 if this list does not contain this element.
    */
   public int indexOf(Object inElement) {
      return getIndexOf(inElement);
   }


   /**
    * Returns a list iterator of the elements in this list (in proper
    * sequence).
    *
    * @return a list iterator of the elements in this list (in proper
    *         sequence).
    */
   public ListIterator<E> listIterator() {
      if( contents == null ) {
         return new ArrayList<E>().listIterator();
      }
      return contents.listIterator();
   }


   /**
    * Returns a view of the portion of this list between the specified <tt>
    * fromIndex</tt> , inclusive, and <tt>toIndex</tt> , exclusive. (If <tt>
    * fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is
    * empty.) The returned list is backed by this list, so non-structural
    * changes in the returned list are reflected in this list, and vice-versa.
    * The returned list supports all of the optional list operations supported
    * by this list.
    *
    * @param inFromIndex low endpoint (inclusive) of the subList.
    * @param inToIndex   high endpoint (exclusive) of the subList.
    * @return the sublist
    */
   public List<E> subList(int inFromIndex, int inToIndex) {
      if( contents == null ) {
         return null;
      }
      return contents.subList(inFromIndex, inToIndex);
   }

   /**
    * Returns the hash code value for this list
    *
    * @return the hashCode
    */
   public int hashCode() {
      if( contents == null ) {
         return 0;
      }
      return contents.hashCode();
   }


   /**
    * Tests if this instance is equals to the passed object
    *
    * @param inOther The other object
    * @return True if this object is equals to the passed object
    */
   public boolean equals(Object inOther) {
      if( contents == null ) {
         return (inOther == null);
      }
      return contents.equals(inOther);
   }


   /**
    * Returns an array containing all of the elements in this list in proper
    * sequence; the runtime type of the returned array is that of the specified
    * array. Obeys the general contract of the <tt>Collection.toArray(Object[])
    * </tt> method.
    *
    * @param inSeed the array into which the elements of this list are to be
    *               stored, if it is big enough; otherwise, a new array of the same
    *               runtime type is allocated for this purpose.
    * @return an array containing the elements of this list.
    */
   public <T> T[] toArray(T[] inSeed){
      if( contents == null ) {
         return null;
      }
      return contents.toArray(inSeed);
   }



   /**
    * Returns an array containing all of the elements in this list in proper
    * sequence. Obeys the general contract of the <tt>Collection.toArray</tt>
    * method.
    *
    * @return an array containing all of the elements in this list in proper
    *         sequence.
    */
   public Object[] toArray() {
      if( contents == null ) {
         return null;
      }
      return getList().toArray();
   }


   /**
    * Returns a string representation of this object
    *
    * @return a string representation
    */
   public String toString() {

      if( contents == null ) {
         return "ListModel: <null>";
      }

      StringBuffer result = new StringBuffer("ListModel (");

      for( Iterator i = contents.iterator(); i.hasNext(); ) {
         Object o = i.next();
         result.append(o == null ? "<null>" : o.toString());
         if( i.hasNext() ) {
            result.append(TO_STRING_SEPARATOR);
         }
      }

      result.append(')');

      return result.toString();
   }

}
