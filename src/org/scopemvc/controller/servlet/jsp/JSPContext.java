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
 * $Id: JSPContext.java,v 1.8 2002/09/05 15:41:48 ludovicc Exp $
 */
package org.scopemvc.controller.servlet.jsp;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.controller.servlet.ScopeServlet;
import org.scopemvc.controller.servlet.ServletContext;
import org.scopemvc.core.View;
import org.scopemvc.view.servlet.Page;
import org.scopemvc.view.servlet.ServletView;
import org.scopemvc.view.servlet.jsp.JSPPage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * <P>
 *
 * A {@link org.scopemvc.controller.servlet.ServletContext ServletContext} that
 * handles showView() for JSPView implementations by redirecting to a JSP. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.8 $ $Date: 2002/09/05 15:41:48 $
 * @see org.scopemvc.controller.servlet.ServletContext
 */
public class JSPContext extends ServletContext {

    /**
     * Key that the JSP's bound model is stored under in the HTTPRequest for
     * access by the JSP.
     */
    public final static String BOUND_MODEL = "org.scopemvc.model";

    private final static Log LOG = LogFactory.getLog(JSPContext.class);


    /**
     * Create with an HttpServletResponse to use on a showView during
     * initialisation, and a HttpServletRequest accessible to application code.
     *
     * @param inServlet TODO: Describe the Parameter
     * @param inRequest TODO: Describe the Parameter
     * @param inResponse TODO: Describe the Parameter
     * @param inFormParameters TODO: Describe the Parameter
     */
    public JSPContext(ScopeServlet inServlet,
            HttpServletRequest inRequest,
            HttpServletResponse inResponse,
            HashMap inFormParameters) {
        super(inServlet, inRequest, inResponse, inFormParameters);
    }


    /**
     * Show the visible JSPView from the passed ServletView, after putting its
     * bound model into the request under the {@link #BOUND_MODEL} key.
     *
     * @param inView TODO: Describe the Parameter
     */
    public void showView(View inView) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("showView: " + inView);
        }
        if (!(inView instanceof ServletView)) {
            throw new IllegalArgumentException("JSPContext can only show Views that are instanceof ServletView, not: " + inView);
        }
        if (response == null) {
            throw new UnsupportedOperationException("Can't show the view because don't have a HTTPServletResponse.\nHas a view already been shown during this servlet request?");
        }

        try {
            Page visiblePage = ((ServletView) inView).getVisible();
            if (!(visiblePage instanceof JSPPage)) {
                throw new IllegalArgumentException("JSPContext can only show Pages that are instanceof JSPPage, not: " + visiblePage);
            }

            // Put the View's bound model into the request for the JSP to access
            request.setAttribute(BOUND_MODEL, visiblePage.getBoundModel());

            if (LOG.isDebugEnabled()) {
                LOG.debug("showView: " + visiblePage);
            }
            request.getRequestDispatcher(((JSPPage) visiblePage).getPath()).forward(request, response);

        } catch (Exception e) {
            // Could be client broke the connection by pressing "Stop" or something...
            if (LOG.isDebugEnabled()) {
                LOG.debug("doShowView", e);
            }
            try {
                handleInternalError(e);
            } catch (IOException e1) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("doShowView", e1);
                }
            }
        } finally {
            response = null;
        }
    }
}
