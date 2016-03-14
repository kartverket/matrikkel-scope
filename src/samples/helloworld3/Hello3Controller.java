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


import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:48 $
 * @created 05 September 2002
 */
public class Hello3Controller extends BasicController {

    /**
     * A Control ID.
     */
    public static final String GOT_NAME = "Enter";

    /**
     * A Control ID.
     */
    public static final String CHANGE_TO_FRED = "Change to Fred";

    /**
     * Use this child controller to validate the user at startup.
     */
    private LoginController loginController;


    /**
     * Constructor for the Hello3Controller object
     */
    public Hello3Controller() {
        setModel(new Hello3Model());
        setView(new Hello3View1());
    }


    /**
     * On startup, kick off a login controller to validate the user before doing
     * anything else. We wait for the login to succeed by responding to the
     * LoginController.LOGIN_OK Control later.
     */
    public void startup() {
        loginController = new LoginController();
        addChild(loginController);
        loginController.startup();
    }


    /**
     * Respond to GOT_NAME and EXIT.
     *
     * @param inControl TODO: Describe the Parameter
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        if (inControl.matchesID(GOT_NAME)) {
            doGotName();
        } else if (inControl.matchesID(CHANGE_TO_FRED)) {
            doChangeToFred();
        } else if (inControl.matchesID(LoginController.LOGIN_OK)) {
            doLoginOK();
        }
    }


    /**
     * On GOT_NAME, switch over to the second View (which will bind to this
     * Controller's model) and show it.
     */
    protected void doGotName() {
        setView(new Hello3View2());
        // This new View will bind to the Controller's model
        showView();
    }


    /**
     * On "CHANGE_TO_FRED", set the model's name to "Fred" and refresh the view.
     * Note that this refresh can be automated by making model objects fire
     * ModelChangeEvents -- see BasicModel.
     */
    protected void doChangeToFred() {
        ((Hello3Model) getModel()).changeNameToFred();
        ((Hello3View1) getView()).refresh();
    }


    /**
     * On LoginController.LOGIN_OK, shutdown and abandon the login controller,
     * then show our view.
     */
    protected void doLoginOK() {
        loginController.shutdown();
        loginController = null;
        showView();
    }
}

