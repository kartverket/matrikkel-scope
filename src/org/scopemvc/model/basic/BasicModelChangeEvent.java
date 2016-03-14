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


import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.Selector;

/**
 * <P>
 *
 * Concrete implementation of {@link org.scopemvc.core.ModelChangeEvent
 * ModelChangeEvent}. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:46 $
 * @created 05 August 2002
 */
public final class BasicModelChangeEvent implements ModelChangeEvent {

    /**
     * The type of change. Initialised to VALUE_CHANGED.
     *
     * @see org.scopemvc.core.ModelChangeTypes
     */
    private int type = VALUE_CHANGED;

    /**
     * Source Model of this event.
     */
    private ModelChangeEventSource model;

    /**
     * Property of the source Model that caused this event.
     */
    private Selector propertySelector;


    /**
     * Constructor for the BasicModelChangeEvent object
     */
    public BasicModelChangeEvent() { }


    /**
     * Gets the type
     *
     * @return The type value
     */
    public int getType() {
        return type;
    }


    /**
     * Gets the model
     *
     * @return The model value
     */
    public ModelChangeEventSource getModel() {
        return model;
    }


    /**
     * Gets the selector
     *
     * @return The selector value
     */
    public Selector getSelector() {
        return propertySelector;
    }


    /**
     * Sets the type
     *
     * @param inType The new type value
     */
    public void setType(int inType) {
        if (!(type == VALUE_CHANGED
                || type == VALUE_ADDED
                || type == VALUE_REMOVED
                || type == ACCESS_CHANGED)) {
            throw new IllegalArgumentException("Illegal ModelChangeEvent type: " + inType);
        }

        type = inType;
    }


    /**
     * Sets the model
     *
     * @param inModel The new model value
     */
    public void setModel(ModelChangeEventSource inModel) {
        if (inModel == null) {
            throw new IllegalArgumentException("Can't set a null model source for a BasicModelChangeEvent.");
        }

        model = inModel;
    }


    /**
     * Sets the selector
     *
     * @param inSelector The new selector value
     */
    public void setSelector(Selector inSelector) {
        propertySelector = inSelector;
    }


    /**
     * Returns a string representation of this object
     *
     * @return a string representation of this object
     */
    public String toString() {
        StringBuffer result = new StringBuffer("BasicModelChangeEvent(type:");
        if (type == VALUE_CHANGED) {
            result.append("VALUE_CHANGED");
        } else if (type == VALUE_ADDED) {
            result.append("VALUE_ADDED");
        } else if (type == VALUE_REMOVED) {
            result.append("VALUE_REMOVED");
        } else if (type == ACCESS_CHANGED) {
            result.append("ACCESS_CHANGED");
        }
        result.append(")(Model:" + model + ")(" + propertySelector + ")");
        return result.toString();
    }
}

