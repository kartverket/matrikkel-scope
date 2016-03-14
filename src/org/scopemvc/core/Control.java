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


import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * A token passed up the chain of Controllers to invoke a piece of presentation
 * logic. Controllers match against the ID of a Control passed into {@link
 * org.scopemvc.core.Controller#handleControl handleControl} If the ID is
 * recognised then the Controller can execute some presentation logic. If the
 * Control ID is not recognised, the Control should be sent back on its journey
 * up the chain of responsibility by passing it to the parent Controller. See
 * {@link org.scopemvc.core.Controller#handleControl Controller.handleControl()}
 * and {@link org.scopemvc.controller.basic.BasicController#passControlToParent
 * BasicController.passControlToParent()}. </P> <P>
 *
 * Controls are received by a Controller from either a View or a child
 * Controller. </P> <P>
 *
 * Controls can optionally contain an Object parameter: see {@link
 * #getParameter} </P> <P>
 *
 * The Control ID is used by Controllers to recognise a Control and also as a
 * key to the user-readable version of the the Control's name in {@link
 * org.scopemvc.util.UIStrings UIStrings} presented to the user by the default
 * error-handling mechanism in {@link org.scopemvc.controller.basic.BasicController
 * BasicController}. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.10 $ $Date: 2002/10/11 15:47:08 $
 * @created 05 August 2002
 * @see View
 * @see Controller
 */
public class Control {

    private String id;

    private Object parameter;

    private boolean matched;


    /**
     * Create a Control with a unique String ID and no parameter.
     *
     * @param inID The unique ID identifying the Control
     */
    public Control(String inID) {
        if (inID == null) {
            throw new IllegalArgumentException("Can't create a Control with a null ControlID");
        }
        matched = false;
        id = inID;
    }


    /**
     * Create a Control with a unique String ID and a parameter.
     *
     * @param inID The unique ID identifying the Control
     * @param inParameter A parameter for the Control
     */
    public Control(String inID, Object inParameter) {
        this(inID);
        parameter = inParameter;
    }


    /**
     * For matching a Control in a Controller's doHandleControl, always use
     * {@link #matchesID} not this method.
     *
     * @return The name value
     */
    public final String getID() {
        return id;
    }


    /**
     * Return the Control ID for this Control. <br>
     * For matching a Control in a Controller's doHandleControl, always use
     * {@link #matchesID}, not this method.
     *
     * @return The name value
     * @deprecated use {@link #matchesID} not this method which will be removed
     *      at some point.
     */
    public final String getName() {
        return id;
    }


    /**
     * Has this Control been matched by a Controller yet?
     *
     * @return The matched value
     */
    public final boolean isMatched() {
        return matched;
    }


    /**
     * Get the parameter for the Control
     *
     * @return The parameter value
     */
    public final Object getParameter() {
        return parameter;
    }


    /**
     * Set a parameter for the Control
     *
     * @param inParameter The new parameter value
     */
    public final void setParameter(Object inParameter) {
        parameter = inParameter;
    }


    /**
     * Mark the Control as unmatched.
     */
    public final void markUnmatched() {
        matched = false;
    }


    /**
     * Mark the Control as matched. <br>
     * Matched controls won't be handled to the parent controller.
     */
    public final void markMatched() {
        matched = true;
    }


    /**
     * Tests if this Control matches the given ID. <br>
     * Use this method in Controller's doHandleControl to discover Controls that
     * you want to handle.
     *
     * @param inID The ID to test against
     * @return true if this control ID matches the passed ID.
     */
    public final boolean matchesID(String inID) {
        if (Debug.ON) {
            Debug.assertTrue(id != null);
        }
        if (inID == null) {
            throw new IllegalArgumentException("Can't match against a null ID.");
        }
        if (matched) {
            throw new RuntimeException("Already matched this Control once.");
        }

        matched = (id.equals(inID));
        return matched;
    }


    /**
     * For use by a ControlException handler only, not for application writers.
     *
     * @param inException exception to populate with info from this Control.
     */
    public void populateControlException(ControlException inException) {
        inException.setSourceControlID(id);
    }


    /**
     * Tests for equality
     *
     * @param inObject the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj argument;
     *      <code>false</code> otherwise.
     */
    public boolean equals(Object inObject) {
        if (!(inObject instanceof Control)) {
            return false;
        }
        Control o = (Control) inObject;
        return (id.equals(o.id)
                && (parameter == null && o.parameter == null)
                || (parameter != null && parameter.equals(o.parameter)));
    }


    /**
     * Returns the hashCode
     *
     * @return the hashCode
     */
    public int hashCode() {
        return id.hashCode();
    }


    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        return "Control(" + id + "," + parameter + ")";
    }
}
