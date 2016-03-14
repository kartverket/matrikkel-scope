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
package samples.servlet.xml.validation;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.controller.servlet.ScopeServlet;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;
import org.scopemvc.util.Debug;
import org.scopemvc.view.servlet.ServletView;
import org.scopemvc.view.servlet.ValidationFailure;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/05 15:41:51 $
 * @created 05 September 2002
 */
public class ValidationController extends BasicController {

    /**
     * A Control ID.
     */
    public static final String SUBMIT = "submit";


    /**
     * Constructor for the ValidationController object
     */
    public ValidationController() {
        setModel(new ValidationModel());

        ServletView viewContainer = new ServletView();
        viewContainer.addPage(new ValidationView1());
        viewContainer.addPage(new ValidationView2());
        setView(viewContainer);
    }


    /**
     * If user interacts with a view parented by this Controller but there's no
     * Control in the request then the Controller is started with this method:
     * this is the default behaviour in the absence of a Control.
     */
    public void startup() {
        ((ServletView) getView()).setVisible(ValidationView1.ID);
        showView();
    }


    /**
     * Respond to SUBMIT.
     *
     * @param inControl TODO: Describe the Parameter
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        if (inControl.matchesID(SUBMIT)) {
            doSubmit();
        }
    }


    /**
     * On SUBMIT, switch over to the second View (which will bind to this
     * Controller's model) and show it. If errors occurred during population of
     * model with data from the view then add a list of the failures to the
     * model and reshow the first page.
     */
    protected void doSubmit() {
        List failures = (List) ViewContext.getViewContext().
                getProperty(ScopeServlet.VALIDATION_FAILURES);
        if (failures != null) {
            List failureList = new LinkedList();
            for (Iterator i = failures.iterator(); i.hasNext(); ) {
                Object o = i.next();
                if (Debug.ON) {
                    Debug.assertTrue(o instanceof ValidationFailure);
                }
                ValidationFailure failure = (ValidationFailure) o;

                StringBuffer message = new StringBuffer();
                message.append("Failed to set '");
                message.append(failure.getProperty());
                message.append("' to '");
                message.append(failure.getValue());
                message.append("' because ");
                message.append(failure.getException().getLocalizedMessage());
                failureList.add(message.toString());
            }
            ((ValidationModel) getModel()).setValidationFailures(failureList);
            ((ServletView) getView()).setVisible(ValidationView1.ID);
            showView();
        } else {
            ((ValidationModel) getModel()).setValidationFailures(null);
            ((ServletView) getView()).setVisible(ValidationView2.ID);
            showView();
        }
    }
}
