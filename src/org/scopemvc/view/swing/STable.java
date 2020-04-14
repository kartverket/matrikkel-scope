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
 * $Id: STable.java,v 1.30 2002/10/31 12:41:46 ludovicc Exp $
 * Changes:
 * - added setChangeSelectionActionID(id) (scope 2.0)
 * - added setDoubleClickActionID(id) (scope 2.0)
 * - added setPointer(pointer) in deprecation of setSelector(selector) (scope 2.0)
 * - added setSelectionPointer(ptr) (scope 2.0)
 * - added setSizePointer(ptr) (scope 2.0)
 * - added setColumnPointers(ptr) (scope 2.0)
 *
 * - added scopeconfig property "org.scopemvc.view.swing.STable.invertColorsWhenDisabled"
 * - added scopeconfig property "org.scopemvc.view.swing.STable.autoAdjustColumnWidths"
 * - added java.util.Date in createDefaultRenderers()
 *
 */
package org.scopemvc.view.swing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.Selector;
import org.scopemvc.core.View;
import org.scopemvc.Pointer;
import org.scopemvc.util.ScopeConfig;

/**
 * <P>
 *
 * A JTable bound to a list property of a model. The table shows a list of rows
 * from the bound property (see {@link #setSelector}). If the rows are model
 * objects, the properties shown for each column are set using {@link
 * #setColumnSelectors}. See also {@link #setColumnNames}. </P> <P>
 *
 * STable uses {@link STableModel} and so the contents can be sorted using
 * {@link SAbstractListModel#setSorted(boolean) setSorted(boolean)} or {@link
 * SAbstractListModel#setSorted(java.util.Comparator) setSorted(Comparator)}.
 * </P> <P>
 *
 * STable uses a standard (non-bound) Swing ListSelectionModel unless a
 * selection Selector is set using {@link #setSelectionSelector} in which case a
 * bound SListSelectionModel is used. This allows both single-selection and
 * multiselection (using a HashSet property to hold the selected objects. See
 * {@link SListSelectionModel}, which is used by STable to maintain bound
 * selections. </P>
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.30 $ $Date: 2002/10/31 12:41:46 $
 * @created 05 September 2002
 * @todo The table renderers and editors should be configurable with ScopeConfig
 *      and EditorManager. Issue: the renderers must implement the View
 *      interface, which makes programming heavier... (ludovicc)
 */
public class STable extends JTable
         implements View, Refreshable, ListSelectionParent {

    private static final Log LOG = LogFactory.getLog(STable.class);

    // -------------------- Controls -----------------------

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
     * Constructor for the STable object
     */
    public STable() {
        super(null, null, null);
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
        // single selection by default
        setSelectionModel(createDefaultSelectionModel());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    // ------------------ Implement View by delegation to STableModel and selection

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return ((STableModel) getModel()).getBoundModel();
    }


    // --------- Set up table binding by delegation -------------

    /**
     * Gets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The component will present this property to the user.
     *
     * @return A selector.
     */
    public final Selector getSelector() {
        return ((STableModel) getModel()).getSelector();
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
     * Gets the selector for the property giving the size of the list of items.
     *
     * @return The selector for the list size.
     */
    public final Selector getSizeSelector() {
        return ((STableModel) getModel()).getSizeSelector();
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
     * Returns the editor to be used when no editor has been set in a <code>TableColumn.</code>
     *
     * @param inColumnClass The class of the column
     * @return The defaultEditor value
     */
    public TableCellEditor getDefaultEditor(Class inColumnClass) {
        // Had to reimplement this method as the JTable implementation doesn't have the correct logic
        // for creating default editors
        if (inColumnClass == null) {
            return null;
        } else {
            TableCellEditor editor = (TableCellEditor) defaultEditorsByColumnClass.get(inColumnClass);
            if (editor == null) {
                try {
                    editor = new STableTextCellEditor(inColumnClass);
                    defaultEditorsByColumnClass.put(inColumnClass, editor);
                } catch (Exception ignore) {
                    editor = getDefaultEditor(inColumnClass.getSuperclass());
                }
            }
            if (editor == null) {
                LOG.warn("No editor could be created for class " + inColumnClass);
            }
            return editor;
        }
    }


    /**
     * Returns the cell renderer to be used when no renderer has been set in a
     * <code>TableColumn</code>.
     *
     * @param inColumnClass The class of the column
     * @return The defaultRenderer value
     */
    public TableCellRenderer getDefaultRenderer(Class inColumnClass) {
        // Had to reimplement this method as the JTable implementation doesn't have the correct logic
        // for creating default renderers
        if (inColumnClass == null) {
            return null;
        } else {
            TableCellRenderer renderer = (TableCellRenderer) defaultRenderersByColumnClass.get(inColumnClass);
            if (renderer == null) {
                try {
                    renderer = new SDefaultTableCellRenderer(inColumnClass);
                    defaultRenderersByColumnClass.put(inColumnClass, renderer);
                } catch (Exception ignore) {
                    renderer = getDefaultRenderer(inColumnClass.getSuperclass());
                }
            }
            if (renderer == null) {
                LOG.warn("No renderer could be created for class " + inColumnClass);
            }
            return renderer;
        }
    }


    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public final void setBoundModel(Object inModel) {
        ((STableModel) getModel()).setBoundModel(inModel);
        ((SListSelectionModel) getSelectionModel()).setBoundModel(inModel);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setChangeSelectionControlID(..)</code> for now.
     *
     * @param actionID the Id of an action
     */
    public void setChangeSelectionActionID(String actionID) {
        setChangeSelectionControlID(actionID);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setDoubleClickControlID(..)</code> for now.
     *
     * @param actionID the Id of an action
     */
    public void setDoubleClickActionID(String actionID) {
        setDoubleClickControlID(actionID);
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
     * For forward compability with Scope 2.0. Delegates to <code>setSelectionSelector(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setSelectionPointer(String pointerPath) {
        setSelectionSelector(pointerPath);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelectionSelector(..)</code> for now.
     *
     * @param pointer a pointer to the model
     */
    public void setSelectionPointer(Pointer pointer) {
        setSelectionSelector(pointer.getSelector());
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSizeSelector(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setSizePointer(String pointerPath) {
        setSizeSelector(pointerPath);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSizeSelector(..)</code> for now.
     *
     * @param pointer a pointer to the model
     */
    public void setSizePointer(Pointer pointer) {
        setSizeSelector(pointer.getSelector());
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setColumnSelectors(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setColumnPointers(String[] pointerPath) {
        setColumnSelectors(pointerPath);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setColumnSelectors(..)</code> for now.
     *
     * @param pointers an arrary of pointers to the model
     */
    public void setColumnPointers(Pointer[] pointers) {
        Selector[] selectors = new Selector[pointers.length];
        for (int i = 0; i < pointers.length; i++) {
            selectors[i] = pointers[i].getSelector();
        }

        setColumnSelectors(selectors);
    }

    /**
     * Set the Selector for the table data. Should be a java.util.List or an
     * Object[] or have a "size" property and properties accessible by an
     * IntIndexedSelector.
     *
     * @param inSelector The new selector value
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSelector(Selector inSelector) {
        ((STableModel) getModel()).setSelector(inSelector);
    }


    /**
     * Set the Selector for the table data. Should be a java.util.List or an
     * Object[] or have a "size" property and properties accessible by an
     * IntIndexedSelector.
     *
     * @param inSelectorString The string representation of the selector
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSelector(String inSelectorString) {
        ((STableModel) getModel()).setSelector(inSelectorString);
    }

    /**
     * Set the Selector for the table data. Should be a java.util.List or an
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
     * @deprecated Will be removed in Scope 2.0
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
     * @deprecated Will be removed in Scope 2.0
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
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSizeSelector(Selector inSelector) {
        ((STableModel) getModel()).setSizeSelector(inSelector);
    }


    /**
     * Optional: set the Selector for the property that is the size of the items
     * list. Not needed for lists that are of type Object[] or java.util.List.
     *
     * @param inSelectorString The string representation of the sizeSelector
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSizeSelector(String inSelectorString) {
        ((STableModel) getModel()).setSizeSelector(inSelectorString);
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
     * Set the Control ID for the Control that will be issued when the selection
     * is changed. If null no Control will be issued.
     *
     * @param inControlID The new changeSelectionControlID value
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setChangeSelectionControlID(String inControlID) {
        selectionControlID = inControlID;
    }

    /**
     * Set the Control ID for the Control that will be issued when the List is
     * double-clicked. If null no Control will be issued.
     *
     * @param inControlID The new doubleClickControlID value
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setDoubleClickControlID(String inControlID) {
        doubleClickControlID = inControlID;
    }


    /**
     * Set up the column selectors.
     *
     * @param inSelectors The new columnSelectors value
     * @deprecated Will be removed in Scope 2.0
     */
    public void setColumnSelectors(Selector[] inSelectors) {
        ((STableModel) getModel()).setColumnSelectors(inSelectors);
        createDefaultColumnsFromModel();
    }


    /**
     * Set up the column selectors.
     *
     * @param inSelectorStrings The new columnSelectorStrings value
     * @deprecated Will be removed in Scope 2.0
     */
    public void setColumnSelectors(String[] inSelectorStrings) {
        ((STableModel) getModel()).setColumnSelectors(inSelectorStrings);
        createDefaultColumnsFromModel();
    }

    /**
     * Set up the column selectors.
     *
     * @param inSelectorStrings The new columnSelectorStrings value
     * @deprecated Use setColumnSelectors(String[]) instead
     */
    public void setColumnSelectorStrings(String[] inSelectorStrings) {
        setColumnSelectors(inSelectorStrings);
    }


    /**
     * Set up the column names.
     *
     * @param inNames The new columnNames value
     */
    public void setColumnNames(String[] inNames) {
        ((STableModel) getModel()).setColumnNames(inNames);
        createDefaultColumnsFromModel();
    }

   /**
    * Enables or disables this component, depending on the value of the
    * parameter <code>b</code>. An enabled component can respond to user input
    * and generate events. Components are enabled initially by default.
    *
    * @param inEnabled If <code>true</code>, this component is enabled;
    *                  otherwise this component is disabled.
    */
   public void setEnabled(boolean inEnabled) {
      boolean shouldInvertColors = "true".equalsIgnoreCase(ScopeConfig.getString("org.scopemvc.view.swing.STable.invertColorsWhenDisabled"));
      shouldInvertColors &= inEnabled != userEnabled;
      userEnabled = inEnabled;
      if( shouldInvertColors ) {
         Color background = getBackground();
         setBackground(getGridColor());
         setGridColor(background);
      }
      super.setEnabled(inEnabled && !readOnly);
   }

    /**
     * <p>
     *
     * Sets the model</p> <p>
     *
     * Model must be an instance of STableModel.</p>
     *
     * @param inModel The new model value
     */
    public void setModel(TableModel inModel) {
        if (!(inModel instanceof STableModel)) {
            throw new IllegalArgumentException("Model must be a STableModel");
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
        ((STableModel) getModel()).setListModel(inModel);
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
        STableModel tableModel = (STableModel) getModel();
        for (int i = tableModel.getSize() - 1; i >= 0; --i) {
            if (inValue.equals(tableModel.getElementAt(i))) {
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
        STableModel tableModel = (STableModel) getModel();
        try {
            return tableModel.getElementAt(inIndex);
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
        ((STableModel) getModel()).refresh();
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
    * @see #validationFailure
     */
    public JToolTip createToolTip() {
        return validationHelper.createToolTip(super.createToolTip());
    }


    /**
     * STable create its own STableModel by default.
     *
     * @return The default table model
     */
    protected TableModel createDefaultDataModel() {
        return new STableModel(this);
    }


    /**
     * Returns an instance of <code>DefaultListSelectionModel</code>. This
     * method is used by the constructor to initialize the <code>selectionModel</code>
     * property.
     *
     * @return the <code>ListSelectionModel</code> used by this <code>STable</code>
     */
    protected ListSelectionModel createDefaultSelectionModel() {
        return new SListSelectionModel(this, false);
    }


    /**
     * Sets default Scope table cell renderers. They use Scope's
     * StringConverters.
     */
    protected void createDefaultRenderers() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("create DefaultRenderers for STable");
        }

        super.createDefaultRenderers();
        // else updateUI() fails in superclass

        setDefaultRenderer(Boolean.class, new BooleanRenderer());
        setDefaultRenderer(Boolean.TYPE, new BooleanRenderer());

        SDefaultTableCellRenderer rend = new SDefaultTableCellRenderer(Double.class);
        rend.setHorizontalAlignment(JLabel.RIGHT);
        setDefaultRenderer(Double.class, rend);
        setDefaultRenderer(Double.TYPE, rend);

        rend = new SDefaultTableCellRenderer(Float.class);
        rend.setHorizontalAlignment(JLabel.RIGHT);
        setDefaultRenderer(Float.class, rend);
        setDefaultRenderer(Float.TYPE, rend);

        rend = new SDefaultTableCellRenderer(Integer.class);
        rend.setHorizontalAlignment(JLabel.RIGHT);
        setDefaultRenderer(Integer.class, rend);
        setDefaultRenderer(Integer.TYPE, rend);

        rend = new SDefaultTableCellRenderer(Long.class);
        rend.setHorizontalAlignment(JLabel.RIGHT);
        setDefaultRenderer(Long.class, rend);
        setDefaultRenderer(Long.TYPE, rend);

        rend = new SDefaultTableCellRenderer(BigInteger.class);
        rend.setHorizontalAlignment(JLabel.RIGHT);
        setDefaultRenderer(BigInteger.class, rend);

        rend = new SDefaultTableCellRenderer(BigDecimal.class);
        rend.setHorizontalAlignment(JLabel.RIGHT);
        setDefaultRenderer(BigDecimal.class, rend);

        rend = new SDefaultTableCellRenderer(java.util.Date.class);
        rend.setHorizontalAlignment(JLabel.RIGHT);
        setDefaultRenderer(java.util.Date.class, rend);
    }


    /**
     * Sets default Scope table cell editors. They use Scope's StringConverters.
     */
    protected void createDefaultEditors() {

        super.createDefaultEditors();
        // else updateUI() fails in superclass

        setDefaultEditor(Boolean.class, new BooleanEditor());
        setDefaultEditor(Boolean.TYPE, new BooleanEditor());
    }


    static class BooleanRenderer extends JCheckBox implements TableCellRenderer {
        /**
         * Constructor for the BooleanRenderer object
         */
        public BooleanRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        /**
         * Returns the component used for drawing the cell. This method is used
         * to configure the renderer appropriately before drawing.
         *
         * @param inTable the <code>JTable</code> that is asking the renderer to
         *      draw; can be <code>null</code>
         * @param inValue the value of the cell to be rendered. It is up to the
         *      specific renderer to interpret and draw the value. For example,
         *      if <code>value</code> is the string "true", it could be rendered
         *      as a string or it could be rendered as a check box that is
         *      checked. <code>null</code> is a valid value
         * @param inSelected true if the cell is to be rendered with the
         *      selection highlighted; otherwise false
         * @param inFocus if true, render cell appropriately. For example, put a
         *      special border on the cell, if the cell can be edited, render in
         *      the color used to indicate editing
         * @param inRow the row index of the cell being drawn. When drawing the
         *      header, the value of <code>row</code> is -1
         * @param inColumn the column index of the cell being drawn
         * @return The tableCellRendererComponent value
         */
        public Component getTableCellRendererComponent(JTable inTable, Object inValue,
                boolean inSelected, boolean inFocus, int inRow, int inColumn) {
            if (inSelected) {
                setForeground(inTable.getSelectionForeground());
                super.setBackground(inTable.getSelectionBackground());
            } else {
                setForeground(inTable.getForeground());
                setBackground(inTable.getBackground());
            }
            setSelected((inValue != null && ((Boolean) inValue).booleanValue()));
            return this;
        }
    }


    static class BooleanEditor extends DefaultCellEditor {
        /**
         * Constructor for the BooleanEditor object
         */
        public BooleanEditor() {
            super(new JCheckBox());
            JCheckBox checkBox = (JCheckBox) getComponent();
            checkBox.setHorizontalAlignment(JCheckBox.CENTER);
        }
    }

    /** Beregner ønsket bredde for hver kolonne */
    public void adjustColumnWidths() {
        JTableHeader header = getTableHeader();

        if( header != null ) {
            TableCellRenderer defaultHeaderRenderer = header.getDefaultRenderer();

            TableColumnModel columns = getColumnModel();
            STableModel data = (STableModel) getModel();

            int margin = columns.getColumnMargin(); // bare JDK1.3
            int rowCount = data.getRowCount();

            for( int i = columns.getColumnCount() - 1; i >= 0; --i ) {
                TableColumn column = columns.getColumn(i);
                int columnIndex = column.getModelIndex();
                int width = -1;

                TableCellRenderer h = column.getHeaderRenderer();
                if( h == null ) {
                    h = defaultHeaderRenderer;
                }
                if( h != null ) { // Ikke eksplisitt umulig
                    Component c = h.getTableCellRendererComponent(this, column.getHeaderValue(), false, false, -1, i);
                    width = c.getPreferredSize().width;
                }
                for( int row = rowCount - 1; row >= 0; --row ) {
                    TableCellRenderer r = getCellRenderer(row, i);
                    Component c = r.getTableCellRendererComponent(this, data.getValueAt(row, columnIndex), false, false, row, i);
                    width = Math.max(width, c.getPreferredSize().width);
                }
                if( width >= 0 ) {
                    column.setPreferredWidth(width + margin);
                }
            }
        }

    }

    public void tableChanged(TableModelEvent e) {
        if( e.getColumn() == TableModelEvent.HEADER_ROW ) {
            if( "true".equalsIgnoreCase(ScopeConfig.getString("org.scopemvc.view.swing.STable.autoAdjustColumnWidths")) ) {
                adjustColumnWidths();
            }
        }
        super.tableChanged(e);
    }

}

