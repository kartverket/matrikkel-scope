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
package samples.swing.readonly;

import java.awt.BorderLayout;
import javax.swing.JLabel;

import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SButton;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STextField;

/**
 * <P>
 *
 * First View allows users to enter their name, and to change the "name"
 * property's writable state to be flipped to read-only and back again. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/10/23 12:41:06 $
 * @created 05 September 2002
 */
public class ReadOnlyView1 extends SPanel {

    /**
     * Constructor for the ReadOnlyView1 object
     */
    public ReadOnlyView1() {

        setLayout(new BorderLayout(12, 12));

        add(new SButton(ReadOnlyController.CHANGE_PROPERTY_STATE), BorderLayout.NORTH);

        JLabel l = new JLabel("What's your name?");
        add(l, BorderLayout.WEST);

        STextField t = new STextField();
        t.setColumns(20);
        t.setSelector("name");
        add(t, BorderLayout.EAST);

        add(new SButton(ReadOnlyController.GOT_NAME), BorderLayout.SOUTH);
    }


    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "ReadOnly - Enter Name";
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(ReadOnlyController.EXIT_CONTROL_ID);
    }
}

