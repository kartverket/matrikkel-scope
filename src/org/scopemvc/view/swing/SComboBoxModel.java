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


import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Selector;
import org.scopemvc.view.util.ModelBindable;

/**
 * The ComboBoxModel used in SComboBox. <br>
 * This class implements a fix for the bug in JCompoBox for JDK 1.2.x,1.3.x:
 * after setting model combobox sets element 0 as selected, in violation of MVC
 * principles.
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.17 $ $Date: 2002/11/06 00:31:57 $
 * @created 05 September 2002
 * @see SComboBox
 * @todo Disable the fix when JDK 1.4 is detected (ludovicc)
 */
public class SComboBoxModel extends SAbstractListModel
         implements ComboBoxModel {

    private static final Log LOG = LogFactory.getLog(SComboBoxModel.class);

    private boolean jdk13FixIgnoreSetSelectedItem = false;

    private SelectionBoundModel selectionDelegate = new SelectionBoundModel();
    private SwingBoundModel selectionBoundModel = new SwingBoundModel(selectionDelegate);

    /**
     * Owning combo.
     */
    private JComboBox combo;

    /**
     * Need to hold this here in case the model decides to veto some values: UI
     * can hold invalid values whereas model can't.
     */
    private Object selectedItem;


    /**
     * Constructor for the SComboBoxModel object
     *
     * @param inCombo The owning combobox
     */
    public SComboBoxModel(JComboBox inCombo) {
        combo = inCombo;
        selectionBoundModel.setSelectorMandatory(false);
    }


    // ------------------- Delegate to BoundModel -------------------

    /**
     * Gets the bound selection model
     *
     * @return The boundSelectionModel value
     */
    public final Object getBoundSelectionModel() {
        return selectionBoundModel.getBoundModel();
    }


    /**
     * Gets the selection selector
     *
     * @return The selectionSelector value
     */
    public final Selector getSelectionSelector() {
        return selectionBoundModel.getSelector();
    }


    /**
     * Gets the selected item
     *
     * @return The selectedItem value
     */
    public Object getSelectedItem() {
        return selectedItem;
    }


    /**
     * Sets the bound selection model
     *
     * @param inModel The new boundSelectionModel value
     */
    public final void setBoundSelectionModel(Object inModel) {
        selectionBoundModel.setBoundModel(inModel);
    }


    /**
     * Sets the selector for the selected item
     *
     * @param inSelector The new selectionSelector value
     */
    public final void setSelectionSelector(Selector inSelector) {
        selectionBoundModel.setSelector(inSelector);
    }


    /**
     * Sets the selector for the selected item
     *
     * @param inSelectorString The string representation of the selector
     */
    public final void setSelectionSelector(String inSelectorString) {
        selectionBoundModel.setSelector(inSelectorString);
    }


    /**
     * Sets the selector for the selected item
     *
     * @param inSelectorString The string representation of the selector
     */
    public final void setSelectionSelectorString(String inSelectorString) {
        setSelectionSelector(inSelectorString);
    }


    // ------------- View to Model ---------------------

    /**
     * Sets the selected item
     *
     * @param inItem The new selectedItem value
     */
    public void setSelectedItem(Object inItem) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setSelectedItem: " + inItem);
        }

        if (jdk13FixIgnoreSetSelectedItem) {
            return;
        }

        if ((selectedItem != null && !selectedItem.equals(inItem))
                || selectedItem == null && inItem != null) {
            selectedItem = inItem;
            selectionBoundModel.updateModel();
        }
    }


    // --------------Override for selection model ----------------

    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        setBoundSelectionModel(inModel);
        super.setBoundModel(inModel);
    }


    // ------------------ Refreshable -------------------------

    /**
     * Updates the component with the current state of the bound model.
     */
    public void refresh() {
        super.refresh();
        Object propertyValue = selectionBoundModel.getPropertyValue();
        boolean propertyReadOnly = selectionBoundModel.getPropertyReadOnly();
        selectionDelegate.updateFromProperty(propertyValue, propertyReadOnly);
    }

    /**
     * Notifies of a change in the shown model. <br>
     * Used by SComboBoxModel for updating its selected item
     */
    protected void shownModelChanged() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("shownModelChanged");
        }
        if (!combo.isEditable()) {
            // ensures that the selected item belongs to the list of items
            if (!containsElement(getSelectedItem())) {
                if (getSize() > 0) {
                    setSelectedItem(getElementAt(0));
                } else {
                    setSelectedItem(null);
                }
            }
        }
    }

    /**
     * Set the combobox owning this model
     *
     * @param inComboBox The comboBox using this model
     */
    void setComboBox(JComboBox inComboBox) {
        combo = inComboBox;
    }


    /**
     * This method is a bug fix for JDK 1.2.x,1.3.x bug: after setting model
     * combobox sets element 0 as selected. This is violation of MVC paradigm -
     * model describes some state and should not be modified by view. JDK 1.4
     * beta has this bug fixed.
     *
     * @param inIgnore true if a call to setSelectedItem should be ignored
     * @see SComboBox.setModel(Object)
     */
    void setJdk13FixIgnoreSetSelectedItem(boolean inIgnore) {
        jdk13FixIgnoreSetSelectedItem = inIgnore;
    }


    // ----------------------- Inner class to handle model binding ---------------------

    /**
     * Fire a contents change event
     */
    void fireContentsChanged() {
        fireContentsChanged(this, -1, -1);
    }

    class SelectionBoundModel implements ModelBindable {

        /**
         * Gets the view value
         *
         * @return The viewValue value
         */
        public Object getViewValue() {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getValue: " + getSelectedItem());
            }

            return getSelectedItem();
        }


        /**
         * Use the passed property value and read-only state to update the View.
         *
         * @param inValue The new value of the property in the bound model
         * @param inReadOnly The new read-only state of the property
         */
        public void updateFromProperty(Object inValue, boolean inReadOnly) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("updateFromProperty: " + inValue + ", " + inReadOnly);
            }

            if (combo instanceof SComboBox) {
                ((SComboBox) combo).setReadOnly(inReadOnly);
            } else {
                combo.setEnabled(!inReadOnly);
            }

            if (inValue == selectedItem) {
                return;
            }

            // ignore the update when the selection selector is not defined
            if (getSelectionSelector() == null) {
                return;
            }

            if (!combo.isEditable()) {
                // Ensures that the selected item belongs to the list of items
                // check for null bound model in the SComboBoxModel for initialisation.
                // If the selected item is null, don't complain
                if (inValue == null) {
                    setSelectedItem(null);
                } else if (getBoundModel() != null && !containsElement(inValue)) {
                    setSelectedItem(null);
                    throw new RuntimeException("Value " + inValue
                            + " is not contained in the list of items of combo box " + combo
                            + " contained in " + combo.getParent());
                }
            }

            setSelectedItem(inValue);
            fireContentsChanged();
        }


        /**
         * Validation of the selected item failed
         *
         * @param inException The validation failure
         */
        public void validationFailed(Exception inException) {
            // clear the invalid selected item
            setSelectedItem(null);
            if (combo instanceof SComboBox) {
                ((SComboBox) combo).validationFailed(inException);
            }
        }


        /**
         * Validation of the selected item succeeded
         */
        public void validationSuccess() {
            if (combo instanceof SComboBox) {
                ((SComboBox) combo).validationSuccess();
            }
        }
    }
}
