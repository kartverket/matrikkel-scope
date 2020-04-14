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
 * $Id: STextField.java,v 1.32 2002/11/20 01:36:58 ludovicc Exp $
 */
package org.scopemvc.view.swing;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.Beans;

import javax.swing.Action;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.PropertyView;
import org.scopemvc.core.Selector;
import org.scopemvc.util.Debug;
import org.scopemvc.util.ScopeConfig;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;
import org.scopemvc.view.util.ModelBindable;
import org.scopemvc.Pointer;

/**
 * <P>
 *
 * A JTextField linked to a property of a bound model object. The property must
 * have a StringConvertor to handle conversion to and from a String
 * representation that will be edited in the textfield. Updates to the textfield
 * result in changes to the model property when focus is lost. </P> <P>
 *
 * STextField responds to the bound model or the particular bound property
 * becoming read-only by disabling itself. An STextField is also disabled if it
 * has no bound model or property. </P> <P>
 *
 * STextField can issue a Control when the user hits the Enter key or leaves the
 * field. </P> <P>
 *
 * Null properties are handled in one of two ways:
 * <OL>
 *   <LI> The field is disabled to prevent editing. </LI>
 *   <LI> The field is populated with an empty String. In this case, the bound
 *   property will contain an empty String not a null, if the textfield is
 *   populated with an empty string. </LI>
 * </OL>
 * The second option is the default but that can be changed by calling {@link
 * #setDisableOnNull}. </P> Similar to JTextField, VK_ENTER events are consumed
 * if there is any control ID defined for this text field, or if there are
 * additional action listeners (those used internally by Scope don't count). If
 * so, then VK_ENTER results in the listeners getting an ActionEvent, and the
 * VK_ENTER event is consumed. This is compatible with how AWT text fields
 * handle VK_ENTER events. If the text field has no action listeners, then as of
 * v 1.3 the VK_ENTER event is not consumed. Instead, the bindings of ancestor
 * components are processed, which enables the default button feature of
 * JFC/Swing to work.
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.32 $ $Date: 2002/11/20 01:36:58 $
 * @created 05 September 2002
 * @see SwingView
 */
public class STextField extends JTextField
         implements PropertyView, ModelBindable, Refreshable {

    private static final Log LOG = LogFactory.getLog(STextField.class);

    private static final Action[] DEFAULT_ACTIONS = {
            new NotifyAction()
            };

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    /**
     * Helper to manage validation state.
     */
    private ValidationHelper validationHelper = new ValidationHelper(this);

    /**
     * ID of the Control issued when user hits Enter in the STextField.
     */
    private String controlID;

    /**
     * STextField can "hold" a null when bound to a property that happens to be
     * null.
     */
    private boolean valueIsNull = false;

    /**
     * Does this textfield disable itself if the model property it is bound to
     * becomes null?
     */
    private boolean disableOnNull = false;

    /**
     * The StringConvertor used to convert the model property to and from the
     * String representation that the user edits in the textfield.
     */
    private StringConvertor stringConvertor;

    /**
     * If this is set, use it instead of finding a convertor to match the
     * datatype being edited.
     */
    private StringConvertor forcedStringConvertor;

    /**
     * True if the bound property is read-only. This property affects the
     * enabled state of the component.
     */
    private boolean readOnly = false;

    /**
     * Allows the user to define the enabled state of this component. <br>
     * The component cannot be forced to enabled=true if the bouund property is
     * read-only.
     */
    private boolean userEnabled = true;

    /**
     * Listens to user events and issue controls when necessary.
     */
    private ControlIssuer controlIssuer =
        new ControlIssuer("STextField") {
            protected Object getComponentValue() {
                try {
                    return getViewValue();
                } catch (Exception ignore) {
                    return null;
                }
            }

            protected void doViewChanged() {
                viewChanged();
            }

            protected void doIssueControl() {
                if (controlID != null) {
                    Control control = new Control(controlID);
                    issueControl(control);
                }
            }
        };


    /**
     * Constructor for the STextField object
     */
    public STextField() {
        this(12);
    }


    /**
     * Constructor for the STextField object
     *
     * @param inColumns The number of columns in the textfield
     */
    public STextField(int inColumns) {
        super(inColumns);
        addActionListener(controlIssuer);
        addFocusListener(controlIssuer);
        setReadOnly(!Beans.isDesignTime());
       addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent e) {
             selectAll();
          }

          public void focusLost(FocusEvent e) {
             select(0,0);
          }
       });
    }


    /**
     * @return true if the textfield should be disabled when it edits a null
     *      property value. Otherwise, a null property is treated as an empty
     *      String.
     */
    public final boolean isDisableOnNull() {
        return disableOnNull;
    }

    /**
     * Return the control settings
     *
     * @return The controlSettings value
     */
    public final int getControlSettings() {
        return controlIssuer.getControlSettings();
    }


    // ------------------- Delegate to BoundModel -------------------

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return boundModel.getBoundModel();
    }


    /**
     * Gets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The component will present this property to the user.
     *
     * @return A selector.
     */
    public final Selector getSelector() {
        return boundModel.getSelector();
    }


    // ---------- Issue a Control when user hits Enter in the STextfield --------------

    /**
     * Gets the control ID
     *
     * @return The controlID value
     */
    public final String getControlID() {
        return controlID;
    }


    /**
     * Get the current value (what would be set as a property of the bound model
     * object) being presented on the View.
     *
     * @return property value from parsing the textfield's current String
     *      representation.
     * @exception IllegalArgumentException if the conversion from String fails.
     */
    public Object getViewValue() throws IllegalArgumentException {
        String text = getText();

        if (stringConvertor == null
                || (valueIsNull && text.length() < 1)) {
            return null;
        }

        return stringConvertor.stringAsValue(text);
    }


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
     * Fetches the command list for the editor. This is the list of commands
     * supported by the plugged-in UI augmented by the collection of commands
     * that the editor itself supports. These are useful for binding to events,
     * such as in a keymap.
     *
     * @return the command list
     */
    public Action[] getActions() {
        return TextAction.augmentList(super.getActions(), DEFAULT_ACTIONS);
    }

    /**
     * Issue a Control to the View's parent (owner) Controller.
     *
     * @param inControl The Control to issue
     */
    public void issueControl(Control inControl) {
        SwingUtil.issueControl(this, inControl);
    }

    /**
     * Set the ID of the Control that will be issued when Enter key is pressed
     * in this STextField. If null no Control will be issued.
     *
     * @param inControlID The new controlID value
     * @deprecated Will be removed in Scope 2.0, use setActionID(..)
     */
    public final void setControlID(String inControlID) {
        controlID = inControlID;
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
     * Defines the settings for controlling the way controls are issues. <br>
     * For example: setControlSettings(ISSUE_CONTROL_ON_ENTER_KEY |
     * ISSUE_CONTROL_ON_LOST_FOCUS);
     *
     * @param inControlSettings A constant or group of constants (made with the
     *      | operator) from SComponentConstants
     * @see ControlIssuer#ISSUE_CONTROL_ON_ENTER_KEY
     * @see ControlIssuer#ISSUE_CONTROL_ON_LOST_FOCUS
     * @see ControlIssuer#ISSUE_CONTROL_ONLY_ON_CHANGE
     */
    public final void setControlSettings(int inControlSettings) {
        controlIssuer.setControlSettings(inControlSettings);
    }

    /**
     * Set this to true if the textfield should be disabled when it edits a null
     * property value. Otherwise, a null property is treated as an empty String.
     *
     * @param inDisable The new disableOnNull value
     */
    public final void setDisableOnNull(boolean inDisable) {
        disableOnNull = inDisable;
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setPointer(String pointerPath) {
        setSelector(pointerPath);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointer a pointer to the model
     */
    public void setPointer(Pointer pointer) {
        setSelector(pointer.getSelector());
    }


    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelector The new selector to use
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSelector(Selector inSelector) {
        boundModel.setSelector(inSelector);
        setupStringConvertor();
    }

    /**
     * Enables or disables this component, depending on the value of the
     * parameter <code>b</code>. An enabled component can respond to user input
     * and generate events. Components are enabled initially by default.
     *
     * @param inEnabled If <code>true</code>, this component is enabled;
     *      otherwise this component is disabled.
     */
    public void setEnabled(boolean inEnabled) {
        userEnabled = inEnabled;
        super.setEnabled(userEnabled && !readOnly);
    }


    /**
     * Sets the bound model.
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        boundModel.setBoundModel(inModel);
        setupStringConvertor();
    }

    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelectorString The string representation of the selector
     * @see Selector#fromString
     * @deprecated Will be removed in Scope 2.0
     */
    public void setSelector(String inSelectorString) {
        boundModel.setSelector(inSelectorString);
        setupStringConvertor();
    }

    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelectorString The string representation of the selector
     * @deprecated Use setSelector(String) instead
     */
    public void setSelectorString(String inSelectorString) {
        setSelector(inSelectorString);
    }

    /**
     * Force use of this StringConvertor instead of automatically finding one to
     * match the datatype being edited.
     *
     * @param inConvertor The new stringConvertor value
     */
    public void setStringConvertor(StringConvertor inConvertor) {
        forcedStringConvertor = inConvertor;
        stringConvertor = forcedStringConvertor;
    }


    /**
     * Override to call super.setText() only if new value not equals() old
     * value.
     *
     * @param inText new text.
     */
    public void setText(String inText) {
        if (Debug.ON) {
            Debug.assertTrue(getText() != null, "null getText()");
        }
        if (!getText().equals(inText)) {
            super.setText(inText);
            controlIssuer.doViewChanged();
        }
        setCaretPosition(0);
        controlIssuer.reset();
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
     * Updates the model object with the data coming from the View. <br>
     * This method is called automatically after some user action on the
     * component.
     */
    public void viewChanged() {
        boundModel.updateModel();
    }

    // --------------------- Implement ModelBindable ----------------------

    /**
     * Use the passed property value and read-only state to update the View.
     * <br>
     * Converts the incoming value to a String via appropriate {@link
     * org.scopemvc.util.convertor.StringConvertor}. For incoming null either
     * disable field or set text to empty String.
     *
     * @param inValue The new value of the property in the bound model
     * @param inReadOnly The new read-only state of the property
     * @todo Call to setupStringConvertor() is a hack. Need to revisit
     *      PropertyManager to traverse nulls and use metadata etc
     */
    public void updateFromProperty(Object inValue, boolean inReadOnly) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromProperty: " + inValue + ", " + inReadOnly);
        }

        // ***** Hack. Need to revisit PropertyManager to traverse nulls and use metadata etc
        setupStringConvertor();

        if (stringConvertor == null) {
            if (getBoundModel() != null && inValue != null) {
                LOG.warn("No StringConvertor found for property " + Selector.asString(getSelector())
                        + " in model " + getBoundModel());
            }
            valueIsNull = true;
            setReadOnly(true);
            setText("");
            return;
        }

        valueIsNull = (inValue == null);
        try {
            String text = stringConvertor.valueAsString(inValue);
            setText(text);
            setReadOnly(inReadOnly
                    || !stringConvertor.supportsStringAsValue()
                    || (valueIsNull && isDisableOnNull()));
        } catch (IllegalArgumentException e) {
            // should never happen normally -- comes from getValue() but the
            // ... property value must always be convertible to String?
            LOG.error("updateFromProperty", e);
            setReadOnly(true);
        }
    }


    /**
     * Validation failed while getting a value from this component into the
     * bound model object.
     *
     * @param inException The exception causing the validation failure
     */
    public void validationFailed(Exception inException) {
        validationHelper.validationFailed(inException);
    }


    /**
     * Clears previous validation failure.
     */
    public void validationSuccess() {
        validationHelper.validationSuccess();
    }


    /**
     * Returns the instance of JToolTip that should be used to display the
     * tooltip. <br>
     * In case of failure, the error message contained in the validation
     * exception is displayed, else the standard tooltip for the component is
     * used.
     *
     * @return The tooltip for the component
     * @see #validationFailed(Exception)
     */
    public JToolTip createToolTip() {
        return validationHelper.createToolTip(super.createToolTip());
    }


    // ------------------ Refreshable -------------------------

    /**
     * Updates the component with the current state of the bound model.
     */
    public void refresh() {
        Object propertyValue = boundModel.getPropertyValue();
        boolean propertyReadOnly = boundModel.getPropertyReadOnly();
        updateFromProperty(propertyValue, propertyReadOnly);
        controlIssuer.reset();
    }

    /**
     * Setup the StringConvertor used to convert the displayed value to string
     */
    protected void setupStringConvertor() {
        if (stringConvertor != null) {
            return;
        }
        if (forcedStringConvertor != null) {
            stringConvertor = forcedStringConvertor;
            return;
        }

        stringConvertor = createDefaultStringConvertor();
    }

    /**
     * Defines if the bound property is read-only in the model. <br>
     * This affects the enabled state of the component
     *
     * @param inReadOnly true if the bound property is read-only in the model
     */
    protected void setReadOnly(boolean inReadOnly) {
        readOnly = inReadOnly;
        boolean enabled = userEnabled && !readOnly;
        if ("true".equalsIgnoreCase(ScopeConfig.getString("org.scopemvc.view.swing.STextField.allowEnabledStateOnNoProperty"))) {
            enabled = userEnabled;
        }
        super.setEnabled(enabled);
    }


    // ---------------------- View to model ----------------------

    /**
     * Create a default StringConvertor
     *
     * @return A StringConvertor
     */
    protected StringConvertor createDefaultStringConvertor() {
        try {
            Object m = getBoundModel();
            Selector s = getSelector();
            if (m != null) {
                Class clazz = boundModel.getPropertyManager().getPropertyClass(m, s);
                return StringConvertors.forClass(clazz);
            }
        } catch (Exception e) {
            LOG.warn("createDefaultStringConvertor", e);
        }
        return null;
    }

    /**
     * Returns true if the receiver has an <code>ActionListener</code>
     * installed, the listener installed by Scope is excepted. <br>
     * (copied from JTextField).
     *
     * @return true if the receiver has an ActionListener
     */
    boolean hasNonScopeActionListener() {
        // If there is a control ID, then it means that the Scope equivalent of an ActionListener is installed
        if (getControlID() != null) {
            return true;
        }

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                if (!(listeners[i + 1] instanceof ControlIssuer)) {
                    return true;
                }
            }
        }
        return false;
    }

    // --- Action implementations -----------------------------------

    /**
     * Replacement for the NotifyAction class from JTextField
     *
     * @author lclaude
     * @version $Revision: 1.32 $
     * @created November 11, 2002
     */
    static class NotifyAction extends TextAction {

        /**
         * Constructor for the NotifyAction object
         */
        NotifyAction() {
            super(notifyAction);
        }

        /**
         * Gets the enabled
         *
         * @return The enabled value
         */
        public boolean isEnabled() {
            boolean enabled = false;
            JTextComponent target = getFocusedComponent();
            if (target instanceof STextField) {
                STextField textField = (STextField) target;
                enabled = textField.hasNonScopeActionListener();
                if (!enabled) {
                    // VK_ENTER pressed but no action event to issue,
                    // need to force an update of the model
                    textField.viewChanged();
                }
            }
            return enabled;
        }

        /**
         * Called when the action is performed
         *
         * @param inEvent The action event
         */
        public void actionPerformed(ActionEvent inEvent) {
            JTextComponent target = getFocusedComponent();
            if (target instanceof STextField) {
                STextField field = (STextField) target;
                field.postActionEvent();
            }
        }
    }

}

