/*
 * Scope: a generic MVC framework.
 * Copyright (c) 2000-2002, Steve Meyfroidt
 * All rights reserved.
 * Email: smeyfroi@users.sourceforge.net
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
 * $Id: TestDateStringConvertors.java,v 1.7 2002/08/05 13:16:45 ludovicc Exp $
 */


package org.scopemvc.util.convertor;


import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import junit.framework.TestCase;
import org.apache.commons.logging.LogFactory;import org.apache.commons.logging.Log;
import org.scopemvc.util.ScopeConfig;

import static java.text.DateFormat.FULL;
import static java.text.DateFormat.LONG;
import static java.text.DateFormat.MEDIUM;
import static java.text.DateFormat.SHORT;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * <P>
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/08/05 13:16:45 $
 */
public final class TestDateStringConvertors extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestDateStringConvertors.class);

    static final DateFormat[] DEFAULT_PARSERS = new DateFormat[4];
    static {
        DEFAULT_PARSERS[3] = DateFormat.getDateInstance(SHORT);
        DEFAULT_PARSERS[2] = DateFormat.getDateInstance(MEDIUM);
        DEFAULT_PARSERS[1] = DateFormat.getDateInstance(LONG);
        DEFAULT_PARSERS[0] = DateFormat.getDateInstance(FULL);
    }


    /**
     * Must set a known default locale for the tests.
     */
    private static final Date jan1_1970;
    static {
        GregorianCalendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(1970, 0, 1, 0, 0, 0);
        jan1_1970 = cal.getTime();

        System.out.println("Default medium format " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(jan1_1970));
        System.out.println("Default locale " + Locale.getDefault());
    }

    public TestDateStringConvertors(String inName) {
        super(inName);
    }


    /**
     * Default parsers are constructed at class loading of ScopeProperties.
     * This test expects same default Locale as when ScopeProperties is initialized.
     * @see ScopeConfig#DEFAULT_CONFIG_NAME
     */
    public void testDefaultParsers() throws Exception {
        DateStringConvertor c = new DateStringConvertor();
        for (int i = 0; i < c.parsers.length; i++) {
            DateFormat defaultParser = DEFAULT_PARSERS[i];
            DateFormat parser = c.parsers[i];
            assertThat(parser.format(jan1_1970))
                    .isEqualTo(defaultParser.format(jan1_1970));
        }
        assertThat(c.parsers).hasSameSizeAs(DEFAULT_PARSERS);
    }

    public void testDefaultDateStringConvertor() throws Exception {

        DateStringConvertor c = new DateStringConvertor();
        String shortFormat = c.parsers[SHORT].format(jan1_1970);
        String mediumFormat = c.parsers[MEDIUM].format(jan1_1970);
        String longFormat = c.parsers[LONG].format(jan1_1970);
        String fullFormat = c.parsers[FULL].format(jan1_1970);

        System.out.println("shortFormat " + shortFormat);
        System.out.println("mediumFormat " + mediumFormat);
        System.out.println("longFormat " + longFormat);
        System.out.println("fullFormat " + fullFormat);

        // don't use string constants as date formats depend on the JVM version.
        assertEquals(mediumFormat, c.valueAsString(jan1_1970));

        if (LOG.isDebugEnabled()) LOG.debug("Try " + shortFormat);
        assertEquals(jan1_1970, c.stringAsValue(shortFormat));
        if (LOG.isDebugEnabled()) LOG.debug("Try " + mediumFormat);
        assertEquals(jan1_1970, c.stringAsValue(mediumFormat));
        if (LOG.isDebugEnabled()) LOG.debug("Try " + longFormat);
        assertEquals(jan1_1970, c.stringAsValue(longFormat));
        if (LOG.isDebugEnabled()) LOG.debug("Try " + fullFormat);
        assertEquals(jan1_1970, c.stringAsValue(fullFormat));
    }


    public void testConfigDateStringConvertor() throws Exception {
        try {
            ScopeConfig.setPropertiesName("test.util.convertor.ConvertorScopeConfig");

            DateStringConvertor c = new DateStringConvertor();

            assertEquals("01-01-1970", c.valueAsString(jan1_1970));

            assertEquals(jan1_1970, c.stringAsValue("01-01-1970"));
            assertEquals(jan1_1970, c.stringAsValue("01/01/1970"));
            assertEquals(jan1_1970, c.stringAsValue("01.01.1970"));
            assertEquals(jan1_1970, c.stringAsValue("01011970"));

            // Is it just me or is the Java calendar API impenetrable?
            Date d = (Date)c.stringAsValue("2-2-2001");
            assertThat(d)
                    .hasYear(2001)
                    .hasMonth(2)
                    .hasDayOfMonth(2);

        } finally {
            ScopeConfig.setPropertiesName(org.scopemvc.util.DefaultScopeConfig.class.getName());
        }
    }

    /**
     * A unit test for JUnit
     */
    public void testDateStringConvertor() {
        DateStringConvertor c = new DateStringConvertor();

        // don't use string constants as date formats depend on the JVM version.
        String dateFormat = c.parsers[MEDIUM].format(jan1_1970);

        assertEquals("", c.valueAsString(null));
        assertEquals(dateFormat, c.valueAsString(jan1_1970));
        try {
            c.valueAsString("");
            fail("DateStringConvertor took an empty String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));

        assertTrue(c.stringAsValue(dateFormat) instanceof Date);
        assertEquals(jan1_1970, c.stringAsValue(dateFormat));

        try {
            c.stringAsValue("1x");
            fail("DateStringConvertor converted nonsense");
        } catch (Exception e) {
            // expected
        }
        try {
            c.stringAsValue("(null)");
            fail("DateStringConvertor parsed '(null)'");
        } catch (Exception e) {
            // expected
        }

        c.setNullAsString("(null)");
        assertNull(c.stringAsValue("(null)"));
        assertNull(c.stringAsValue(""));
        assertEquals("(null)", c.valueAsString(null));
        try {
            c.stringAsValue("xyz");
            fail("DateStringConvertor parsed 'xyz'");
        } catch (Exception e) {
            // expected
        }
    }



}
