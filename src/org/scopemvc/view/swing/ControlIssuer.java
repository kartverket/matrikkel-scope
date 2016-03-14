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
package org.scopemvc.view.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.ScopeConfig;

/**
 * Helper class for managing the way controls are issued in some SComponents.
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.5 $
 * @created 29 October 2002
 */
public abstract class ControlIssuer extends FocusAdapter implements ActionListener {

    /**
     * Never issue a control
     */
    public static final int ISSUE_NO_CONTROL = 0x00;

    /**
     * Never issue a control
     */
    public static final String ISSUE_NO_CONTROL_STRING = "never";

    /**
     * Issue a control when the user presses the Enter key
     */
    public static final int ISSUE_CONTROL_ON_ENTER_KEY = 0x01;

    /**
     * Issue a control when the user presses the Enter key
     */
    public static final String ISSUE_CONTROL_ON_ENTER_KEY_STRING = "onEnter";

    /**
     * Issue a control when the component loses focus
     */
    public static final int ISSUE_CONTROL_ON_LOST_FOCUS = 0x02;

    /**
     * Issue a control when the component loses focus
     */
    public static final String ISSUE_CONTROL_ON_LOST_FOCUS_STRING = "onLostFocus";

    /**
     * Issue a control only if the value of the component changed (since it
     * received the focus)
     */
    public static final int ISSUE_CONTROL_ONLY_ON_CHANGE = 0x04;

    /**
     * Issue a control only if the value of the component changed (since it
     * received the focus)
     */
    public static final String ISSUE_CONTROL_ONLY_ON_CHANGE_STRING = "onlyOnChange";

    private static final Log LOG = LogFactory.getLog(ControlIssuer.class);

    /**
     * The previous value.
     */
    private Object previousValue;

    /**
     * Settings for issuing controls
     *
     * @see ISSUE_CONTROL_ON_ENTER_KEY
     * @see ISSUE_CONTROL_ON_LOST_FOCUS
     * @see ISSUE_CONTROL_ONLY_ON_CHANGE
     */
    private int controlSettings = ISSUE_CONTROL_ON_ENTER_KEY
            | ISSUE_CONTROL_ON_LOST_FOCUS
            | ISSUE_CONTROL_ONLY_ON_CHANGE;

    /**
     * Constructor for the ControlIssuer
     *
     * @param inComponentType Type of the component using this ControlIssuer
     *      instance, used to read the default configuration from ScopeConfig.
     */
    public ControlIssuer(String inComponentType) {
        String lostFocusString = ScopeConfig.getString("org.scopemvc.view.swing.SFields.control_on_lost_focus");
        if (lostFocusString != null && !"true".equals(lostFocusString)) {
            LOG.warn("Deprecated configuration key: org.scopemvc.view.swing.SFields.control_on_lost_focus, "
                    + "use org.scopemvc.view.swing." + inComponentType + ".control_settings instead");
            controlSettings = ISSUE_CONTROL_ON_ENTER_KEY;
        }
        String controlSettingsString = ScopeConfig.getString("org.scopemvc.view.swing."
                + inComponentType + ".control_settings");
        if (controlSettingsString != null) {
            controlSettings = 0;
            StringTokenizer st = new StringTokenizer(controlSettingsString, ", ");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (ISSUE_NO_CONTROL_STRING.equals(token)) {
                    controlSettings = ISSUE_NO_CONTROL;
                    break;
                } else if (ISSUE_CONTROL_ON_ENTER_KEY_STRING.equals(token)) {
                    controlSettings |= ISSUE_CONTROL_ON_ENTER_KEY;
                } else if (ISSUE_CONTROL_ON_LOST_FOCUS_STRING.equals(token)) {
                    controlSettings |= ISSUE_CONTROL_ON_LOST_FOCUS;
                } else if (ISSUE_CONTROL_ONLY_ON_CHANGE_STRING.equals(token)) {
                    controlSettings |= ISSUE_CONTROL_ONLY_ON_CHANGE;
                }
            }
        }
    }

    /**
     * Return the control settings
     *
     * @return The controlSettings value
     */
    public final int getControlSettings() {
        return controlSettings;
    }

    /**
     * Defines the settings for controlling the way controls are issues. <br>
     * For example: setControlSettings(ISSUE_CONTROL_ON_ENTER_KEY |
     * ISSUE_CONTROL_ON_LOST_FOCUS)
     *
     * @param inControlSettings A constant or group of constants (made with the
     *      | operator) from SComponentConstants
     * @see ISSUE_CONTROL_ON_ENTER_KEY
     * @see ISSUE_CONTROL_ON_LOST_FOCUS
     * @see ISSUE_CONTROL_ONLY_ON_CHANGE
     */
    public final void setControlSettings(int inControlSettings) {
        controlSettings = inControlSettings;
    }

    /**
     * Reset the previous value stored in the control issuer (to force issuing a
     * control the next time enter key is pressed or focus is lost)
     */
    public void reset() {
        previousValue = null;
    }

    /**
     * Invoked when a component get the keyboard focus.
     *
     * @param inEvent The focus event
     */
    public void focusGained(FocusEvent inEvent) {
        previousValue = getComponentValue();
    }

    /**
     * Invoked when a component loses the keyboard focus.
     *
     * @param inEvent The focus event
     */
    public void focusLost(FocusEvent inEvent) {
        if (!inEvent.isTemporary()) {
            doViewChanged();
            if (needIssueControl(ISSUE_CONTROL_ON_LOST_FOCUS)) {
                doIssueControl();
            }
        }
    }

    /**
     * Invoked when an action is performed on the component
     *
     * @param inEvent The action event
     */
    public void actionPerformed(ActionEvent inEvent) {
        doViewChanged();
        if (needIssueControl(ISSUE_CONTROL_ON_ENTER_KEY)) {
            doIssueControl();
            previousValue = getComponentValue();
        }
    }

    /**
     * Gets the current value of the component
     *
     * @return The componentValue value
     */
    protected abstract Object getComponentValue();

    /**
     * Override this method to issue a control from the component
     */
    protected abstract void doIssueControl();

    /**
     * Override this method to update the model after the value stored in the
     * view has changed.
     */
    protected abstract void doViewChanged();

    private boolean needIssueControl(int inOnAction) {
        boolean goodAction = ((controlSettings & inOnAction) > 0);
        if ((controlSettings & (ISSUE_CONTROL_ONLY_ON_CHANGE)) > 0) {
            boolean changed = (previousValue == null || !previousValue.equals(getComponentValue()));
            return changed && goodAction;
        } else {
            return goodAction;
        }
    }
}

