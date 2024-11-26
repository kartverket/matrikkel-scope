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
 * $Id: ScopeServlet.java,v 1.16 2002/09/06 16:11:48 ludovicc Exp $
 */
package org.scopemvc.controller.servlet;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.util.BasicObjectPool;
import org.scopemvc.util.Debug;
import org.scopemvc.util.ObjectPool;
import org.scopemvc.util.PoolableObjectFactory;
import org.scopemvc.util.ScopeConfig;
import org.scopemvc.view.servlet.Page;
import org.scopemvc.view.servlet.ServletView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * <P>
 *
 * Base class for a web app's servlet dispatcher: subclass this to implement
 * application startup and initialisation (use a static initializer). This class
 * accepts incoming requests, collects the parameters into a mutable HashMap,
 * parses them to create a Control and to find a ViewID to identify the View the
 * user interacted with. The request parameters are then passed to the View to
 * populate its Model, before the View issues the Control for the owning
 * Controller to handle. </P> <P>
 *
 * A configurable number of Application Controllers (and sub-Controllers and
 * their Views and Models) are created on the first request. These are put into
 * a pool to be shared between all future requests. For this reason, Controllers
 * that are shared must be aware of the possible need to reset their model's
 * state before handling a Control. </P> <P>
 *
 * A form request is handled as follows:<BR />
 * <A HREF="../../../../../images/api/ScopeServlet.doPost.gif"> <IMG
 * SRC="../../../../../images/api/ScopeServlet.doPost.gif" WIDTH="240"
 * HEIGHT="240"> </A> </P> <P>
 *
 * Most steps in this sequence are implemented by Template Methods that can be
 * overridden to change the default behaviour. </P> <P>
 *
 * The issue of session state management and model scope is not resolved here.
 * </P> <P>
 *
 * See the various XML/XSLT and JSP servlet samples for examples of use. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.16 $ $Date: 2002/09/06 16:11:48 $
 * @see ServletContext
 * @see org.scopemvc.controller.servlet.xml.XSLScopeServlet
 * @see org.scopemvc.controller.servlet.jsp.JSPScopeServlet
 */
public abstract class ScopeServlet extends HttpServlet {

    /**
     * The default validation error handler in {@link #handleValidationFailures}
     * puts the list of {@link org.scopemvc.view.servlet.ValidationFailure}s in
     * the properties of the {@link org.scopemvc.controller.basic.ViewContext}
     * under this key for later retrieval by a Controller.
     */
    public final static String VALIDATION_FAILURES = "org.scopemvc.controller.servlet.ValidationFailures";

    /**
     * The key used to identify the Control ID in the request parameters for
     * this implementation. Initialised from the
     * "org.scopemvc.controller.servlet.ScopeServlet.ControlParam" value from
     * ScopeConfig.
     *
     * @see #createControl
     */
    public static String CONTROL_PARAM;

    /**
     * The request's key that identifies the View ID used to find the active
     * View that a Control was sent from. Initialised from the
     * "org.scopemvc.controller.servlet.ScopeServlet.ViewIDParam" value of
     * ScopeConfig.
     *
     * @see #findPageByID
     */
    public static String VIEW_ID_PARAM;

    private final static Log LOG = LogFactory.getLog(ScopeServlet.class);

    /**
     * Pool of shared application Controllers for this servlet instance.
     */
    protected ObjectPool sharedControllerPool = null;


    /**
     * Constructor for the ScopeServlet object
     */
    public ScopeServlet() {
        // Initialise statics from config. Do it this way rather than
        // in static initializers to allow custom ScopeConfigs to be
        // installed and used.
        VIEW_ID_PARAM = ScopeConfig.getString("org.scopemvc.controller.servlet.ScopeServlet.ViewIDParam");
        if (VIEW_ID_PARAM == null) {
            LOG.fatal("No ViewIDParam in config.");
        }

        CONTROL_PARAM = ScopeConfig.getString("org.scopemvc.controller.servlet.ScopeServlet.ControlParam");
        if (CONTROL_PARAM == null) {
            LOG.fatal("No ControlParam in config.");
        }

        // Create a pool of shared applications to handle requests
        try {
            initSharedControllerPool();
        } catch (Exception e) {
            LOG.fatal("Can't create shared pool of applications", e);
            throw new RuntimeException("Can't create application!\n" + e.toString());
        }
    }

//      StringBuffer message = new StringBuffer();
//      for (Iterator i = inFailures.iterator(); i.hasNext(); ) {
//        Object o = i.next();
//        if (Debug.ON) Debug.assert(o instanceof ValidationFailure);
//        ValidationFailure failure = (ValidationFailure)o;
//
//        message.append("Failed to set ");
//        message.append(failure.getProperty());
//        message.append(" to ");
//        message.append(failure.getValue());
//        message.append(" because <I>");
//        message.append(failure.getException().getLocalizedMessage());
//        message.append("</I><BR/>");
//      }
//
//      ViewContext.getViewContext().showError("Validation failed", message.toString());
//      return true;  // the request has been completed
//    }


    /**
     * Call from a Controller instead of showing a View to force an internal
     * redirect. Pass a HashMap of form parameters for the new request.
     *
     * @param inFormParameters TODO: Describe the Parameter
     */
    public static void redirect(HashMap inFormParameters) {
        if (Debug.ON) {
            Debug.assertTrue(ViewContext.getViewContext() instanceof ServletContext);
        }
        ServletContext context = (ServletContext) ViewContext.getViewContext();

        context.getServlet().handleRequest(inFormParameters);
    }


    /**
     * Copy references to all form parameters into a mutable Map. If a parameter
     * has multiple values then the parameter value will be a String[] else a
     * String. <P>
     *
     * This is a useful place to insert default values for missing parameters,
     * for example to map .../servlet/MyServlet onto some default "action" and
     * "view" by inserting those default parameters into the returned HashMap if
     * missing in incoming request. </P>
     *
     * @param inRequest find parameters in this request
     * @return Map containing references to all form parameters, either String
     *      or String[] if multiple values.
     */
    protected HashMap getFormParameters(HttpServletRequest inRequest) {
        HashMap result = new HashMap();
        for (Enumeration e = inRequest.getParameterNames(); e.hasMoreElements(); ) {

            Object o = e.nextElement();
            if (Debug.ON) {
                Debug.assertTrue(o instanceof String);
            }
            String name = (String) o;

            o = inRequest.getParameterValues(name);
            if (Debug.ON) {
                Debug.assertTrue(o instanceof String[], "not String[]: " + o);
            }
            String[] values = (String[]) o;
            if (Debug.ON) {
                Debug.assertTrue(values.length >= 1);
            }

            if (values.length == 1) {
                result.put(name, values[0]);
            } else {
                result.put(name, values);
            }
        }
        return result;
    }


    /**
     * This implementation maps GET requests onto POST requests.
     *
     * @param req TODO: Describe the Parameter
     * @param resp TODO: Describe the Parameter
     * @throws ServletException TODO: Describe the Exception
     * @throws IOException TODO: Describe the Exception
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
             throws ServletException, IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("doGet: " + req + ", " + resp);
        }
        doPost(req, resp);
    }


    /**
     * Default implementation uses shared application instances.
     *
     * @param req TODO: Describe the Parameter
     * @param resp TODO: Describe the Parameter
     * @throws ServletException TODO: Describe the Exception
     * @throws IOException TODO: Describe the Exception
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
             throws ServletException, IOException {
        // Useful debug output
        if (LOG.isDebugEnabled()) {
            LOG.debug("doPost: " + req + ", " + resp);
        }
        if (LOG.isDebugEnabled()) {
            for (Enumeration e = req.getParameterNames(); e.hasMoreElements(); ) {
                Object o = e.nextElement();
                LOG.debug("doPost: (" + o + ")=(" + req.getParameter(o.toString()) + ")");
            }
        }

        // Copy references to the form parameters into a mutable container
        HashMap formParameters = getFormParameters(req);

        // Install a ViewContext for this request if one not already set (by subclass override)
        if (Debug.ON) {
            Debug.assertTrue(ViewContext.getViewContext() == null || ViewContext.getViewContext() instanceof ServletContext);
        }
        if (ViewContext.getViewContext() == null) {
            ViewContext.setThreadContext(createServletContext(req, resp, formParameters));
        }

        // Need a 'finally' to clear the ViewContext after handling the request
        try {

            // Now handle the request using the parameters
            handleRequest(formParameters);

        } catch (Exception e) {
            LOG.fatal("doPost failed", e);
            throw new ServletException(e);
        } finally {
            // Clear the ViewContext for this request (Thread)
            ViewContext.clearThreadContext();
        }
    }


    /**
     * @param formParameters TODO: Describe the Parameter
     * @todo document the method
     */
    protected void handleRequest(HashMap formParameters) {

        // Make a Control from the form parameters
        Control control = createControl(formParameters);

        // Find the View ID from the form parameters
        String viewID = findViewID(formParameters);

        // Get an application from the shared pool to handle the request
        if (Debug.ON) {
            Debug.assertTrue(sharedControllerPool != null, "no controller pool");
        }
        Controller applicationController = (Controller) sharedControllerPool.borrowObject();
        if (Debug.ON) {
            Debug.assertTrue(applicationController != null, "can't find an applicationController");
        }

        // Need a finally block to return the shared application to the pool
        try {

            // Find the Page the user interacted with by the incoming View ID
            Page page = findPageByID(applicationController, viewID);
            // If no Page found then try to get a default Page ***** don't like this behaviour... option to invoke an error handler?
            if (page == null) {
                page = findDefaultPage(applicationController);
            }

            if (page != null) {

                // Let the Page populate its bound model if it wants to
                List failures = page.populateModel(formParameters);
                if (failures != null) {
                    // Validation errors during the populate are handled here.
                    // On return true the error was completely handled so don't carry on.
                    if (handleValidationFailures(page, failures)) {
                        return;
                    }
                }

                // Now issue the Control, or call BasicController.startup() if no Control.
                // Note that ControlExceptions are handled by the normal ViewContext.showError() mechanism.
                if (control == null) {
                    BasicController controller = (BasicController) page.getController();
                    controller.startup();
                } else {
                    page.issueControl(control);
                }
            }

            // If no View shown so far then force a showView on the active Controller's ServletView
            if (Debug.ON) {
                Debug.assertTrue(ViewContext.getViewContext() instanceof ServletContext);
            }
            ServletContext context = (ServletContext) ViewContext.getViewContext();
            if (!context.hasShownView()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("doPost: not shown view so showing: " + page);
                }
                ServletView sv = page.getParent();
                context.showView(sv);
            }
        } finally {
            sharedControllerPool.returnObject(applicationController);
        }
    }


    /**
     * Create a ViewContext that will be used for a request: default impl here
     * returns a new {@link ServletContext}. For example to implement your own
     * error handling, extend the default ServletContext to override showError,
     * and then override this method in your servlet subclass to create an
     * instance of your own ServletContext.
     *
     * @param req TODO: Describe the Parameter
     * @param resp TODO: Describe the Parameter
     * @param inFormParameters TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected abstract ServletContext createServletContext(HttpServletRequest req, HttpServletResponse resp, HashMap inFormParameters);


    /**
     * <P>
     *
     * Return a Control instance from request form parameters getting the
     * Control ID from the CONTROL_PARAM form value, and setting the
     * formParameters HashMap as the Control's parameter. Also handles imagemap
     * requests of the form <CODE>imagename.x=ControlId</CODE>. </P> <P>
     *
     * Override this for an application to create a default Control when none is
     * supplied in the request, eg for a simple home request: <PRE>http://localhost:8080/myapp/MyServlet</PRE>
     * with no parameters to display the application's home page. But also see
     * {@link #getFormParameters}. </P>
     *
     * @param ioFormParameters request's form parameters.
     * @return Control instance created from the form parameters
     */
    protected Control createControl(HashMap ioFormParameters) {

        Object o = ioFormParameters.get(CONTROL_PARAM);
        if (o instanceof String[]) {
            LOG.warn("Multiple Control parameters (using first one): " + o);
            if (Debug.ON) {
                Debug.assertTrue(((String[]) o).length > 0);
            }
            o = ((String[]) o)[0];
        }

        if (Debug.ON) {
            Debug.assertTrue(o == null || o instanceof String);
        }
        String controlID = (String) o;
        if (controlID != null && controlID.length() < 1) {
            controlID = null;
        }

        // Got it?
        if (controlID != null) {
            return new Control(controlID, ioFormParameters);
        }

        // Search all params looking for something of the form "name.x" coming from an image button
        for (Iterator i = ioFormParameters.keySet().iterator(); i.hasNext(); ) {
            o = i.next();
            if (o instanceof String) {
                String name = (String) o;
                if (name.endsWith(".x")) {
                    controlID = name.substring(0, name.length() - 2);
                    break;
                }
            }
        }

        // Got a Control ID? Then make a Control and remove the foo.x and foo.y parameters
        if (controlID != null && controlID.length() > 0) {
            return new Control(controlID, ioFormParameters);
        }

        // Got no control
        return null;
    }


    /**
     * Could be overidden to provide a default ViewID if none in the parameters,
     * but also see {@link #getFormParameters}.
     *
     * @param inParameters TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected String findViewID(HashMap inParameters) {
        return (String) inParameters.get(VIEW_ID_PARAM);
    }


    /**
     * Search through application's Controller hierarchy to find a Page matching
     * the ViewID. Return null if not found.
     *
     * @param inRootController TODO: Describe the Parameter
     * @param inViewID TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected Page findPageByID(Controller inRootController, String inViewID) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("findPageByID: " + inRootController + ", " + inViewID);
        }
        if (Debug.ON) {
            Debug.assertTrue(inRootController != null);
        }
        if (!(inRootController instanceof BasicController)) {
            throw new RuntimeException("ScopeServlet relies on application Controllers being instanceof BasicController.");
        }

        // Search at the current root
        if (Debug.ON) {
            Debug.assertTrue(inRootController.getView() == null || inRootController.getView() instanceof ServletView);
        }
        ServletView v = (ServletView) inRootController.getView();
        if (v != null) {
            Page p = v.findPageByID(inViewID);
            if (p != null) {
                return p;
            }
        }

        // If not there, recurse through all children
        for (Iterator i = ((BasicController) inRootController).getChildren().iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (Debug.ON) {
                Debug.assertTrue(o instanceof Controller);
            }
            Page result = findPageByID((Controller) o, inViewID);
            if (result != null) {
                return result;
            }
        }

        // And if not found then return null
        return null;
    }


    /**
     * If the request ViewID doesn't match any Page then this provides a default
     * Page: could be the start page of the application that the user gets to by
     * invoking the servlet with no parameters. Here returns the first Page
     * found by a depth-first traversal of the application hierarchy. <P>
     *
     * If you manage the {@link #getFormParameters} method to validate form
     * parameters then this method might never be used. But if there's a single
     * page that you want to use when an invalid ViewID is passed (eg an error
     * page) then this is the place to do it. </P> <P>
     *
     * Don't like this. Should be allowed to redirect to an error handler on
     * invalid ViewID? ***** </P>
     *
     * @param inRootController TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    protected Page findDefaultPage(Controller inRootController) {
        // Search at the current root
        if (Debug.ON) {
            Debug.assertTrue(inRootController.getView() == null || inRootController.getView() instanceof ServletView);
        }
        ServletView v = (ServletView) inRootController.getView();
        if (v != null) {
            Page result = v.getFirstPage();
            if (result != null) {
                return result;
            }
        }

        for (Iterator i = ((BasicController) inRootController).getChildren().iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (Debug.ON) {
                Debug.assertTrue(o instanceof Controller);
            }
            Page result = findDefaultPage((Controller) o);
            if (result != null) {
                return result;
            }
        }

        // And if not found then return null
        return null;
    }


    /**
     * <P>
     *
     * Called if an exception is thrown by {@link
     * org.scopemvc.view.servlet.Page#populateModel Page.populateModel}. </P>
     * <P>
     *
     * Default implementation here puts the List of {@link
     * org.scopemvc.view.servlet.ValidationFailure}s into the ViewContext under
     * the {@link #VALIDATION_FAILURES} key for retrieval ({@link
     * org.scopemvc.controller.basic.ViewContext#getProperty} and handling by
     * Controllers, ie: <PRE>
     * protected void doSomeHandler() throws ControlException {
     *   List validationFailures = (List)ViewContext.getViewContext().getProperty(ScopeServlet.VALIDATION_FAILURES);
     *   if (validationFailures != null) {
     *     // TODO: Handle the validation failures
     *   } else {
     *     // TODO: No validation failures so handle the control
     *   }
     * }
     * </PRE> </P>
     *
     * @param inPage TODO: Describe the Parameter
     * @param inFailures TODO: Describe the Parameter
     * @return true if this handler has finished the request, ie the normal
     *      request handler can stop immediately.
     */
    protected boolean handleValidationFailures(Page inPage, List inFailures) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("handleValidationFailures: " + inFailures);
        }
        if (Debug.ON) {
            Debug.assertTrue(inFailures != null);
        }

        ViewContext.getViewContext().addProperty(VALIDATION_FAILURES, inFailures);
        return false;
    }


    /**
     * <P>
     *
     * Override this to create the root application Controller. The application
     * Controller should setup any child Controllers that it needs to handle
     * parts of the application for it. </P>
     *
     * @return new application Controller
     * @exception Exception on any failure
     */
    protected abstract Controller createApplicationController() throws Exception;


    // --------------- Shared controller pool -------------------------

    /**
     * @todo document the method
     * @throws Exception TODO: Describe the Exception
     */
    protected void initSharedControllerPool() throws Exception {
        int size = 10;
        Integer i = ScopeConfig.getInteger("org.scopemvc.controller.servlet.ScopeServlet.maxControllerPoolSize");
        if (i != null) {
            size = i.intValue();
        }
        sharedControllerPool = new BasicObjectPool(new SharedControllerFactory(), size);
    }


    /**
     * @author smefroi
     * @created 05 August 2002
     * @todo document the class
     */
    protected class SharedControllerFactory implements PoolableObjectFactory {
        /**
         * @return TODO: Describe the Return Value
         * @todo document the method
         */
        public Object createObject() {
            try {
                return createApplicationController();
            } catch (Exception e) {
                LOG.fatal("Can't create application Controller", e);
                return null;
            }
        }

        /**
         * @param object TODO: Describe the Parameter
         * @todo document the method
         */
        public void destroyObject(Object object) {
            // noop
        }

        /**
         * @param object TODO: Describe the Parameter
         * @todo document the method
         */
        public void activateObject(Object object) {
            // noop
        }

        /**
         * @param object TODO: Describe the Parameter
         * @todo document the method
         */
        public void passivateObject(Object object) {
            // noop
        }
    }
}
