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

import java.text.DateFormat;

/**
 * <p>
 *
 * String convertor for type {@link org.scopemvc.util.DateTime} or <code>java.util.Date.</code>
 * <br>
 * Both date and time parts of <code>Date</code> instances are used. </p> <p>
 *
 * It uses one <code>java.text.DateFormat DateFormat</code> for converting into
 * <code>String</code> and an array of <code>DateFormat</code>s for parsing.
 * Parsing formats are successively used to try to parse until one is
 * successful. </p> <p>
 *
 * Formats are picked up from config (see {@link
 * org.scopemvc.util.DefaultScopeConfig} for details) or if none in config, the
 * default formatter is: <pre>
 *   DateFormat.getDateTimeInstance()
 * </pre> <br>
 * and default parsers are: <br>
 * <pre>
 *   DateFormat.getDateTimeInstance((FULL|LONG|MEDIUM|SHORT),
 *                                  (FULL|LONG|MEDIUM|SHORT));
 * </pre><br>
 * <i>Note:</i> the default parsing set is initialized during class loading and
 * based on default platform locale. If application uses other locales, there
 * will be need to set parsing objects explicitly. </p>
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/25 13:53:06 $
 * @created 05 September 2002
 * @see DateStringConvertor
 * @see TimeStringConvertor
 */
public class DateTimeStringConvertor extends DateStringConvertor {

    // ------------- Defaults if no config specified ----------------
    private static final DateFormat DEFAULT_PARSERS[] = new DateFormat[16];
    static {
        DEFAULT_PARSERS[15] = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.SHORT);
        DEFAULT_PARSERS[14] = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.MEDIUM);
        DEFAULT_PARSERS[13] = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.LONG);
        DEFAULT_PARSERS[12] = DateFormat.getDateTimeInstance(
                DateFormat.SHORT, DateFormat.FULL);

        DEFAULT_PARSERS[11] = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.SHORT);
        DEFAULT_PARSERS[10] = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.MEDIUM);
        DEFAULT_PARSERS[9] = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.LONG);
        DEFAULT_PARSERS[8] = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.FULL);

        DEFAULT_PARSERS[7] = DateFormat.getDateTimeInstance(
                DateFormat.LONG, DateFormat.SHORT);
        DEFAULT_PARSERS[6] = DateFormat.getDateTimeInstance(
                DateFormat.LONG, DateFormat.MEDIUM);
        DEFAULT_PARSERS[5] = DateFormat.getDateTimeInstance(
                DateFormat.LONG, DateFormat.LONG);
        DEFAULT_PARSERS[4] = DateFormat.getDateTimeInstance(
                DateFormat.LONG, DateFormat.FULL);

        DEFAULT_PARSERS[3] = DateFormat.getDateTimeInstance(
                DateFormat.FULL, DateFormat.SHORT);
        DEFAULT_PARSERS[2] = DateFormat.getDateTimeInstance(
                DateFormat.FULL, DateFormat.MEDIUM);
        DEFAULT_PARSERS[1] = DateFormat.getDateTimeInstance(
                DateFormat.FULL, DateFormat.LONG);
        DEFAULT_PARSERS[0] = DateFormat.getDateTimeInstance(
                DateFormat.FULL, DateFormat.FULL);
    }

    // ------------------------------------------------------------------

    /**
     * Creates new TimeStringConvertor. If formats and parsers are specified in
     * config then use those else use current locale default format and platform
     * locale default parsers.
     */
    public DateTimeStringConvertor() {
        super();
    }


    /**
     * Creates new DateTimeStringConvertor with specified formatter and parsers.
     *
     * @param inFormatter The formatter used for converting a DateTime to a
     *      string
     * @param inParsers The parsers for all possible date formats that can be
     *      input by the user
     */
    public DateTimeStringConvertor(DateFormat inFormatter, DateFormat inParsers[]) {
        super(inFormatter, inParsers);
    }


    /**
     * Gets the default parsers
     *
     * @return The defaultParsers value
     */
    protected DateFormat[] getDefaultParsers() {
        return DateTimeStringConvertor.DEFAULT_PARSERS;
    }
}

