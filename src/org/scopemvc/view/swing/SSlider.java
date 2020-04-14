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
 * $Id: SSlider.java,v 1.12 2002/11/11 00:48:15 ludovicc Exp $
 */
package org.scopemvc.view.swing;

import java.beans.Beans;
import javax.swing.JSlider;
import javax.swing.JToolTip;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.PropertyView;
import org.scopemvc.core.Selector;
import org.scopemvc.view.util.ModelBindable;

/**
 * <P>
 *
 * A JSlider whose value is bound to an int property of a model object. </P> <P>
 *
 * Note that the Selector specified for a SSlider must select a single Integer
 * or int property. </P> <P>
 *
 * SSlider responds to the bound model or the particular bound property becoming
 * read-only by disabling itself. A SSlider is also disabled if it has no bound
 * model or property, or the property is a null Integer. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.12 $ $Date: 2002/11/11 00:48:15 $
 * @created 05 September 2002
 */
public class SSlider extends JSlider
         implements PropertyView, ModelBindable, Refreshable {

    private static final Log LOG = LogFactory.getLog(SSlider.class);

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    /**
     * Helper to manage validation state.
     */
    private ValidationHelper validationHelper = new ValidationHelper(this);

    /**
     * SSlider can "hold" a null when bound to a Integer property that happens
     * to be null.
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
     * Keep the previous value of the slider, to avoid updating the model when a
     * property like max or min is updated. Needed because fireStateChanged()
     * doesn't give the name of the property(ies) that have changed.
     */
    private int previousValue;

    /**
     * Constructor for the SSlider object
     */
    public SSlider() {
        super();
        setReadOnly(!Beans.isDesignTime());
        previousValue = getValue();
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


    /**
     * Get the current value (what would be set as a property of the bound model
     * object) being presented on the View.
     *
     * @return an Integer or null when (! isEnabled())
     */
    public Object getViewValue() {
        if (valueIsNull) {
            return null;
        }

        return new Integer(getModel().getValue());
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
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelector The new selector to use
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
     * Sets the bound model
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

        if (!(inValue instanceof Integer)) {
            LOG.warn("Expecting an Integer property, not a property of type " + inValue.getClass()
                    + ", property selector is " + Selector.asString(getSelector()));
            valueIsNull = true;
            setReadOnly(true);
            return;
        }

        valueIsNull = false;
        setReadOnly(inReadOnly);
        int value = ((Integer) inValue).intValue();
        setValue(value);
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


    // ------------------ Refreshable -------------------------

    /**
     * Updates the component with the current state of the bound model.
     */
    public void refresh() {
        Object propertyValue = boundModel.getPropertyValue();
        boolean propertyReadOnly = boundModel.getPropertyReadOnly();
        updateFromProperty(propertyValue, propertyReadOnly);
    }


    // ---------------------- View to model ----------------------

    /**
     * Update the bound model with the new value, then send a ChangeEvent, whose
     * source is this Slider, to each listener. <br>
     * This method method is called each time a ChangeEvent is received from the
     * model.
     */
    protected void fireStateChanged() {
        valueIsNull = false;
        if (previousValue != getValue()) {
            previousValue = getValue();
            boundModel.updateModel();
        }
        super.fireStateChanged();
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

