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
 * String convertor for type <code>java.util.Date</code>.
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/25 13:53:06 $
 * @created 05 September 2002
 * @todo Support other possible values for true and false, in particular if
 *      localisation is needed - UIStrings will be useful here (ludovicc)
 */
public class BooleanStringConvertor extends NullStringConvertor {

    private static final String TRUE = Boolean.TRUE.toString();
    private static final String FALSE = Boolean.FALSE.toString();

    /**
     * Returns parsed boolean as object of type <code>Boolean</code>. <p>
     *
     * Note: the parsing supports only 'true' and 'false' as valid values. </p>
     *
     * @param inString The string to parse
     * @return An object of type <code>Boolean</code> initialised with the
     *      parsed string
     * @throws IllegalArgumentException can't convert from String.
     */
    public Object stringAsValue(String inString) throws IllegalArgumentException {
        if (isNull(inString)) {
            return null;
        }
        if (!(inString.equals(TRUE) || inString.equals(FALSE))) {
            throw new IllegalArgumentException("Illegal value '" + inString + "' not '"
                    + TRUE + "' or '" + FALSE + "'");
        }
        return Boolean.valueOf(inString);
    }

    /**
     * Formats object into <code>String</code>. It never return a null.
     *
     * @param inValue The object to convert
     * @return text representation of boolean object. For a null argument the
     *      result comes from method {@link #getNullAsString() getNullAsString}
     * @throws IllegalArgumentException when argument is not a subclass of
     *      java.lang.Boolean
     */
    public String valueAsString(Object inValue) throws IllegalArgumentException {
        if (inValue == null) {
            return getNullAsString();
        }
        if (!(inValue instanceof Boolean)) {
            throw new IllegalArgumentException("BooleanStringConvertor only operates on Boolean, not on: " + inValue);
        }
        return inValue.toString();
    }

}
