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
 * $Id: NullStringConvertor.java,v 1.5 2002/10/16 18:10:03 ludovicc Exp $
 */
package org.scopemvc.util.convertor;


/**
 * <P>
 *
 * Abstract base class for StringConvertors that recognise a String value to
 * represent <CODE>null</CODE>. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.5 $ $Date: 2002/10/16 18:10:03 $
 * @created 05 September 2002
 */
public abstract class NullStringConvertor implements StringConvertor {

    private String nullString = "";


    /**
     * Returns the string representing <code>null</code>
     *
     * @return a String representation used when parameter in {@link
     *      #valueAsString(Object) valueAsString} is <code>null</code>. Default
     *      value is the empty string.
     */
    public String getNullAsString() {
        return nullString;
    }


    /**
     * Sets String representation used when parameter in {@link
     * #valueAsString(Object) valueAsString} is <code>null</code>. Method {@link
     * #stringAsValue(String) stringAsValue} also uses this value: if parameter
     * is equal to this representation, return value should be null. Default
     * value is empty string.
     *
     * @param inNullString The new nullAsString value
     * @throws IllegalArgumentException if passed value is null.
     */
    public void setNullAsString(String inNullString) throws IllegalArgumentException {
        if (inNullString == null) {
            throw new IllegalArgumentException("String representation of <null> cannot be null");
        }
        nullString = inNullString;
    }

    /**
     * Returns true if the convertor supports the stringAsValue() operation.
     * <br>
     * This method is useful when you want to display a string for complex
     * object but you don't want to convert a string to a value object in any
     * case. Editors can use this method to become automatically read-only when
     * the returned value is false.
     *
     * @return true in most cases, false if the StringConvertor cannot parse
     *      strings
     */
    public boolean supportsStringAsValue() {
        // the most common case, valid for all convertors defined in this package
        return true;
    }

    /**
     * Does the passed String represent <CODE>null</CODE>.
     *
     * @param inString The String to test
     * @return True if the String represents <CODE>null</CODE> or is null
     */
    protected boolean isNull(String inString) {
        return ((inString == null)
                || (inString.length() == 0)
                || (inString.equals(getNullAsString())));
    }
}
