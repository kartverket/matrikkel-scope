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
 * $Id: ModelChangeEventSupport.java,v 1.13 2002/11/12 00:41:12 ludovicc Exp $
 */
package org.scopemvc.model.basic;


import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.core.Selector;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * Delegate to help ModelChangeEvent listener registration and firing. Used by
 * {@link BasicModel}. Events are fired synchronously in this implementation.
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.13 $ $Date: 2002/11/12 00:41:12 $
 * @created 05 August 2002
 * @see BasicModel
 */
public class ModelChangeEventSupport {

    private static final Log LOG = LogFactory.getLog(ModelChangeEventSupport.class);

    private ModelChangeEventSource source;
    private ArrayList listeners;
    private int activationCount;


    /**
     * Constructor for the ModelChangeEventSupport object
     *
     * @param inSource The source of ModelChangeEvents
     */
    public ModelChangeEventSupport(ModelChangeEventSource inSource) {
        if (inSource == null) {
            throw new IllegalArgumentException("Make an MCESupport for a specified model, not null");
        }
        source = inSource;
    }


    /**
     * Returns true if the model is deactivated
     *
     * @return true if the model is deactivated
     */
    public boolean isModelDeactivated() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("isModelDeactivated: " + source);
        }

        return (activationCount > 0);
    }


    /**
     * Disable ModelChangeEvent broadcasting for a Model. This can 'stack'
     * multiple inactivation requests, allowing nested inactivations.
     *
     * @see BasicModel#makeActive
     */
    public final void activateModel() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("activateModel: " + source);
        }

        --activationCount;

        if (activationCount < 0) {
            LOG.warn("unmatched activation? activationCount < 0 for: " + source);
        }
    }


    /**
     * Enable ModelChangeEvent broadcasting for a Model that was disabled. This
     * can 'stack' multiple activation requests, allowing nested activations.
     *
     * @see BasicModel#makeActive
     */
    public final void deactivateModel() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("deactivateModel: " + source);
        }

        ++activationCount;
    }


    /**
     * Register a ModelChangeListener to receive ModelChangeEvent broadcasts
     * from the passed Model.
     *
     * @param inListener the ModelChangeEvent listener to register with the
     *      Model to receive its ModelChangeEvent broadcasts.
     * @see BasicModel#addModelChangeListener
     */
    public synchronized void addModelChangeListener(ModelChangeListener inListener) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("addModelChangeListener: " + source + " listener: " + inListener);
        }

        if (inListener == null) {
            throw new IllegalArgumentException("Can't add null listener to: " + source);
        }

        if (listeners == null) {
            listeners = new ArrayList();
        }

        listeners.add(inListener);
    }


    /**
     * Remove a ModelChangeListener from the set of registered listeners to the
     * passed Model. The passed listener must be registered as a listener to the
     * Model.
     *
     * @param inListener the ModelChangeListener to deregister from the passed
     *      Model.
     * @see BasicModel#removeModelChangeListener
     */
    public synchronized void removeModelChangeListener(ModelChangeListener inListener) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("removeModelChangeListener: " + source + " listener: " + inListener);
        }

        if (inListener == null) {
            throw new IllegalArgumentException("Can't remove null listener for: " + source);
        }

        if (listeners != null) {
            listeners.remove(inListener);
        }
    }


    // ------------ ModelChangeEvent firing --------------------

    /**
     * Fire a ModelChangeEvent to all listeners
     *
     * @param inSelector The Selector for the property of the model affected by
     *      the change
     * @param inType The type of the change, as defined in ModelChangeTypes
     * @see org.scopemvc.core.ModelChangeTypes
     */
    public synchronized void fireModelChange(int inType, Selector inSelector) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("fireModelChange: " + inType + ", " + inSelector + ", " + source);
        }

        if (isModelDeactivated()) {
            return;
        }

        if (listeners == null || listeners.size() < 1) {
            return;
        }

        ModelChangeEvent event = new BasicModelChangeEvent();
        event.setType(inType);
        event.setModel(source);
        event.setSelector(inSelector);

        // Safe copy of the listeners, can't be affected by add or remove while firing the event
        // (modelChanged can call user code that changes the model...)
        ArrayList toFire = new ArrayList(listeners);

        for (Iterator i = toFire.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (Debug.ON) {
                Debug.assertTrue(o instanceof ModelChangeListener);
            }
            ModelChangeListener listener = (ModelChangeListener) o;
            listener.modelChanged(event);
        }
    }
}
