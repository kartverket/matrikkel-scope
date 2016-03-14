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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.ScopeConfig;

/**
 * <p>
 *
 * String convertor for type <code>java.util.Date</code>. The Time part of
 * <code>Date</code> instances are ignored. See {@link DateTimeStringConvertor
 * DateTimeStringConvertor} and {@link TimeStringConvertor TimeStringConvertor}.
 * </p><p>
 *
 * It uses one java.text.DateFormat for converting into <code>String</code> and
 * set of <code>DateFormat</code>s for parsing. Parsing formats are successively
 * used to try to parse until one is successful. </p> <p>
 *
 * Formats are picked up from config (see {@link
 * org.scopemvc.util.DefaultScopeConfig} for details) or if none in config, the
 * default formatter is: <br>
 * <pre>
 *   DateFormat.getDateInstance(DateFormat.MEDIUM)
 * </pre> <br>
 * and default parsers are: <br>
 * <pre>
 *   DateFormat.getDateInstance(DateFormat.FULL);
 *   DateFormat.getDateInstance(DateFormat.LONG);
 *   DateFormat.getDateInstance(DateFormat.MEDIUM);
 *   DateFormat.getDateInstance(DateFormat.SHORT);
 * </pre> <br>
 * <i>Note:</i> the default parsing set is initialized during class loading and
 * based on default platform locale. If application uses other locales, there
 * will be need to set parsing objects explicitly. </p>
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.11 $ $Date: 2002/10/31 12:15:53 $
 * @created 05 September 2002
 * @see DateTimeStringConvertor
 * @see TimeStringConvertor
 */
public class DateStringConvertor extends NullStringConvertor {

    private static final Log LOG = LogFactory.getLog(DateStringConvertor.class);

    // ------------- Defaults if no config specified ----------------
    private static final DateFormat DEFAULT_PARSERS[] = new DateFormat[4];

    private DateFormat formatter;
    private DateFormat parsers[];
    static {
        DEFAULT_PARSERS[3] = DateFormat.getDateInstance(DateFormat.SHORT);
        DEFAULT_PARSERS[2] = DateFormat.getDateInstance(DateFormat.MEDIUM);
        DEFAULT_PARSERS[1] = DateFormat.getDateInstance(DateFormat.LONG);
        DEFAULT_PARSERS[0] = DateFormat.getDateInstance(DateFormat.FULL);
    }

    // ------------------------------------------------------------------

    /**
     * Creates new DateStringConvertor. If formats and parsers are specified in
     * config then use those else use current locale default format and platform
     * locale default parsers.
     */
    public DateStringConvertor() {
        initDefaults();
    }


    /**
     * Creates new DateStringConvertor with specified formatter and parsers.
     *
     * @param inFormatter The formatter used for converting a Date to a string
     * @param inParsers The parsers for all possible date formats that can be
     *      input by the user
     */
    public DateStringConvertor(DateFormat inFormatter, DateFormat inParsers[]) {
        setFormatter(inFormatter);
        setParsers(inParsers);
    }


    /**
     * Sets format used by this convertor for converting <code>Date</code>s into
     * <code>String</code>s.
     *
     * @param inFormat The new formatter value
     * @throws IllegalArgumentException if passed format is null or not a
     *      DateFormat or not a String.
     */
    public final void setFormatter(Object inFormat) throws IllegalArgumentException {
        if (inFormat instanceof DateFormat) {
            formatter = (DateFormat) inFormat;
        } else if (inFormat instanceof String) {
            formatter = new SimpleDateFormat((String) inFormat);
        } else {
            throw new IllegalArgumentException("Can't set formatter to: " + inFormat);
        }
    }


    /**
     * Set parser array for this converter.
     *
     * @param inParsers The new parsers value
     * @throws IllegalArgumentException if the array is <code>null</code> or
     *      contains a non-String or non-DateFormat object.
     */
    public final void setParsers(Object[] inParsers) throws IllegalArgumentException {
        if (inParsers == null) {
            throw new IllegalArgumentException("Passed array of formats cannot be null");
        }

        parsers = new DateFormat[inParsers.length];
        for (int i = 0; i < inParsers.length; i++) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("setParsers: " + inParsers[i]);
            }
            if (inParsers[i] instanceof String) {
                SimpleDateFormat f = new SimpleDateFormat((String) inParsers[i]);
                parsers[i] = f;
            } else if (inParsers[i] instanceof DateFormat) {
                parsers[i] = (DateFormat) inParsers[i];
            } else {
                throw new IllegalArgumentException("Cannot set a parser to be: " + inParsers[i]);
            }
            parsers[i].setLenient(false);
        }
    }


    /**
     * Returns instance of <code>java.util.Date</code>.Parsing formats are
     * successively used to try to parse until one is successful or exception is
     * thrown. <p>
     *
     * Empty, <code>null</code> and {@link #getNullAsString() getNullAsString()}
     * strings are converted into <code>null</code>. </p>
     *
     * @param inString The string to parse
     * @return An object of the supported type initialised with the parsed
     *      string
     * @throws IllegalArgumentException if can't convert from String using
     *      current {@link java.text.DateFormat DateFormat}
     */
    public Object stringAsValue(String inString) throws IllegalArgumentException {

        if (isNull(inString)) {
            return null;
        }

        for (int i = parsers.length - 1; i >= 0; i--) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Parser: " + inString + ", " + parsers[i].format(new Date(0)));
            }
            try {
                Date result = parsers[i].parse(inString);
                if (result != null) {
                    return result;
                }
            } catch (ParseException ex) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("stringAsValue", ex);
                }
                // ignore and try again
            }
        }
        throw new IllegalArgumentException("Cannot convert to Date/Time: " + inString);
    }


    /**
     * Formats object into <code>String</code>.
     *
     * @param inValue The Date object to convert
     * @return text representation of Date object. For null argument is called
     *      method {@link #getNullAsString() getNullAsString}
     * @throws IllegalArgumentException it argument is not instance of {@link
     *      java.util.Date java.util.Date}
     */
    public String valueAsString(Object inValue) throws IllegalArgumentException {
        if (inValue == null) {
            return getNullAsString();
        }
        if (!(inValue instanceof Date)) {
            throw new IllegalArgumentException("Passed object is not subclass "
                    + "of java.util.Date. Its class is " + inValue.getClass());
        }
        return formatter.format(inValue);
    }


    /**
     * Gets the default parsers
     *
     * @return The defaultParsers value
     */
    protected DateFormat[] getDefaultParsers() {
        return DateStringConvertor.DEFAULT_PARSERS;
    }


    /**
     * Initialise the default formatter and parsers from ScopeConfig
     */
    protected void initDefaults() {
        Object configFormatter = ScopeConfig.getObject(getClass().getName() + ".formatter");
        setFormatter(configFormatter);

        List parserList = new ArrayList();
        for (Iterator i = ScopeConfig.getKeysMatching(getClass().getName() + ".parser"); i.hasNext(); ) {

            parserList.add(ScopeConfig.getObject((String) i.next()));

        }
        if (parserList.size() < 1) {
            setParsers(getDefaultParsers());
        } else {
            setParsers(parserList.toArray());
        }
    }
}
