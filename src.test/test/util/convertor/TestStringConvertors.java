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
 * $Id: TestStringConvertors.java,v 1.9 2002/11/20 00:19:57 ludovicc Exp $
 */
package test.util.convertor;


import junit.framework.TestCase;
import org.scopemvc.util.convertor.BigDecimalStringConvertor;
import org.scopemvc.util.convertor.BigIntegerStringConvertor;
import org.scopemvc.util.convertor.DateTimeStringConvertor;
import org.scopemvc.util.convertor.DoubleStringConvertor;
import org.scopemvc.util.convertor.FloatStringConvertor;
import org.scopemvc.util.convertor.IntegerStringConvertor;
import org.scopemvc.util.convertor.LongStringConvertor;
import org.scopemvc.util.convertor.NumberStringConvertor;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;
import org.scopemvc.util.convertor.StringStringConvertor;
import org.scopemvc.util.convertor.TimeStringConvertor;
import test.TestUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.9 $ $Date: 2002/11/20 00:19:57 $
 * @created 18 September 2002
 */
public final class TestStringConvertors extends TestCase {

    private static final Date jan1_1970;
    static {
        GregorianCalendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(1970, 0, 1, 0, 0, 0);
        jan1_1970 = cal.getTime();
    }


    /**
     * Constructor for the TestStringConvertors object
     *
     * @param inName Name of the test
     */
    public TestStringConvertors(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     */
    public void testStringStringConvertor() {

        StringStringConvertor c = new StringStringConvertor();

        assertEquals("", c.valueAsString(null));
        assertEquals("abc", c.valueAsString("abc"));
        try {
            c.valueAsString(1);
            fail("StringStringConvertor took an Integer");
        } catch (Exception e) {
            // expected
        }

        assertTrue(c.stringAsValue("xyz") instanceof String);
        assertEquals(null, c.stringAsValue(""));
        assertEquals("xyz", c.stringAsValue("xyz"));

        c.setNullAsString("(null)");
        assertNull(c.stringAsValue("(null)"));
        assertEquals("(null)", c.valueAsString(null));
    }



    public void testNegativeNumbers() {
        final List<NumberStringConvertor> numberStringConvertors = Arrays.asList(
                new IntegerStringConvertor()
                , new LongStringConvertor()
                , new DoubleStringConvertor()
                , new FloatStringConvertor()
                , new BigIntegerStringConvertor()
        );

        for (NumberStringConvertor c : numberStringConvertors) {
            ((DecimalFormat) c.getNumberFormat()).setNegativePrefix("minus ");
            assertEquals(-2, c.stringAsValue("minus 2").intValue());
            assertEquals(-2, c.stringAsValue("-2").intValue());
            assertEquals(-2, c.stringAsValue("\u002d2").intValue());
            assertEquals(-2, c.stringAsValue("\u22122").intValue());
            assertEquals(-2, c.stringAsValue("\uFE632").intValue());
            assertEquals(-2, c.stringAsValue("\uFF0D2").intValue());
            assertEquals(-2, c.stringAsValue("\u20102").intValue());
            assertEquals(-2, c.stringAsValue("\u20122").intValue());
        }
    }

    /**
     * A unit test for JUnit
     */
    public void testIntegerStringConvertor() {

        IntegerStringConvertor c = new IntegerStringConvertor();

        assertEquals("", c.valueAsString(null));
        assertEquals("1", c.valueAsString(1));
        try {
            c.valueAsString("");
            fail("IntegerStringConvertor took a String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));
        assertEquals(Integer.valueOf(1), c.stringAsValue("1"));

        try {
            c.stringAsValue("(null)");
            fail("IntegerStringConvertor parsed '(null)'");
        } catch (Exception e) {
            // expected
        }
        c.setNullAsString("(null)");
        assertNull(c.stringAsValue("(null)"));
        assertNull(c.stringAsValue(""));
        assertEquals("(null)", c.valueAsString(null));
        try {
            c.stringAsValue("xyz");
            fail("IntegerStringConvertor parsed 'xyz'");
        } catch (Exception e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testLongStringConvertor() {

        LongStringConvertor c = new LongStringConvertor();

        assertEquals("", c.valueAsString(null));
        assertEquals("1", c.valueAsString(1L));
        try {
            c.valueAsString("");
            fail("LongStringConvertor took a String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));
        assertEquals(Long.valueOf(1), c.stringAsValue("1"));

        try {
            c.stringAsValue("(null)");
            fail("LongStringConvertor parsed '(null)'");
        } catch (Exception e) {
            // expected
        }
        c.setNullAsString("(null)");
        assertNull(c.stringAsValue("(null)"));
        assertNull(c.stringAsValue(""));
        assertEquals("(null)", c.valueAsString(null));
        try {
            c.stringAsValue("xyz");
            fail("LongStringConvertor parsed 'xyz'");
        } catch (Exception e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testDoubleStringConvertor() {
        DoubleStringConvertor c = new DoubleStringConvertor();
        c.setNumberFormat(NumberFormat.getInstance(Locale.UK));

        assertEquals("", c.valueAsString(null));
        assertEquals("1.1", c.valueAsString(1.1d));
        try {
            c.valueAsString("");
            fail("DoubleStringConvertor took a String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));
        assertEquals(1.1d, c.stringAsValue("1.1"));

        try {
            c.stringAsValue("(null)");
            fail("DoubleStringConvertor parsed '(null)'");
        } catch (Exception e) {
            // expected
        }
        c.setNullAsString("(null)");
        assertNull(c.stringAsValue("(null)"));
        assertNull(c.stringAsValue(""));
        assertEquals("(null)", c.valueAsString(null));
        try {
            c.stringAsValue("xyz");
            fail("DoubleStringConvertor parsed 'xyz'");
        } catch (Exception e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testFloatStringConvertor() {
        FloatStringConvertor c = new FloatStringConvertor();
        c.setNumberFormat(NumberFormat.getInstance(Locale.UK));

        assertEquals("", c.valueAsString(null));
        assertEquals("1.1", c.valueAsString(1.1f));
        try {
            c.valueAsString("");
            fail("FloatStringConvertor took a String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));
        assertEquals(1.1f, c.stringAsValue("1.1"));

        try {
            c.stringAsValue("(null)");
            fail("FloatStringConvertor parsed '(null)'");
        } catch (Exception e) {
            // expected
        }
        c.setNullAsString("(null)");
        assertNull(c.stringAsValue("(null)"));
        assertNull(c.stringAsValue(""));
        assertEquals("(null)", c.valueAsString(null));
        try {
            c.stringAsValue("xyz");
            fail("FloatStringConvertor parsed 'xyz'");
        } catch (Exception e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testDateTimeStringConvertor() {
        DateTimeStringConvertor c = new DateTimeStringConvertor();
        // don't use string constants as date formats depend on the JVM version.
        String dateTimeFormat = DateFormat.getDateTimeInstance().format(jan1_1970);

        assertEquals("", c.valueAsString(null));
        assertEquals(dateTimeFormat, c.valueAsString(jan1_1970));
        try {
            c.valueAsString("");
            fail("DateTimeStringConvertor took a String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));

        assertEquals(Date.class, c.stringAsValue(dateTimeFormat).getClass());
        assertEquals(jan1_1970, c.stringAsValue(dateTimeFormat));

        try {
            c.stringAsValue("1x");
            fail("DateTimeStringConvertor converted nonsense");
        } catch (Exception e) {
            // expected
        }
        try {
            c.stringAsValue("(null)");
            fail("DateTimeStringConvertor parsed '(null)'");
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


    /**
     * A unit test for JUnit
     */
    public void testTimeStringConvertor() {
        TimeStringConvertor c = new TimeStringConvertor();
        // don't use string constants as date formats depend on the JVM version.
        String timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM).format(jan1_1970);

        assertEquals("", c.valueAsString(null));
        assertEquals(timeFormat, c.valueAsString(jan1_1970));
        try {
            c.valueAsString("");
            fail("DateTimeStringConvertor took a String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));

        assertTrue(c.stringAsValue(timeFormat) instanceof Date);
        assertEquals(jan1_1970, c.stringAsValue(timeFormat));

        try {
            c.stringAsValue("1x");
            fail("DateTimeStringConvertor converted nonsense");
        } catch (Exception e) {
            // expected
        }
        try {
            c.stringAsValue("(null)");
            fail("DateTimeStringConvertor parsed '(null)'");
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


    /**
     * A unit test for JUnit
     */
    public void testBigIntegerStringConvertor() {

        BigIntegerStringConvertor c = new BigIntegerStringConvertor();

        assertEquals("", c.valueAsString(null));
        assertEquals("1", c.valueAsString(BigInteger.valueOf(1L)));
        try {
            c.valueAsString("");
            fail("BigIntegerStringConvertor took a String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));
        assertEquals(BigInteger.valueOf(1L), c.stringAsValue("1"));

        try {
            c.stringAsValue("(null)");
            fail("BigIntegerStringConvertor parsed '(null)'");
        } catch (Exception e) {
            // expected
        }
        c.setNullAsString("(null)");
        assertNull(c.stringAsValue("(null)"));
        assertNull(c.stringAsValue(""));
        assertEquals("(null)", c.valueAsString(null));
        try {
            c.stringAsValue("xyz");
            fail("BigIntegerStringConvertor parsed 'xyz'");
        } catch (Exception e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testBigDecimalStringConvertor() {

        BigDecimalStringConvertor c = new BigDecimalStringConvertor();

        assertEquals("", c.valueAsString(null));
        assertTrue(c.valueAsString(new BigDecimal(1.1d)).startsWith("1.1000"));
        // rounding errors!
        try {
            c.valueAsString("");
            fail("BigDecimalStringConvertor took a String");
        } catch (Exception e) {
            // expected
        }

        assertNull(c.stringAsValue(""));
        assertNull(c.stringAsValue(null));
        assertTrue(c.stringAsValue("1.1") instanceof BigDecimal);
        assertTrue(((BigDecimal) c.stringAsValue("1.1")).floatValue() - 1.1f < .05);

        try {
            c.stringAsValue("(null)");
            fail("BigDecimalStringConvertor parsed '(null)'");
        } catch (Exception e) {
            // expected
        }
        c.setNullAsString("(null)");
        assertNull(c.stringAsValue("(null)"));
        assertNull(c.stringAsValue(""));
        assertEquals("(null)", c.valueAsString(null));
        try {
            c.stringAsValue("xyz");
            fail("BigDecimalStringConvertor parsed 'xyz'");
        } catch (Exception e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testStringConvertorFactory() {
        TestUtils.withScopeConfig(test.util.convertor.ConvertorScopeConfig.class, () -> {
            assertNull(StringConvertors.forClass(java.util.Enumeration.class));
            assertTrue(StringConvertors.forClass(java.lang.Long.class) instanceof LongStringConvertor);
            assertTrue(StringConvertors.forClass(java.lang.Long.TYPE) instanceof LongStringConvertor);
        });
    }

    public void testCustomStringConvertorsStrategy() {
        TestUtils.withScopeConfig(test.util.convertor.ConvertorScopeConfig.class, () -> {
            assertThat(StringConvertors.getInstance()).isInstanceOf(StringConvertorsConfig.class);
        });
    }

    public void testReloadDefaultConvertors() {
        final Locale previousDefault = Locale.getDefault(Locale.Category.FORMAT);
        try {
            final StringConvertor firstStringConvertor = StringConvertors.forClass(long.class);

            changeStringConvertorsLocale(Locale.CANADA);
            assertThat(StringConvertors.forClass(long.class))
                    .isSameAs(StringConvertors.forClass(long.class))
                    .isNotSameAs(firstStringConvertor);

            changeStringConvertorsLocale(Locale.CANADA);
            assertThat(StringConvertors.forClass(long.class).valueAsString(1_000L)).isEqualTo("1,000");

            changeStringConvertorsLocale(new Locale("no", "NO"));
            char noneBreakingSpace = 160; //&nbsp;\u00A0
            assertThat(StringConvertors.forClass(long.class).valueAsString(1_000L)).isEqualTo("1" + noneBreakingSpace + "000");

        } finally {
            changeStringConvertorsLocale(previousDefault);
        }
    }

    private void changeStringConvertorsLocale(Locale locale) {
        Locale.setDefault(Locale.Category.FORMAT, locale);
        StringConvertors.updateLocale();
    }

    public void testStrictParsing() {
        TestUtils.withScopeProperty("org.scopemvc.util.convertor.NumberStringConvertor.strict", false, () -> {
            assertThat(new IntegerStringConvertor().stringAsValue("1F")).isEqualTo(1);
            assertThat(new IntegerStringConvertor().stringAsValue("2_")).isEqualTo(2);
            assertThat(new IntegerStringConvertor().stringAsValue("3 ")).isEqualTo(3);
            assertThat(new IntegerStringConvertor().stringAsValue("4")).isEqualTo(4);
        });
        TestUtils.withScopeProperty("org.scopemvc.util.convertor.NumberStringConvertor.strict", true, () -> {
            assertThatCode(() -> new IntegerStringConvertor().stringAsValue("1F")).isInstanceOf(IllegalArgumentException.class);
            assertThatCode(() -> new IntegerStringConvertor().stringAsValue("2_")).isInstanceOf(IllegalArgumentException.class);
            assertThatCode(() -> new IntegerStringConvertor().stringAsValue("3 ")).isInstanceOf(IllegalArgumentException.class);
            assertThatCode(() -> new IntegerStringConvertor().stringAsValue("4")).doesNotThrowAnyException();
        });
    }

}
