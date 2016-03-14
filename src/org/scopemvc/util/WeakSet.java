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
package org.scopemvc.util;


import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Holds a set of objects where the fact that the object is contained within the
 * set does not prevent it from being garbage collected. Sort of like
 * WeakHashMap only no values. Could have used WeakHashMap and just set the
 * value to null I guess???
 *
 * @author <A HREF="mailto:haruki_zaemon@users.sourceforge.net">Simon Harris</A>
 * @version $Revision: 1.7 $
 * @created 05 September 2002
 */
public class WeakSet implements Set {

    private static final Log LOG = LogFactory.getLog(WeakSet.class);

    /**
     * The set of objects wrapped in References
     */
    private final Set contents = new HashSet();

    /**
     * References get placed here after they are cleaned up
     */
    private final ReferenceQueue referenceQueue = new ReferenceQueue();

    /**
     * Default constructor.
     */
    public WeakSet() { }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    public boolean isEmpty() {
        processQueue();
        return contents.isEmpty();
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.
     *
     * @param c elements to be retained in this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>retainAll</tt> method is
     *      not supported by this Collection.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection c) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this collection
     * contains at least one element <tt>e</tt> such that <tt>(o==null ? e==null
     * : o.equals(e))</tt> .
     *
     * @param o element whose presence in this collection is to be tested.
     * @return <tt>true</tt> if this collection contains the specified element
     */
    public boolean contains(Object o) {
        processQueue();
        return contents.contains(WeakEntry.create(o));
    }

    /**
     * Returns an array containing all of the elements in this collection whose
     * runtime type is that of the specified array. If the collection fits in
     * the specified array, it is returned therein. Otherwise, a new array is
     * allocated with the runtime type of the specified array and the size of
     * this collection.<p>
     *
     * If this collection fits in the specified array with room to spare (i.e.,
     * the array has more elements than this collection), the element in the
     * array immediately following the end of the collection is set to <tt>null
     * </tt>. This is useful in determining the length of this collection <i>
     * only</i> if the caller knows that this collection does not contain any
     * <tt>null</tt> elements.)<p>
     *
     * If this collection makes any guarantees as to what order its elements are
     * returned by its iterator, this method must return the elements in the
     * same order.<p>
     *
     * Like the <tt>toArray</tt> method, this method acts as bridge between
     * array-based and collection-based APIs. Further, this method allows
     * precise control over the runtime type of the output array, and may, under
     * certain circumstances, be used to save allocation costs<p>
     *
     * Suppose <tt>l</tt> is a <tt>List</tt> known to contain only strings. The
     * following code can be used to dump the list into a newly allocated array
     * of <tt>String</tt> : <pre>
     *    String[] x = (String[]) v.toArray(new String[0]);
     * </pre><p>
     *
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt> .
     *
     * @param a the array into which the elements of this collection are to be
     *      stored, if it is big enough; otherwise, a new array of the same
     *      runtime type is allocated for this purpose.
     * @return an array containing the elements of this collection
     * @throws ArrayStoreException the runtime type of the specified array is
     *      not a supertype of the runtime type of every element in this
     *      collection.
     */
    public Object[] toArray(Object[] a) {
        processQueue();
        return getEntrySet().toArray(a);
    }

    /**
     * Removes all this collection's elements that are also contained in the
     * specified collection (optional operation). After this call returns, this
     * collection will contain no elements in common with the specified
     * collection.
     *
     * @param c elements to be removed from this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>removeAll</tt> method is
     *      not supported by this collection.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns an array containing all of the elements in this collection. If
     * the collection makes any guarantees as to what order its elements are
     * returned by its iterator, this method must return the elements in the
     * same order.<p>
     *
     * The returned array will be "safe" in that no references to it are
     * maintained by this collection. (In other words, this method must allocate
     * a new array even if this collection is backed by an array). The caller is
     * thus free to modify the returned array.<p>
     *
     * This method acts as bridge between array-based and collection-based APIs.
     *
     * @return an array containing all of the elements in this collection
     */
    public Object[] toArray() {
        processQueue();
        return getEntrySet().toArray();
    }

    /**
     * Returns an iterator over the elements in this collection. There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     *
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    public Iterator iterator() {
        processQueue();
        return new WeakIterator();
    }

    /**
     * Removes a single instance of the specified element from this collection,
     * if it is present (optional operation). More formally, removes an element
     * <tt>e</tt> such that <tt>(o==null ? e==null : o.equals(e))</tt> , if this
     * collection contains one or more such elements. Returns true if this
     * collection contained the specified element (or equivalently, if this
     * collection changed as a result of the call).
     *
     * @param o element to be removed from this collection, if present.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException remove is not supported by this
     *      collection.
     */
    public boolean remove(Object o) {
        return contents.remove(WeakEntry.create(o));
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * This collection will be empty after this method returns unless it throws
     * an exception.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> method is not
     *      supported by this collection.
     */
    public void clear() {
        contents.clear();
    }

    /**
     * Returns the hash code value for this collection. While the <tt>Collection
     * </tt> interface adds no stipulations to the general contract for the <tt>
     * Object.hashCode</tt> method, programmers should take note that any class
     * that overrides the <tt>Object.equals</tt> method must also override the
     * <tt>Object.hashCode</tt> method in order to satisfy the general contract
     * for the <tt>Object.hashCode</tt> method. In particular, <tt>c1.equals(c2)
     * </tt> implies that <tt>c1.hashCode()==c2.hashCode()</tt> .
     *
     * @return the hash code value for this collection
     * @see Object#hashCode()
     * @see Object#equals(Object)
     */
    public int hashCode() {
        processQueue();
        return contents.hashCode();
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation). The behavior of this operation is undefined if the
     * specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param c elements to be inserted into this collection.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if this collection does not support
     *      the <tt>addAll</tt> method.
     * @throws ClassCastException if the class of an element of the specified
     *      collection prevents it from being added to this collection.
     * @throws IllegalArgumentException some aspect of an element of the
     *      specified collection prevents it from being added to this
     *      collection.
     * @see #add(Object)
     */
    public boolean addAll(Collection c) {
        boolean changed = false;
        for (Iterator i = c.iterator(); i.hasNext(); ) {
            changed |= add(i.next());
        }
        return changed;
    }

    /**
     * Returns the number of elements in this collection. If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns <tt>
     * Integer.MAX_VALUE</tt> .
     *
     * @return the number of elements in this collection
     */
    public int size() {
        processQueue();
        return contents.size();
    }

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements in
     * the specified collection.
     *
     * @param c collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements in
     *      the specified collection
     * @see #contains(Object)
     */
    public boolean containsAll(Collection c) {
        processQueue();
        return getEntrySet().containsAll(c);
    }

    /**
     * Ensures that this collection contains the specified element (optional
     * operation). Returns <tt>true</tt> if this collection changed as a result
     * of the call. (Returns <tt>false</tt> if this collection does not permit
     * duplicates and already contains the specified element.)<p>
     *
     * Collections that support this operation may place limitations on what
     * elements may be added to this collection. In particular, some collections
     * will refuse to add <tt>null</tt> elements, and others will impose
     * restrictions on the type of elements that may be added. Collection
     * classes should clearly specify in their documentation any restrictions on
     * what elements may be added.<p>
     *
     * If a collection refuses to add a particular element for any reason other
     * than that it already contains the element, it <i>must</i> throw an
     * exception (rather than returning <tt>false</tt> ). This preserves the
     * invariant that a collection always contains the specified element after
     * this call returns.
     *
     * @param o element whose presence in this collection is to be ensured.
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException add is not supported by this
     *      collection.
     * @throws ClassCastException class of the specified element prevents it
     *      from being added to this collection.
     * @throws IllegalArgumentException some aspect of this element prevents it
     *      from being added to this collection.
     */
    public boolean add(Object o) {
        return contents.add(WeakEntry.create(o, referenceQueue));
    }

    /**
     * Compares the specified object with this collection for equality. <p>
     *
     * While the <tt>Collection</tt> interface adds no stipulations to the
     * general contract for the <tt>Object.equals</tt> , programmers who
     * implement the <tt>Collection</tt> interface "directly" (in other words,
     * create a class that is a <tt>Collection</tt> but is not a <tt>Set</tt> or
     * a <tt>List</tt> ) must exercise care if they choose to override the <tt>
     * Object.equals</tt> . It is not necessary to do so, and the simplest
     * course of action is to rely on <tt>Object</tt> 's implementation, but the
     * implementer may wish to implement a "value comparison" in place of the
     * default "reference comparison." (The <tt>List</tt> and <tt>Set</tt>
     * interfaces mandate such value comparisons.)<p>
     *
     * The general contract for the <tt>Object.equals</tt> method states that
     * equals must be symmetric (in other words, <tt>a.equals(b)</tt> if and
     * only if <tt>b.equals(a)</tt> ). The contracts for <tt>List.equals</tt>
     * and <tt>Set.equals</tt> state that lists are only equal to other lists,
     * and sets to other sets. Thus, a custom <tt>equals</tt> method for a
     * collection class that implements neither the <tt>List</tt> nor <tt>Set
     * </tt> interface must return <tt>false</tt> when this collection is
     * compared to any list or set. (By the same logic, it is not possible to
     * write a class that correctly implements both the <tt>Set</tt> and <tt>
     * List</tt> interfaces.)
     *
     * @param o Object to be compared for equality with this collection.
     * @return <tt>true</tt> if the specified object is equal to this collection
     * @see Object#equals(Object)
     * @see java.util.Set#equals(Object)
     * @see java.util.List#equals(Object)
     */
    public boolean equals(Object o) {
        processQueue();
        return contents.equals(o);
    }

    /**
     * Creates an unwrappered copy of the contents
     *
     * @return The entrySet value
     */
    private final Set getEntrySet() {
        Set temp = new HashSet();
        for (Iterator i = contents.iterator(); i.hasNext(); ) {
            temp.add(i.next());
        }
        return temp;
    }

    /**
     * Removes any entries that have been garbage collected. I saw a note in
     * WeakHashMap that warns against calling this any mutators so I have taken
     * the same approach here.
     */
    private final void processQueue() {
        Reference reference = null;
        while ((reference = referenceQueue.poll()) != null) {
            contents.remove(reference);
        }
    }

    /**
     * Wraps an entry with a weak reference.
     *
     * @author lclaude
     * @version $Revision: 1.7 $
     * @created 05 September 2002
     */
    static class WeakEntry extends WeakReference {
        private final int hashCode;

        private WeakEntry(Object entry) {
            super(entry);
            hashCode = entry.hashCode();
        }

        private WeakEntry(Object entry, ReferenceQueue referenceQueue) {
            super(entry, referenceQueue);
            hashCode = entry.hashCode();
        }

        /**
         * TODO: document the method
         *
         * @param entry TODO: Describe the Parameter
         * @return TODO: Describe the Return Value
         */
        static WeakEntry create(Object entry) {
            if (entry != null) {
                return new WeakEntry(entry);
            } else {
                return null;
            }
        }

        /**
         * TODO: document the method
         *
         * @param entry TODO: Describe the Parameter
         * @param referenceQueue TODO: Describe the Parameter
         * @return TODO: Describe the Return Value
         */
        static WeakEntry create(Object entry, ReferenceQueue referenceQueue) {
            if (entry != null) {
                return new WeakEntry(entry, referenceQueue);
            } else {
                return null;
            }
        }

        /**
         * TODO: document the method
         *
         * @return TODO: Describe the Return Value
         */
        public int hashCode() {
            return hashCode;
        }

        /**
         * TODO: document the method
         *
         * @param o TODO: Describe the Parameter
         * @return TODO: Describe the Return Value
         */
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (!(o instanceof WeakEntry)) {
                return false;
            }

            Object t = this.get();
            Object u = ((WeakEntry) o).get();
            if (Debug.ON) {
                Debug.assertTrue(!(t == null && u == null));
            }
            if ((t == null) || (u == null)) {
                return false;
            } else if (t == u) {
                return true;
            } else {
//                if (LOG.isDebugEnabled()) LOG.debug("equals: " + t + ", " + u);
                return t.equals(u);
            }
        }
    }

    /**
     * Smart iterator over the contents. Smart because it handles the effects of
     * garbage collection behind its back.
     *
     * @author <A HREF="mailto:haruki_zaemon@users.sourceforge.net">Simon Harris
     *      </A>
     * @version $Revision: 1.7 $
     * @created 05 September 2002
     */
    class WeakIterator implements Iterator {
        /**
         * The "real" iterator over the contents
         */
        private final Iterator iterator = contents.iterator();

        /**
         * Has the caller obtained the next object yet?
         */
        private boolean gotNext = true;

        /**
         * Is there a next object?
         */
        private boolean hasNext = false;

        /**
         * The next object (if any)
         */
        private Object next = null;

        /**
         * Returns <tt>true</tt> if the iteration has more elements. (In other
         * words, returns <tt>true</tt> if <tt>next</tt> would return an element
         * rather than throwing an exception.)
         *
         * @return <tt>true</tt> if the iterator has more elements.
         */
        public boolean hasNext() {
            if (gotNext) {
                next = null;
                hasNext = false;
                while (iterator.hasNext()) {
                    Reference reference = (Reference) iterator.next();
                    if (reference != null) {
                        next = reference.get();
                        if (next != null) {
                            hasNext = true;
                            break;
                        }
                    } else {
                        hasNext = true;
                        break;
                    }
                }
            }
            gotNext = false;
            return hasNext;
        }

        /**
         * Returns the next element in the interation.
         *
         * @return the next element in the iteration.
         */
        public Object next() {
            if (hasNext()) {
                Object o = next;
                next = null;
                gotNext = true;
                return o;
            }
            throw new NoSuchElementException();
        }

        /**
         * Removes from the underlying collection the last element returned by
         * the iterator (optional operation). This method can be called only
         * once per call to <tt>next</tt> . The behavior of an iterator is
         * unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         */
        public void remove() {
            next = null;
            gotNext = true;
            iterator.remove();
        }
    }
}
