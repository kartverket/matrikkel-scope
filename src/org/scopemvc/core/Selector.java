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
package org.scopemvc.core;


import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * An identifier for model properties. Selectors are created by the factory
 * methods {@link #fromString} and {@link #fromInt}. Properties can be
 * identified by a String name (eg the "address" property of a Customer) or an
 * integer index (eg element 1 of a List). </P> <P>
 *
 * Selectors can be assembled in a list to identify a property in a model
 * contained within another model. For example, the <CODE>name</CODE> of the
 * <CODE>pet</CODE> of a <CODE>Person</CODE>. This Selector would be created by
 * <CODE>Selector.fromString("pet.name")</CODE> and applied to a Person model
 * object. Similarly, the name of a Person's first pet could be identified using
 * <CODE>Selector.fromString("pets.0.name")</CODE> assuming that Person contains
 * a List or array of Pets that contain a name property. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.13 $ $Date: 2002/09/19 15:18:00 $
 * @created 05 August 2002
 */
public abstract class Selector {

    /**
     * Separator character between Selectors expressed as a String.
     *
     * @see #fromString
     * @see #asString
     */
    public static final String DELIMITER = ".";

    private static final Log LOG = LogFactory.getLog(Selector.class);

    /**
     * Link to next Selector in the list.
     */
    private Selector next;


    /**
     * Package private ctor for the factory. Application code should use {@link
     * Selector#fromString} or {@link Selector#fromInt} to create Selectors.
     */
    Selector() { }


    // -------------------- Factory -------------------------

    /**
     * Make a simple Selector to identify a property at an int index. eg this
     * returns a Selector that identifies the first element of a List: <PRE>
     * return Selector.fromInt(0);
     * </PRE>
     *
     * @param inIndex The index of the property in a List
     * @return A selector identifying a property in a List
     */
    public static IntIndexSelector fromInt(int inIndex) {
        return new IntIndexSelector(inIndex);
    }


    /**
     * Make a Selector to identify a property by its String name, or to create a
     * Selector list that identifies a property by navigating a hierarchy of
     * submodels. For example:
     * <UL>
     *   <LI> <CODE>Selector.fromString("name");</CODE> will identify the name
     *   property of a Person when applied to a Person model object. </LI>
     *   <CODE>Selector.fromString("pet.0.name");</CODE> will identify the name
     *   property of the first Pet of a Person when applied to a Person model
     *   object.
     * </UL>
     *
     *
     * @param inSelectorDescription The string description of a Selector
     * @return A selector identifying a property
     */
    public static Selector fromString(String inSelectorDescription) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("fromString: " + inSelectorDescription);
        }

        StringTokenizer tokenizer = new StringTokenizer(inSelectorDescription, DELIMITER);
        Selector result = null;

        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            // Empty property descriptor is a null Selector
            if (nextToken.length() < 1) {
                continue;
            }

            Selector nextSelector = null;
            // OK then, does it parse as an int so we get an IntIndexSelector?
            // TODO: this is a bit nasty. could use [0] notation to mark this?
            try {
                int intIndex = Integer.parseInt(nextToken);
                nextSelector = new IntIndexSelector(intIndex);
            } catch (NumberFormatException e) {
                // OK then so make a StringIndexSelector
                nextSelector = new StringIndexSelector(nextToken);
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("fromString: nextToken " + nextToken + ": nextSelector " + nextSelector);
            }
            if (Debug.ON) {
                Debug.assertTrue(nextSelector != null, "Couldn't create Selector for: " + nextToken);
            }

            if (result == null) {
                result = nextSelector;
            } else {
                result.chain(nextSelector);
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("fromString: result: " + result);
            }
        }
        return result;
    }


    /**
     * Flatten the Selector list to a String that could be passed into {@link
     * #fromString} to recreate it.
     *
     * @param inSelector flatten this Selector to a String description.
     * @return String description of the passed Selector suitable for passing
     *      back into {@link #fromString} to recreate the Selector list. Return
     *      "" for null Selector.
     */
    public static String asString(Selector inSelector) {
        if (inSelector == null) {
            return "";
        }

        StringBuffer result = new StringBuffer();
        Selector current = inSelector;
        while (current != null) {
            result.append(current.getName());
            result.append(DELIMITER);
            current = current.getNext();
        }
        return result.substring(0, result.length() - DELIMITER.length());
    }


    /**
     * Get the next Selector in the list, if any. For example, <PRE>
     * Selector petNameSelector = Selector.fromString("pet.name");
     * return petNameSelector.getNext();
     * </PRE> returns a Selector that is <CODE>equals()</CODE> the following
     * Selector: <PRE>
     * Selector.fromString("name");
     * </PRE>
     *
     * @return The next value
     */
    public final Selector getNext() {
        return next;
    }


    /**
     * Get the last Selector in the list. For example, <PRE>
     * Selector petNameSelector = Selector.fromString("pets.1.name");
     * return petNameSelector.getLast();
     * </PRE> returns a Selector that is <CODE>equals()</CODE> the following
     * Selector: <PRE>
     * Selector.fromString("name");
     * </PRE>
     *
     * @return The last value
     */
    public final Selector getLast() {
        Selector result = this;
        while (result.getNext() != null) {
            result = result.getNext();
        }

        if (Debug.ON) {
            Debug.assertTrue(result != null);
        }
        return result;
    }


    /**
     * Used to serialise Selectors {@link #asString} and for debug by {@link
     * #toString}.
     *
     * @return The name value
     * @todo public access for test cases -- this is nasty.
     */
    public abstract String getName();


    /**
     * <P>
     *
     * Add a Selector on the end of this list. </P> <P>
     *
     * For example: <PRE>
     * Selector petSelector = Selector.fromString("pet");
     * Selector nameSelector = Selector.fromString("name");
     * petSelector.chain(nameSelector);
     * return petSelector;
     * </PRE> returns a Selector that is <CODE>equals()</CODE> this one: <PRE>
     * Selector.fromString("pet.name");
     * </PRE> </P>
     *
     * @param inSelector The Selector to chain at the end of the current
     *      Selector
     */
    public final void chain(Selector inSelector) {
        if (inSelector == null) {
            throw new IllegalArgumentException("Can't chain a null Selector.");
        }

        ((Selector) getLast()).setNext(inSelector);
    }


    /**
     * Remove the terminal Selector. Throws UnsupportedOperationException if
     * Selector has no chain.
     */
    public final void removeLast() {
        if (getNext() == null) {
            throw new UnsupportedOperationException("No terminal Selector to remove");
        }
        Selector penultimate = this;
        while (penultimate.getNext().getNext() != null) {
            penultimate = penultimate.getNext();
        }
        penultimate.setNext(null);
    }


    /**
     * Remove the terminal Selector. Throws UnsupportedOperationException if
     * Selector has no chain or does no ends with the passed terminal selector.
     *
     * @param inTerminalSelector The Selector to remove from the end of this
     *      Selector
     */
    public final void removeLast(Selector inTerminalSelector) {
        if (getNext() == null) {
            throw new UnsupportedOperationException("No terminal Selector to remove");
        }
        Selector beforeTerminal = this;
        while (beforeTerminal.getNext().getNext() != null && !beforeTerminal.getNext().equals(inTerminalSelector)) {
            beforeTerminal = beforeTerminal.getNext();
        }
        if (!beforeTerminal.getNext().equals(inTerminalSelector)) {
            throw new UnsupportedOperationException("Terminal Selector " + inTerminalSelector
                    + " doesn't end Selector " + this);
        }
        beforeTerminal.setNext(null);
    }

    /**
     * <P>
     *
     * Does this Selector list start with the list passed in? </P> <P>
     *
     * For example, this returns <CODE>true</CODE>: <PRE>
     * Selector petSelector = Selector.fromString("pet");
     * Selector petNameSelector = Selector.fromString("pet.name");
     * return (petNameSelector.startsWith(petSelector));
     * </PRE> but this returns <CODE>false</CODE>: <PRE>
     * Selector nameSelector = Selector.fromString("name");
     * Selector petNameSelector = Selector.fromString("pet.name");
     * return (petNameSelector.startsWith(nameSelector));
     * </PRE> </P>
     *
     * @param inSelector A Selector
     * @return True if this Selector list start with the list passed in
     */
    public final boolean startsWith(Selector inSelector) {
        Selector matchedTarget = inSelector;
        Selector matchedThis = this;
        while (matchedTarget != null) {
            if (matchedThis == null) {
                // passed in is longer than this
                return false;
            }
            if (matchedThis.shallowEquals(matchedTarget)) {
                matchedThis = matchedThis.getNext();
                matchedTarget = matchedTarget.getNext();
            } else {
                // No match
                return false;
            }
        }
        return true;
    }


    /**
     * <P>
     *
     * A deep compare, following down the list of selectors. </P> <P>
     *
     * For example, this returns <CODE>true</CODE>: <PRE>
     * Selector petSelector = Selector.fromString("pet");
     * Selector nameSelector = Selector.fromString("name");
     * Selector petNameSelector = Selector.fromString("pet.name");
     * petSelector.chain(nameSelector);
     * return (petSelector.equals(petNameSelector));
     * </PRE> but this returns <CODE>false</CODE>: <PRE>
     * Selector petSelector = Selector.fromString("pet");
     * Selector petNameSelector = Selector.fromString("pet.name");
     * return (petSelector.equals(petNameSelector));
     * </PRE> </P>
     *
     * @param inObject An Object to test for equality, mostly another Selector
     * @return true if this Selector and the passed object are equal, including
     *      the chained Selectors in the list.
     */
    public final boolean equals(Object inObject) {

        // Trivial case: same object
        if (inObject == this) {
            return true;
        }

        // null or wrong class
        if (!(inObject instanceof Selector)) {
            return false;
        }

        // try the shallow equals
        Selector inSelector = (Selector) inObject;
        if (!shallowEquals(inSelector)) {
            return false;
        }

        if (getNext() == null) {
            if (inSelector.getNext() == null) {
                return true;
            }
            return false;
        }

        // and recurse down the list
        return getNext().equals(inSelector.getNext());
    }


    /**
     * Return a clone of the entire list of Selectors from <CODE>this</CODE>.
     *
     * @return A complete close of this Selector.
     * @post result.equals(this)
     */
    public final Selector deepClone() {
        Selector result = getShallowCopy();
        if (getNext() != null) {
            result.chain(getNext().deepClone());
        }
        return result;
    }

    /**
     * Conveniance method. <br>
     * Returns the description of the selector
     *
     * @return the description
     * @see #asString
     */
    public final String toStringDescription() {
        return asString(this);
    }

    // ------------------- Debug -----------------------

    /**
     * For debug.
     *
     * @return A string representation of this Selector
     */
    public final String toString() {
        StringBuffer result = new StringBuffer();
        Package p = getClass().getPackage();
        if (p == null) {
            result.append(getClass().getName());
        } else {
            result.append(getClass().getName().substring(p.getName().length() + 1));
        }
        result.append("(");
        result.append(Selector.asString(this));
        result.append(")");
        return result.toString();
    }


    /**
     * Return a shallow copy of the head of <CODE>this</CODE>.
     *
     * @return The shallowCopy value
     */
    protected abstract Selector getShallowCopy();


    /**
     * Set the next Selector in the list.
     *
     * @param inSelector Selector to set as the next in the list after <CODE>this</CODE>
     * @see #chain
     */
    protected final void setNext(Selector inSelector) {
        next = inSelector;
    }


    /**
     * Compare the head Selector of <CODE>this</CODE> against the head of
     * another Selector list -- ie a shallow compare operation (not including
     * the chained selectors).
     *
     * @param inSelector The Selector to test
     * @return true if there is equality between this Selector and the other
     *      Selector, excluding the other chained Selectors.
     */
    protected abstract boolean shallowEquals(Selector inSelector);
}

