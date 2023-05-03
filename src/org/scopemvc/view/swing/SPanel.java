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
 * $Id: SPanel.java,v 1.10 2002/09/25 13:53:08 ludovicc Exp $
 * Changes:
 *  - added setPointer(pointer) in deprecation of setSelector(selector) (scope 2.0)
 */
package org.scopemvc.view.swing;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.Pointer;
import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.core.Control;
import org.scopemvc.core.PropertyView;
import org.scopemvc.core.Selector;
import org.scopemvc.core.View;
import org.scopemvc.view.util.ModelBindable;

import java.awt.*;

/**
 * <P>
 *
 * A JPanel implementation of View for use in Swing-based user interfaces.
 * {@link org.scopemvc.controller.swing.SwingContext} uses the following methods
 * from the {@link SwingView} base class that should be overridden in subclasses
 * that can be shown as top-level views in a Window:
 * <UL>
 *   <LI> {@link #getTitle}</LI>
 *   <LI> {@link #getDisplayMode}</LI>
 *   <LI> {@link #getCloseControl}</LI>
 *   <LI> {@link #isResizable}</LI>
 * </UL>
 * </P> <P>
 *
 * SPanel can be bound either to an entire model object, or to a property of a
 * model. In either case it binds child components to the model it shows (either
 * the entire model or a property of it, assumed to be a submodel). <B>Note:
 * </B>See {@link SUnboundPanel} for an SPanel that doesn't bind to a model,
 * isolating its contained views from the model-binding hierarchy. </P> <P>
 *
 * SPanel uses a {@link org.scopemvc.view.swing.SwingBoundModel SwingBoundModel}
 * delegate. </P> <P>
 *
 * SPanel implements the CHANGE_MODEL_CONTROL_ID Control when the shown model
 * object changes. This supports BasicController which needs to know when its
 * current model changes. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.10 $ $Date: 2002/09/25 13:53:08 $
 * @created 05 September 2002
 */
public class SPanel
         extends SwingView
         implements ModelBindable, Refreshable, PropertyView {

    private static final Log LOG = LogFactory.getLog(SPanel.class);

    // ------------------- Delegate to SwingBoundModel -------------------

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    // --------------------- shownModel -----------------------

    /**
     * The model object that the SPanel presents, which may be a property of the
     * bound model if a Selector is specified.
     */
    private Object shownModel;


    /**
     * Populate with the model the components implementing View or the subviews
     * contained in this component.
     *
     * @param inComponent The component parent of the subcomponents to populate
     * @param inModel The model bound to the parent component
     */
    private static void populateSubComponents(Component inComponent, Object inModel) {
        if (inComponent instanceof SwingView) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("populateSubComponents: component: " + inComponent + ", subview count: "
                        + ((SwingView) inComponent).getSubViewCount());
            }
            for (int i = ((SwingView) inComponent).getSubViewCount() - 1; i >= 0; --i) {
                SwingSubView ssv = ((SwingView) inComponent).getSubView(i);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("populateSubComponents: subview: " + ssv);
                }
                ssv.setBoundModel(inModel);
            }
        }

        if (!(inComponent instanceof Container)) {
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("populateSubComponents: component: " + inComponent + ", component count: "
                    + ((Container) inComponent).getComponentCount());
        }
        for (int i = ((Container) inComponent).getComponentCount() - 1; i >= 0; --i) {
            Component c = ((Container) inComponent).getComponent(i);
            if (c instanceof View) {
                ((View) c).setBoundModel(inModel);
            } else {
                populateSubComponents(c, inModel);
            }
        }
    }


    /**
     * Refreshs the components implementing View or the subviews contained in
     * this component.
     *
     * @param inComponent The component parent of the subcomponents to refresh
     */
    private static void refreshSubComponents(Component inComponent) {
        if (inComponent instanceof SwingView) {
            for (int i = ((SwingView) inComponent).getSubViewCount() - 1; i >= 0; --i) {
                SwingSubView ssv = ((SwingView) inComponent).getSubView(i);
                if (ssv instanceof Refreshable) {
                    ((Refreshable) ssv).refresh();
                }
            }
        }

        if (!(inComponent instanceof Container)) {
            return;
        }

        for (int i = ((Container) inComponent).getComponentCount() - 1; i >= 0; --i) {
            Component c = ((Container) inComponent).getComponent(i);
            if (c instanceof Refreshable) {
                ((Refreshable) c).refresh();
            } else {
                refreshSubComponents(c);
            }
        }
    }


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
     * @return The viewValue value
     */
    public final Object getViewValue() {
        return getShownModel();
    }


    /**
     * Get the model object that the SPanel presents, which may be a property of
     * the bound model if a Selector is specified.
     *
     * @return The shownModel value
     */
    public final Object getShownModel() {
        return shownModel;
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
     * Ignores inReadOnly because it makes no sense here.
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
     * Updates the component with the current state of the bound model. <br>
     * The refresh will be propagated to all child components included in this
     * panel
     *
     * @see #refreshSubComponents
     */
    public void refresh() {
        // Get the model's state
        Object propertyValue = boundModel.getPropertyValue();
        boolean propertyReadOnly = boundModel.getPropertyReadOnly();

        // If no change in shown model then just refresh children
        // ... else update this and children will be updated automatically
        if (getShownModel() == propertyValue) {
            refreshSubComponents(this);
        } else {
            updateFromProperty(propertyValue, propertyReadOnly);
        }
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

        // Issue the BasicController.CHANGE_MODEL_CONTROL_ID
        // Only valid for this View, so don't use the usual delegation to
        // ... the nearest View that has a Controller
        if (getController() != null) {
            getController().handleControl(new Control(BasicController.CHANGE_MODEL_CONTROL_ID, shownModel));
        }

        // Now pass on the new shown model object to all sub-Views
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromModel: updating child Views with model " + inModel);
        }
        populateSubComponents(this, inModel);
    }
}

