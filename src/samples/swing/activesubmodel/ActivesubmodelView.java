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
package samples.swing.activesubmodel;


import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SButton;
import org.scopemvc.view.swing.SList;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STextField;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:59 $
 * @created 05 September 2002
 */
public class ActivesubmodelView extends SPanel {

    /**
     * Constructor for the ActivesubmodelView object
     */
    public ActivesubmodelView() {

        setLayout(new BorderLayout(12, 12));

        SPanel namePanel = new SPanel();
        namePanel.add(new JLabel("Name: "));
        STextField personNameField = new STextField();
        personNameField.setSelector("person.name");
        namePanel.add(personNameField);

        add(namePanel, BorderLayout.NORTH);

        SPanel petPanel = new SPanel();
        petPanel.setLayout(new BorderLayout(12, 12));
        petPanel.setBorder(new TitledBorder("Pet"));
        add(petPanel, BorderLayout.CENTER);

        SPanel petNamePanel = new SPanel();
        petNamePanel.add(new JLabel("Name: "));
        STextField petNameField = new STextField();
        petNameField.setSelector("person.pet.name");
        petNamePanel.add(petNameField);

        SList toysList = new SList();
        toysList.setSelector("person.pet.toys");
        toysList.setSelectionSelector("selectedToy");
        petPanel.add(new JScrollPane(toysList), BorderLayout.CENTER);

        SPanel toyPanel = new SPanel();
        STextField newToyField = new STextField();
        newToyField.setSelector("newToy");
        toyPanel.add(newToyField);

        SButton addToyButton = new SButton();
        addToyButton.setControlID(ActivesubmodelController.ADD_TOY);
        toyPanel.add(addToyButton);

        SButton removeToyButton = new SButton();
        removeToyButton.setControlID(ActivesubmodelController.REMOVE_TOY);
        toyPanel.add(removeToyButton);

        SButton clearToysButton = new SButton();
        clearToysButton.setControlID(ActivesubmodelController.CLEAR_TOYS);
        toyPanel.add(clearToysButton);

        add(toyPanel, BorderLayout.SOUTH);
    }


    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "Activesubmodel - Edit Person";
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(ActivesubmodelController.EXIT_CONTROL_ID);
    }
}
