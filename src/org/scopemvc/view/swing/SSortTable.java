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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <font color="READ">ALPHA VERSION - may change in the next release</font> <p>
 *
 * STable with sorting facilities. The column headers are clickable and the
 * content can be sorted by one column, ascending or descending. </p> <p>
 *
 * It creates a SSortTableModel, which wraps the original STableModel. </p>
 *
 * @author Patrik Nordwall
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.4 $
 * @created 29 October 2002
 * @see SSortTableModel
 */
public class SSortTable extends STable {

    private static final Log LOG = LogFactory.getLog(SSortTable.class);

    private SortHeaderCellRenderer headerRenderer;

    /**
     * Constructor for the SSortTable object
     */
    public SSortTable() {
        super();

        headerRenderer = new SortHeaderCellRenderer();
        getTableHeader().setDefaultRenderer(headerRenderer);
        getTableHeader().addMouseListener(new HeaderListener());
    }

    /**
     * Overrides method in ListSelectionParent interface, which is used by
     * SListSelectionModel. Converts the visible row index to the row index in
     * the underlaying model. These indexes can differ, since the table is
     * sorted.
     *
     * @param inValue The value of the element in the model
     * @return The row index for the element, or -1 if not found.
     */
    public int findIndexFor(Object inValue) {
        if (inValue == null) {
            return -1;
        }
        SSortTableModel sorter = (SSortTableModel) getModel();
        for (int i = getModel().getRowCount() - 1; i >= 0; --i) {
            int convertedIndex = sorter.convertRowIndexToModel(i);
            if (inValue.equals(((SAbstractListModel) getModel()).getElementAt(convertedIndex))) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Overrides method in ListSelectionParent interface, which is used by
     * SListSelectionModel. Convertes the visible row index to the row index in
     * the underlaying model. These indexes can differ, since the table is
     * sorted.
     *
     * @param inRowIndex The index of the row in the table
     * @return null if not found.
     */
    public Object findElementAt(int inRowIndex) {
        if (inRowIndex < 0) {
            return null;
        }
        try {
            int convertedIndex = ((SSortTableModel) getModel()).convertRowIndexToModel(inRowIndex);
            return ((SAbstractListModel) getModel()).getElementAt(convertedIndex);
        } catch (Exception e) {
            LOG.warn("Can't findElementAt: " + inRowIndex, e);
        }
        return null;
    }


    /**
     * This method can be used to programatically sort the table. The column
     * headers will also be updated. Use parameter -1 to reset the table and
     * remove the sorting.
     *
     * @param inColumnIndex The column to sort by, the column index refer to the
     *      underlaying model
     */
    public void sortByColumn(int inColumnIndex) {
        if (inColumnIndex == -1) {
            //reset sorting by fire change of entire model
            ((STableModel) getModel()).fireTableChanged(new TableModelEvent(getModel()));
        } else {
            int viewCol = convertColumnIndexToView(inColumnIndex);
            headerRenderer.setSelectedColumn(viewCol);
            getTableHeader().repaint();
            boolean sortAscending = headerRenderer.isAscending(viewCol);
            ((SSortTableModel) getModel()).sortByColumn(inColumnIndex, sortAscending);
        }
    }

    /**
     * Creates a SSortTableModel model, which is a STableModel
     *
     * @return The table model to use in this table
     */
    protected TableModel createDefaultDataModel() {
        return new SSortTableModel(this);
    }

    /**
     * Remove the sorting icons on the headers
     */
    protected void resetHeader() {
        headerRenderer.setSelectedColumn(-1);
        getTableHeader().repaint();
    }


    /**
     * A blank icon
     *
     * @author Patrik Nordwall
     * @version $Revision: 1.4 $
     * @created 29 October 2002
     */
    public static class BlankIcon implements Icon {
        private Color fillColor;
        private int size;

        /**
         * Constructor for the BlankIcon object
         */
        public BlankIcon() {
            this(null, 11);
        }

        /**
         * Constructor for the BlankIcon object
         *
         * @param inColor The background color
         * @param inSize The size of the icon
         */
        public BlankIcon(Color inColor, int inSize) {
            //UIManager.getColor("control")
            //UIManager.getColor("controlShadow")
            fillColor = inColor;

            this.size = inSize;
        }

        /**
         * Gets the icon width
         *
         * @return The iconWidth value
         */
        public int getIconWidth() {
            return size;
        }

        /**
         * Gets the icon height
         *
         * @return The iconHeight value
         */
        public int getIconHeight() {
            return size;
        }

        /**
         * Paint the icon
         *
         * @param inComponent The component to paint the icon on
         * @param inGraphics The graphics object to use for painting
         * @param inX The X position
         * @param inY The Y position
         */
        public void paintIcon(Component inComponent, Graphics inGraphics, int inX, int inY) {
            if (fillColor != null) {
                inGraphics.setColor(fillColor);
                inGraphics.drawRect(inX, inY, size - 1, size - 1);
            }
        }
    }

    /**
     * <p>
     *
     * This renderer draws the buttons in the headers of a table. It draws
     * down/up arrows to indicate that sort has been made on a column. </p> <p>
     *
     * This class is based on code from <a
     * href="http://www2.gol.com/users/tame/swing/examples/JTableExamples5.html">
     * JTableExample5</a> </p>
     *
     * @author Patrik Nordwall
     * @version $Revision: 1.4 $
     * @created 29 October 2002
     */
    static class SortHeaderCellRenderer implements TableCellRenderer {

        private static final Log LOG = LogFactory.getLog(SortHeaderCellRenderer.class);

        private int pushedColumn = -1;
        private Map columnStates = new HashMap();
        private boolean active = true;

        private State noneState;
        private State upState;
        private State downState;

        /**
         * Constructor for the SortHeaderCellRenderer object
         */
        public SortHeaderCellRenderer() {
            // init the different states
            noneState = new NoneState();
            upState = new UpState();
            downState = new DownState();
            noneState.setNext(upState);
            upState.setNext(downState);
            downState.setNext(noneState);
        }

        /**
         * Implements javax.swing.table.TableCellRenderer.
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
            State state = noneState;
            if (active) {
                state = getState(inColumn);
            }
            JButton button = state.getButton();
            button.setText((inValue == null) ? "" : inValue.toString());
            boolean isPressed = (inColumn == pushedColumn);
            button.getModel().setPressed(isPressed);
            button.getModel().setArmed(isPressed);
            return button;
        }

        /**
         * @param inColumn The column index
         * @return true if the state of the column is ascending
         */
        public boolean isAscending(int inColumn) {
            return getState(inColumn).isAscending();
        }

        /**
         * Changes the font of the buttons.
         *
         * @param inFont new font to use
         */
        public void setFont(Font inFont) {
            noneState.getButton().setFont(inFont);
            upState.getButton().setFont(inFont);
            downState.getButton().setFont(inFont);
        }

        /**
         * Should be invoked when a button is pressed. Should be invoked with -1
         * when released.
         *
         * @param inColumn The new pressedColumn value
         */
        public void setPressedColumn(int inColumn) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Pressed column = " + inColumn);
            }
            pushedColumn = inColumn;
        }

        /**
         * Should be invoked when a sort will be performed on a column.
         *
         * @param inColumn The new selectedColumn value
         */
        public void setSelectedColumn(int inColumn) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Selected column = " + inColumn);
            }
            if (inColumn < 0) {
                columnStates.clear();
            }
            Integer column = new Integer(inColumn);
            State state = (State) columnStates.get(column);
            if (state == null) {
                state = noneState;
            }
            columnStates.clear();
            columnStates.put(column, state.next());
            if (LOG.isDebugEnabled()) {
                LOG.debug("New state: " + state.next());
            }
        }

        /**
         * Activates or deactivates this renderer. When deactivated no arrows
         * are drawn.
         *
         * @param inActive The new active value
         */
        public void setActive(boolean inActive) {
            active = inActive;
        }

        /**
         * @param inColumn The column index
         * @return the state of a column
         */
        private State getState(int inColumn) {
            State state = (State) columnStates.get(new Integer(inColumn));
            if (state == null) {
                return noneState;
            } else {
                return state;
            }
        }
    }

    //End of inner class BlancIcon

    /**
     * State pattern instead of if statements.
     *
     * @author Patrik Nordwall
     * @version $Revision: 1.4 $
     * @created 29 October 2002
     */
    abstract static class State {

        private State next;
        // for debugging
        private String description;

        /**
         * Constructor for the State object
         *
         * @param inDescription Plain english description of the state
         */
        protected State(String inDescription) {
            description = inDescription;
        }

        /**
         * Returns a string representation of this object
         *
         * @return a string representation
         */
        public String toString() {
            return description + " state";
        }

        /**
         * Gets the button
         *
         * @return The button value
         */
        protected abstract JButton getButton();

        /**
         * Returns true if the sort is ascending
         *
         * @return true if the sort is ascending
         */
        protected abstract boolean isAscending();

        /**
         * Sets the next state
         *
         * @param inNext The new next state
         */
        protected void setNext(State inNext) {
            next = inNext;
        }

        /**
         * Returns the next state
         *
         * @return the next state
         */
        protected State next() {
            return next;
        }

    }

    static class NoneState extends State {
        private JButton button;

        /**
         * Constructor for the NoneState object
         */
        protected NoneState() {
            super("None");
            Insets zeroInsets = new Insets(0, 0, 0, 0);
            button = new JButton();
            button.setBorder(new BevelBorder(BevelBorder.RAISED));
            button.setMargin(zeroInsets);
            button.setHorizontalTextPosition(JLabel.LEFT);
            button.setIcon(new BlankIcon());
        }

        /**
         * Gets the button
         *
         * @return The button value
         */
        protected JButton getButton() {
            return button;
        }

        /**
         * Gets the ascending
         *
         * @return The ascending value
         */
        protected boolean isAscending() {
            return true;
        }
    }

    static class UpState extends State {
        private JButton button;

        /**
         * Constructor for the UpState object
         */
        protected UpState() {
            super("Up");
            Insets zeroInsets = new Insets(0, 0, 0, 0);
            button = new JButton();
            button.setBorder(new BevelBorder(BevelBorder.RAISED));
            button.setMargin(zeroInsets);
            button.setHorizontalTextPosition(JLabel.LEFT);
            button.setIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, false));
            button.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.UP, false, true));
        }

        /**
         * Gets the button
         *
         * @return The button value
         */
        protected JButton getButton() {
            return button;
        }

        /**
         * Gets the ascending
         *
         * @return The ascending value
         */
        protected boolean isAscending() {
            return true;
        }
    }

    static class DownState extends State {
        private JButton button;

        /**
         * Constructor for the DownState object
         */
        protected DownState() {
            super("Down");
            Insets zeroInsets = new Insets(0, 0, 0, 0);
            button = new JButton();
            button.setBorder(new BevelBorder(BevelBorder.RAISED));
            button.setMargin(zeroInsets);
            button.setHorizontalTextPosition(JLabel.LEFT);
            button.setIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, false));
            button.setPressedIcon(new BevelArrowIcon(BevelArrowIcon.DOWN, false, true));
        }

        /**
         * Gets the button
         *
         * @return The button value
         */
        protected JButton getButton() {
            return button;
        }

        /**
         * Gets the ascending
         *
         * @return The ascending value
         */
        protected boolean isAscending() {
            return false;
        }
    }

    /**
     * Icon of the arrow that is used to indicate the sorting column. The arrow
     * can be UP or DOWN. <p>
     *
     * This class is based on code from http://www2.gol.com/users/tame/swing/examples/JTableExamples5.html
     *
     * @author Patrik Nordwall
     * @version $Revision: 1.4 $
     * @created 29 October 2002
     */
    private static class BevelArrowIcon implements Icon {
        /**
         * The Up direction for the arrow
         */
        public static final int UP = 0;

        /**
         * The Down direction for the arrow
         */
        public static final int DOWN = 1;

        private static final int DEFAULT_SIZE = 11;

        private Color edge1;
        private Color edge2;
        private Color fill;
        private int size;
        private int direction;

        /**
         * Constructor for the BevelArrowIcon object
         *
         * @param inDirection The direction of the array, UP or DOWN
         * @param inRaisedView true if the view is raised
         * @param inPressedView true if the button is pressed
         */
        public BevelArrowIcon(int inDirection, boolean inRaisedView, boolean inPressedView) {
            if (inRaisedView) {
                if (inPressedView) {
                    init(UIManager.getColor("controlLtHighlight"),
                            UIManager.getColor("controlDkShadow"),
                            UIManager.getColor("controlShadow"),
                            DEFAULT_SIZE, inDirection);
                } else {
                    init(UIManager.getColor("controlLtHighlight"),
                            UIManager.getColor("controlShadow"),
                            UIManager.getColor("control"),
                            DEFAULT_SIZE, inDirection);
                }
            } else {
                if (inPressedView) {
                    init(UIManager.getColor("controlDkShadow"),
                            UIManager.getColor("controlLtHighlight"),
                            UIManager.getColor("controlShadow"),
                            DEFAULT_SIZE, inDirection);
                } else {
                    init(UIManager.getColor("controlShadow"),
                            UIManager.getColor("controlLtHighlight"),
                            UIManager.getColor("control"),
                            DEFAULT_SIZE, inDirection);
                }
            }
        }

        /**
         * Constructor for the BevelArrowIcon object
         *
         * @param inEdge1 The color for the first edge
         * @param inEdge2 The color for the second edge
         * @param inFill The color used for filling
         * @param inSize The size of the arrow
         * @param inDirection The direction of the array, UP or DOWN
         */
        public BevelArrowIcon(Color inEdge1, Color inEdge2, Color inFill,
                int inSize, int inDirection) {
            init(inEdge1, inEdge2, inFill, inSize, inDirection);
        }

        /**
         * Gets the icon width
         *
         * @return The iconWidth value
         */
        public int getIconWidth() {
            return size;
        }

        /**
         * Gets the icon height
         *
         * @return The iconHeight value
         */
        public int getIconHeight() {
            return size;
        }


        /**
         * Paint the icon
         *
         * @param inComponent The component to paint the icon on
         * @param inGraphics The graphics object to use for painting
         * @param inX The X position
         * @param inY The Y position
         */
        public void paintIcon(Component inComponent, Graphics inGraphics, int inX, int inY) {
            switch (direction) {
                case DOWN:
                    drawDownArrow(inGraphics, inX, inY);
                    break;
                case UP:
                    drawUpArrow(inGraphics, inX, inY);
                    break;
            }
        }

        private void init(Color inEdge1, Color inEdge2, Color inFill,
                int inSize, int inDirection) {
            this.edge1 = inEdge1;
            this.edge2 = inEdge2;
            this.fill = inFill;
            this.size = inSize;
            this.direction = inDirection;
        }

        private void drawDownArrow(Graphics inGraphics, int inXo, int inYo) {
            inGraphics.setColor(edge1);
            inGraphics.drawLine(inXo, inYo, inXo + size - 1, inYo);
            inGraphics.drawLine(inXo, inYo + 1, inXo + size - 3, inYo + 1);
            inGraphics.setColor(edge2);
            inGraphics.drawLine(inXo + size - 2, inYo + 1, inXo + size - 1, inYo + 1);
            int x = inXo + 1;
            int y = inYo + 2;
            int dx = size - 6;
            while (y + 1 < inYo + size) {
                inGraphics.setColor(edge1);
                inGraphics.drawLine(x, y, x + 1, y);
                inGraphics.drawLine(x, y + 1, x + 1, y + 1);
                if (0 < dx) {
                    inGraphics.setColor(fill);
                    inGraphics.drawLine(x + 2, y, x + 1 + dx, y);
                    inGraphics.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
                }
                inGraphics.setColor(edge2);
                inGraphics.drawLine(x + dx + 2, y, x + dx + 3, y);
                inGraphics.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
                x += 1;
                y += 2;
                dx -= 2;
            }
            inGraphics.setColor(edge1);
            inGraphics.drawLine(inXo + (size / 2), inYo + size - 1, inXo + (size / 2), inYo + size - 1);
        }

        private void drawUpArrow(Graphics inGraphics, int inXo, int inYo) {
            inGraphics.setColor(edge1);
            int x = inXo + (size / 2);
            inGraphics.drawLine(x, inYo, x, inYo);
            x--;
            int y = inYo + 1;
            int dx = 0;
            while (y + 3 < inYo + size) {
                inGraphics.setColor(edge1);
                inGraphics.drawLine(x, y, x + 1, y);
                inGraphics.drawLine(x, y + 1, x + 1, y + 1);
                if (0 < dx) {
                    inGraphics.setColor(fill);
                    inGraphics.drawLine(x + 2, y, x + 1 + dx, y);
                    inGraphics.drawLine(x + 2, y + 1, x + 1 + dx, y + 1);
                }
                inGraphics.setColor(edge2);
                inGraphics.drawLine(x + dx + 2, y, x + dx + 3, y);
                inGraphics.drawLine(x + dx + 2, y + 1, x + dx + 3, y + 1);
                x -= 1;
                y += 2;
                dx += 2;
            }
            inGraphics.setColor(edge1);
            inGraphics.drawLine(inXo, inYo + size - 3, inXo + 1, inYo + size - 3);
            inGraphics.setColor(edge2);
            inGraphics.drawLine(inXo + 2, inYo + size - 2, inXo + size - 1, inYo + size - 2);
            inGraphics.drawLine(inXo, inYo + size - 1, inXo + size, inYo + size - 1);
        }
    }

    /**
     * Listener of clicks on the column headers. Will sort the table when a
     * column is clicked.
     *
     * @author Patrik Nordwall
     * @version $Revision: 1.4 $
     * @created 29 October 2002
     */
    private class HeaderListener extends MouseAdapter {

        /**
         * Constructor for the HeaderListener object
         */
        public HeaderListener() { }

        /**
         * Updates the look of the pressed column header.
         *
         * @param inEvent The mouse event
         */
        public void mousePressed(MouseEvent inEvent) {
            if (getTable().isEditing()) {
                getTable().getCellEditor().stopCellEditing();
                if (getTable().isEditing()) {
                    return;
                    //couldn't stop editing
                }
            }
            int col = getTable().getTableHeader().columnAtPoint(inEvent.getPoint());
            headerRenderer.setPressedColumn(col);
            getTable().getTableHeader().repaint();
        }

        /**
         * Updates the look of the pressed column header.
         *
         * @param inEvent The mouse event
         */
        public void mouseReleased(MouseEvent inEvent) {
            if (getTable().isEditing()) {
                return;
                //couldn't stop editing
            }
            headerRenderer.setPressedColumn(-1);
            // clear
            getTable().getTableHeader().repaint();
        }

        /**
         * Sortes the table by the clicked column.
         *
         * @param inEvent The mouse event
         */
        public void mouseClicked(MouseEvent inEvent) {
            if (getTable().isEditing()) {
                return;
                //couldn't stop editing
            }
            int viewCol = getTable().getTableHeader().columnAtPoint(inEvent.getPoint());
            int column = getTable().convertColumnIndexToModel(viewCol);
            if (column != -1) {
                sortByColumn(column);
            }
        }

        private JTable getTable() {
            return SSortTable.this;
        }
    }
    //End of inner class BevelArrowIcon

}
