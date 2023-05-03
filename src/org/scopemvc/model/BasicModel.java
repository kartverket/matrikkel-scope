/*
 * ScopeMVC: a generic MVC framework for rich gui applications.
 * Copyright (c) 2000-2003, The Scope team
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
 * Neither the name "Scope", "ScopeMVC" nor the names of its contributors
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
 * $Id: BasicModel.java,v 1.10 2003/08/02 14:34:06 ludovicc Exp $
 */

package org.scopemvc.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.Pointer;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.Selector;
import org.scopemvc.impl.model.ModelChangeSource;


/**
 * A simple implementation of ModelChangeSource to use as a base class or as a delegate. <P>
 *
 * To implement a BasicModel:
 * <OL>
 *   <LI> extend <CODE>BasicModel</CODE>, </LI>
 *   <LI> Notify interested listeners of changes in Model state at the end of
 *   <CODE>set</CODE> methods by: <CODE>modelChanged()</CODE> when the whole model
 *   is affected by the change or <CODE>valueChanged(Pointer)</CODE> when a property 
 *   designed by the pointer changes</LI>
 *   <LI> Register the submodels in the appropriate
 *   set methods to ensure propagation of change notifications up the tree of models. 
 *   For example:
 *   <PRE>
 *   public final static Pointer NAME = PointerFactory.getPointer("name");
 *   public final static Pointer ADDRESS = PointerFactory.getPointer("address");
 *
 *   public void setName(String newName) {
 *     name = newName;
 *     valueChanged(NAME);
 *   }
 * 
 *   public void setAddress(AddressModel newAddress) {
 *       if (inAddress == address) {
 *           return;
 *       }
 *     <FONT COLOR="red">unregisterSubModel(address);</FONT>
 *     address = newAddress;
 *     <FONT COLOR="red">registerSubModel(address);</FONT>
 *     valueChanged(ADDRESS);
 *   }
 * 
 *   public void reset() {
 *     name = null;
 *     address = null;
 *     modelChanged();
 *   } 
 * 
 *   </PRE> <br>
 *   This ensures that any change in the Address's state will be propagated by this model 
 *   to the controller and the view using it. Note the use of the Pointer constants: 
 *   this is for convenience and performance. </LI>
 *   <LI> Use Scope's collection models instead of the regular Java collections:
 *   Scope collections propagate changes to submodels as above and are compliant
 *   ModelChangeEventSources. These collections must be listened to in the same
 *   way as other submodel properties. Scope collections are just thin
 *   wrappers on native Java collections. </LI>
 * </OL>
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.10 $ $Date: 2003/08/02 14:34:06 $
 * @created 05 August 2002
 */
public class BasicModel extends org.scopemvc.model.basic.BasicModel implements ModelChangeSource{

    private static final Log LOG = LogFactory.getLog(BasicModel.class);


    /**
     * Activate or deactivate the valueChanged notifications to the model wrapper. 
     *
     * @param active true to activate this model, false to desactivate it
     */
    public void setActive(boolean active) {
        makeActive(active);
    }

    /**
     * Notifies that this model has changed
     */
    protected void modelChanged() {
        fireModelChange(VALUE_CHANGED, Selector.fromString(""));
    }

    /**
     * Notifies that a property stored in this model has changed
     *
     * @param relativePointer The pointer to the property from this model
     */
    protected void valueChanged(Pointer relativePointer) {
        valueChanged(relativePointer, VALUE_CHANGED);
    }

    /**
     * Notifies that a value has changed.
     *
     * @param relativePointer The pointer to the property from this model
     * @param changeType The type of the change, one of the {@link
     *      org.scopemvc.impl.model.ModelChangeSource ModelChangeTypes} values
     */
    protected void valueChanged(Pointer relativePointer, int changeType) {
        fireModelChange(changeType, relativePointer.getSelector());
    }

    /**
     * Registers a sub model
     *
     * @param relativePointer The pointer to the submodel from this model
     * @param subModel The sub model
     */
    protected void registerSubModel(Pointer relativePointer, ModelChangeSource subModel) {
       if (subModel instanceof ModelChangeEventSource) {
           ((ModelChangeEventSource) subModel).addModelChangeListener(this);
       }
    }
}
