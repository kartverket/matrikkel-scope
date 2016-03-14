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
package org.scopemvc.view.servlet.xml;


import org.scopemvc.core.Selector;

/**
 * <P>
 *
 * Generates IDs to identify properties during the traversal of the model
 * containment hierarchy. Used by ModelToXML. The ids in the xml could be used
 * as form parameters to identify properties to be populated with user input.
 * </P> <P>
 *
 * This is used by the ID generators defined as inner classes in {@link
 * XSLPage}. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/05 15:41:45 $
 * @created 05 September 2002
 */
public abstract class PropertyIDGenerator {

    /**
     * TODO: describe of the Field
     */
    protected Selector currentPropertySelector;


    /**
     * Gets the property ID
     *
     * @return The propertyID value
     */
    public abstract String getPropertyID();


    /**
     * Call this to recurse down a property.
     *
     * @param inSelector TODO: Describe the Parameter
     */
    public void startProperty(Selector inSelector) {
        if (currentPropertySelector == null) {
            currentPropertySelector = inSelector.deepClone();
        } else {
            currentPropertySelector.chain(inSelector.deepClone());
        }
    }


    /**
     * Call this to pop up a property.
     */
    public void endProperty() {
        if (currentPropertySelector.getNext() == null) {
            currentPropertySelector = null;
        } else {
            currentPropertySelector.removeLast();
        }
    }
}
