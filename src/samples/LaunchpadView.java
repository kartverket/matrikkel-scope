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
package samples;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SButton;
import org.scopemvc.view.swing.SList;
import org.scopemvc.view.swing.SListCellRenderer;
import org.scopemvc.view.swing.SPanel;

/**
 * <P>
 *
 * List of available applications on left and a HTML viewer on the right. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/10/23 12:41:05 $
 * @created 25 September 2002
 */
public class LaunchpadView extends SPanel {

    private static final Log LOG = LogFactory.getLog(LaunchpadView.class);

    private JEditorPane editorPane;


    /**
     * Constructor for the LaunchpadView object
     */
    public LaunchpadView() {

        setLayout(new BorderLayout(0, 12));

        SList l = new SList();
        l.setSelector("examples");
        ((SListCellRenderer) l.getCellRenderer()).setTextSelector("name");
        l.setSelectionSelector("selectedExample");
        l.setChangeSelectionControlID(LaunchpadController.SELECT_CONTROL_ID);
        l.setDoubleClickControlID(LaunchpadController.LAUNCH_CONTROL_ID);
        JScrollPane left = new JScrollPane(l);

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        JScrollPane right = new JScrollPane(editorPane);

        JSplitPane s = new JSplitPane();
        s.setLeftComponent(left);
        s.setRightComponent(right);
        add(s);

        add(new SButton(LaunchpadController.LAUNCH_CONTROL_ID), BorderLayout.SOUTH);

        setViewBounds(new Rectangle(0, 0, 640, 320));
    }


    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "Examples Launcher";
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(LaunchpadController.QUIT_CONTROL_ID);
        // a special Control to differentiate from regular EXIT coming from the example controllers
    }


    /**
     * Show the document at the given URL in the editor pane
     *
     * @param inURL The url of the document
     */
    public void showURL(URL inURL) {
        try {
            editorPane.setPage(inURL);
        } catch (Exception e) {
            LOG.fatal("Can't show: " + inURL, e);
        }
    }
}

