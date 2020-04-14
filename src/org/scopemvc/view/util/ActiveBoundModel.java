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
 * $Id: ActiveBoundModel.java,v 1.20 2002/11/20 01:36:58 ludovicc Exp $
 * Changes:
 *  - Added IncorrectImplementationException handling in getPropertyValue() to detect binding errors.
 */
package org.scopemvc.view.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.IncorrectImplementationException;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.core.ModelChangeTypes;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * A {@link BoundModel} that handles ModelChangeEvents and provides full two-way
 * data-binding between a View and its bound model property. The parent View
 * needs to implement the {@link ModelBindable} interface to provide a generic
 * interface used by this delegate. An ActiveBoundModel registers itself as a
 * ModelChangeListener to the bound model object so that it can update its
 * parent View. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.20 $ $Date: 2002/11/20 01:36:58 $
 * @created 05 September 2002
 * @see org.scopemvc.view.swing.SwingBoundModel
 */
public class ActiveBoundModel extends BoundModel implements ModelChangeListener {

    private static final Log LOG = LogFactory.getLog(ActiveBoundModel.class);

    /**
     * The ModelBindable View that delegates bound Model handling to this
     * helper.
     */
    private ModelBindable view;

    /**
     * PropertyManager for the bound model.
     */
    private PropertyManager manager;

    /**
     * Flag specifying if the selector is mandatory. <br>
     * True most of the time, except when the view is optionally used, like the
     * SListSelectionModel in a STable or SList
     */
    private boolean selectorMandatory = true;

    /**
     * Constructor for the ActiveBoundModel object
     *
     * @param inView The view to bind to the model
     */
    public ActiveBoundModel(ModelBindable inView) {
        super();

        if (inView == null) {
            throw new IllegalArgumentException("Can't create for null View.");
        }
        view = inView;
    }


    /**
     * Gets the property manager
     *
     * @return The propertyManager value
     */
    public PropertyManager getPropertyManager() {
        return manager;
    }


    // --------------------- Model to View -----------------------

    /**
     * Returns true if the property in the bound model is read only
     *
     * @return true if the property in the bound model is read only
     */
    public boolean getPropertyReadOnly() {
        boolean result = true;
        if (getBoundModel() != null) {
            try {
                if (Debug.ON) {
                    Debug.assertTrue(manager != null, "null manager");
                }
                result = manager.isReadOnly(getBoundModel(), getSelector());
            } catch (Exception e) {
                LOG.warn("Could not get property read-only state for selector " + Selector.asString(getSelector())
                        + " in model " + getBoundModel() + " because: " + e.getMessage());
                // ignore and leave readOnly == true
            }
        }
        return result;
    }


    /**
     * Gets the value of the property in the bound model
     *
     * @return The value of the property in the bound model
     */
    public Object getPropertyValue() {
        Object result = null;
        if (getBoundModel() != null) {
            try {
                if (Debug.ON) {
                    Debug.assertTrue(manager != null, "null manager");
                }
                result = manager.get(getBoundModel(), getSelector());
            } catch(IncorrectImplementationException e) {
                debugLogCouldNotGetProperty(e);
                throw e;
            } catch (Exception e) {
                debugLogCouldNotGetProperty(e);
                // ignore and leave result == null
            }
        }
        return result;
    }

    private void debugLogCouldNotGetProperty(Exception e) {
        LOG.warn("Could not get property value for selector " + Selector.asString(getSelector())
                + " in model " + getBoundModel() + " because: " + e.getMessage());
    }


    /**
     * Gets the class of the property in the bound model
     *
     * @return The class of the property in the bound model
     */
    public Class getPropertyClass() {
        Class result = null;
        if (getBoundModel() != null) {
            try {
                if (Debug.ON) {
                    Debug.assertTrue(manager != null, "null manager");
                }
                result = manager.getPropertyClass(getBoundModel(), getSelector());
            } catch (Exception e) {
                LOG.warn("Could not get property class for selector " + Selector.asString(getSelector())
                        + " in model " + getBoundModel() + " because: " + e.getMessage());
                // ignore and leave result == null
            }
        }
        return result;
    }

    /**
     * Return true if the selector is mandatory.
     *
     * @return The selectorMandatory value
     */
    public boolean isSelectorMandatory() {
        return selectorMandatory;
    }

    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setBoundModel: " + inModel);
        }

        if (getBoundModel() instanceof ModelChangeEventSource) {
            ((ModelChangeEventSource) getBoundModel()).removeModelChangeListener(this);
        }

        manager = null;
        if (inModel instanceof ModelChangeEventSource) {
            ((ModelChangeEventSource) inModel).addModelChangeListener(this);
        }
        if (inModel != null) {
            manager = PropertyManager.getInstance(inModel);
        }

        super.setBoundModel(inModel);
        updateFromModel(ModelChangeTypes.VALUE_CHANGED);
    }


    /**
     * Sets the selector
     *
     * @param inSelector The new selector value
     */
    public void setSelector(Selector inSelector) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setSelector: " + inSelector);
        }

        super.setSelector(inSelector);
        updateFromModel(ModelChangeTypes.VALUE_CHANGED);
    }

    /**
     * Specifies if the selector is mandatory. <br>
     * True most of the time, except when the view is optionally used, like the
     * SListSelectionModel in a STable or SList
     *
     * @param inFlag The new selectorMandatory value
     */
    public void setSelectorMandatory(boolean inFlag) {
        selectorMandatory = inFlag;
    }

    /**
     * Update the parent View in response to the passed ModelChangeEvent, if the
     * event reports that the property we are bound to has changed.
     *
     * @param inEvent The event describing the change in the model
     */
    public void modelChanged(ModelChangeEvent inEvent) {
        if (Debug.ON) {
            Debug.assertTrue(getBoundModel() instanceof ModelChangeEventSource, "boundModel not a Model");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("modelChanged: view: " + view + ", event: " + inEvent);
            LOG.debug("modelChanged: property selector: " + Selector.asString(getSelector()) + ", event selector: "
                    + Selector.asString(inEvent.getSelector()));
        }

        // Event affects us if a Model changes somewhere at or above us in the hierarchy.
        if (inEvent.getModel() == getBoundModel() && (getSelector() == null
                || getSelector().startsWith(inEvent.getSelector()))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("modelChanged: for me!");
            }
            updateFromModel(inEvent.getType());
            view.validationSuccess();
        }
    }


    // ---------------------- View to Model ----------------------

    /**
     * Put the current View contents into the bound model property. <br>
     * Errors in getting the UI's new value into the model property are handled
     * by calling back into the parent View's
     * {org.scopemvc.view.util.ModelBindable#validationFailed} with the
     * Exception. <br>
     * On no errors, call the parent's {org.scopemvc.view.util.ModelBindable#validationSuccess}
     */
    public void updateModel() {
        try {
            // If selector==null then the view is hooked directly to a top-level model in a model tree,
            // ...in which case there's no way we could be updating that model.
            if (getSelector() == null) {
                if (isSelectorMandatory()) {
                    LOG.warn("View " + view + " doesn't have a selector");
                }
                return;
            }

            Object currentViewValue = view.getViewValue();
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateModel: view: " + view + ", currentViewValue: " + currentViewValue);
            }

            // If getBoundModel()==null then we're not hooked up.
            if (getBoundModel() == null) {
                if (currentViewValue != null) {
                    // only warn if the bound model is null and the view value is not null
                    LOG.warn("View " + view + " is not bound to a model");
                }
                return;
            }

            if (Debug.ON) {
                Debug.assertTrue(manager != null, "null manager");
            }

            // Test if the value has changed. Useful for preventing views that have been
            // updated from the model to detect the change as coming from the user, and so
            // update the model in return, which might be impossible and cause an error if the
            // property is read-only...
            Object previousModelValue = getPropertyValue();
            if (!equals(previousModelValue, currentViewValue)) {
                manager.set(getBoundModel(), getSelector(), currentViewValue);
            }
            view.validationSuccess();
        } catch (Exception e) {
            LOG.warn("Exception when updating model on view " + view, e);
            view.validationFailed(e);
        }
    }


    /**
     * Calls parent View's {org.scopemvc.view.util.ModelBindable#validationSuccess}
     * to clear any previous validation failure.
     *
     * @param inEventType The type of the change event, one of the {@link
     *      org.scopemvc.core.ModelChangeTypes ModelChangeTypes} values
     */
    public void updateFromModel(int inEventType) {
        // Find the new property value
        Object property = getPropertyValue();
        boolean readOnly = getPropertyReadOnly();
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromModel: view: " + view + ", eventType: " + inEventType
                    + ", property: " + property + ", readOnly: " + readOnly);
        }

        // for ACCESS_CHANGED and VALUE_CHANGED *****
        view.updateFromProperty(property, readOnly);
    }

    private boolean equals(Object in1, Object in2) {
        return (in1 == in2 || (in1 != null && in1.equals(in2)));
    }
}
