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

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SList;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STextField;

/**
 * <p>
 *
 * The Model, rather than view model part of the timesheet application. This
 * contains the timesheet view, the list of projects and of course the persons
 * name.</p>
 *
 * @author <a href="mailto:steve.jones@netdecisions.co.uk>Steve Jones</a>
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:50 $
 * @created 05 September 2002
 * @since Scope v0.8
 */
public class PersonView extends SPanel {

    /**
     * Constructor for the PersonView object
     */
    public PersonView() {
        // Add the list to the left
        SList list = new SList();
        list.setSelector(Selector.fromString("projects"));
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout());
        list.setToolTipText("Select project to start work");
        listPanel.add(new JLabel("Project List"), BorderLayout.NORTH);
        listPanel.add(list, BorderLayout.CENTER);

        setLayout(new BorderLayout());

        // Add the Timesheet bit in the centre
        TimesheetView timesheetView = new TimesheetView();
        timesheetView.setSelector(Selector.fromString("timesheet"));
        JSplitPane s = new JSplitPane();
        s.setLeftComponent(new JScrollPane(listPanel));
        s.setRightComponent(new JScrollPane(timesheetView));
        add(s, BorderLayout.CENTER);

        // Add the text field to the top
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BorderLayout());
        STextField textField = new STextField();
        textField.setSelector(Selector.fromString("name"));
        textField.setName("Name");
        textField.setToolTipText("The name of the person for the timesheet");
        subPanel.add(textField, BorderLayout.CENTER);
        JLabel label = new JLabel();
        label.setText("Name : ");
        subPanel.add(label, BorderLayout.WEST);
        add(subPanel, BorderLayout.NORTH);

        // Finally set the selected item of the list to be the current project of the timesheet
        list.setSelectionSelector(Selector.fromString("timesheet.currentProject"));
    }

}
