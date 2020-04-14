/*
 * Scope: a generic MVC framework.
 * Copyright (c) 2000-2002, Steve Meyfroidt
 * All rights reserved.
 * Email: smeyfroi@users.sourceforge.net
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
 * $Id: SubController.java,v 1.4 2002/06/17 09:56:00 ludovicc Exp $
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
 * Recognises Control("Test2Control"). Initialises
 * itself with a ServletViewTest("2") and a 
 * BasicTestModel("2TestModel2"). View is bound
 * to the NAME Selector.
 *
 * Adds a child SubController().
 * </P>
 * 
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.4 $ $Date: 2002/06/17 09:56:00 $
 */
class SubController extends BasicController {
    private static final Log LOG = LogFactory.getLog(SubController.class);
    static boolean doneTest2Control = false;
    protected void doHandleControl(Control inControl) throws ControlException {
        if (LOG.isDebugEnabled()) LOG.debug("doHandleControl: " + inControl);
        if (inControl.matchesID("Test2Control")) {
            doneTest2Control = true;
            showView();
        }
    }
    public SubController() {
        if (LOG.isDebugEnabled()) LOG.debug("SubController.<init>");
        DummyPage v = new DummyPage("2");
        v.setSelector(BasicTestModel.NAME);
        ServletView sv = new ServletView();
        sv.addPage(v);
        setView(sv);

        BasicTestModel m = new BasicTestModel("2TestModel2");
        setModel(m);
    }
    public String toString() {
        return "SubController";
    }
}

