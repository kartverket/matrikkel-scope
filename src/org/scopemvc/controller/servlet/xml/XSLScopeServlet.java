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
 * $Id: XSLScopeServlet.java,v 1.7 2002/09/05 15:41:51 ludovicc Exp $
 */
package org.scopemvc.controller.servlet.xml;


import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.view.servlet.xml.AbstractXSLPage;
import org.scopemvc.controller.servlet.ScopeServlet;
import org.scopemvc.controller.servlet.ServletContext;

/**
 * <P>
 *
 * A {@link org.scopemvc.controller.servlet.ScopeServlet} for use in webapps
 * that use {@link org.scopemvc.view.servlet.xml}: this initialises the SystemID
 * of {@link org.scopemvc.view.servlet.xml.AbstractXSLPage} to point to the root
 * of the deployed web archive, which is where all XSL files are referenced
 * from. Also installs a {@link XSLServletContext}. </P>
 *
 * @author Beat Glattfelder
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.7 $ $Date: 2002/09/05 15:41:51 $
 */
public abstract class XSLScopeServlet extends ScopeServlet {

    private final static Log LOG = LogFactory.getLog(XSLScopeServlet.class);


    /**
     * Initialise the XSL SystemID relative to the web archive.
     *
     * @param config TODO: Describe the Parameter
     * @throws ServletException TODO: Describe the Exception
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            String xslSystemId = config.getServletContext().getResource("/").toString();
            LOG.info("SystemId: " + xslSystemId);
            AbstractXSLPage.setSystemID(xslSystemId);
        } catch (Exception e) {
            throw new ServletException("Error initializing SystemID ", e);
        }
    }


    /**
     * TODO: document the method
     *
     * @param req TODO: Describe the Parameter
     * @param resp TODO: Describe the Parameter
     * @param inFormParameters TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected ServletContext createServletContext(HttpServletRequest req, HttpServletResponse resp, HashMap inFormParameters) {
        return new XSLServletContext(this, req, resp, inFormParameters);
    }
}
