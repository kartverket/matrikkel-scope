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
 * $Id: SetModel.java,v 1.9 2002/09/12 10:51:03 ludovicc Exp $
 */
package org.scopemvc.model.collection;


import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.Selector;
import org.scopemvc.util.Debug;
import org.scopemvc.model.basic.BasicModel;

/**
 * <P>
 *
 * A BasicModel wrapper for an <CODE>Set</CODE> that can propagate changes to
 * contained Models up the Model hierarchy. The set itself is exposed via the
 * "set" property, however, changes to this underlying set must be made through
 * this class's public API in order to maintain the event propagation. </P> <P>
 *
 * By default SetModel registers itself as a listener to Models that are added
 * to the list and deregisters when those Models are removed. This behaviour can
 * be changed at creation so that ModelChangeEvent propagation from contained
 * models is disabled. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.9 $
 */
public class SetModel extends BasicModel implements Set {

    /**
     * TODO: describe of the Field
     */
    public static final Selector SET = Selector.fromString("set");

    // ----------------- for debug -----------------------

    /**
     * TODO: describe of the Field
     */
    protected static final String TO_STRING_SEPARATOR = ", ";
    /**
     * TODO: describe of the Field
     */
    protected static final int TO_STRING_SEPARATOR_LENGTH = TO_STRING_SEPARATOR.length();

    private static final Log LOG = LogFactory.getLog(ListModel.class);

    /**
     * TODO: describe of the Field
     */
    protected Set contents;

    /**
     * TODO: describe of the Field
     */
    protected boolean propagateModelChanges;


    /**
     * Constructor for the SetModel object
     */
    public SetModel() {
        this(true, new HashSet());
    }


    /**
     * Constructor for the SetModel object
     *
     * @param inSet TODO: Describe the Parameter
     */
    public SetModel(Set inSet) {
        this(true, inSet);
    }


    /**
     * Constructor for the SetModel object
     *
     * @param inPropagateModelChanges TODO: Describe the Parameter
     */
    public SetModel(boolean inPropagateModelChanges) {
        propagateModelChanges = inPropagateModelChanges;
    }


    /**
     * Constructor for the SetModel object
     *
     * @param inPropagateModelChanges TODO: Describe the Parameter
     * @param inSet TODO: Describe the Parameter
     */
    public SetModel(boolean inPropagateModelChanges, Set inSet) {
        this(inPropagateModelChanges);
        setSet(inSet);
    }


    /**
     * Gets the set
     *
     * @return The set value
     */
    public Set getSet() {
        return contents;
    }


    /**
     * Gets the size
     *
     * @return The size value
     */
    public int getSize() {
        if (contents == null) {
            return 0;
        }
        return contents.size();
    }


    /**
     * Gets the empty
     *
     * @return The empty value
     */
    public boolean isEmpty() {
        return contents.isEmpty();
    }


    /**
     * Set contents to the passed Object list and fire a ModelChangeEvent.
     *
     * @param inContents The new set value
     */
    public void setSet(Set inContents) {

        if (propagateModelChanges) {
            // Clear up old list if it contained any Models
            if (contents != null) {
                for (Iterator i = contents.iterator(); i.hasNext(); ) {
                    Object o = i.next();
                    if (o instanceof ModelChangeEventSource) {
                        ((ModelChangeEventSource) o).removeModelChangeListener(this);
                    }
                }
            }
        }

        contents = inContents;

        if (propagateModelChanges) {
            // Register as listener to any Models in the new list
            if (contents != null) {
                for (Iterator i = contents.iterator(); i.hasNext(); ) {
                    Object o = i.next();
                    if (o instanceof ModelChangeEventSource) {
                        ((ModelChangeEventSource) o).addModelChangeListener(this);
                    }
                }
            }
        }

        fireModelChange(ModelChangeEvent.VALUE_CHANGED, SET);
    }


    /**
     * TODO: document the method
     *
     * @param inEvent TODO: Describe the Parameter
     */
    public void modelChanged(ModelChangeEvent inEvent) {
        if (Debug.ON) {
            Debug.assertTrue(inEvent != null);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("modelChanged: " + inEvent);
        }

        // Just say that the set changed somehow: can't select elements of a set
        fireModelChange(VALUE_CHANGED, null);
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

        for (Iterator i = contents.iterator(); i.hasNext(); ) {
            Object o = i.next();
            result.append(o == null ? "<null>" : o.toString());
            result.append(TO_STRING_SEPARATOR);
        }

        if (contents.size() > 0) {
            result.setLength(result.length() - TO_STRING_SEPARATOR_LENGTH);
        }
        result.append(')');

        return result.toString();
    }


    // -------------------------- implement Set ------------------------------

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public int size() {
        return contents.size();
    }


    /**
     * TODO: document the method
     *
     * @param arg0 TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean contains(Object arg0) {
        return contents.contains(arg0);
    }


    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public Iterator iterator() {
        return contents.iterator();
    }


    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public Object[] toArray() {
        return contents.toArray();
    }


    /**
     * TODO: document the method
     *
     * @param arg0 TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public Object[] toArray(Object[] arg0) {
        return contents.toArray(arg0);
    }


    /**
     * TODO: document the method
     *
     * @param arg0 TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean containsAll(Collection arg0) {
        return contents.containsAll(arg0);
    }


    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean add(Object o) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("add: " + o);
        }
        if (!contents.add(o)) {
            return false;
        }

        if (propagateModelChanges && o instanceof ModelChangeEventSource) {
            ((ModelChangeEventSource) o).addModelChangeListener(this);
        }
        fireModelChange(VALUE_ADDED, null);
        return true;
    }

    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean remove(Object o) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("remove: " + o);
        }
        if (!contents.remove(o)) {
            return false;
        }

        if (propagateModelChanges && o instanceof ModelChangeEventSource) {
            ((ModelChangeEventSource) o).removeModelChangeListener(this);
        }
        fireModelChange(VALUE_REMOVED, null);
        return true;
    }


    /**
     * Adds an element to the All attribute of the SetModel object
     *
     * @param c The element to be added to the All attribute
     * @return TODO: Describe the Return Value
     */
    public boolean addAll(Collection c) {
        boolean result = false;
        makeActive(false);
        try {
            for (Iterator i = c.iterator(); i.hasNext(); ) {
                result = result | add(i.next());
            }
        } finally {
            makeActive(true);
        }
        fireModelChange(VALUE_CHANGED, null);
        return result;
    }


    /**
     * TODO: document the method
     *
     * @param c TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean retainAll(Collection c) {
        Set original = new HashSet(contents);
        boolean result = false;
        makeActive(false);
        try {
            for (Iterator i = original.iterator(); i.hasNext(); ) {
                Object o = i.next();
                if (!c.contains(o)) {
                    remove(o);
                    result = true;
                }
            }
        } finally {
            makeActive(true);
        }
        fireModelChange(VALUE_CHANGED, null);
        return result;
    }


    /**
     * TODO: document the method
     *
     * @param c TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public boolean removeAll(Collection c) {
        boolean result = false;
        makeActive(false);
        try {
            for (Iterator i = c.iterator(); i.hasNext(); ) {
                result = result | remove(i.next());
            }
        } finally {
            makeActive(true);
        }
        fireModelChange(VALUE_CHANGED, null);
        return result;
    }


    /**
     * TODO: document the method
     */
    public void clear() {
        makeActive(false);
        try {
            if (propagateModelChanges) {
                for (Iterator i = contents.iterator(); i.hasNext(); ) {
                    Object o = i.next();
                    if (o instanceof ModelChangeEventSource) {
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
     * @return TODO: Describe the Return Value
     */
    public int hashCode() {
        return contents.hashCode();
    }
}
