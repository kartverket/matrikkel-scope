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
 * $Id: SModelButton.java,v 1.5 2002/11/11 00:48:15 ludovicc Exp $
 */
package org.scopemvc.view.swing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.PropertyView;
import org.scopemvc.core.Selector;
import org.scopemvc.view.util.ModelBindable;

/**
 * <p>
 *
 * An SButton that is bound to a property and performs a test on the value of
 * the property to determine its active state. </P> <P>
 *
 * A comparable object is used to perform the test on the view value. This
 * button is active when the comparable object returns a value greater than 0
 * when passed the view value in its {@link Comparable#compareTo compareTo()}
 * method.</P> <P>
 *
 * Note: it is convenient to use the Comparable interface to perform tests
 * because it is already implemented in many places (natural ordering). For
 * example, to have this SModelButton enabled when the view value is an Integer
 * less than 1, then do: <br>
 * <code>setValueTest(new Integer(1))</code> <br>
 * because <code>new Integer(1).compareTo(value)</code> will return 1 if value
 * is an Integer less than 1</P> <P>
 *
 * If the comparison fails because of an exception coming from the Comparable
 * test, then this button is disabled. </P>
 *
 * @author <a href="mailto:steve.jones@netdecisions.co.uk">Steve Jones</a>
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.5 $ $Date: 2002/11/11 00:48:15 $
 * @created 03 September 2002
 */
public class SModelButton extends SButton
         implements ModelBindable, PropertyView, Refreshable {

    private static final Log LOG = LogFactory.getLog(SModelButton.class);

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    // --------------------- shownModel -----------------------

    /**
     * The model object that this component presents, which may be a property of
     * the bound model if a Selector is specified.
     */
    private Object shownModel;

    private Comparable valueTest;

    /**
     * Constructor for the SModelButton object. <BR>
     * It defines a test that will activate the button if the view value is not
     * null.
     */
    public SModelButton() {
        super();
        setValueTest(new SModelAction.NotNullComparable());
    }

    /**
     * Constructor for the SModelButton object. <BR>
     * It defines a test that will activate the action if the view value is not
     * null, and if the view value is boolean then the action is activated only
     * if the view value is true.
     *
     * @param inControlID The control ID to be issued by this Button
     */
    public SModelButton(String inControlID) {
        super(inControlID);
        setValueTest(new SModelAction.NotNullComparable());
    }

    /**
     * Constructor for the SModelButton object. <BR>
     * It defines a test that will activate the action if the view value is not
     * null, and if the view value is boolean then the action is activated only
     * if the view value is true.
     *
     * @param inControlID The control ID to be issued by this Button
     * @param inSelector The selector for the property
     */
    public SModelButton(String inControlID, Selector inSelector) {
        this(inControlID, inSelector, new SModelAction.NotNullComparable());
    }

    /**
     * Constructor for the SModelButton object. <BR>
     * It defines a test that will activate the button if the view value is not
     * null.
     *
     * @param inControlID The control ID to be issued by this Button
     * @param inSelector The selector for the property
     * @param inValueTest The test for the enabled state. If the compareTo()
     *      method returns a value greater than 0, then this button is active.
     */
    public SModelButton(String inControlID, Selector inSelector, Comparable inValueTest) {
        super(inControlID);
        setSelector(inSelector);
        setValueTest(inValueTest);
    }

    // -------------- implement View ------------------

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
     * The component will use this property to determine the enabled state of
     * the button.
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
     * @return property's value from the UI.
     */
    public final Object getViewValue() {
        return shownModel;
    }

    /**
     * Returns the Comparable used to test the view value.
     *
     * @return The valueTest value
     */
    public Comparable getValueTest() {
        return valueTest;
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
     * Sets the Comparable used to test the view value. <BR>
     * This action is enabled when the compareTo() method of the test returns a
     * value greater than 0 when the passed value is the model value bound to
     * this SModelButton.
     *
     * @param inValueTest The new valueTest value
     */
    public void setValueTest(Comparable inValueTest) {
        valueTest = inValueTest;
        updateEnabledState();
    }

    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        boundModel.setBoundModel(inModel);
    }

    // --------------------- Implement ModelBindable ----------------------

    /**
     * Use the passed property value and read-only state to update the View.
     * <BR>
     * Ignores inReadOnly.
     *
     * @param inValue The new value of the property in the bound model
     * @param inReadOnly The new read-only state of the property
     */
    public void updateFromProperty(Object inValue, boolean inReadOnly) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromProperty: " + inValue + ", " + inReadOnly);
        }

        setShownModel(inValue);
    }

    /**
     * Validation failed while getting a value from this component into the
     * bound model object. <BR>
     * Does nothing here, as the component cannot change the property.
     *
     * @param inException The exception causing the validation failure
     */
    public void validationFailed(Exception inException) {
        // noop
    }

    /**
     * Clears previous validation failure. <BR>
     * Does nothing here, as the component cannot change the property.
     */
    public void validationSuccess() {
        // noop
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
     * Now overwrite the firing of the control to include the additional
     * information of the model
     *
     * @return The Control to fire
     */
    protected Control createControl() {
        Control returnValue = super.createControl();
        if (LOG.isDebugEnabled()) {
            LOG.debug("createControl: Creating the control " + returnValue + "with value " + this.shownModel);
        }

        returnValue.setParameter(this.shownModel);
        return returnValue;
    }

    /**
     * Called internally from updateFromProperty(). Issues a
     * CHANGE_MODEL_CONTROL_ID Control to notify parent Controller of the
     * change.
     *
     * @param inModel The new shownModel value
     */
    private void setShownModel(Object inModel) {

        if (shownModel == inModel) {
            return;
        }
        shownModel = inModel;
        updateEnabledState();
    }

    private void updateEnabledState() {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateEnabledState: Testing " + shownModel + " with test "
                        + valueTest + ", result: " + valueTest.compareTo(shownModel));
            }
            this.setEnabled(valueTest.compareTo(shownModel) > 0);
        } catch (NullPointerException ex) {
            LOG.info("NPE when testing the view value with the valueTest. Assuming that this action: "
                    + this.getName() + " is disabled");
            setEnabled(false);
        } catch (Exception ex) {
            LOG.warn("Could not test the view value with the valueTest", ex);
            setEnabled(false);
        }
    }

}

