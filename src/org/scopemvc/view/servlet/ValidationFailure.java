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
 * $Id: ValidationFailure.java,v 1.4 2002/09/05 15:41:50 ludovicc Exp $
 */
package org.scopemvc.view.servlet;


/**
 * <P>
 *
 * Object to describe a failure to populate a model property with a certain
 * value. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:50 $
 * @see org.scopemvc.controller.servlet.ScopeServlet
 */
public class ValidationFailure {

    private String property;
    private String value;
    private Exception exception;


    /**
     * Constructor for the ValidationFailure object
     *
     * @param inProperty TODO: Describe the Parameter
     * @param inValue TODO: Describe the Parameter
     * @param inException TODO: Describe the Parameter
     */
    public ValidationFailure(String inProperty, String inValue, Exception inException) {
        property = inProperty;
        value = inValue;
        exception = inException;
    }


    /**
     * Gets the property
     *
     * @return The property value
     */
    public String getProperty() {
        return property;
    }


    /**
     * Gets the value
     *
     * @return The value value
     */
    public String getValue() {
        return value;
    }


    /**
     * Gets the exception
     *
     * @return The exception value
     */
    public Exception getException() {
        return exception;
    }


    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public String toString() {
        return "(ValidationFailure property: " + property + ", value: " + value + ", exception: " + exception + ")";
    }
}
