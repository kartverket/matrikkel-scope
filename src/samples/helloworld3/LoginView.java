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
package samples.helloworld3;

import java.awt.GridLayout;
import javax.swing.JLabel;

import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SButton;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.SPasswordField;
import org.scopemvc.view.swing.STextField;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/10/23 12:41:05 $
 * @created 05 September 2002
 */
public class LoginView extends SPanel {

    /**
     * Constructor for the LoginView object
     */
    public LoginView() {

        setLayout(new GridLayout(3, 2, 12, 12));

        JLabel l = new JLabel("Username:");
        add(l);

        STextField t = new STextField();
        t.setColumns(20);
        t.setSelector("username");
        add(t);

        l = new JLabel("Password:");
        add(l);

        SPasswordField p = new SPasswordField();
        p.setColumns(20);
        p.setSelector("password");
        p.setControlID(LoginController.VALIDATE_LOGIN);
        add(p);

        add(new SButton(LoginController.VALIDATE_LOGIN));
        add(new SButton(LoginController.EXIT_CONTROL_ID));
    }


    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "HelloWorld3 - Login";
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(LoginController.EXIT_CONTROL_ID);
    }


    /**
     * Gets the display model
     *
     * @return The displayModel value
     */
    public int getDisplayModel() {
        return MODAL_DIALOG;
    }
}

