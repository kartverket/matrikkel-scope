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
 * $Id: SLabel.java,v 1.16 2002/11/20 00:19:59 ludovicc Exp $
 * Changes:
 *  - added setPointer(pointer) in deprecation of setSelector(selector) (scope 2.0)
 */
package org.scopemvc.view.swing;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

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
 * A JLabel linked to a property of a bound model object. The property must have
 * a StringConvertor to handle conversion to and from a String representation
 * that will be edited in the textfield. </P> <P>
 *
 * An SLabel is disabled if it has no bound model or property, else it is always
 * enabled. If you associate another component to thi label, like a textfield,
 * with the {link javax.swing.JLabel#setLabelFor JLabel.setLabelFor()) method,
 * then the label is disabled when the other component is disabled. This
 * behaviour can be blocked by setting the property
 * org.scopemvc.view.swing.SLabel.use_labelFor_component_enabled_state to false
 * in ScopeConfig</P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <A HREF="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</A>
 * @version $Revision: 1.16 $ $Date: 2002/11/20 00:19:59 $
 * @created June 12, 2002
 */
public class SLabel extends JLabel implements PropertyView, ModelBindable, Refreshable {

    private static final Log LOG = LogFactory.getLog(SLabel.class);

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    /**
     * The StringConvertor used to convert the model property to the String
     * representation that the user see in the label.
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
     * Listener for the 'enabled' property in the associated labelFor component.
     */
    private PropertyChangeListener enabledPropertyListener = null;

    /**
     * Creates a <code>SLabel</code> instance with the specified text, image,
     * and horizontal alignment. The label is centered vertically in its display
     * area. The text is on the trailing edge of the image.
     *
     * @param inText The text to be displayed by the label.
     * @param inImage The image to display in the label
     * @param inHorizontalAlignment One of the following constants defined in
     *      <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>,
     *      <code>RIGHT</code>, <code>LEADING</code> or <code>TRAILING</code>.
     */
    public SLabel(String inText, Icon inImage, int inHorizontalAlignment) {
        super(inText, inImage, inHorizontalAlignment);
        //setReadOnly(!Beans.isDesignTime());
    }

    /**
     * Creates a <code>SLabel</code> instance with the specified text and
     * horizontal alignment. The label is centered vertically in its display
     * area.
     *
     * @param inText The text to be displayed by the label.
     * @param inHorizontalAlignment One of the following constants defined in
     *      <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>,
     *      <code>RIGHT</code>, <code>LEADING</code> or <code>TRAILING</code>.
     */
    public SLabel(String inText, int inHorizontalAlignment) {
        this(inText, null, inHorizontalAlignment);
    }

    /**
     * Creates a <code>SLabel</code> instance with the specified text. The label
     * is aligned against the leading edge of its display area, and centered
     * vertically.
     *
     * @param inText The inText to be displayed by the label.
     */
    public SLabel(String inText) {
        this(inText, null, LEADING);
    }

    /**
     * Creates a <code>SLabel</code> instance with the specified image and
     * horizontal alignment. The label is centered vertically in its display
     * area.
     *
     * @param inHorizontalAlignment One of the following constants defined in
     *      <code>SwingConstants</code>: <code>LEFT</code>, <code>CENTER</code>,
     *      <code>RIGHT</code>, <code>LEADING</code> or <code>TRAILING</code>.
     * @param inImage The image to display in the label
     */
    public SLabel(Icon inImage, int inHorizontalAlignment) {
        this(null, inImage, inHorizontalAlignment);
    }

    /**
     * Creates a <code>SLabel</code> instance with the specified image. The
     * label is centered vertically and horizontally in its display area.
     *
     * @param inImage The image to display in the label
     */
    public SLabel(Icon inImage) {
        this(null, inImage, CENTER);
    }

    /**
     * Creates a <code>SLabel</code> instance with no image and with an empty
     * string for the title. The label is centered vertically in its display
     * area. The label's contents, once set, will be displayed on the leading
     * edge of the label's display area.
     */
    public SLabel() {
        this("", null, LEADING);
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
     * @return property value from parsing the textfield's current String
     *      representation.
     * @exception IllegalArgumentException if the conversion from String fails.
     */
    public Object getViewValue() throws IllegalArgumentException {
        String text = getText();

        if (stringConvertor == null) {
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
     * Issue a Control to the View's parent (owner) Controller. <br>
     * Always throw an exception here.
     *
     * @param inControl The Control to issue
     * @throws UnsupportedOperationException Can't issue a control from a
     *      SLabel.
     */
    public void issueControl(Control inControl) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Can't issue a control from a SLabel");
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
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setPointer(String pointerPath) {
        setSelector(pointerPath);
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

    /**
     * Set the component this is labelling. Can be null if this does not label a
     * Component. If the displayedMnemonic property is set and the labelFor
     * property is also set, the label will call the requestFocus method of the
     * component specified by the labelFor property when the mnemonic is
     * activated. <br/>
     * SLabel will also be in the same enabled state as the component it is
     * labelling.
     *
     * @param inComponent the Component this label is for, or null if the label
     *      is not the label for a component
     * @see #getDisplayedMnemonic
     * @see #setDisplayedMnemonic
     * @beaninfo bound: true description: The component this is labelling.
     */
    public void setLabelFor(Component inComponent) {
        Component oldC = getLabelFor();
        if (oldC != null && enabledPropertyListener != null) {
            oldC.removePropertyChangeListener(enabledPropertyListener);
        }

        super.setLabelFor(inComponent);

        if (inComponent != null
                && "true".equals(ScopeConfig.getString("org.scopemvc.view.swing.SLabel.use_labelFor_component_enabled_state"))) {
            setReadOnly(!getLabelFor().isEnabled());
            if (enabledPropertyListener == null) {
                enabledPropertyListener =
                    new PropertyChangeListener() {
                        public void propertyChange(PropertyChangeEvent inEvent) {
                            setReadOnly(!getLabelFor().isEnabled());
                        }
                    };
            }
            inComponent.addPropertyChangeListener("enabled", enabledPropertyListener);
        }
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
            //setReadOnly(true);
            setText("");
            revalidate();
            return;
        }

        try {
            String text = stringConvertor.valueAsString(inValue);
            setText(text);
            //setReadOnly(inReadOnly
            //        || !stringConvertor.supportsStringAsValue());
        } catch (IllegalArgumentException e) {
            // should never happen normally -- comes from getValue() but the
            // ... property value must always be convertible to String?
            LOG.error("updateFromProperty", e);
            //setReadOnly(true);
        }
        revalidate();
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
        super.setEnabled(userEnabled && !readOnly);
    }

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

}

