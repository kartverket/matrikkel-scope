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
 * $Id: BigDecimalStringConvertor.java,v 1.6 2002/10/31 12:15:53 ludovicc Exp $
 */
package org.scopemvc.util.convertor;


import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * String convertor for type <code>BigDecimal</code>.
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.6 $ $Date: 2002/10/31 12:15:53 $
 * @created 05 September 2002
 */
public class BigDecimalStringConvertor extends NullStringConvertor {

    private final DecimalFormatSymbols symbols;
    private final boolean convertDot;


    /**
     * Constructor for the BigDecimalStringConvertor object
     */
    public BigDecimalStringConvertor() {
        this(Locale.getDefault(Locale.Category.FORMAT));
    }

    public BigDecimalStringConvertor(Locale locale) {
        symbols = new DecimalFormatSymbols(locale);
        convertDot = (symbols.getDecimalSeparator() == '.');
    }

    /**
     * Returns parsed number as object of type <code>BigDecimal</code>. <p>
     *
     * Note: for parsing is not used NumberFormat, but BigDecimal constructor.
     * Passed String argument is changed from format with locale decimal
     * separator into format recognizable by the constructor. </p>
     *
     * @param inString The string to parse
     * @return An object of the supported type initialised with the parsed
     *      string
     * @throws IllegalArgumentException can't convert from String.
     */
    public Object stringAsValue(String inString) throws IllegalArgumentException {
        // Note: default BigDecimal(String ) constructor is not correct because
        // it does not deal with locales. For example in Czech locale
        // decimal numbers are written with coma, not with dot (i.e. "3,1415"),
        // which leads BigDecimal's constructor to throw exceptions.
        // That is why we perform some correction first.

        if (isNull(inString)) {
            return null;
        }
        String newString = convertDot ? inString.replace(
                symbols.getDecimalSeparator(), '.')
                : inString;
        try {
            Object result = new BigDecimal(newString);
            return result;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Illegal value: "
                    + ex.getMessage());
        }
    }


    /**
     * Formats object into <code>String</code>. It never return a null.
     *
     * @param inValue The object to convert
     * @return text representation of numeric object. For a null argument the
     *      result comes from method {@link #getNullAsString() getNullAsString}
     * @throws IllegalArgumentException when argument is not subclass of
     *      java.lang.Number
     */
    public String valueAsString(Object inValue) throws IllegalArgumentException {
        if (inValue == null) {
            return getNullAsString();
        }
        if (!(inValue instanceof BigDecimal)) {
            throw new IllegalArgumentException("Passed object is not BigDecimal");
        }
        BigDecimal d = (BigDecimal) inValue;
        return convertDot
                ? d.toString().replace('.', symbols.getDecimalSeparator())
                : d.toString();
    }
}
