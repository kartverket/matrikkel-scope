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
 * $Id: BigIntegerStringConvertor.java,v 1.4 2002/09/25 13:53:06 ludovicc Exp $
 */
package org.scopemvc.util.convertor;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * String convertor for type <code>BigInteger</code>.
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/25 13:53:06 $
 * @created 05 September 2002
 */
public class BigIntegerStringConvertor extends NumberStringConvertor {

    public BigIntegerStringConvertor() {
        this(Locale.getDefault(Locale.Category.FORMAT));
    }

    public BigIntegerStringConvertor(Locale locale) {
        super(locale);
        ((DecimalFormat) getNumberFormat()).setParseBigDecimal(true);
    }

    /**
     * Returns parsed number as object of type <code>BigInteger</code>. <p>
     *
     * Note: for parsing is not used NumberFormat, but BigInteger constructor.
     * </p>
     *
     * @param inString The string to parse
     * @return An object of the supported type initialised with the parsed
     *      string
     * @throws IllegalArgumentException can't convert from String.
     */
    public BigInteger stringAsValue(String inString) throws IllegalArgumentException {
        if (isNull(inString)) {
            return null;
        }
        final BigDecimal bigDecimal = (BigDecimal) super.stringAsValue(inString);
        return bigDecimal.toBigInteger();
    }
}
