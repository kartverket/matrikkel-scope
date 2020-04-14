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
 * $Id: SButton.java,v 1.13 2002/10/11 15:46:07 ludovicc Exp $
 * Changes:
 *  - added constructor SButton(String s, Icon icon)
 *  - added constructor SButton(Action action)
 *  - added constructor SButton(Icon icon)
 *  - added setActionID(id) in deprecation of setControlID(id) (scope 2.0)
 */
package org.scopemvc.view.swing;

import java.awt.event.ActionEvent;
import javax.swing.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.View;
import org.scopemvc.util.UIStrings;

/**
 * <P>
 *
 * A JButton that issues a Control when pressed. <br>
 * The button label comes from UIStrings keyed against the Control ID. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.13 $ $Date: 2002/10/11 15:46:07 $
 * @created 05 September 2002
 */
public class SButton extends JButton implements View {

    private static final Log LOG = LogFactory.getLog(SButton.class);

    private String controlID;

    /**
     * Constructor for the SButton object
     */
    public SButton() {
        this((String) null);
    }

    /**
     * Constructor for the SButton object
     *
     * @param inControlID The ID of the control to be issued when this button is
     *      pressed.
     */
    public SButton(String inControlID) {
        setControlID(inControlID);
    }

    public SButton(Icon icon) {
        super(icon);
    }

    public SButton(Action action) {
        super(action);
    }

    public SButton(String s, Icon icon) {
        super(s, icon);
    }



    /**
     * Constructor for the SButton object
     *
     * @param inAction The action to attach to this button.
     */
    public SButton(SAction inAction) {
        super(inAction);
    }

    // -------------- implement View ------------------

    /**
     * Gets the Controller for this View. <br>
     * Don't assign a Controller to this component, instead delegate to the
     * containing SwingView that has a parent Controller.
     *
     * @return The controller value - always null here
     */
    public Controller getController() {
        return null;
    }

    /**
     * Gets the model object bound to this View.
     *
     * @return The boundModel value - always null here
     */
    public Object getBoundModel() {
        return null;
    }

    /**
     * Gets the control ID
     *
     * @return The controlID value
     */
    public String getControlID() {
        return controlID;
    }

    /**
     * Issue a Control to the View's parent (owner) Controller.
     *
     * @param inControl The Control to issue
     */
    public void issueControl(Control inControl) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("issueControl: control: " + inControl);
        }
        SwingUtil.issueControl(this, inControl);
    }

    /**
     * Sets the control ID
     *
     * @param inControlID The new controlID value
     * @deprecated Will be removed in Scope 2.0, use setActionID(..)
     */
    public void setControlID(String inControlID) {
        controlID = inControlID;
        if (controlID != null && (getText() == null || getText().length() == 0)) {
            setText(UIStrings.get(controlID));
        }
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setControlID(..)</code> for now.
     *
     * @param actionID the Id of an action
     */
    public void setActionID(String actionID) {
        setControlID(actionID);
    }

    /**
     * Don't assign a Controller to this component, instead delegate to the
     * containing SwingView that has a parent Controller.
     *
     * @param inController The new controller value
     */
    public void setController(Controller inController) {
        throw new UnsupportedOperationException("Can't assign a Controller to a " + getClass());
    }

    /**
     * Sets the bound model. <br>
     * Does nothing here.
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        // noop
    }

    /**
     * Sets the <code>Action</code> for the button. <br>
     * The SButton will use the same control ID as the action.
     *
     * @param inAction the <code>Action</code> for the button, or <code>null.</code>
     *      <br>
     *      The action must be an instance of <code>SAction</code>.
     * @since 1.3
     * @see SAction
     */
    public void setAction(Action inAction) {
        if (inAction != null && !(inAction instanceof SAction)) {
            throw new IllegalArgumentException("Action must be an instance of SAction");
        }
        if (inAction != null) {
            setControlID(((SAction) inAction).getControlID());
        }
        super.setAction(inAction);
    }

    /**
     * Overriden to only issue a Control to the bound Controller.
     *
     * @param inEvent the <code>ActionEvent</code> object
     */
    protected void fireActionPerformed(ActionEvent inEvent) {
        if (controlID != null) {
            issueControl(createControl());
        }
    }


    /**
     * Override this to create something other than a simple no-parameter
     * Control or ModelActionControl.
     *
     * @return Control issued when button pressed: here a simple no-parameter
     *      Control
     */
    protected Control createControl() {
        if (controlID == null) {
            throw new RuntimeException("Can't create a Control because no ControlID set.");
        }

        return new Control(controlID);
    }
}
