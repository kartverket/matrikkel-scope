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
 * $Id: SListSelectionModel.java,v 1.13 2002/10/23 12:38:46 ludovicc Exp $
 */
package org.scopemvc.view.swing;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Selector;
import org.scopemvc.view.util.ModelBindable;

import javax.swing.*;
import java.beans.Beans;
import java.util.HashSet;
import java.util.Iterator;

/**
 * <P>
 *
 * A ListSelectionModel bound to a model property that will contain a reference
 * to the current selection. This implementation allows read-only properties to
 * disable the parent JList or JTable. SINGLE_SELECTION,
 * SINGLE_INTERVAL_SELECTION and MULTIPLE_INTERVAL_SELECTION are supported. </P>
 * <P>
 *
 * The bound property can be an Object of the type being selected in which case
 * the topmost single selection is updated. If the property is a HashSet then it
 * is updated with all selections. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <A HREF="mailto:patrik_nordwall@yahoo.se">Patrik Nordwall</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.13 $ $Date: 2002/10/23 12:38:46 $
 * @created 05 September 2002
 * @see SList
 * @see STable
 */
public class SListSelectionModel extends DefaultListSelectionModel
         implements ModelBindable, Refreshable {

    private static final Log LOG = LogFactory.getLog(SListSelectionModel.class);

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    /**
     * Parent so we can find the currently selected object and pass on
     * validation failures.
     */
    private ListSelectionParent parent;

    /**
     * Create for a parent ListSelectionParent.
     *
     * @param inParent The component using this ListSelectionModel, not null.
     * @param inForceSelector Force the selection model to have a selector to a
     *      valid property in the model. <br>
     *      If forceSelector is true, then the ListSelectionParent is enabled
     *      only if there is a bound property in the model for the selected
     *      item(s) and this property is writeable. <br>
     *      If forceSelector is false, then the ListSelectionParent is enabled
     *      if there is no selector (and no bound property in the model), or if
     *      there is a selector for a writeable property in the model.
     */
    public SListSelectionModel(ListSelectionParent inParent, boolean inForceSelector) {
        super();

        if (inParent == null) {
            throw new IllegalArgumentException("SListSelectionModel cannot use a null ListSelectionParent");
        }

        parent = inParent;
        parent.setReadOnly(inForceSelector && !Beans.isDesignTime());
        boundModel.setSelectorMandatory(inForceSelector);
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
     * The model will use this property as the reference to the current
     * selection.
     *
     * @return A selector.
     */
    public final Selector getSelector() {
        return boundModel.getSelector();
    }


    /**
     * Gets the view value
     *
     * @return The viewValue value
     */
    public Object getViewValue() {

        // Figure out the type of the bound property
        Class propertyClass = boundModel.getPropertyClass();

        // Populate appropriately
        if (HashSet.class.equals(propertyClass)) {
            HashSet elements = new HashSet();
            for (int i = getMinSelectionIndex(); i <= getMaxSelectionIndex(); ++i) {
                if (isSelectedIndex(i)) {
                    Object element = parent.findElementAt(i);
                    if (element != null) {
                        elements.add(parent.findElementAt(i));
                    }
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("getViewValue: " + elements);
            }
            return elements;
        } else {
            int index = getMinSelectionIndex();
            if (LOG.isDebugEnabled()) {
                LOG.debug("getViewValue: " + parent.findElementAt(index));
            }
            return parent.findElementAt(index);
        }
    }


    /**
     * Return true if the selector is mandatory
     *
     * @return The selectorMandatory value
     */
    public boolean isSelectorMandatory() {
        return boundModel.isSelectorMandatory();
    }

    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public final void setBoundModel(Object inModel) {
        boundModel.setBoundModel(inModel);
    }


    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The model will use this property as the reference to the current
     * selection.
     *
     * @param inSelector The new selector to use
     */
    public final void setSelector(Selector inSelector) {
        boundModel.setSelector(inSelector);
    }


    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The model will use this property as the reference to the current
     * selection.
     *
     * @param inSelectorString The string representation of the selector
     * @see Selector#fromString
     */
    public final void setSelector(String inSelectorString) {
        boundModel.setSelector(inSelectorString);
    }


    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The model will use this property as the reference to the current
     * selection.
     *
     * @param inSelectorString The string representation of the selector
     * @deprecated Use setSelector(String) instead
     */
    public final void setSelectorString(String inSelectorString) {
        setSelector(inSelectorString);
    }

    // --------------------- Implement ModelBindable ----------------------

    /**
     * Use the passed property value and read-only state to update the View.
     * <BR>
     * Incoming value is null, an Object or a java.util.HashSet.
     *
     * @param inValue The new value of the property in the bound model
     * @param inReadOnly The new read-only state of the property
     */
    public void updateFromProperty(Object inValue, boolean inReadOnly) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromProperty: " + inValue + ", " + inReadOnly, new Throwable());
        }

        if (isSelectorMandatory()) {
            parent.setReadOnly(inReadOnly);
        } else if (getSelector() != null) {
            parent.setReadOnly(inReadOnly);
        }

        if (inValue == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateFromProperty: clear selection because null property value");
            }
            super.clearSelection();
            return;
        }

        if (inValue instanceof HashSet) {
            updateFromHashSet((HashSet) inValue);
        } else {
            updateFromObject(inValue);
        }
    }


    /**
     * If selection fails, make validation fail on parent.
     *
     * @param inException The exception causing the validation failure
     */
    public void validationFailed(Exception inException) {
        parent.validationFailed(inException);
    }


    /**
     * If selection fails, make validation fail on parent.
     */
    public void validationSuccess() {
        parent.validationSuccess();
    }


    // ------------------ Refreshable -------------------------

    /**
     * Updates this list selection model with the current state of the bound
     * model.
     */
    public void refresh() {
        Object propertyValue = boundModel.getPropertyValue();
        boolean propertyReadOnly = boundModel.getPropertyReadOnly();
        updateFromProperty(propertyValue, propertyReadOnly);
    }


    /**
     * Updates the selection from an Object value (single selection)
     *
     * @param inValue The selected item
     */
    protected void updateFromObject(Object inValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromObject: value: " + inValue);
        }

        if (inValue == null) {
            if (!isSelectionEmpty()) {
                clearSelection();
            }
            return;
        }

        int index = parent.findIndexFor(inValue);
        if (index < 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateFromObject: value not in list of items for " + parent);
            }
            if (!isSelectionEmpty()) {
                clearSelection();
            }
        } else {
            if (getMinSelectionIndex() != index) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("updateFromObject: selection index: " + index);
                }
                setSelectionInterval(index, index);
            }
        }
    }


    /**
     * Updates the selection from an HashSet value (multiple selection)
     *
     * @param inValue The HashSet containing the selecte items
     */
    protected void updateFromHashSet(HashSet inValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromHashSet: value: " + inValue);
        }

        if (inValue == null || inValue.size() < 1) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateFromHashSet: empty values");
            }
            if (!isSelectionEmpty()) {
                clearSelection();
            }
            return;
        }

        // two algorithms depending on selection mode
        if (getSelectionMode() == MULTIPLE_INTERVAL_SELECTION) {
            // check if the new selection is identical with current selection
            if (inValue.equals(getViewValue())) {
                return;
            }
            // clear selection and then add the new ones
            if (!isSelectionEmpty()) {
                clearSelection();
            }
            for (Iterator i = inValue.iterator(); i.hasNext(); ) {
                int index = parent.findIndexFor(i.next());
                if (!isSelectedIndex(index)) {
                    addSelectionInterval(index, index);
                }
            }
        } else {
            int i0 = -1;
            int i1 = -1;
            for (Iterator i = inValue.iterator(); i.hasNext(); ) {
                int index = parent.findIndexFor(i.next());
                i1 = Math.max(i1, index);
                if (i0 < 0) {
                    i0 = i1;
                } else {
                    i0 = Math.min(i0, index);
                }
            }

            if (i0 < 0) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("updateFromHashSet: values not in list of items for " + parent);
                }
                if (!isSelectionEmpty()) {
                    clearSelection();
                }
            } else {
                if (getMinSelectionIndex() != i0 || getMaxSelectionIndex() != i1) {
                    setSelectionInterval(i0, i1);
                }
            }
        }
    }


    // ---------------------- View to model ----------------------

    /**
     * When selection changes, update the bound model.
     *
     * @param inFirstIndex the first index in the interval
     * @param inLastIndex the last index in the interval
     * @param inAdjusting true if this is the final change in a series of
     *      adjustments
     */
    protected void fireValueChanged(int inFirstIndex, int inLastIndex, boolean inAdjusting) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("fireValueChanged: " + inFirstIndex + "," + inLastIndex + "," + inAdjusting, new Throwable());
        }

        super.fireValueChanged(inFirstIndex, inLastIndex, inAdjusting);

        // If final change in a sequence then adjust bound model
        // ... and make the parent list issue a control.
        if (!inAdjusting) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("fireValueChanged: " + inFirstIndex);
            }
            // Test if the bound model is null to avoid uncessary warnings,
            // in particular on STable initialisation
            if (getBoundModel() != null) {
                boundModel.updateModel();
            }
            parent.issueChangeSelectionControl();
        }
    }
}

