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


package test.util.convertor;


import java.text.DateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import junit.framework.TestCase;
import org.apache.commons.logging.LogFactory;import org.apache.commons.logging.Log;
import org.scopemvc.util.ScopeConfig;
import org.scopemvc.util.convertor.DateStringConvertor;


/**
 * <P>
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/08/05 13:16:45 $
 */
public final class TestDateStringConvertors extends TestCase {


    private static final Log LOG = LogFactory.getLog(TestDateStringConvertors.class);


    /**
     * Must set a known default locale for the tests.
     */
    static {
        Locale.setDefault(Locale.UK);
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        System.out.println("Default medium format " + DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(0)));
        System.out.println("Default locale " + Locale.getDefault());
    }

    private Date jan1_1970;
    private GregorianCalendar cal = new GregorianCalendar();

    public TestDateStringConvertors(String inName) {
        super(inName);
    }


    protected void setUp() {
        cal.clear();
        cal.set(1970, 0, 1, 0, 0, 0);
        jan1_1970 = cal.getTime();
    }


    public void testDefaultDateStringConvertor() throws Exception {

        DateStringConvertor c = new DateStringConvertor();
        String shortFormat = DateFormat.getDateInstance(DateFormat.SHORT).format(jan1_1970);
        String mediumFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(jan1_1970);
        String longFormat = DateFormat.getDateInstance(DateFormat.LONG).format(jan1_1970);
        String fullFormat = DateFormat.getDateInstance(DateFormat.FULL).format(jan1_1970);

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

        ScopeConfig.setPropertiesName("test.util.convertor.ConvertorScopeConfig");

        DateStringConvertor c = new DateStringConvertor();

        assertEquals("01-01-1970", c.valueAsString(jan1_1970));

        assertEquals(jan1_1970, c.stringAsValue("01-01-1970"));
        assertEquals(jan1_1970, c.stringAsValue("01/01/1970"));
        assertEquals(jan1_1970, c.stringAsValue("01.01.1970"));
        assertEquals(jan1_1970, c.stringAsValue("01011970"));

        // Is it just me or is the Java calendar API impenetrable?
        Date d = (Date)c.stringAsValue("2-2-2001");
        cal.setTime(d);
        assertEquals("Date: " + d, 2001, cal.get(cal.YEAR));
        assertEquals("Date: " + d, 1, cal.get(cal.MONTH));
        assertEquals("Date: " + d, 2, cal.get(cal.DAY_OF_MONTH));
    }
}
