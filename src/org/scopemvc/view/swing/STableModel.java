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

import javax.swing.JTable;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.util.convertor.StringConvertor;

/**
 * The TableModel used in STable.
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.17 $ $Date: 2002/10/31 12:41:46 $
 * @created 05 September 2002
 * @see STable
 */
public class STableModel extends SAbstractListModel
         implements TableModel {

    private static final Log LOG = LogFactory.getLog(STableModel.class);

    /**
     * Selectors for the values displayed in the columns. Each Selector is for
     * one property in an element of the bound list.
     */
    private Selector[] columnSelectors;

    /**
     * Names of the columns displayed in the table header
     */
    private String[] columnNames;

    /**
     * if not editable when read-write property else read-only
     */
    private boolean[] columnsEditable;

    /**
     * The property manager for the items in the bound list
     */
    private PropertyManager itemsManager;

    /**
     * The table using this model
     */
    private JTable table;


    /**
     * Constructor for the STableModel object
     *
     * @param inTable The table using this TableModel
     */
    public STableModel(JTable inTable) {
        if (inTable == null) {
            throw new IllegalArgumentException("Cannot create a model for a null parent JTable");
        }
        table = inTable;
    }


    // --------------------- implement TableModel ---------------------------

    /**
     * Returns the number of rows in the model. A <code>JTable</code> uses this
     * method to determine how many rows it should display. This method should
     * be quick, as it is called frequently during rendering.
     *
     * @return the number of rows in the model
     */
    public int getRowCount() {
        int rowCount = getSize();
        if (LOG.isDebugEnabled()) {
            LOG.debug("getRowCount: result: " + rowCount);
        }
        return rowCount;
    }


    /**
     * Returns the number of columns in the model. A <code>JTable</code> uses
     * this method to determine how many columns it should create and display by
     * default.
     *
     * @return the number of columns in the model
     */
    public int getColumnCount() {
        if (columnSelectors == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getColumnCount: result: 0");
            }
            return 0;
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("getColumnCount: result: " + columnSelectors.length);
        }
        return columnSelectors.length;
    }


    /**
     * Returns the name of the column at <code>columnIndex</code>. This is used
     * to initialize the table's column header name. Note: this name does not
     * need to be unique; two columns in a table can have the same name.
     *
     * @param inColumnIndex the index of the column
     * @return the name of the column
     */
    public String getColumnName(int inColumnIndex) {
        if (columnNames == null || inColumnIndex >= columnNames.length) {
            return String.valueOf((char) ('A' + inColumnIndex));
        }
        return columnNames[inColumnIndex];
    }


    /**
     * Returns the most specific superclass for all the cell values in the
     * column. This is used by the <code>JTable</code> to set up a default
     * renderer and editor for the column.
     *
     * @param inColumnIndex the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    public Class getColumnClass(int inColumnIndex) {
        PropertyManager manager = getItemsManager();
        Object row = getElementAt(0);
        Selector selector = getColumnSelector(inColumnIndex);
        if (manager == null || row == null || selector == null) {
            LOG.warn("getColumnClass: no manager or row or selector for column " + inColumnIndex);
            return Object.class;
        }

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getColumnClass: " + manager.getPropertyClass(row, selector));
            }
            return manager.getPropertyClass(row, selector);
        } catch (Exception e) {
            LOG.warn("getColumnClass: can't get class for " + inColumnIndex, e);
        }
        return Object.class;
    }


    /**
     * Returns true if the cell at <code>rowIndex</code> and <code>columnIndex</code>
     * is editable. Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param inRowIndex the row whose value to be queried
     * @param inColumnIndex the column whose value to be queried
     * @return true if the cell is editable
     * @todo When the TableCellEditor implements View, update this method
     */
    public boolean isCellEditable(int inRowIndex, int inColumnIndex) {
        if (columnsEditable != null && inColumnIndex < columnsEditable.length) {
            // If the user forces the column to be not editable, return false immediately
            // else double-check if the column is editable
            if (!columnsEditable[inColumnIndex]) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("isCellEditable(" + inRowIndex + "," + inColumnIndex + "): user forced to false");
                }
                return false;
            }
        }

        Object row = getElementAt(inRowIndex);
        PropertyManager manager = getItemsManager();
        Selector selector = getColumnSelector(inColumnIndex);
        if (manager == null || row == null || selector == null) {
            LOG.warn("isCellEditable(" + inRowIndex + "," + inColumnIndex
                    + "): found no manager or row or selector");
            return false;
        }

        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("isCellEditable(" + inRowIndex + "," + inColumnIndex + "): property is read-only: "
                        + manager.isReadOnly(row, selector));
            }
            // check if the property is read-only
            if (!manager.isReadOnly(row, selector)) {
                // now check that the StringConvertor for the property does support convertion from string
                Class propertyClass = manager.getPropertyClass(row, selector);
                TableCellEditor editor = table.getDefaultEditor(propertyClass);
                if (editor instanceof STextCellEditor) {
                    StringConvertor convertor = ((STextCellEditor) editor).getStringConvertor();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("isCellEditable(" + inRowIndex + "," + inColumnIndex
                                + "): string convertor is: " + convertor + " for class: " + propertyClass);
                        LOG.debug("isCellEditable(" + inRowIndex + "," + inColumnIndex
                                + "): convertor supports stringAsValue: " + convertor.supportsStringAsValue());
                    }
                    return convertor.supportsStringAsValue();
                } else {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("isCellEditable(" + inRowIndex + "," + inColumnIndex
                                + "): editor " + editor.getClass() + " not recognized, assuming the cell is editable");
                    }
                    // can't check, assuming that the editor does support the class being edited
                    return true;
                }
            }
        } catch (Exception ex) {
            LOG.warn("isCellEditable(" + inRowIndex + "," + inColumnIndex + "): Unable "
                    + "to find out if cell is editable. Return false.", ex);
        }
        return false;
    }


    /**
     * Returns the value for the cell at <code>columnIndex</code> and <code>rowIndex.</code>
     *
     * @param inRowIndex the row whose value is to be queried
     * @param inColumnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int inRowIndex, int inColumnIndex) {
        Object row = getElementAt(inRowIndex);
        Selector selector = getColumnSelector(inColumnIndex);
        PropertyManager manager = getItemsManager();
        if (manager == null || row == null || selector == null) {
            LOG.warn("getValueAt(" + inRowIndex + "," + inColumnIndex + "): found no manager or row or selector");
            return null;
        }
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getValueAt: " + manager.get(row, selector) + "("
                        + manager.get(row, selector).getClass() + ")");
            }
            return manager.get(row, selector);
        } catch (Exception e) {
            LOG.debug("Can't get column " + inColumnIndex + " from row " + inRowIndex, e);
        }
        return null;
    }

    // ------------- public API -------------------

    /**
     * Sets the column selectors
     *
     * @param inSelectors The new columnSelectors value
     */
    public void setColumnSelectors(Selector[] inSelectors) {
        columnSelectors = inSelectors;
    }


    /**
     * Sets the column selector strings
     *
     * @param inSelectorStrings The new columnSelectorStrings value
     */
    public void setColumnSelectors(String[] inSelectorStrings) {
        if (inSelectorStrings == null) {
            columnSelectors = null;
        }
        columnSelectors = new Selector[inSelectorStrings.length];
        for (int i = 0; i < inSelectorStrings.length; i++) {
            if (inSelectorStrings[i] == null) {
                throw new IllegalArgumentException("Can't create column for null Selector");
            }
            columnSelectors[i] = Selector.fromString(inSelectorStrings[i]);
        }
    }


    /**
     * Sets the column selector strings
     *
     * @param inSelectorStrings The new columnSelectorStrings value
     * @deprecated Use setColumnSelectors(String[]) instead
     */
    public void setColumnSelectorStrings(String[] inSelectorStrings) {
        setColumnSelectors(inSelectorStrings);
    }


    /**
     * Sets the column names
     *
     * @param inNames The new columnNames value
     */
    public void setColumnNames(String[] inNames) {
        columnNames = inNames;
    }


    /**
     * Sets the editable columns
     *
     * @param inEditables The new editableColumns value
     */
    public void setEditableColumns(boolean[] inEditables) {
        columnsEditable = inEditables;
    }


    /**
     * Sets the value for the cell at <code>columnIndex</code> and <code>rowIndex.</code>
     *
     * @param inValue The new value of the cell
     * @param inRowIndex The row of the cell
     * @param inColumnIndex The column of the cell
     */
    public void setValueAt(Object inValue, int inRowIndex, int inColumnIndex) {
        Object row = getElementAt(inRowIndex);
        Selector selector = getColumnSelector(inColumnIndex);
        PropertyManager manager = getItemsManager();
        if (manager == null || row == null || selector == null) {
            LOG.warn("setValueAt: no manager or row or selector");
        }
        try {
            manager.set(row, selector, inValue);
        } catch (Exception e) {
            LOG.warn("Can't set column " + inColumnIndex + " from row " + inRowIndex, e);
        }
    }


    // ------ Override ListDataListener stuff to forward to TableModelListener ------

    /**
     * Adds a ListDataListener
     *
     * @param inListener The element to be added to the ListDataListener
     *      attribute
     */
    public void addListDataListener(ListDataListener inListener) {
        throw new UnsupportedOperationException("Can't add ListDataListener to STableModel");
    }


    /**
     * Removes a ListDataListener
     *
     * @param inListener The element to be added to the ListDataListener
     *      attribute
     */
    public void removeListDataListener(ListDataListener inListener) {
        throw new UnsupportedOperationException("Can't remove ListDataListener from STableModel");
    }


    // ------- TableModelListeners copied from JDK1.3.1 AbstractTableModel -------

    /**
     * Adds a listener to the list that's notified each time a change to the
     * data model occurs.
     *
     * @param inListener the TableModelListener
     */
    public void addTableModelListener(TableModelListener inListener) {
        listenerList.add(TableModelListener.class, inListener);
    }

    /**
     * Removes a listener from the list that's notified each time a change to
     * the data model occurs.
     *
     * @param inListener the TableModelListener
     */
    public void removeTableModelListener(TableModelListener inListener) {
        listenerList.remove(TableModelListener.class, inListener);
    }

    /**
     * Notifies all listeners that all cell values in the table's rows may have
     * changed. The number of rows may also have changed and the <code>JTable</code>
     * should redraw the table from scratch. The structure of the table (as in
     * the order of the columns) is assumed to be the same.
     *
     * @see TableModelEvent
     */
    public void fireTableDataChanged() {
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Notifies all listeners that the table's structure has changed. The number
     * of columns in the table, and the names and types of the new columns may
     * be different from the previous state. If the <code>JTable</code> receives
     * this event and its <code>autoCreateColumnsFromModel</code> flag is set it
     * discards any table columns that it had and reallocates default columns in
     * the order they appear in the model. This is the same as calling <code>setModel(TableModel)</code>
     * on the <code>JTable</code>.
     *
     * @see TableModelEvent
     */
    public void fireTableStructureChanged() {
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    /**
     * Notifies all listeners that rows in the range <code>[firstRow, lastRow]</code>
     * , inclusive, have been inserted.
     *
     * @param inFirstRow the first row
     * @param inLastRow the last row
     * @see TableModelEvent
     */
    public void fireTableRowsInserted(int inFirstRow, int inLastRow) {
        fireTableChanged(
                new TableModelEvent(
                this,
                inFirstRow,
                inLastRow,
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.INSERT));
    }

    /**
     * Notifies all listeners that rows in the range <code>[firstRow, lastRow]</code>
     * , inclusive, have been updated.
     *
     * @param inFirstRow the first row
     * @param inLastRow the last row
     * @see TableModelEvent
     */
    public void fireTableRowsUpdated(int inFirstRow, int inLastRow) {
        fireTableChanged(
                new TableModelEvent(
                this,
                inFirstRow,
                inLastRow,
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.UPDATE));
    }

    /**
     * Notifies all listeners that rows in the range <code>[firstRow, lastRow]</code>
     * , inclusive, have been deleted.
     *
     * @param inFirstRow the first row
     * @param inLastRow the last row
     * @see TableModelEvent
     */
    public void fireTableRowsDeleted(int inFirstRow, int inLastRow) {
        fireTableChanged(
                new TableModelEvent(
                this,
                inFirstRow,
                inLastRow,
                TableModelEvent.ALL_COLUMNS,
                TableModelEvent.DELETE));
    }

    /**
     * Notifies all listeners that the value of the cell at <code>[row, column]</code>
     * has been updated.
     *
     * @param inRow row of cell which has been updated
     * @param inColumn column of cell which has been updated
     * @see TableModelEvent
     */
    public void fireTableCellUpdated(int inRow, int inColumn) {
        fireTableChanged(new TableModelEvent(this, inRow, inRow, inColumn));
    }

    /**
     * Forwards the given notification event to all <code>TableModelListeners</code>
     * that registered themselves as listeners for this table model.
     *
     * @param inEvent the event to be forwarded
     * @see #addTableModelListener
     * @see TableModelEvent
     */
    public void fireTableChanged(TableModelEvent inEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TableModelListener.class) {
                ((TableModelListener) listeners[i + 1]).tableChanged(inEvent);
            }
        }
    }


    // -------------- internal -----------------

    /**
     * @return the table using this model
     */
    protected JTable getTable() {
        return table;
    }

    /**
     * Gets the column selector
     *
     * @param inColumnIndex The index of the column
     * @return The columnSelector value
     */
    protected Selector getColumnSelector(int inColumnIndex) {
        if (columnSelectors == null) {
            return null;
        }
        return columnSelectors[inColumnIndex];
    }


    /**
     * Gets the property manager for the items in the bound list.
     *
     * @return The propertyManager value
     */
    protected PropertyManager getItemsManager() {
        if (itemsManager == null) {
            if (getRowCount() > 0) {
                Object row = getElementAt(0);
                itemsManager = PropertyManager.getInstance(row);
            }
        }
        return itemsManager;
    }


    // ---------------- Bound model stuff ------------------

    /**
     * Called internally from updateFromProperty().
     *
     * @param inModel The new shownModel value
     */
    protected void setShownModel(Object inModel) {

        super.setShownModel(inModel);

        // ***** This is necessary for updates to work at all...
        // ... but it looks very wrong.
        fireTableDataChanged();

        itemsManager = null;

        // ***** This is broken because of the above hack
        if (table instanceof STable) {
            ((STable) table).refreshSelection();
        } else {
            // Could use a better strategy for completeness
            table.clearSelection();
        }
    }


    /**
     * Call this method <b>after</b> one or more elements of the list change.
     * The changed elements are specified by a closed interval index0, index1,
     * i.e. the range that includes both index0 and index1. Note that index0
     * need not be less than or equal to index1.
     *
     * @param inSource The ListModel that changed, typically "this".
     * @param inIndex0 One end of the new interval.
     * @param inIndex1 The other end of the new interval.
     */
    protected void fireContentsChanged(Object inSource, int inIndex0, int inIndex1) {
        fireTableRowsUpdated(inIndex0, inIndex1);
    }


    /**
     * Call this method <b>after</b> one or more elements are added to the
     * model. The new elements are specified by a closed interval index0,
     * index1, i.e. the range that includes both index0 and index1. Note that
     * index0 need not be less than or equal to index1.
     *
     * @param inSource The ListModel that changed, typically "this".
     * @param inIndex0 One end of the new interval.
     * @param inIndex1 The other end of the new interval.
     */
    protected void fireIntervalAdded(Object inSource, int inIndex0, int inIndex1) {
        fireTableRowsInserted(inIndex0, inIndex1);
    }


    /**
     * Call this method <b>after</b> one or more elements are removed from the
     * model. The new elements are specified by a closed interval index0,
     * index1, i.e. the range that includes both index0 and index1. Note that
     * index0 need not be less than or equal to index1.
     *
     * @param inSource The ListModel that changed, typically "this".
     * @param inIndex0 One end of the new interval.
     * @param inIndex1 The other end of the new interval.
     */
    protected void fireIntervalRemoved(Object inSource, int inIndex0, int inIndex1) {
        fireTableRowsDeleted(inIndex0, inIndex1);
    }
}
