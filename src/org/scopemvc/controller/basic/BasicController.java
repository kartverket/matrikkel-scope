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
 * $Id: BasicController.java,v 1.18 2002/10/31 11:19:35 ludovicc Exp $
 */
package org.scopemvc.controller.basic;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;
import org.scopemvc.core.Controller;
import org.scopemvc.core.PropertyView;
import org.scopemvc.core.View;
import org.scopemvc.util.Debug;
import org.scopemvc.util.UIStrings;

/**
 * <P>
 *
 * Full implementation of {@link org.scopemvc.core.Controller Controller} that
 * adds:
 * <UL>
 *   <LI> support for a View to notify its parent Controller when its bound
 *   model object is replaced with another (implemented completely in {@link
 *   org.scopemvc.view.swing.SwingView SwingView}) via the
 *   CHANGE_MODEL_CONTROL_ID ControlID. Note that the PropertyView that a
 *   top-level Controller owns must not have a Selector set: this is only
 *   allowed for child Controllers that are delegated by a parent to handle a
 *   subview and associated submodel that is part of the parent model: the
 *   binding will be handled by the parent in this case. </LI>
 *   <LI> {@link org.scopemvc.core.ControlException ControlException} handling
 *   by using {@link ViewContext#showError}. </LI>
 * </UL>
 * </P> <P>
 *
 * To implement a subclass of BasicController:
 * <UL>
 *   <LI> implement a constructor to set up the Controller's initial model and
 *   View, and to create any child Controllers it may need. {@link
 *   #setModelAndView} may be useful here. </LI>
 *   <LI> implement {@link #doHandleControl} to recognise the ID of incoming
 *   {@link org.scopemvc.core.Control Control}s and respond to them
 *   appropriately. For example: <PRE>
 * protected void doHandleControl(Control inControl) throws ControlException {
 *     if (inControl.matchesID(FOO_CONTROL_ID)) {
 *         doFoo(inControl.getParameter());
 *     } else if (inControl.matchesID(BAR_CONTROL_ID)) {
 *         doBar(inControl.getParameter());
 *     }
 * }
 * </PRE> </LI>
 *   <LI> if necessary, implement a startup() method for the Controller to take
 *   its initial action (if your application calls startup() on this
 *   Controller). </LI>
 *   <LI> <FONT COLOR="GRAY">Internal: if using a View that can dynamically
 *   change its bound model, ensure the View sends the appropriate
 *   CHANGE_MODEL_CONTROL_ID Control to inform the parent Controller of the
 *   change. This is fully implemented in {@link
 *   org.scopemvc.view.swing.SwingView SwingView} and is not needed for {@link
 *   org.scopemvc.view.servlet.ServletView ServletView} with the default
 *   implementation in {@link org.scopemvc.view.servlet.xml.XSLPage}. </FONT>
 *   </LI>
 * </UL>
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.18 $ $Date: 2002/10/31 11:19:35 $
 * @created 05 August 2002
 */
public abstract class BasicController implements Controller {

    /**
     * ID of error message for RuntimeExceptions caught by BasicController.
     */
    public static final String HANDLE_CONTROL_RUNTIME_ERROR_MSG_ID = "_HANDLE_CONTROL_RUNTIME_ERROR_MSG";

    /**
     * ID of common Control that is handled by BasicController to hide the
     * current view.
     */
    public static final String HIDE_VIEW_CONTROL_ID = "_HIDE_VIEW";

    /**
     * An internal Control calls changeModel(). This is an internal Control to
     * keep Controller's model in sync with the currently shown model in its
     * View. This occurs when a parent controller modifies its model when that
     * contains the submodel managed by a child controller. This is fully
     * implemented by the concrete impl of SwingView. ServletView doesn't need
     * it.
     */
    public static final String CHANGE_MODEL_CONTROL_ID = "_CHANGE_MODEL";

    /**
     * <P>
     *
     * A convenience Control that can be used when a Controller wants to exit.
     * For an application Controller this means exitting the application, but
     * for a sub-controller it probably means just an exit from that local area
     * of functionality. This impl causes this Control to propagate up the chain
     * of responsibility so that if unhandled the application will exit. </P>
     * <P>
     *
     * The parameter of this Control is the Controller that issued it (ie the
     * one that's shutting down), or null if just issued by <code>this</code>.
     * The default impl here propagates the Control up, changing the shutdown
     * Controller as it goes up, until it meets the top of the Controller tree
     * at which point {@link ViewContext#exit} is called: for Swing this does
     * System.exit and for Servlets it is ignored. If you use this Control,
     * recognise it at some parent of the Controller that can issue it, and take
     * appropriate action. </P>
     *
     * @todo The servlet implementation of exit should logout the user from the
     *      web application (ludovicc)
     */
    public static final String EXIT_CONTROL_ID = "_exit";

    private static final Log LOG = LogFactory.getLog(BasicController.class);

    private BasicController parent;

    // Note that the only reason for Controllers to keep a handle on
    // ... their children is to allow the ScopeServlet impl to traverse an
    // ... application's controller graph to find a view by its id.
    private LinkedList children = new LinkedList();

    private Object model;

    private View view;


    /**
     * <P>
     *
     * Construct subclasses by either using a passed model object and View, or
     * creating new ones. Use {@link #setModel} and {@link #setView} or {@link
     * #setModelAndView}. Never show a View on construction: initialisation
     * should set the application up without actually starting it by showing a
     * View. An initial startup action implemented in startup() can show a View
     * when called after construction. </P> <P>
     *
     * Throw a ControlException from subclasses if something goes wrong. </P>
     */
    public BasicController() { }


    // ---------------------- Child management ------------------------

    /**
     * Returns the list of child Controllers. <br>
     * Used by ScopeServlet.
     *
     * @return List of child Controllers.
     */
    public final List getChildren() {
        return children;
    }


    // ----------------------------- Implement Controller --------------------------------

    /**
     * Get the parent of this Controller
     *
     * @return the parent Controller of this Controller in the chain of command
     */
    public final Controller getParent() {
        return parent;
    }


    /**
     * Return the model bound to this Controller
     *
     * @return the model object bound to the View that this Controller maintains
     */
    public final Object getModel() {
        return model;
    }


    /**
     * Return the View bound to this Controller.
     *
     * @return the View that this Controller maintains
     */
    public final View getView() {
        return view;
    }


    /**
     * Convenience method. <br>
     * Get the topmost parent Controller.
     *
     * @return the topmost parent Controller in the chain of responsibility.
     */
    public final Controller getTopParent() {
        Controller result = this;
        while (result.getParent() != null) {
            result = result.getParent();
        }
        return result;
    }


    /**
     * Sets the model object that this Controller links to its View. If you need
     * to set both the View and model then use {@link #setModelAndView} rather
     * than calling setModel and setView separately.
     *
     * @param inModel The new model value
     */
    public final void setModel(Object inModel) {
        if (model == inModel) {
            return;
        }
        model = inModel;
        bindModelToView(view, model);
    }


    /**
     * Sets the View that this Controller links to its model object. Unlinks the
     * old View from the current model object and also hides it, however,
     * doesn't show the new view. If you need to set both the View and model
     * object then slightly more efficient in establishing the binding to use
     * {@link #setModelAndView} rather than calling setModel() and setView()
     * separately.
     *
     * @param inView The new view value
     */
    public final void setView(View inView) {
        if (inView == view) {
            return;
        }
        if (view != null) {
            hideView();
            view.setController(null);
            bindModelToView(view, null);
        }
        view = inView;
        if (view != null) {
            bindModelToView(view, model);
            view.setController(this);
        }
    }


    /**
     * Change to both a new model object and new View, binding the two together
     * properly. Also disconnect and discard/hide the previous model/View pair.
     * <br>
     * Slightly more efficient in changing to a new model/view binding than
     * calling setModel and setView separately.
     *
     * @param inModel new model object, can be null
     * @param inView new View, cannot be null
     */
    public final void setModelAndView(Object inModel, View inView) {

        // break existing model/view connection to avoid hooking
        // ... new view to old model then immediately rebinding to
        // ... new model.
        setModel(null);

        setModel(inModel);
        setView(inView);
    }


    /**
     * Add a child Controller. <br>
     * The child Controller will use this Controller as its parent.
     *
     * @param inChild The child Controller to add.
     */
    public final void addChild(BasicController inChild) {
        if (inChild == null) {
            throw new IllegalArgumentException("Can't add a null child Controller.");
        }
        inChild.setParent(this);
    }


    /**
     * Remove a child Controller from this Controller. <br>
     * The child Controller will have no more parent.
     *
     * @param inChild The child Controller to remove.
     */
    public final void removeChild(BasicController inChild) {
        if (getChildren().contains(inChild)) {
            inChild.setParent(null);
        }
    }


    /**
     * Application writers see {@link #doHandleControl}. This base
     * implementation handles
     * <UL>
     *   <LI> HIDE_VIEW_CONTROL_ID</LI>
     *   <LI> the internal CHANGE_MODEL_CONTROL_ID</LI>
     *   <LI> EXIT_CONTROL_ID after allowing application code to intercept in
     *   doHandleControl. If this Controller has a parent, then hideView and
     *   pass it up, else call the ViewContext to do the exit according to
     *   context.</LI>
     * </UL>
     *
     *
     * @param inControl The Control to handle
     * @todo Should get children to hideView too on EXIT_CONTROL_ID
     */
    public final void handleControl(Control inControl) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("handleControl: " + inControl);
        }
        if (inControl == null) {
            throw new IllegalArgumentException("Can't handle a null Control.");
        }

        try {
            // Internal CHANGE_MODEL_CONTROL_ID
            if (inControl.matchesID(CHANGE_MODEL_CONTROL_ID)) {
                changeModel(inControl.getParameter());
            } else {
                // else subclass impl
                ViewContext.getViewContext().startProgress();
                try {
                    doHandleControl(inControl);
                } finally {
                    ViewContext.getViewContext().stopProgress();
                }
            }
            // For unhandled Controls
            if (!inControl.isMatched()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("handleControl: not matched: " + inControl);
                }
                // Default handler for EXIT_CONTROL_ID
                if (inControl.matchesID(EXIT_CONTROL_ID)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("handleControl: EXIT: " + getParent());
                    }
                    if (getParent() == null) {
                        if (ViewContext.getViewContext() == null) {
                            throw new RuntimeException("No ViewContext: setup at start of application"
                                    + "using ViewContext.");
                        }
                        ViewContext.getViewContext().exit();
                    } else {
                        inControl.setParameter(this);
                        // propagate from this
                        inControl.markUnmatched();
                        // allow to bubble upwards to parent
                        passControlToParent(inControl);
                    }
                } else if (inControl.matchesID(HIDE_VIEW_CONTROL_ID)) {
                    // else default handler for HIDE_VIEW_CONTROL_ID
                    hideView();
                } else {
                    // else pass up chain of responsibility
                    passControlToParent(inControl);
                }
            }
            if (!inControl.isMatched()) {
                LOG.warn("Control not handled: " + inControl);
            }
        } catch (ControlException exception) {
            inControl.markMatched();
            // stop propagation of the Control!
            inControl.populateControlException(exception);
            handleControlException(exception);
        } catch (RuntimeException exception) {
            // Log unchecked exceptions even if app code ignores
            LOG.error("Failed to handle Control: " + inControl, exception);
            ControlException cex = new ControlException(HANDLE_CONTROL_RUNTIME_ERROR_MSG_ID, exception);
            inControl.markMatched();
            // stop propagation of the Control!
            inControl.populateControlException(cex);
            handleControlException(cex);
        }
    }


    // ------------- Startup and shutdown -----------

    /**
     * <p>
     *
     * Starts the Controller and its bound View and Model. </p> Call this method
     * after creating the Controller to make it perform its initial action. This
     * method is not called automatically by Scope, so you have to call it
     * yourself.<br>
     * Default implementation here just calls showView() if a View is set.
     */
    public void startup() {
        if (getView() != null) {
            showView();
        }
    }


    /**
     * <p>
     *
     * Shutdown the Controller and its bound View and Model. </p> Can be called
     * by a parent Controller to shutdown and remove this from the chain of
     * responsibility. <br>
     * Default implementation does this:
     * <UL>
     *   <LI> call shutdown() on every child controller</LI>
     *   <LI> call hideView()</LI>
     *   <LI> setParent(null)</LI>
     * </UL>
     *
     */
    public void shutdown() {
        if (Debug.ON) {
            Debug.assertTrue(getChildren() != null);
        }
        // Make an array copy to avoid modifying while iterating
        BasicController[] c = (BasicController[]) getChildren().toArray(new BasicController[0]);
        for (int i = 0; i < c.length; ++i) {
            c[i].shutdown();
        }
        hideView();
        setParent(null);
    }


    /**
     * Hook this Controller into the chain of responsiblity as a child of the
     * passed Controller. See {@link #addChild}
     *
     * @param inParent The new parent value
     */
    protected final void setParent(BasicController inParent) {
        if (parent != null) {
            parent.getChildren().remove(this);
        }

        parent = inParent;

        if (parent != null) {
            parent.getChildren().add(this);
        }
    }


    /**
     * Feed a Control to the parent Controller up the chain of command.
     *
     * @param inControl The Control to delegate to the parent Controller
     */
    protected final void passControlToParent(Control inControl) {
        if (inControl == null) {
            throw new IllegalArgumentException("Can't pass null Control to parent.");
        }

        // thread-safety
        Controller localParent = parent;
        if (LOG.isDebugEnabled()) {
            LOG.debug("passControlToParent: to: " + localParent + " control: " + inControl);
        }

        if (localParent == null) {
            // Reached the top of this chain of command without handling the control
            return;
        }

        localParent.handleControl(inControl);
    }


    // ------------- Convenience View management -----------
    // Methods here just present a simpler API from ViewContext,
    // so it may be usefull to call ViewContext directly if the
    // functionality required is not present here

    /**
     * Show the view bound to this Controller.
     */
    protected final void showView() {
        showView(getView());
    }


    /**
     * Show the given view.
     *
     * @param inView The View to show
     */
    protected final void showView(View inView) {
        if (inView == null) {
            throw new RuntimeException("No View to show.");
        }
        if (ViewContext.getViewContext() == null) {
            throw new RuntimeException("No ViewContext: setup at start of application using ViewContext.");
        }
        try {
            ViewContext.getViewContext().showView(inView);
        } catch (Exception e) {
            // Log unchecked exceptions even if app code ignores
            LOG.error("Failed to showView: " + inView, e);
        }
    }


    /**
     * Hide the View bound to this Controller.
     */
    protected final void hideView() {
        hideView(getView());
    }


    /**
     * Hide the given View
     *
     * @param inView The View to hide
     */
    protected final void hideView(View inView) {
        if (inView == null) {
            throw new RuntimeException("No View to hide.");
        }
        if (ViewContext.getViewContext() == null) {
            throw new RuntimeException("No ViewContext: setup at start of application using ViewContext.");
        }
        try {
            ViewContext.getViewContext().hideView(inView);
        } catch (Exception e) {
            // Log unchecked exceptions even if app code ignores
            LOG.error("Failed to showView: " + inView, e);
        }
    }


    /**
     * Convenience to show an error using the current {@link
     * org.scopemvc.controller.basic.ViewContext ViewContext}.
     *
     * @param inErrorTitle The title for the error message window
     * @param inErrorMessage The content of the error message
     */
    protected final void showError(String inErrorTitle, String inErrorMessage) {
        if (ViewContext.getViewContext() == null) {
            throw new RuntimeException("No ViewContext: setup at start of application using ViewContext.");
        }
        ViewContext.getViewContext().showError(
                inErrorTitle, inErrorMessage);
    }


    /**
     * Bind a model object to a View if that is possible (model and view must be
     * not null, the view must not have a selector marking it as being handled
     * by a parent view)
     *
     * @param inView the View to bind
     * @param inModel the model object to bind to the View
     */
    protected void bindModelToView(View inView, Object inModel) {
        if (inView == null) {
            return;
        }
        if (inView instanceof PropertyView && ((PropertyView) inView).getSelector() != null) {
            // views with selectors set are never bound by this controller: assumed to be handled by parent
            // ... that has delegated responsibility of a subsystem to this child. The high-level binding
            // ... is done by the parent.
            return;
        }
        inView.setBoundModel(inModel);
    }


    /**
     * <P>
     *
     * Custom implementation of some presentation logic. </P> <P>
     *
     * Override this to recognise Controls that this Controller can handle. Any
     * unhandled Controls are passed up the chain of responsibility to parent
     * Controllers. <PRE>
     * protected void doHandleControl(Control inControl) throws ControlException {
     *     if (inControl.matchesID(FOO_CONTROL_ID)) {
     *         doFoo(inControl.getParameter());
     *     } else if (inControl.matchesID(BAR_CONTROL_ID)) {
     *         doBar(inControl.getParameter());
     *     }
     * }
     * </PRE> </P> <P>
     *
     * If something goes wrong when running some presentation logic, throw a
     * {@link org.scopemvc.core.ControlException ControlException} which results
     * in a call to {@link #handleControlException}). </P>
     *
     * @param inControl The Control to handle
     * @throws ControlException If something goes wrong when running some
     *      presentation logic
     * @todo The implementation of doHandleControl is hugly, with its long if
     *      ... else if sequence. The Command pattern may help to provide a
     *      cleaner implementation to the users (ludovicc)
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        // do nothing by default -- see handleControl
    }


    /**
     * Called by {@link #handleControl} when a {@link org.scopemvc.core.Control
     * Control} throws a {@link org.scopemvc.core.ControlException
     * ControlException}. <br>
     * This implementation uses the {@link #showError} method. <br>
     * A ContolException with a HANDLE_CONTROL_RUNTIME_ERROR_MSG_ID message can
     * be generated when the Controller runs code that throws some unchecked
     * exception.
     *
     * @param inException An exception thrown when something goes wrong with the
     *      presentation logic.
     */
    protected void handleControlException(ControlException inException) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("handleControlException: " + inException);
        }
        if (inException == null) {
            throw new IllegalArgumentException("Can't handle null ControlException.");
        }

        if (inException == null) {
            showError(UIStrings.get("UnknownErrorTitle"),
                    UIStrings.get("UnknownErrorMessage"));
        } else {
            showError(inException.getLocalizedSourceControlName(),
                    inException.getLocalizedMessage());
        }
    }


    // ---------------------- Internal CHANGE_MODEL_CONTROL_ID Control support ------------------------

    /**
     * Respond to CHANGE_MODEL_CONTROL_ID to keep the controller's model in sync
     * with the currently shown model if it is changed as a submodel of a model
     * managed by a parent controller.
     *
     * @param inParameter the new model object to set on this Controller.
     */
    private void changeModel(Object inParameter) {
        setModel(inParameter);
    }
}

