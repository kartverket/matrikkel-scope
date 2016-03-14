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
package org.scopemvc.util.convertor;


/**
 * String convertor for type {@link java.lang.String java.lang.String}. The main
 * sense of the class is to correctly handle <code>null</code> value. Useful
 * results can be obtained if <code>null</code> representation is set for
 * example to <code>"(null)"</code> or <code>"N/A"</code> - this strings are
 * then parsed into <code>null</code> value.
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/25 13:53:06 $
 * @created 05 September 2002
 */
public final class StringStringConvertor extends NullStringConvertor {

    /**
     * <p>
     *
     * Converts String to String. </p> <p>
     *
     * <i>Handling of <code>null</code> value:</i> it is converted into {@link
     * #getNullAsString() getNullAsString()} which is default empty String. </p>
     *
     * @param inValue The object to convert
     * @return The string representation of the value object
     * @throws IllegalArgumentException if passed value is not instance of
     *      String
     */
    public String valueAsString(Object inValue) throws IllegalArgumentException {
        if (inValue == null) {
            return getNullAsString();
        }
        if (!(inValue instanceof String)) {
            throw new IllegalArgumentException("StringStringConvertor only "
                    + "operates on String, not on: " + inValue.getClass());
        }
        return (String) inValue;
    }


    /**
     * <p>
     *
     * Parses String to String. If passed String is non-null and non-empty, the
     * same instance is returned. </p> <p>
     *
     * <i>Handling of <code>null</code> value</i> is based on the following
     * sequence of conditions:
     * <ol>
     *   <li> for empty input string also empty String is returned.</li>
     *   <li> for input equal to value of {@link #getNullAsString()
     *   getNullAsString()} <code>null</code> is returned.</li>
     * </ol>
     * </p>
     *
     * @param inString The string to parse
     * @return passed input string or <code>null</code>.
     * @throws IllegalArgumentException if passed value is not instance of
     *      String
     */
    public Object stringAsValue(String inString) throws IllegalArgumentException {
        if (isNull(inString)) {
            return null;
        }
        return inString;
    }
}
