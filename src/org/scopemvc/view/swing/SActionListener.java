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
import java.awt.event.ActionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.ActionManager;
import org.scopemvc.core.ModelAction;

/**
 * Connects component action event to a method in a model. <p>
 *
 * Let's have button <i>Save</i> and method <code>void saveData()</code> in
 * model object, which should be called, when button is pressed. The connection
 * can be done in following way: <br>
 * <pre>
 *     JButton button = new JButton("Save");
 *     SActionListener action = new SActionListener();
 *     action.setModelActionString("saveData");
 *     button.addActionListener(action);
 *     ...
 *     action.setBoundModel(myModel);
 * </pre> </p>
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/19 18:08:02 $
 * @created 05 September 2002
 */
public class SActionListener implements ActionListener {
    private static final Log LOG = LogFactory.getLog(SActionListener.class);

    private Object model;
    private ModelAction modelAction;

    /**
     * Creates new SActionListener
     */
    public SActionListener() { }

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return model;
    }

    /**
     * Sets name of method to call on model. The name is case sensitive.
     *
     * @param inAction The new modelActionString value
     */
    public final void setModelActionString(String inAction) {
        setModelAction(new ModelAction(inAction));
    }

    /**
     * Sets model object on which the method will be called.
     *
     * @param inModel The new boundModel value
     */
    public final void setBoundModel(Object inModel) {
        model = inModel;
    }

    /**
     * Called by component to notify, that user performed action on the
     * component. Specified method is called on the model.
     *
     * @param inEvent The action event
     * @throws IllegalStateException if model action (method) is not set
     * @see #setModelActionString(java.lang.String)
     */
    public void actionPerformed(ActionEvent inEvent) throws IllegalStateException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("actionPerformed " + inEvent);
        }
        if (model == null) {
            LOG.warn("actionPerformed, but model is null. Action ignored");
            return;
        }
        if (modelAction == null) {
            throw new IllegalStateException("actionPerformed, but modelAction is not set.");
        }
        ActionManager actionManager = ActionManager.getInstance(model);
        try {
            actionManager.doAction(model, modelAction, null);
        } catch (Exception ex) {
            LOG.error("Cannot perform action on model " + model, ex);
        }
    }

    private void setModelAction(ModelAction inAction) {
        modelAction = inAction;
    }
}
