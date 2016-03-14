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
package samples.swing.list;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.scopemvc.core.Control;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SLabel;
import org.scopemvc.view.swing.SList;
import org.scopemvc.view.swing.SPanel;

/**
 * <P>
 *
 * List of customers on left and linked customer viewer on right. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/10/23 12:41:05 $
 * @created 05 September 2002
 */
public class ListView extends SPanel {

    private SPanel customerView;


    /**
     * Constructor for the ListView object
     */
    public ListView() {

        setLayout(new BorderLayout(0, 12));

        SList l = new SList();
        l.setSelector("customers");
        l.setRendererSelector(Selector.fromString("name"));
        l.setSelectionSelector(Selector.fromString("selection"));
        l.setChangeSelectionControlID(ListController.SELECTED);
        JScrollPane left = new JScrollPane(l);

        customerView = new SPanel();
        customerView.setSelector("selection");
        customerView.setLayout(new GridLayout(3, 2, 12, 12));

        customerView.add(new JLabel("Name: "));
        SLabel b = new SLabel();
        b.setSelector("name");
        customerView.add(b);

        customerView.add(new JLabel("Address: "));
        b = new SLabel();
        b.setSelector("address");
        customerView.add(b);

        customerView.add(new JLabel("Age: "));
        b = new SLabel();
        b.setSelector("age");
        customerView.add(b);

        JPanel right = new JPanel();
        right.add(customerView);

        JSplitPane s = new JSplitPane();
        s.setLeftComponent(left);
        s.setRightComponent(right);
        add(s);
    }


    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "Swing List";
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(ListController.EXIT_CONTROL_ID);
    }


    /**
     * TODO: document the method
     */
    public void refreshCustomerView() {
        customerView.refresh();
    }
}

