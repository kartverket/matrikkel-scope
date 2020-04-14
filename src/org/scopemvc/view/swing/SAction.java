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
 * $Id: SAction.java,v 1.9 2002/11/20 01:36:58 ludovicc Exp $
 */
package org.scopemvc.view.swing;

import java.awt.event.ActionEvent;
import java.beans.Beans;
import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.util.ResourceLoader;
import org.scopemvc.util.UIStrings;

/**
 * A swing Action that issues a Control on action performed. <p>
 *
 * SAction uses the control ID and the resources in UIStrings to initialise
 * itself. <br>
 * The following properties in SAction are initialised from UIStrings as follow
 * (replace [Control ID] by the actual value of the controlID property):
 * <ul>
 *   <li> label is keyed against [Control ID]
 *   <li> shortDescription is keyed against [Control ID].ShortDescription
 *   <li> longDescription is keyed against [Control ID].LongDescription
 *   <li> small icon is keyed against [Control ID].SmallIcon (containing the
 *   path of the icon)
 *   <li> acceleratorKey is keyed against [Control ID].AcceleratorKey
 *   <li> mnemonicKey is keyed against [Control ID].MnemonicKey
 * </ul>
 *
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.9 $
 * @created June 6, 2002
 * @see org.scopemvc.util.ResourceLoader for more explanation on own to load
 *      custom icons
 */

public class SAction extends AbstractAction implements SwingSubView {

    private static final Log LOG = LogFactory.getLog(SAction.class);

    /**
     * View that 'owns' this menuitem at any time. When selected, the action
     * causes its owner to issue a Control.
     */
    private SwingView owner;

    private String controlID;

    /**
     * Constructor for the action with no control and no selector.
     */
    public SAction() {
        this(null, null);
    }

    /**
     * Constructor for the action with a control but no selector.
     *
     * @param inControlID The ID of the control to be issued when this action is
     *      performed.
     */
    public SAction(String inControlID) {
        this(inControlID, null);
    }


    /**
     * Constructor for the action with a control but no selector. <br>
     * Sets the action text by looking up ControlID in UIStrings.
     *
     * @param inControlID The ID of the control to be issued when this action is
     *      performed.
     * @param inView the parent View that owns this action. This is the view
     *      that will issue a Control when the action is performed.
     */
    public SAction(String inControlID, SwingView inView) {
        super();
        setControlID(inControlID);
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
     * Gets the name
     *
     * @return The name value
     */
    public String getName() {
        return (String) getValue(NAME);
    }

    /**
     * Gets the small icon
     *
     * @return The smallIcon value
     */
    public Icon getSmallIcon() {
        return (Icon) getValue(SMALL_ICON);
    }

    /**
     * Gets the accelerator key
     *
     * @return The acceleratorKey value
     */
    public String getAcceleratorKey() {
        return (String) getValue(ACCELERATOR_KEY);
    }

    /**
     * Gets the long description
     *
     * @return The longDescription value
     */
    public String getLongDescription() {
        return (String) getValue(LONG_DESCRIPTION);
    }

    /**
     * Gets the short description
     *
     * @return The shortDescription value
     */
    public String getShortDescription() {
        return (String) getValue(SHORT_DESCRIPTION);
    }

    /**
     * Gets the mnemonic key
     *
     * @return The mnemonicKey value
     */
    public Integer getMnemonicKey() {
        return (Integer) getValue(MNEMONIC_KEY);
    }

    /**
     * Gets the owner of this subview element
     *
     * @return The View owning this subview element
     */
    public SwingView getOwner() {
        return owner;
    }


    /**
     * Issue a Control to the View's parent (owner) Controller. <br>
     * Don't assign a Controller to SAction, instead delegate to the containing
     * SwingView that has a parent Controller.
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
     * Gets the model object bound to this View.
     *
     * @return The boundModel value - always null here
     */
    public Object getBoundModel() {
        return null;
    }

    /**
     * Sets the control ID
     *
     * @param inControlID The new controlID value
     */
    public void setControlID(String inControlID) {
        controlID = inControlID;
        if (controlID != null) {
            if (getName() == null) {
                String name = UIStrings.get(controlID);
                setName(name);
            }
            if (getSmallIcon() == null) {
                String iconPath = UIStrings.get(controlID + "." + SMALL_ICON, null);
                if (iconPath != null) {
                    try {
                        Icon icon = ResourceLoader.getIcon(iconPath);
                        setSmallIcon(icon);
                    } catch (RuntimeException ex) {
                        LOG.warn("Could not find the icon, path was " + iconPath, ex);
                    }
                }
            }
            if (getAcceleratorKey() == null) {
                String acceleratorKey = UIStrings.get(controlID + "." + ACCELERATOR_KEY, null);
                if (acceleratorKey != null) {
                    setAcceleratorKey(acceleratorKey);
                }
            }
            if (getMnemonicKey() == null) {
                String mnemonicKeyStr = UIStrings.get(controlID + "." + MNEMONIC_KEY, null);
                if (mnemonicKeyStr != null) {
                    try {
                        Integer mnemonicKey = Integer.valueOf(mnemonicKeyStr);
                        setMnemonicKey(mnemonicKey);
                    } catch (RuntimeException ex) {
                        LOG.warn("Could not parse the mnemonic key, value was " + mnemonicKeyStr, ex);
                    }
                }
            }
            if (getShortDescription() == null) {
                String shortDescription = UIStrings.get(controlID + "." + SHORT_DESCRIPTION, null);
                if (shortDescription != null) {
                    setShortDescription(shortDescription);
                }
            }
            if (getLongDescription() == null) {
                String longDescription = UIStrings.get(controlID + "." + LONG_DESCRIPTION, null);
                if (longDescription != null) {
                    setLongDescription(longDescription);
                }
            }
        }
    }


    /**
     * Sets the owner of this subview element
     *
     * @param inView The new View owning this subview element
     */
    public void setOwner(SwingView inView) {
        owner = inView;
        if (owner != null) {
            owner.addSubView(this);
        }
        setEnabled(owner != null || Beans.isDesignTime());
    }

    /**
     * Sets the name of the action
     *
     * @param inValue The new name value
     */
    public void setName(String inValue) {
        putValue(NAME, inValue);
    }

    /**
     * Sets the small icon of the action
     *
     * @param inValue The new smallIcon value
     * @see SMALL_ICON
     */
    public void setSmallIcon(Icon inValue) {
        putValue(SMALL_ICON, inValue);
    }

    /**
     * Sets the accelerator key of the action
     *
     * @param inValue The new acceleratorKey value
     * @see ACCELERATOR_KEY
     */
    public void setAcceleratorKey(String inValue) {
        putValue(ACCELERATOR_KEY, inValue);
    }

    /**
     * Sets the long description of the action
     *
     * @param inValue The new longDescription value
     * @see LONG_DESCRIPTION
     */
    public void setLongDescription(String inValue) {
        putValue(LONG_DESCRIPTION, inValue);
    }

    /**
     * Sets the short description of the action
     *
     * @param inValue The new shortDescription value
     * @see SHORT_DESCRIPTION
     */
    public void setShortDescription(String inValue) {
        putValue(SHORT_DESCRIPTION, inValue);
    }

    /**
     * Sets the mnemonic key of the action
     *
     * @param inValue The new mnemonicKey value
     * @see MNEMONIC_KEY
     */
    public void setMnemonicKey(Integer inValue) {
        putValue(MNEMONIC_KEY, inValue);
    }

    /**
     * Sets the Controller for this View. <br>
     * Don't assign a Controller to this component, instead delegate to the
     * containing SwingView that has a parent Controller. This method will
     * always fail here.
     *
     * @param inController The new controller value
     */
    public void setController(Controller inController) {
        throw new UnsupportedOperationException("Can't assign a Controller to a " + getClass());
    }


    /**
     * Sets the bound model. <br>
     * Does nothing here.
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

        owner = null;
        setEnabled(false);
    }


    /**
     * Invoked when an action occurs.
     *
     * @param inEvent The event for the action
     */
    public void actionPerformed(ActionEvent inEvent) {
        if (controlID != null) {
            issueControl(createControl());
        }
    }

    /**
     * Override this to create something other than a simple no-parameter
     * Control.
     *
     * @return Control issued when button pressed: here a simple no-parameter
     *      Control
     */
    protected Control createControl() {
        if (controlID == null) {
            throw new RuntimeException("Can't create a Control because no ControlID set.");
        }

        return new Control(controlID);
    }

}
