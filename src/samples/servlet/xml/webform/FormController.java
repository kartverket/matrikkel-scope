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
package samples.servlet.xml.webform;


import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;
import org.scopemvc.view.servlet.ServletView;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:48 $
 * @created 05 September 2002
 */
public class FormController extends BasicController {

    /**
     * A Control ID.
     */
    public static final String GOT_NAME = "Enter";


    /**
     * Constructor for the FormController object
     */
    public FormController() {
        setModel(new FormModel());

        ServletView viewContainer = new ServletView();
        viewContainer.addPage(new FormView1());
        viewContainer.addPage(new FormView2());
        setView(viewContainer);
    }


    /**
     * If user interacts with a view parented by this Controller but there's no
     * Control in the request then the Controller is started with this method:
     * this is the default behaviour in the absence of a Control.
     */
    public void startup() {
        ((ServletView) getView()).setVisible(FormView1.ID);
        showView();
    }


    /**
     * Respond to GOT_NAME.
     *
     * @param inControl TODO: Describe the Parameter
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        if (inControl.matchesID(GOT_NAME)) {
            doGotName();
        }
    }


    /**
     * On GOT_NAME, switch over to the second View (which will bind to this
     * Controller's model) and show it.
     */
    protected void doGotName() {
        ((ServletView) getView()).setVisible(FormView2.ID);
        showView();
    }
}
