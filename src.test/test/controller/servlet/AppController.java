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
 * $Id: AppController.java,v 1.5 2002/09/19 18:09:34 ludovicc Exp $
 */
package test.controller.servlet;

import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;
import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;
import org.scopemvc.view.servlet.ServletView;
import test.model.basic.BasicTestModel;

/**
 * <P>
 *
 * Recognises Control("Test1Control"). Initialises itself with a
 * ServletViewTest("1") and a BasicTestModel("TestModel"). View is bound to the
 * NAME Selector. Adds a child SubController(). </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 18 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/09/19 18:09:34 $
 */
class AppController extends BasicController {
    static boolean doneTest1Control = false;
    static boolean doneTest1aControl = false;
    private static final Log LOG = LogFactory.getLog(AppController.class);

    /**
     * Constructor for the AppController object
     */
    public AppController() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("AppController.<init>");
        }

        ServletView sv = new ServletView();
        setView(sv);

        DummyPage v = new DummyPage("1");
        v.setSelector(BasicTestModel.NAME);
        sv.addPage(v);

        v = new DummyPage("1a");
        v.setSelector(BasicTestModel.LONG_PROPERTY);
        sv.addPage(v);

        BasicTestModel m = new BasicTestModel("TestModel");
        setModel(m);

        SubController child = new SubController();
        addChild(child);
    }

    /**
     * TODO: document the method
     */
    public void startup() {
        ((ServletView) getView()).setVisible("1a");
        showView();
    }

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public String toString() {
        return "AppController";
    }

    /**
     * TODO: document the method
     *
     * @param inControl TODO: Describe the Parameter
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("doHandleControl: " + inControl);
        }
        if (inControl.matchesID("Test1Control")) {
            doneTest1Control = true;
            showView();
        } else if (inControl.matchesID("Test1aControl")) {
            doneTest1aControl = true;
            ((ServletView) getView()).setVisible("1a");
            // no showView
        }
    }
}

