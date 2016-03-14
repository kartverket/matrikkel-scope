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

import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;
import org.scopemvc.util.Debug;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/10/23 12:41:05 $
 * @created 22 October 2002
 */
public class LaunchpadController extends BasicController {

    /**
     * A Control ID.
     */
    public static final String LAUNCH_CONTROL_ID = "launch";

    /**
     * A Control ID.
     */
    public static final String SELECT_CONTROL_ID = "select";

    /**
     * A Control ID.
     */
    public static final String QUIT_CONTROL_ID = "quit";

    private static final Log LOG = LogFactory.getLog(LaunchpadController.class);


    /**
     * Constructor for the LaunchpadController object
     */
    public LaunchpadController() {
        setModel(new LaunchpadModel());
        setView(new LaunchpadView());
    }


    /**
     * TODO: document the method
     */
    public void startup() {
        showView();
    }


    /**
     * Respond to LAUNCH and SELECT.
     *
     * @param inControl TODO: Describe the Parameter
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        if (inControl.matchesID(LAUNCH_CONTROL_ID)) {
            doLaunch();
        } else if (inControl.matchesID(SELECT_CONTROL_ID)) {
            doSelect();
        } else if (inControl.matchesID(EXIT_CONTROL_ID)) {
            // handle these explicitly by calling shutdown on the controller that wants to shutdown.
            if (Debug.ON) {
                Debug.assertTrue(inControl.getParameter() instanceof BasicController);
            }
            ((BasicController) inControl.getParameter()).shutdown();
        } else if (inControl.matchesID(QUIT_CONTROL_ID)) {
            ViewContext.getViewContext().exit();
        }
    }


    /**
     * On LAUNCH, fire off a new instance of the selected example's Controller
     * as a child of this.
     *
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doLaunch() throws ControlException {
        ExampleModel selected = ((LaunchpadModel) getModel()).getSelectedExample();
        if (selected == null) {
            return;
        }
        String controller = selected.getControllerName();
        try {
            BasicController c = (BasicController) Class.forName(controller).newInstance();
            addChild(c);
            c.startup();
        } catch (Exception e) {
            LOG.fatal("Can't launch: " + controller, e);
            throw new ControlException("CANT_LAUNCH", controller);
        }
    }


    /**
     * On SELECT, get the view to load the appropriate HTML into the viewer
     * pane. (Note that the selection in the model is automatically updated with
     * the newly selected object).
     */
    protected void doSelect() {
        URL url = ((LaunchpadModel) getModel()).getSelectedExample().getDocumentUrl();
        ((LaunchpadView) getView()).showURL(url);
    }
}

