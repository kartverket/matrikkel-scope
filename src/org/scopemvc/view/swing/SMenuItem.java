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
package org.scopemvc.view.swing;

import java.awt.event.ActionEvent;
import java.beans.Beans;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.util.UIStrings;

/**
 * <P>
 *
 * A JMenuItem that can be owned by a SwingView using {@link
 * org.scopemvc.controller.swing.SwingContext}, and which causes its owning view
 * to issue a Control when selected. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.14 $ $Date: 2002/09/25 13:53:09 $
 * @created 05 September 2002
 */
public class SMenuItem extends JMenuItem implements SwingSubView {

    private static final Log LOG = LogFactory.getLog(SMenuItem.class);

    /**
     * View that 'owns' this menuitem at any time. When selected, the menuitem
     * causes its owner to issue a Control.
     */
    private SwingView owner;

    private String controlID;

    /**
     * Empty constructor required by GUI designers
     */
    public SMenuItem() {
        this(null);
    }

    /**
     * Constructor for the SMenuItem object
     *
     * @param inControlID issue this Control when the user chooses this
     *      menuitem.
     */
    public SMenuItem(String inControlID) {
        this(inControlID, null, null);
    }


    /**
     * Sets text by looking up ControlID in UIStrings.
     *
     * @param inControlID issue this Control when the user chooses this
     *      menuitem.
     * @param inView the parent View that owns this menuitem. This is the view
     *      that will issue a Control when the menuitem is actioned.
     */
    public SMenuItem(String inControlID, SwingView inView) {
        this(inControlID, inView, null);
    }


    /**
     * Sets text by looking up ControlID in UIStrings.
     *
     * @param inControlID issue this Control when the user chooses this
     *      menuitem.
     * @param inView the parent View that owns this menuitem. This is the view
     *      that will issue a Control when the menuitem is actioned.
     * @param inAccelerator the KeyStroke to use as an accelerator for this
     *      menuitem.
     */
    public SMenuItem(String inControlID, SwingView inView, KeyStroke inAccelerator) {
        setControlID(inControlID);
        setAccelerator(inAccelerator);
        setOwner(inView);
    }


    /**
     * Gets the control ID
     *
     * @return The controlID value
     */
    public String getControlID() {
        return controlID;
    }


    /**
     * Gets the owner
     *
     * @return The owner value
     */
    public SwingView getOwner() {
        return owner;
    }


    /**
     * Issue a Control to the View's parent (owner) Controller.
     *
     * @param inControl The Control to issue
     */
    public void issueControl(Control inControl) {
        if (getOwner() == null) {
            LOG.warn("Cannot issue control: Owner not set");
        } else {
            SwingUtil.issueControl(getOwner(), inControl);
        }
    }


    /**
     * Gets the Controller for this View. <br>
     * Don't assign a Controller to this component, instead delegate to the
     * containing SwingView that has a parent Controller.
     *
     * @return The controller value - always null here
     */
    public Controller getController() {
        return null;
    }

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public Object getBoundModel() {
        return null;
    }


    /**
     * Sets the control ID
     *
     * @param inControlID The new controlID value
     * @deprecated Will be removed in Scope 2.0, use setActionID(..)
     */
    public void setControlID(String inControlID) {
        controlID = inControlID;
        if (controlID != null) {
            setText(UIStrings.get(controlID));
        }
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setControlID(..)</code> for now.
     *
     * @param actionID the Id of an action
     */
    public void setActionID(String actionID) {
        setControlID(actionID);
    }

    /**
     * Sets the owner of this subview element
     *
     * @param inView The new View owning this subview element
     */
    public void setOwner(SwingView inView) {
        owner = inView;
        if (owner != null) {
            inView.addSubView(this);
        }
        setEnabled(owner != null || Beans.isDesignTime());
    }

    /**
     * Don't assign a Controller to this component, instead delegate to the
     * containing SwingView that has a parent Controller.
     *
     * @param inController The new controller value
     */
    public void setController(Controller inController) {
        throw new UnsupportedOperationException("Can't assign a Controller to a " + getClass());
    }

    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        // noop
    }


    /**
     * Unset the owner of this subview element
     *
     * @param inView The View that was owning this subview element
     */
    public void unsetOwner(SwingView inView) {
        if (owner != inView) {
            return;
        }

        if (owner != null) {
            owner.removeSubView(this);
        }
        owner = null;
        setEnabled(false);
    }


    /**
     * Overriden to only issue a Control to the bound Controller.
     *
     * @param inEvent the <code>ActionEvent</code> object
     */
    protected void fireActionPerformed(ActionEvent inEvent) {
        if (controlID != null) {
            issueControl(createControl());
        } else {
            LOG.warn("Cannot issue a Control because no controlID is defined");
        }
    }

    /**
     * Override this method to create something other than a simple no-parameter
     * Control.
     *
     * @return the Control issued when the menuitem is pressed: here a simple
     *      no-parameter Control
     */
    protected Control createControl() {
        if (controlID == null) {
            throw new RuntimeException("Can't create a Control because no ControlID set.");
        }

        return new Control(controlID);
    }
}
