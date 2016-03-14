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
package org.scopemvc.model.basic;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.core.ModelChangeTypes;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * A simple implementation of ModelChangeEventSource for use as a base class. An
 * alternative (JavaBeans style) is to use ModelChangeEventSupport as a delegate
 * in all models. </P> <P>
 *
 * To implement a BasicModel:
 * <OL>
 *   <LI> extend <CODE>BasicModel</CODE>, </LI>
 *   <LI> Notify interested listeners of changes in Model state at the end of
 *   <CODE>set</CODE> methods by: <CODE>fireModelChange(...)</CODE> </LI>
 *   <LI> Register as a ModelChangeListener with submodels in the appropriate
 *   set methods to ensure event propagation up the tree of models. For example:
 *   <PRE>
 *   public final static Selector NAME = Selector.fromString("name");
 *   public final static Selector ADDRESS = Selector.fromString("address");
 *
 *   public void setAddress(AddressModel inAddress) throws ModelException {
 *       if (inAddress == address) {
 *           return;
 *       }
 *     <FONT COLOR="red">unlistenOldSubmodel(ADDRESS);</FONT> address =
 *   inAddress; <FONT COLOR="red">listenNewSubmodel(ADDRESS);</FONT>
 *   fireModelChange(VALUE_CHANGE, ADDRESS); } </PRE> <br>
 *   This ensures that any change in the Address's state is received by this
 *   parent which will then fire its own ModelChangeEvent to its listeners. See
 *   {@link org.scopemvc.core.ModelChangeEventSource} for more discussion. Note
 *   the use of the Selector constants: this is for convenience (and
 *   performance). </LI>
 *   <LI> Use Scope's collection models instead of the regular Java collections:
 *   Scope collections propagate changes to submodels as above and are compliant
 *   ModelChangeEventSources. These collections must be listened to in the same
 *   way as other submodel properties. Note that Scope collections are just thin
 *   wrappers on native Java collections. </LI>
 * </OL>
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.13 $ $Date: 2002/11/20 01:36:58 $
 * @created 05 August 2002
 * @deprecated Use org.scopemvc.model.BasicModel and the methods defined there
 */
public abstract class BasicModel implements ModelChangeEventSource, ModelChangeTypes {

    private static final Log LOG = LogFactory.getLog(BasicModel.class);
    private ModelChangeEventSupport mceSupport = new ModelChangeEventSupport(this);
    private PropertyManager manager = PropertyManager.getInstance(this);


    /**
     * Adds a listener for ModelChangeEvent
     *
     * @param inListener The listener to be added
     */
    public void addModelChangeListener(ModelChangeListener inListener) {
        mceSupport.addModelChangeListener(inListener);
    }


    /**
     * Removes a listener for ModelChangeEvent
     *
     * @param inListener The listener to be removed
     */
    public void removeModelChangeListener(ModelChangeListener inListener) {
        mceSupport.removeModelChangeListener(inListener);
    }


    /**
     * Fire a ModelChangeEvent to all listeners
     *
     * @param inChangeType The type of the change. One of the {@link
     *      org.scopemvc.core.ModelChangeTypes} values
     * @param inSelector The Selector for the property of the model affected by
     *      the change
     * @deprecated Will be removed in Scope 2.0
     */
    public void fireModelChange(int inChangeType, Selector inSelector) {
        mceSupport.fireModelChange(inChangeType, inSelector);
    }


    /**
     * <P>
     *
     * Handle changes to children ModelChangeEventSources by firing a change
     * event from <CODE>this</CODE> (and propagating the original Selector). If
     * the event's source is no longer a child property of this parent then
     * there is no need to continue propagating the event. </P>
     *
     * @param inEvent the event received from a child ModelChangeEventSource.
     * @deprecated Will be removed in Scope 2.0
s     */
    public void modelChanged(ModelChangeEvent inEvent) {
        if (Debug.ON) {
            Debug.assertTrue(inEvent != null);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("modelChanged: " + inEvent);
        }

        // Construct a Selector that includes the 'next' selector we received.
        Selector selector = manager.getSelectorFor(this, inEvent.getModel());

        // Is the model unrecognised? If so then the model
        // has changed since this change so
        // don't need to propagate it any further.
        if (selector == null) {
            return;
        }

        if (inEvent.getSelector() != null) {
            selector.chain(inEvent.getSelector().deepClone());
        }

        fireModelChange(inEvent.getType(), selector);
    }


    // ------------------- Model activation and inactivation -------------------


   /**
    * Checks if this model is activated, that is broadcasts ModelChangeEvents.
    * 
    * @return <code>true</code> if this model broadcasts ModelChangeEvents.
    */
   public boolean isActive(){
      return !mceSupport.isModelDeactivated();
   }

    /**
     * <P>
     *
     * Control whether this BasicModel broadcasts ModelChangeEvents. Make sure
     * nested calls are properly matched to fully re-activate a BasicModel that
     * was deactivated. </P> <P>
     *
     * Subclasses may override this to propagate the activation state to child
     * BasicModel properties if necessary. </P>
     *
     * @param inActive true to activate this model, false to desactivate it
     * @deprecated Will be removed in Scope 2.0
     */
    public void makeActive(boolean inActive) {
        if (inActive) {
            mceSupport.activateModel();
        } else {
            mceSupport.deactivateModel();
        }
    }

    /**
     * Return the property manager used by this model
     *
     * @return the property manager
     */
    protected PropertyManager getPropertyManager() {
        return manager;
    }


    /**
     * Convenience for BasicModel implementors: call this at the start of
     * setters for submodel properties (ie properties of type
     * ModelChangeEventSource) to deregister this as a ModelChangeListener to
     * the current submodel (about to be set to another Model).
     *
     * @param inSelector The Selector for the submodel
     */
    protected void unlistenOldSubmodel(Selector inSelector) {
        try {
            Object o = manager.get(this, inSelector);
            if (o instanceof ModelChangeEventSource) {
                ((ModelChangeEventSource) o).removeModelChangeListener(this);
            }
        } catch (Exception e) {
            LOG.warn("unlistenOldSubmodel: selector " + inSelector, e);
        }
    }


    /**
     * Convenience for BasicModel implementors: call this at the end of setters
     * for submodel properties (ie properties of type ModelChangeEventSource) to
     * register this as a ModelChangeListener to the current submodel for event
     * propagation.
     *
     * @param inSelector The Selector for the submodel
     * @deprecated Will be removed in Scope 2.0
     */
    protected void listenNewSubmodel(Selector inSelector) {
        try {
            Object o = manager.get(this, inSelector);
            if (o instanceof ModelChangeEventSource) {
                ((ModelChangeEventSource) o).addModelChangeListener(this);
            }
        } catch (Exception e) {
            LOG.warn("listenNewSubmodel: selector " + inSelector, e);
        }
    }
}
