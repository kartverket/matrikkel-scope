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
 * $Id: ServletView.java,v 1.9 2002/09/05 15:41:50 ludovicc Exp $
 */
package org.scopemvc.view.servlet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.View;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * A container View to aggregate a set of {@link Page}s the allows the currently
 * visible Page to be set. </P> <P>
 *
 * In a servlet application, a Controller's View must 'contain' all the possible
 * pages that the user could be interacting with because the user is free to hit
 * the browser's forward and back buttons, use history or bookmarks etc. This
 * "View" therefore acts as a simple container for other Views ({@link Page}s)
 * that represent the actual interfaces that the user interacts with. It
 * forwards any {@link #setController} and {@link #setBoundModel} calls to all
 * children. </P> <P>
 *
 * A Controller in a web application must create an instance of ServletView to
 * put all its possible Pages into using {@link #addPage}. The Controller then
 * setView() establishes the container as the Controller's View. To determine
 * the Page that is shown to the user on {@link
 * org.scopemvc.controller.basic.BasicController#showView showView}, call {@link
 * #setVisible} with the Page ID. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.9 $ $Date: 2002/09/05 15:41:50 $
 */
public class ServletView implements View {

    private final static Log LOG = LogFactory.getLog(ServletView.class);

    /**
     * The child Pages this ServletView contains.
     */
    protected List pages;

    /**
     * The subview that is "visible". ie the Page that will streamView to the
     * user.
     */
    private Page visible;

    /**
     * Parent Controller for this View and all child Pages.
     */
    private Controller controller;

    /**
     * Bound model for this View and all child Pages.
     */
    private Object model;


    /**
     * Create a container for the Pages that a Controller manages.
     */
    public ServletView() { }


    // -------------- implement View ------------------

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return model;
    }


    /**
     * Gets the controller
     *
     * @return The controller value
     */
    public final Controller getController() {
        return controller;
    }


    /**
     * Note that ServletViews inherit their parent's Controller.
     *
     * @param inControl TODO: Describe the Parameter
     */
    public final void issueControl(Control inControl) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("issueControl: parent controller: " + controller);
        }

        if (controller == null) {
            throw new UnsupportedOperationException("Can't issue Control because can't find a Controller");
        }

        controller.handleControl(inControl);
    }


    /**
     * Used by {@link org.scopemvc.controller.servlet.ScopeServlet#findDefaultPage
     * ScopeServlet.findDefaultPage} .
     *
     * @return The firstPage value
     */
    public final Page getFirstPage() {
        if (pages == null || pages.size() < 1) {
            return null;
        }
        return (Page) pages.get(0);
    }


    /**
     * Gets the visible
     *
     * @return The visible value
     */
    public final Page getVisible() {
        if (visible != null) {
            return visible;
        }
        if (pages != null && !pages.isEmpty()) {
            return (Page) pages.get(0);
        }
        return null;
    }


    /**
     * The passed model is bound to this view and to all children ServletViews
     * as well.
     *
     * @param inModel The new boundModel value
     */
    public final void setBoundModel(Object inModel) {
        model = inModel;
    }


    /**
     * The parent Controller must register itself with the ServletView via this
     * method in order to receive Controls from it.
     *
     * @param inController the parent Controller.
     */
    public final void setController(Controller inController) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setController: " + inController);
        }

        controller = inController;
    }


    // ---------- Select the child that will be asked to streamView ----------

    /**
     * Sets the visible
     *
     * @param inViewID The new visible value
     */
    public final void setVisible(String inViewID) {
        Page view = findPageByID(inViewID);
        if (view == null) {
            throw new UnsupportedOperationException("Can't find View with ID: " + inViewID + " to make it visible.");
        }
        visible = view;
    }


    // -------------- view container ------------------

    /**
     * Adds an element to the Page attribute of the ServletView object
     *
     * @param inPage The element to be added to the Page attribute
     */
    public final void addPage(Page inPage) {

        if (pages == null) {
            pages = new LinkedList();
        }

        pages.add(inPage);

        inPage.setParent(this);

        if (visible == null) {
            visible = inPage;
        }
    }


    /**
     * TODO: document the method
     *
     * @param inViewID TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public final Page findPageByID(String inViewID) {
        if (pages == null) {
            return null;
        }

        for (Iterator i = pages.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (Debug.ON) {
                Debug.assertTrue(o instanceof Page, "not Page: " + o);
            }
            if (((Page) o).equalsID(inViewID)) {
                return (Page) o;
            }
        }

        return null;
    }


    /**
     * For debug.
     *
     * @return TODO: Describe the Return Value
     */
    public String toString() {
        String result = "(Page:";
        if (pages != null) {
            result += "," + pages.toString();
        }
        result += ")";
        return result;
    }
}
