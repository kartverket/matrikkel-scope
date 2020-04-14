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
 * $Id: Page.java,v 1.9 2002/09/05 15:41:50 ludovicc Exp $
 */
package org.scopemvc.view.servlet;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.View;

/**
 * <P>
 *
 * Base class for views used by servlet implementation. </P> <P>
 *
 * Pages in a browser do not communicate with the web server, so this class does
 * not implement ModelChangeListener. Model objects in a web application
 * shouldn't bother to implement ModelChangeEventSource unless change
 * notification is used for some purpose other than updating Views. </P> <P>
 *
 * In a servlet application, a Controller must use a {@link ServletView} that
 * contains all possible {@link Page}s that the Controller can show. </P> <P>
 *
 * Pages must be created with unique View IDs to allow incoming requests to be
 * linked to the correct parent View instance in ScopeServlet. eg {@code http://localhost/scope/servlet/Test?view=TestView&action=TestControl}
 * causes the View with ID "TestView" to issue a Control whose ID is
 * "TestControl". </P> <P>
 *
 * The concrete implementation will need to support the appropriate ViewContext:
 * for example a JSPPage is tailored for use by the JSPContext whereas a
 * ServletXSLPage offers a different API to the XSLServletContext. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.9 $ $Date: 2002/09/05 15:41:50 $
 */
public abstract class Page implements View {

    private final static Log LOG = LogFactory.getLog(Page.class);

    /**
     * Unique ID.
     */
    private String id;

    /**
     * ServletView that contains this Page.
     */
    private ServletView parent;


    /**
     * Create with a unique ID.
     *
     * @param inViewID TODO: Describe the Parameter
     */
    protected Page(String inViewID) {
        id = inViewID;
    }


    /**
     * Gets the ID
     *
     * @return The iD value
     */
    public final String getID() {
        return id;
    }


    /**
     * Gets the parent
     *
     * @return The parent value
     */
    public final ServletView getParent() {
        return parent;
    }


    // -------------- implement View ------------------

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        if (getParent() == null) {
            return null;
        }
        return parent.getBoundModel();
    }


    /**
     * Gets the controller
     *
     * @return The controller value
     */
    public final Controller getController() {
        if (getParent() == null) {
            return null;
        }
        return parent.getController();
    }


    /**
     * Issue Control via the parent ServletView.
     *
     * @param inControl TODO: Describe the Parameter
     */
    public void issueControl(Control inControl) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("issueControl: " + inControl);
        }

        if (getController() == null) {
            throw new UnsupportedOperationException("Can't issue Control because can't find a Controller for Page with ID: " + getID());
        }

        getController().handleControl(inControl);
    }


    /**
     * Sets the parent
     *
     * @param inServletView The new parent value
     */
    public final void setParent(ServletView inServletView) {
        parent = inServletView;
    }


    /**
     * Parent ServletView is bound to a model, not each Page.
     *
     * @param inModel The new boundModel value
     */
    public final void setBoundModel(Object inModel) {
        throw new UnsupportedOperationException("Can't setBoundModel on Page: setBoundModel on parent ServletView instead.");
    }


    /**
     * Parent ServletView has a Controller, not each Page.
     *
     * @param inController The new controller value
     */
    public final void setController(Controller inController) {
        throw new UnsupportedOperationException("Can't setController on Page: setController on parent ServletView instead.");
    }


    /**
     * TODO: document the method
     *
     * @param inID TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public final boolean equalsID(String inID) {
        if (id == null && inID == null) {
            return true;
        } else if (id != null && id.equals(inID)) {
            return true;
        } else {
            return false;
        }
    }


    // ----------- support ScopeServlet ------------

    /**
     * <P>
     *
     * Called from {@link org.scopemvc.controller.servlet.ScopeServlet#doPost
     * ScopeServlet.doPost}. </P> <P>
     *
     * Implementing this method is optional -- Pages don't have to support
     * population of their model. The default implementation here does nothing.
     * </P>
     *
     * @param inParameters TODO: Describe the Parameter
     * @return List list of ValidationFailures or null if none
     */
    public List populateModel(HashMap inParameters) {
        return null;
    }
}
