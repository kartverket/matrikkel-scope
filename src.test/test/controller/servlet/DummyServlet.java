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
 * $Id: DummyServlet.java,v 1.6 2002/09/05 15:41:48 ludovicc Exp $
 */
package test.controller.servlet;


import java.util.List;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.controller.servlet.xml.XSLScopeServlet;
import org.scopemvc.core.Controller;
import org.scopemvc.util.Debug;
import org.scopemvc.view.servlet.Page;

/**
 * <P>
 *
 * Servlet that creates a new application as a new AppController(), and that
 * exposes the doPost method. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/05 15:41:48 $
 */
class DummyServlet extends XSLScopeServlet {
    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     * @throws Exception TODO: Describe the Exception
     */
    protected Controller createApplicationController() throws Exception {
        return new AppController();
    }

    /**
     * TODO: document the method
     *
     * @param inPage TODO: Describe the Parameter
     * @param inFailures TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected boolean handleValidationFailures(Page inPage, List inFailures) {
        if (Debug.ON) {
            Debug.assertTrue(inFailures != null);
        }

        ViewContext.getViewContext().showError("Error", "Error");
        return true;
    }

    /**
     * TODO: document the method
     *
     * @param inRequest TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     * @throws Exception TODO: Describe the Exception
     */
    DummyHTTPResponse handlePost(DummyHTTPRequest inRequest) throws Exception {
        DummyHTTPResponse response = new DummyHTTPResponse();
        doPost(inRequest, response);
        return response;
    }

}

