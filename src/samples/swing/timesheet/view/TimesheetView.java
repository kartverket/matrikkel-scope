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
package samples.swing.timesheet.view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SLabel;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STable;
import org.scopemvc.view.swing.STableModel;

/**
 * <p>
 *
 * The view for the timesheet itself. This is made of a table and the
 * information fields.</p>
 *
 * @author <a href="mailto:steve.jones@netdecisions.co.uk>Steve Jones</a>
 * @version $Revision: 1.8 $ $Date: 2002/09/25 18:07:45 $
 * @created 05 September 2002
 * @since Scope v0.8
 */
public class TimesheetView extends SPanel {

    /**
     * Constructor for the view, the view uses a Gridbag layout with the table
     * at its centre, the current project field at the top and the total work
     * field at the bottom.
     */
    public TimesheetView() {

        // set the layout
        setLayout(new GridBagLayout());

        // Place the table in the centre
        STable table = new STable();
        JScrollPane scroll = new JScrollPane(table);
        add(scroll, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(1, 1, 1, 1), 1, 1));
        table.setSelector("workItems");

        // Create the selector which determines which elements from the
        // list should be used.
        String[] columns = {"project", "start", "duration"};
        table.setColumnSelectors(columns);
        String[] names = {"", "", ""};
        table.setColumnNames(names);
        boolean[] editables = {false, false, false};
        ((STableModel) table.getModel()).setEditableColumns(editables);

        scroll.setPreferredSize(new Dimension(150, 200));

        // Now add the total work field at the bottom

        JLabel totalWorkLabel = new JLabel();
        totalWorkLabel.setText("Total Work:");
        SLabel totalWorkValue = new SLabel();
        totalWorkValue.setSelector(Selector.fromString("totalWork"));

        add(totalWorkLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
        add(totalWorkValue, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

        // Now add the current project fields to the top
        JLabel currentProjectLabel = new JLabel();
        currentProjectLabel.setText("Current Project:");
        SLabel currentProjectValue = new SLabel();
        currentProjectValue.setSelector(Selector.fromString("currentProject"));
        JLabel projectTotalLabel = new JLabel();
        projectTotalLabel.setText("Project Total:");
        SLabel projectTotalValue = new SLabel();
        projectTotalValue.setSelector(Selector.fromString("projectTotal"));

        add(currentProjectLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
        add(currentProjectValue, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

        add(projectTotalLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
        add(projectTotalValue, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
    }
}
