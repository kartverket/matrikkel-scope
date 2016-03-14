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


/**
 * <P>
 *
 * Interface for a component parent of a SListSelectionModel. </P> <P>
 *
 * STable and SList implement this interface to allow their SListSelectionModel
 * to refresh parent. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/10/23 12:38:46 $
 * @created 05 September 2002
 * @see SListSelectionModel
 */
public interface ListSelectionParent {

    /**
     * Validation failed while getting a value from View into the bound model
     * object. <BR>
     * Use this to indicate to the user that the value being edited is invalid.
     *
     * @param inException The exception causing the validation failure
     */
    void validationFailed(Exception inException);


    /**
     * Clear previous validation failure.
     *
     * @see #validationFailed
     */
    void validationSuccess();


    /**
     * Sets the read-only state of the component
     *
     * @param inReadOnly The new read-only value
     */
    void setReadOnly(boolean inReadOnly);


    /**
     * Issue a control when the selection changes.
     */
    void issueChangeSelectionControl();


    /**
     * Find the index for the elememt in the bound list
     *
     * @param inElement The elememt to find the index for
     * @return The index for the elememt in the bound list, or -1 if not found
     */
    int findIndexFor(Object inElement);


    /**
     * Find the element in the bound list at the given index
     *
     * @param inIndex The index of the elemement in the bound list
     * @return The element, or null if not found
     */
    Object findElementAt(int inIndex);

}
