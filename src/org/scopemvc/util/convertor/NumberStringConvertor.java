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
 * $Id: NumberStringConvertor.java,v 1.6 2002/09/25 13:53:06 ludovicc Exp $
 */
package org.scopemvc.util.convertor;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.ScopeConfig;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

/**
 * Abstract base class for numeric StringConvertors. <p>
 *
 * It uses default <code>java.text.Number</code> format. New format can be set.
 * </p>
 *
 * Changes:
 *  - Added parsing support for unicode variants for minus signs (default)
 *  - Added strict parsing of input string (default)
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/25 13:53:06 $
 * @created 05 September 2002
 */
public abstract class NumberStringConvertor extends NullStringConvertor {
    private static final Log LOG = LogFactory.getLog(NumberStringConvertor.class);

    private NumberFormat format;
    private boolean substituteMinusSign = (boolean)
            ScopeConfig.getObject("org.scopemvc.util.convertor.NumberStringConvertor.substituteMinusSign");
    private boolean strict = (boolean)
            ScopeConfig.getObject("org.scopemvc.util.convertor.NumberStringConvertor.strict");


    /**
     * Creates new NumberStringConvertor with default platform number format.
     *
     * @see java.text.NumberFormat#getInstance()
     */
    public NumberStringConvertor() {
        this(Locale.getDefault(Locale.Category.FORMAT));
    }

    public NumberStringConvertor(Locale locale) {
        this.format = NumberFormat.getInstance(locale);
        LOG.debug("Using " + getClass().getName());
    }

    /**
     * Returns the formatter for numbers.
     *
     * @return a instance of format used in this convertor. The value is never
     *      null.
     */
    public NumberFormat getNumberFormat() {
        return format;
    }

    /**
     * Sets the formatter for numbers.
     *
     * @param inFormat The new numberFormat value
     * @throws IllegalArgumentException if passed format is null.
     */
    public void setNumberFormat(NumberFormat inFormat)
             throws IllegalArgumentException {
        if (inFormat == null) {
            throw new IllegalArgumentException("Passed number format cannot "
                    + "be null");
        }
        format = inFormat;
    }


    /**
     * <p>
     *
     * Returns instance of some subclass of {@link java.lang.Number Number} as
     * returned by {@link java.text.NumberFormat NumberFormat}. If there is
     * required specific numeric class, corresponding <code>XXXStringConvertor</code>
     * should be used. Subclasses use this method and result converts to proper
     * type. </p> <p>
     *
     * Empty, <code>null</code> and {@link #getNullAsString() getNullAsString()}
     * strings are converted into <code>null</code>. </p>
     *
     * @param inString The string to parse
     * @return A number object of the supported type initialised with the parsed
     *      string
     * @see DoubleStringConvertor
     * @see FloatStringConvertor
     * @see IntegerStringConvertor
     * @see LongStringConvertor
     * @throws IllegalArgumentException can't convert from String using current
     *      NumberFormat.
     */
    public Number stringAsValue(String inString)
             throws IllegalArgumentException {
        if (isNull(inString)) {
            return null;
        }

        if (substituteMinusSign) {
            inString = substituteMinus(inString, ((DecimalFormat) format).getNegativePrefix());
            inString = substituteMinus(inString, ((DecimalFormat) format).getNegativeSuffix());
        }

        ParsePosition parsePosition = new ParsePosition(0);
        Number result = format.parse(inString, parsePosition);
        if ((strict && parsePosition.getIndex() != inString.length())
                || parsePosition.getIndex() == 0) {
            throw new IllegalArgumentException("Unparseable number: \"" + inString + '"');
        }
        return result;
    }

    protected static String substituteMinus(String inString, String token) {
        if (!token.isEmpty()) {
            inString = inString.replaceFirst("^\\s*\\u002D", token); //Hyphen-minus
            inString = inString.replaceFirst("^\\s*\\u2212", token); //Minus (mathematical)
            inString = inString.replaceFirst("^\\s*\\uFE63", token); //Small Hyphen-minus
            inString = inString.replaceFirst("^\\s*\\uFF0D", token); //Full-width Hyphen-minus
            inString = inString.replaceFirst("^\\s*\\u2010", token); //Hyphen
            inString = inString.replaceFirst("^\\s*\\u2012", token); //Hyphen
        }
        return inString;
    }

    /**
     * Formats object into <code>String</code>. It never return a null.
     *
     * @param inValue The object to convert
     * @return text representation of numeric object. For null argument is
     *      called method {@link #getNullAsString() getNullAsString()}
     * @throws IllegalArgumentException when argument is not subclass of
     *      java.lang.Number
     */
    public String valueAsString(Object inValue)
             throws IllegalArgumentException {
        if (inValue == null) {
            return getNullAsString();
        }
        if (!(inValue instanceof Number)) {
            throw new IllegalArgumentException("Passed object is not subclass "
                    + "of java.lang.Number. Its class is " + inValue.getClass());
        }
        return format.format(inValue);
    }
}
