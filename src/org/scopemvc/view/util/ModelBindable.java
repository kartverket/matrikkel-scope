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
 * $Id: ModelBindable.java,v 1.6 2002/09/25 13:53:10 ludovicc Exp $
 */
package org.scopemvc.view.util;


/**
 * <P>
 *
 * Views that use an {@link ActiveBoundModel} delegate must implement this
 * interface so that ActiveBoundModel can change the state of the View through
 * this generic interface. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/25 13:53:10 $
 * @created 05 September 2002
 * @see ActiveBoundModel
 */
public interface ModelBindable {

    /**
     * Get the current value (what would be set as a property of the bound model
     * object) being presented on the View.
     *
     * @return property's value from the UI.
     * @exception IllegalArgumentException if conversion from the UI
     *      representation of the property to the typed value fails.
     */
    Object getViewValue() throws IllegalArgumentException;


    /**
     * Use the passed property value and read-only state to update the View.
     *
     * @param inValue The new value of the property in the bound model
     * @param inReadOnly The new read-only state of the property
     */
    void updateFromProperty(Object inValue, boolean inReadOnly);


    /**
     * Validation failed while getting a value from View into the bound model
     * object. <BR>
     * Use this to indicate to the user that the value being edited is invalid.
     *
     * @param inException The exception causing the validation failure
     */
    void validationFailed(Exception inException);


    /**
     * Clears previous validation failure.
     *
     * @see #validationFailed
     */
    void validationSuccess();
}

