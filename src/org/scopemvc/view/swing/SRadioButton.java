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
 * $Id: SRadioButton.java,v 1.13 2002/10/30 09:13:08 ludovicc Exp $
 * Changes:
 *  - added setPointer(pointer) in deprecation of setSetlector(selector) (scope 2.0)
 *  - added setActionID(id) in deprecation of setControlID(id) (scope 2.0)
 */
package org.scopemvc.view.swing;


import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.Beans;
import javax.swing.JRadioButton;
import javax.swing.JToolTip;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.PropertyView;
import org.scopemvc.core.Selector;
import org.scopemvc.util.UIStrings;
import org.scopemvc.view.util.ModelBindable;
import org.scopemvc.Pointer;

/**
 * <P>
 *
 * A JRadioButton linked to a Boolean or boolean property of a bound model
 * object. Updates to the radiobutton result in changes to the model property
 * immediately. </P> <P>
 *
 * Note that the Selector specified for a SRadioButton must select a single
 * Boolean or boolean property. </P> <P>
 *
 * SRadioButton responds to the bound model or the particular bound property
 * becoming read-only by disabling itself. A SRadioButton is also disabled if it
 * has no bound model or property, or the property is a null Boolean. </P> <P>
 *
 * SRadioButton can issue a Control when it changes state. The button label
 * comes from UIStrings keyed against the Control ID. </P>
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.13 $ $Date: 2002/10/30 09:13:08 $
 * @created 05 September 2002
 */
public class SRadioButton extends JRadioButton implements PropertyView, ItemListener, ModelBindable, Refreshable {

    private static final Log LOG = LogFactory.getLog(SRadioButton.class);

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    /**
     * Helper to manage validation state.
     */
    private ValidationHelper validationHelper = new ValidationHelper(this);

    /**
     * ID of the Control issued when the radiobutton changes state.
     */
    private String controlID;

    /**
     * SRadioButton can "hold" a null when bound to a Boolean property that
     * happens to be null.
     */
    private boolean valueIsNull = false;

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
     * Constructor for the SRadioButton object
     */
    public SRadioButton() {
        addItemListener(this);
        setReadOnly(!Beans.isDesignTime());
    }

    /**
     * Constructor for the SButton object
     *
     * @param inControlID The ID of the control to be issued when this button is
     *      pressed.
     */
    public SRadioButton(String inControlID) {
        this();
        setControlID(inControlID);
    }

    /**
     * Constructor for the SButton object
     *
     * @param inControlID The ID of the control to be issued when this button is
     *      pressed.
     * @param inSelector The selector for the property
     */
    public SRadioButton(String inControlID, Selector inSelector) {
        this();
        setControlID(inControlID);
        setSelector(inSelector);
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


    // ---------- Issue a Control on change of state --------------

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
     * @return a Boolean or null when (! isEnabled())
     */
    public Object getViewValue() {
        if (valueIsNull) {
            return null;
        }

        if (isSelected()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
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
     * Issue a Control to the View's parent (owner) Controller.
     *
     * @param inControl The Control to issue
     */
    public void issueControl(Control inControl) {
        SwingUtil.issueControl(this, inControl);
    }


    /**
     * Set the ID of the Control that will be issued when state changes. If null
     * no Control will be issued.
     *
     * @param inControlID The new controlID value
     * @deprecated Will be removed in Scope 2.0, use setActionID(..)
     */
    public final void setControlID(String inControlID) {
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
     * Sets the state of the radiobutton. Note that this method does not trigger
     * an <code>actionEvent</code>.
     *
     * @param inSelected true if the radiobutton is selected, otherwise false
     */
    public void setSelected(boolean inSelected) {
        if (inSelected && !isSelected()) {
            super.setSelected(true);
        } else if (!inSelected && isSelected()) {
            super.setSelected(false);
        }
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

    // --------------------- Implement ModelBindable ----------------------

    /**
     * Use the passed property value and read-only state to update the View.
     * <br>
     * Incoming value is a Boolean or null.
     *
     * @param inValue The new value of the property in the bound model
     * @param inReadOnly The new read-only state of the property
     */
    public void updateFromProperty(Object inValue, boolean inReadOnly) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromProperty: " + inValue + ", " + inReadOnly);
        }

        if (inValue == null) {
            valueIsNull = true;
            setReadOnly(true);
            return;
        }

        if (!(inValue instanceof Boolean)) {
            LOG.warn("Expecting a Boolean property, not a property of type " + inValue.getClass()
                    + ", property selector is " + Selector.asString(getSelector()));
            valueIsNull = true;
            setReadOnly(true);
            return;
        }

        valueIsNull = false;
        setReadOnly(inReadOnly);
        boolean value = ((Boolean) inValue).booleanValue();
        setSelected(value);
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
     * @see #validationFailure
     */
    public JToolTip createToolTip() {
        return validationHelper.createToolTip(super.createToolTip());
    }


    // ---------------------- View to model ----------------------

    /**
     * Invoked when an item has been selected or deselected. The code written
     * for this method performs the operations that need to occur when an item
     * is selected (or deselected).
     *
     * @param inEvent The event describing the change on the item
     */
    public void itemStateChanged(ItemEvent inEvent) {
        valueIsNull = false;
        if (isEnabled()) {
            boundModel.updateModel();
        }
        if (controlID != null) {
            Control control = new Control(controlID);
            issueControl(control);
        }
    }


    // ------------------ Refreshable -------------------------

    /**
     * Updates the component with the current state of the bound model.
     */
    public void refresh() {
        Object propertyValue = boundModel.getPropertyValue();
        boolean propertyReadOnly = boundModel.getPropertyReadOnly();
        updateFromProperty(propertyValue, propertyReadOnly);
    }

    /**
     * Defines if the bound property is read-only in the model. <br>
     * This affects the enabled state of the component
     *
     * @param inReadOnly true if the bound property is read-only in the model
     */
    private void setReadOnly(boolean inReadOnly) {
        readOnly = inReadOnly;
        super.setEnabled(userEnabled && !readOnly);
    }

}

