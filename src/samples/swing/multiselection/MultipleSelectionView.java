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
package samples.swing.multiselection;


import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SLabel;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STable;

/**
 * <P>
 *
 * List of customers in a table with a count of selected customers underneath.
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:19:58 $
 * @created 05 September 2002
 */
public class MultipleSelectionView extends SPanel {

    private JLabel selectionCountLabel = new JLabel();

    /**
     * Constructor for the MultipleSelectionView object
     */
    public MultipleSelectionView() {

        setLayout(new BorderLayout(0, 12));

        STable t = new STable();
        t.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        t.setChangeSelectionControlID(MultipleSelectionController.SELECTION_CHANGED);
        t.setSelector("customers");
        String[] columns = {"name", "address", "age"};
        t.setColumnSelectors(columns);
        t.setSelectionSelector("selection");
        JScrollPane sp = new JScrollPane(t);
        add(sp, BorderLayout.CENTER);

        selectionCountLabel.setText("");
        add(selectionCountLabel, BorderLayout.NORTH);

        SLabel l = new SLabel();
        l.setSelector("selectionCount");
        add(l, BorderLayout.SOUTH);
    }

    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "MultiSelection";
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(MultipleSelectionController.EXIT_CONTROL_ID);
    }


    /**
     * Sets the selected many customers
     *
     * @param selected The new selectedManyCustomers value
     */
    public void setSelectedManyCustomers(boolean selected) {
        if (selected) {
            selectionCountLabel.setText("");
        } else {
            selectionCountLabel.setText("Please select at least two customers");
        }
    }
}
