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
 * $Id: SList.java,v 1.17 2002/10/23 12:38:46 ludovicc Exp $
 * Changes:
 * - delegating to ListCellRendererSelector in stead of SListCellRenderer (scope 2.0 migration)
 */
package org.scopemvc.view.swing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.Selector;
import org.scopemvc.core.View;

import javax.swing.*;
import javax.swing.plaf.ListUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * <P>
 *
 * A JList that uses a {@link SListModel} to bind to model properties, and a
 * {@link SListSelectionModel} to bind the selected item to a property. Uses a
 * {@link ListCellRendererSelector} to draw items in the list. </P> <P>
 *
 * SList can issue Controls on selection changes and on double-click. </P> <P>
 *
 * SList doesn't itself bind to a model: it delegates all binding to its
 * SListModel and SListSelectionModel. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.17 $ $Date: 2002/10/23 12:38:46 $
 * @created 05 September 2002
 * @see SListModel
 * @see SListSelectionModel
 * @see ListCellRendererSelector
 * @see SAbstractListModel
 * @see SComboBox
 * @todo SList should support editing items in the list
 * @todo The list should define the stringConvertor for editing the selected
 *      item as soon as the model is bound to it. It's editable only if there is
 *      a selector for the selected item, and if the bound model contains the
 *      property defined by the selector, and also if the string convertor for
 *      the class of the selected item allows convertion string to value.
 *      (ludovicc)
 */
public class SList extends JList
         implements View, Refreshable, ListSelectionParent {

    private static final Log LOG = LogFactory.getLog(SList.class);

    /**
     * Control to issue on selection change.
     */
    private String selectionControlID;

    /**
     * Control to issue on double click.
     */
    private String doubleClickControlID;

    // ------------- Support validation failures from selection -------------

    /**
     * Helper to manage validation state.
     */
    private ValidationHelper validationHelper = new ValidationHelper(this);

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
     * Constructor for the SList object
     */
    public SList() {
        super();
        setCellRenderer(new SListCellRenderer());
        setModel(new SListModel(this));
        addMouseListener(
            new MouseAdapter() {
                /**
                 * Invoked when the mouse has been clicked on a component.
                 *
                 * @param inEvent The mouse event
                 */
                public void mouseClicked(MouseEvent inEvent) {
                    if (inEvent.getClickCount() == 2) {
                        if (doubleClickControlID != null) {
                            Control control = new Control(doubleClickControlID);
                            issueControl(control);
                        }
                    }
                }
            });
        setSelectionModel(createSelectionModel());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateUI();
    }


    // ---------- Implement View by delegation to SListModel and SListSelectionModel ----------

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return ((SListModel) getModel()).getBoundModel();
    }


    /**
     * Gets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The component will present this property to the user. <br>
     * The bound property should be an array (<code>Object[]</code>) or a list
     * derived from <code>java.util.List</code>, or the JavaBean indexed
     * property for one element of the list (eg. getPerson(int)).
     *
     * @return A selector.
     */
    public final Selector getSelector() {
        return ((SListModel) getModel()).getSelector();
    }


    /**
     * Gets the selector for the selected item
     *
     * @return The selector for the selected item
     */
    public final Selector getSelectionSelector() {
        return ((SListSelectionModel) getSelectionModel()).getSelector();
    }


    /**
     * Gets the selector for the item to render in the SList list.
     *
     * @return The selector for the item to render
     * @see ListCellRendererSelector
     */
    public final Selector getRendererSelector() {
        ListCellRendererSelector renderer = (ListCellRendererSelector) getCellRenderer();
        return renderer.getTextSelector();
    }


    /**
     * Gets the selector for the icon to render in the SList list.
     *
     * @return The selector for the icon to render
     * @see ListCellRendererSelector
     */
    public final Selector getRendererIconSelector() {
        ListCellRendererSelector renderer = (ListCellRendererSelector) getCellRenderer();
        return renderer.getIconSelector();
    }


    /**
     * Gets the selector for the property giving the size of the list of items.
     *
     * @return The selector for the list size.
     */
    public final Selector getSizeSelector() {
        return ((SListModel) getModel()).getSizeSelector();
    }


    /**
     * Gets the control ID to be issued when the selection changes.
     *
     * @return The control ID
     */
    public final String getChangeSelectionControlID() {
        return selectionControlID;
    }


    /**
     * Gets the control ID to be issued when the user double-click on an item in
     * the list.
     *
     * @return The control ID
     */
    public final String getDoubleClickControlID() {
        return doubleClickControlID;
    }


    // -------------------- Controls -----------------------

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
     * Issues a control notifying that the selection has changed.
     */
    public void issueChangeSelectionControl() {
        if (selectionControlID != null) {
            Control control = new Control(selectionControlID);
            issueControl(control);
        }
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
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public final void setBoundModel(Object inModel) {
        ((SListModel) getModel()).setBoundModel(inModel);
        ((SListSelectionModel) getSelectionModel()).setBoundModel(inModel);
    }

    /**
     * Set the Selector for the list data. Should be a java.util.List or an
     * Object[] or have a "size" property and properties accessible by an
     * IntIndexedSelector.
     *
     * @param inSelector The new selector value
     */
    public final void setSelector(Selector inSelector) {
        ((SListModel) getModel()).setSelector(inSelector);
    }


    /**
     * Set the Selector for the list data. Should be a java.util.List or an
     * Object[] or have a "size" property and properties accessible by an
     * IntIndexedSelector.
     *
     * @param inSelectorString The string representation of the selector
     */
    public final void setSelector(String inSelectorString) {
        ((SListModel) getModel()).setSelector(inSelectorString);
    }

    /**
     * Set the Selector for the list data. Should be a java.util.List or an
     * Object[] or have a "size" property and properties accessible by an
     * IntIndexedSelector.
     *
     * @param inSelectorString The string representation of the selector
     * @deprecated Use setSelector(String) instead
     */
    public final void setSelectorString(String inSelectorString) {
        setSelector(inSelectorString);
    }

    /**
     * Set the Selector for the list selection: this property will be bound to
     * the list's single selection.
     *
     * @param inSelector The new selectionSelector value
     */
    public final void setSelectionSelector(Selector inSelector) {
        ((SListSelectionModel) getSelectionModel()).setSelector(inSelector);
    }


    /**
     * Set the Selector for the list selection: this property will be bound to
     * the list's single selection.
     *
     * @param inSelectorString The string representation of the
     *      selectionSelector
     */
    public final void setSelectionSelector(String inSelectorString) {
        ((SListSelectionModel) getSelectionModel()).setSelector(inSelectorString);
    }

    /**
     * Set the Selector for the list selection: this property will be bound to
     * the list's single selection.
     *
     * @param inSelectorString The string representation of the
     *      selectionSelector
     * @deprecated Use setSelectionSelector(String) instead
     */
    public final void setSelectionSelectorString(String inSelectorString) {
        setSelectionSelector(inSelectorString);
    }

    /**
     * Optional: set the Selector for the property that is the size of the items
     * list. Not needed for lists that are of type Object[] or java.util.List.
     *
     * @param inSelector The new sizeSelector value
     */
    public final void setSizeSelector(Selector inSelector) {
        ((SListModel) getModel()).setSizeSelector(inSelector);
    }


    /**
     * Optional: set the Selector for the property that is the size of the items
     * list. Not needed for lists that are of type Object[] or java.util.List.
     *
     * @param inSelectorString The string representation of the sizeSelector
     */
    public final void setSizeSelector(String inSelectorString) {
        ((SListModel) getModel()).setSizeSelector(inSelectorString);
    }

    /**
     * Optional: set the Selector for the property that is the size of the items
     * list. Not needed for lists that are of type Object[] or java.util.List.
     *
     * @param inSelectorString The string representation of the sizeSelector
     * @deprecated Use setSizeSelector(String) instead
     */
    public final void setSizeSelectorString(String inSelectorString) {
        setSizeSelector(inSelectorString);
    }


    /**
     * Set the Selector for the list cell renderer: this is the property that
     * will be shown in a list cell (converted to a String).
     *
     * @param inSelector The new rendererSelector value
     */
    public final void setRendererSelector(Selector inSelector) {
        ListCellRendererSelector renderer = (ListCellRendererSelector) getCellRenderer();
        renderer.setTextSelector(inSelector);
    }

    /**
     * Set the Selector for the list cell renderer: this is the property that
     * will be shown in a list cell (converted to a String).
     *
     * @param inSelectorString The string representation of the rendererSelector
     */
    public final void setRendererSelector(String inSelectorString) {
        ListCellRendererSelector renderer = (ListCellRendererSelector) getCellRenderer();
        renderer.setTextSelector(inSelectorString);
    }


    /**
     * Set the Selector for the list cell renderer: this is the property that
     * will be shown in a list cell (converted to a String).
     *
     * @param inSelectorString The string representation of the rendererSelector
     * @deprecated Use setRendererSelector(String) instead
     */
    public final void setRendererSelectorString(String inSelectorString) {
        setRendererSelector(inSelectorString);
    }


    /**
     * Optional: Set the Selector for the list cell renderer to get an Icon:
     * this is the property that will be shown as an Icon in a list cell.
     *
     * @param inSelector The new rendererIconSelector value
     */
    public final void setRendererIconSelector(Selector inSelector) {
        ListCellRendererSelector renderer = (ListCellRendererSelector) getCellRenderer();
        renderer.setIconSelector(inSelector);
    }


    /**
     * Optional: Set the Selector for the list cell renderer to get an Icon:
     * this is the property that will be shown as an Icon in a list cell.
     *
     * @param inSelectorString The string representation of the
     *      rendererIconSelector
     */
    public final void setRendererIconSelector(String inSelectorString) {
        ListCellRendererSelector renderer = (ListCellRendererSelector) getCellRenderer();
        renderer.setIconSelector(inSelectorString);
    }

    /**
     * Optional: Set the Selector for the list cell renderer to get an Icon:
     * this is the property that will be shown as an Icon in a list cell.
     *
     * @param inSelectorString The string representation of the
     *      rendererIconSelector
     * @deprecated Use setRendererIconSelector(String) instead
     */
    public final void setRendererIconSelectorString(String inSelectorString) {
        setRendererIconSelector(inSelectorString);
    }

    /**
     * Set the Control ID for the Control that will be issued when the selection
     * is changed. If null no Control will be issued.
     *
     * @param inControlID The new changeSelectionControlID value
     */
    public final void setChangeSelectionControlID(String inControlID) {
        selectionControlID = inControlID;
    }

    /**
     * Set the Control ID for the Control that will be issued when the List is
     * double-clicked. If null no Control will be issued.
     *
     * @param inControlID The new doubleClickControlID value
     */
    public final void setDoubleClickControlID(String inControlID) {
        doubleClickControlID = inControlID;
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
     * Sets the look and feel (L&amp;F) object that renders this component. <br>
     * Overriden here because there is no createDefaultCellRenderer() method in
     * JList.
     *
     * @param inUI the ListUI L&amp;F object
     * @see javax.swing.UIDefaults#getUI
     */
    public void setUI(ListUI inUI) {
        if (getCellRenderer() == null) {
            setCellRenderer(new SListCellRenderer());
        }
        super.setUI(inUI);
    }

    /**
     * <p>
     *
     * Sets the renderer that paints the item selected from the list in the
     * SList field. </p> <p>
     *
     * You need to use a ListCellRendererSelector in the SList, as this renderer uses
     * the bound model to display the information. </p>
     *
     * @param inRenderer the <code>ListCellRendererSelector</code> that displays the
     *      selected item
     * @see #setRendererSelector
     */
    public void setCellRenderer(ListCellRenderer inRenderer) {
        if (!(inRenderer instanceof ListCellRendererSelector)) {
            throw new IllegalArgumentException("Renderer must be an instance of ListCellRendererSelector");
        }
        super.setCellRenderer(inRenderer);
    }


    /**
     * <p>
     *
     * Sets the model</p> <p>
     *
     * Model must be an instance of SListModel.</p>
     *
     * @param inModel The new model value
     */
    public void setModel(ListModel inModel) {
        if (!(inModel instanceof SListModel)) {
            throw new IllegalArgumentException("Model must be a SListModel");
        }
        super.setModel(inModel);
    }

    /**
     * <p>
     *
     * Sets the <code>selectionModel</code> for the list to a non-<code>null</code>
     * <code>ListSelectionModel</code> implementation. The selection model
     * handles the task of making single selections, selections of contiguous
     * ranges, and non-contiguous selections. </p> <p>
     *
     * Model must be an instance of SListSelectionModel.</p>
     *
     * @param inSelectionModel The new model value
     */
    public void setSelectionModel(ListSelectionModel inSelectionModel) {
        if (!(inSelectionModel instanceof SListSelectionModel)) {
            throw new IllegalArgumentException("Model must be a SListSelectionModel");
        }
        super.setSelectionModel(inSelectionModel);
    }

    // --------- Set up list binding by delegation -------------

    /**
     * Can use this method to specify a static list model for the contents of
     * the list rather than binding to a dynamic property of some view model.
     *
     * @param inModel The new listModel value
     * @see org.scopemvc.model.collection.ListModelAdaptor
     */
    public void setListModel(Object inModel) {
        ((SListModel) getModel()).setListModel(inModel);
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
     * Defines if the bound property is read-only in the model. <br>
     * This affects the enabled state of the component. <br>
     * This method is public because of implementation constraints (method in
     * interface). You should not have to call it yourself.
     *
     * @param inReadOnly true if the bound property is read-only in the model
     */
    public void setReadOnly(boolean inReadOnly) {
        readOnly = inReadOnly;
        super.setEnabled(userEnabled && !readOnly);
    }

    // ------------- Implement SingleListSelectionParent ----------------

    /**
     * Find the index for the elememt in the bound list
     *
     * @param inValue The element
     * @return The index for the element in the bound list, or -1 if not found
     */
    public int findIndexFor(Object inValue) {
        if (inValue == null) {
            return -1;
        }
        for (int i = getModel().getSize() - 1; i >= 0; --i) {
            if (inValue.equals(getModel().getElementAt(i))) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Find the element in the bound list at the given index
     *
     * @param inIndex The index of the element in the list
     * @return The element, or null if not found
     */
    public Object findElementAt(int inIndex) {
        if (inIndex < 0) {
            return null;
        }
        try {
            return getModel().getElementAt(inIndex);
        } catch (Exception e) {
            LOG.warn("Can't findElementAt: " + inIndex, e);
        }
        return null;
    }


    // ------------------ Refreshable -------------------------

    /**
     * Updates the component with the current state of the bound model.
     */
    public void refresh() {
        ((SListModel) getModel()).refresh();
        refreshSelection();
    }


    // ------------- Support maintaining selection when list data changes ----------

    /**
     * Refresh the selection
     */
    public void refreshSelection() {
        ((SListSelectionModel) getSelectionModel()).refresh();
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


    // ------------------------ Create Selection ------------------------------

    /**
     * Returns an instance of <code>DefaultListSelectionModel</code>. This
     * method is used by the constructor to initialize the <code>selectionModel</code>
     * property.
     *
     * @return the <code>ListSelectionModel</code> used by this <code>SList</code>
     */
    protected ListSelectionModel createSelectionModel() {
        return new SListSelectionModel(this, false);
    }
}

