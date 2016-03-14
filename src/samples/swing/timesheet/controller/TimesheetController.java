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
package samples.swing.timesheet.controller;

import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;
import samples.swing.timesheet.model.PersonViewModel;
import samples.swing.timesheet.view.MainView;

/**
 * <p>
 *
 * The main controller for the timesheet app</p>
 *
 * @author <a href="mailto:steve.jones@netdecisions.co.uk>Steve Jones</a>
 * @version $Revision: 1.5 $ $Date: 2002/09/05 15:41:51 $
 * @created 05 September 2002
 * @since Scope v0.8
 */
public class TimesheetController extends BasicController {

    /**
     * TODO: describe of the Field
     */
    public static final String CREATE_PROJECT = "CreateProject";


    /**
     * Constructor for the TimesheetController object
     */
    public TimesheetController() {
        setModel(new PersonViewModel());
        setView(new MainView());
    }


    /**
     * TODO: document the method
     *
     * @param inControl TODO: Describe the Parameter
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        if (inControl.matchesID(CREATE_PROJECT)) {
            doCreateProject();
        }
    }


    /**
     * Create the new project in the project model
     *
     * @throws ControlException TODO: Describe the Exception
     */
    private void doCreateProject() throws ControlException {
        PersonViewModel viewModel = (PersonViewModel) this.getModel();
        viewModel.getPerson().addProject(viewModel.getNewProject());
    }
}
