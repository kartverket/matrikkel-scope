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
 * $Id: ServletContext.java,v 1.11 2002/09/05 15:41:51 ludovicc Exp $
 */
package org.scopemvc.controller.servlet;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.core.View;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

/**
 * <P>
 *
 * A {@link org.scopemvc.controller.basic.ViewContext ViewContext} that handles
 * showView(), hideView() and showError() for servlet implementations. </P> <P>
 *
 * The showView() expects a ServletView that is asked to stream the currently
 * visible Page to the HTTP Response's OutputStream. </P> <P>
 *
 * Two interesting behaviours can be customized with a ServletContext subclass:
 *
 * <UL>
 *   <LI> To handle errors with a global error page override {@link #showError}
 *   to implement the required behaviour. </LI>
 * </UL>
 * Custom ServletContexts can be used for requests by overriding {@link
 * ScopeServlet#createServletContext} in the application's ScopeServlet subclass
 * to return an instance of the custom ServletContext. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.11 $ $Date: 2002/09/05 15:41:51 $
 * @see org.scopemvc.controller.servlet.jsp.JSPContext
 * @see org.scopemvc.controller.servlet.xml.XSLServletContext
 */
public abstract class ServletContext extends ViewContext {

    private final static Log LOG = LogFactory.getLog(ServletContext.class);

    /**
     * The HttpServletResponse to use on showView. Set on creation then nulled
     * after showView/showError.
     */
    protected HttpServletResponse response;

    /**
     * TODO: describe of the Field
     */
    protected HttpServletRequest request;

    /**
     * TODO: describe of the Field
     */
    protected HashMap formParameters;

    /**
     * TODO: describe of the Field
     */
    protected ScopeServlet servlet;


    /**
     * Create with an HttpServletResponse to use on a showView during
     * initialisation, and a HttpServletRequest accessible to application code.
     *
     * @param inServlet TODO: Describe the Parameter
     * @param inRequest TODO: Describe the Parameter
     * @param inResponse TODO: Describe the Parameter
     * @param inFormParameters TODO: Describe the Parameter
     */
    public ServletContext(ScopeServlet inServlet,
            HttpServletRequest inRequest,
            HttpServletResponse inResponse,
            HashMap inFormParameters) {
        servlet = inServlet;
        request = inRequest;
        setHttpResponse(inResponse);
        formParameters = inFormParameters;
    }


    /**
     * Allow access to the response object. Should rarely be used.
     *
     * @return The httpResponse value
     */
    public final HttpServletResponse getHttpResponse() {
        return response;
    }


    /**
     * Allow access to the request object. Can be used to get access to cookies,
     * session etc.
     *
     * @return The httpRequest value
     */
    public final HttpServletRequest getHttpRequest() {
        return request;
    }


    /**
     * Return the ScopeServlet that handled this context's request.
     *
     * @return The servlet value
     */
    public final ScopeServlet getServlet() {
        return servlet;
    }


    /**
     * @return the form parameters for the current request.
     */
    public final HashMap getFormParameters() {
        return formParameters;
    }


    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public final boolean hasShownView() {
        return (getHttpResponse() == null);
    }


    /**
     * Show the ServletView passed.
     *
     * @param inView TODO: Describe the Parameter
     */
    public abstract void showView(View inView);


    /**
     * Don't do anything in this impl. <P>
     *
     * ***** A nicer impl of this view manager would maintain a "stack" of shown
     * views and implement "doHideView" to step back through the stack if
     * available. Could be tied to some rudimentary state management in
     * ScopeServlet. </P>
     *
     * @param inView TODO: Describe the Parameter
     */
    public void hideView(View inView) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("hideView: " + inView);
        }
        // noop
    }


    /**
     * This is a very simple default error handler. To implement your own error
     * handler, override this method and also override {@link
     * ScopeServlet#createServletContext} to return an instance of your custom
     * ServletContext that will handle servlet requests.
     *
     * @param inErrorTitle TODO: Describe the Parameter
     * @param inErrorMessage TODO: Describe the Parameter
     */
    public void showError(String inErrorTitle, String inErrorMessage) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("showError: " + inErrorTitle + ", " + inErrorMessage);
        }

        response.setContentType("text/html");
        try {
            PrintStream ps = new PrintStream(response.getOutputStream());
            ps.println("<HTML><H1>Error</H1><H3>"
                    + formatMessageToHTML(inErrorTitle) + "</H3><P>"
                    + formatMessageToHTML(inErrorMessage)
                    + "</P></HTML>");
            ps.flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            LOG.fatal("Failed to showError", e);
        } finally {
            setHttpResponse(null);
        }
    }


    /**
     * Used by default BasicController's handler for the EXIT Control. Doesn't
     * do anything in a servlet context.
     */
    public void exit() {
        // noop for servlets
    }


    /**
     * TODO: document the method
     */
    public void startProgress() {
        // noop for servlets
    }


    /**
     * TODO: document the method
     */
    public void stopProgress() {
        // noop for servlets
    }


    /**
     * The response to use for showView. Set in ctor and then nulled after
     * showing a view.
     *
     * @param inResponse The new httpResponse value
     */
    protected final void setHttpResponse(HttpServletResponse inResponse) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setHttpResponse: " + this);
        }
        response = inResponse;
    }


    /**
     * Got an error while streaming the view into the response OutputStream. The
     * stream could be corrupt by this point, but it hasn't been closed so do
     * the best you can.
     *
     * @param t TODO: Describe the Parameter
     * @throws IOException TODO: Describe the Exception
     */
    protected void handleInternalError(Throwable t) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("handleInternalError: ", t);
        }

        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(stringWriter);
            t.printStackTrace(writer);
            String dump = stringWriter.toString();

            String formattedMessage = formatMessageToHTML(dump);

            PrintStream ps = new PrintStream(response.getOutputStream());
            ps.println("<HTML><H1>Internal error:</H1>" + formattedMessage + "</HTML>");
            ps.flush();
            response.getOutputStream().close();
        } finally {
            setHttpResponse(null);
        }
    }


    /**
     * Replace all low ASCII chars (&lt;32) in the message with '{@code <BR />}'.
     *
     * @param inMessage TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected String formatMessageToHTML(String inMessage) {
        StringBuffer formattedMessage = new StringBuffer();
        boolean addBreak = false;
        for (int i = 0; i < inMessage.length(); ++i) {
            char c = inMessage.charAt(i);
            if (c < 32) {
                addBreak = true;
            } else {
                if (addBreak) {
                    formattedMessage.append("<BR />");
                    addBreak = false;
                }
                formattedMessage.append(c);
            }
        }
        return formattedMessage.toString();
    }
}
