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
package test.core;


import junit.framework.TestCase;
import org.scopemvc.core.IntIndexSelector;
import org.scopemvc.core.Selector;
import org.scopemvc.core.StringIndexSelector;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/12 19:09:34 $
 * @created 05 September 2002
 */
public final class TestSelectors extends TestCase {

    /**
     * Constructor for the TestSelectors object
     *
     * @param inName Name of the test
     */
    public TestSelectors(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     */
    public void testSelectorFactorySimple() {

        assertTrue(Selector.fromInt(1) instanceof IntIndexSelector);
        assertNull(Selector.fromInt(1).getNext());
        assertEquals("1", Selector.fromInt(1).getName());

        assertTrue(Selector.fromString("xyz") instanceof StringIndexSelector);
        assertNull(Selector.fromString("xyz").getNext());
        assertEquals("xyz", Selector.fromString("xyz").getName());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSelectorFactoryPropertyDescriptions() throws Exception {

        assertEquals(Selector.asString(null), "");

        Selector selector = Selector.fromString("hello");
        assertEquals("hello", Selector.asString(selector));

        Selector chain = selector.deepClone();
        chain.chain(Selector.fromInt(2));

        String desc = Selector.asString(chain);
        assertEquals("hello.2", Selector.asString(chain));

        assertEquals(Selector.fromString("hello"), selector);
        assertEquals(Selector.fromString(desc), chain);
    }


    /**
     * A unit test for JUnit
     */
    public void testStringIndexSelector() {

        StringIndexSelector stringSelector = (StringIndexSelector) Selector.fromString("stringSelector");

        assertTrue(!stringSelector.equals(null));
        assertEquals("stringSelector", stringSelector.getIndex());
        assertNull(stringSelector.getNext());
        assertSame(stringSelector, stringSelector.getLast());

        StringIndexSelector stringSelector2 = (StringIndexSelector) Selector.fromString("stringSelector");
        assertEquals(stringSelector, stringSelector2);
        assertTrue(!stringSelector.equals(Selector.fromString("notThis")));
        assertEquals(stringSelector, stringSelector.deepClone());

        stringSelector.chain(Selector.fromString("chainThis"));
        assertEquals("stringSelector", stringSelector.getIndex());
        assertEquals(stringSelector.getNext(), Selector.fromString("chainThis"));

        Selector stringSelectorClone = stringSelector.deepClone();
        assertEquals(stringSelector, stringSelectorClone);

        assertTrue(!stringSelector.equals(stringSelector2));

        assertTrue(stringSelector.startsWith(stringSelector2));
        assertTrue(stringSelector.startsWith(stringSelector));
        assertTrue(!stringSelector.startsWith(Selector.fromString("chainThis")));
    }


    /**
     * A unit test for JUnit
     */
    public void testIntIndexSelector() {

        IntIndexSelector intSelector = Selector.fromInt(1);

        assertTrue(!intSelector.equals(null));
        assertEquals(1, intSelector.getIndex());
        assertNull(intSelector.getNext());
        assertSame(intSelector, intSelector.getLast());

        IntIndexSelector intSelector2 = Selector.fromInt(1);
        assertEquals(intSelector, intSelector2);
        assertTrue(!intSelector.equals(Selector.fromInt(3)));
        assertEquals(intSelector, intSelector.deepClone());

        intSelector.chain(Selector.fromInt(4));
        assertEquals(1, intSelector.getIndex());
        assertEquals(intSelector.getNext(), Selector.fromInt(4));

        Selector intSelectorClone = intSelector.deepClone();
        assertEquals(intSelector, intSelectorClone);

        assertTrue(!intSelector.equals(intSelector2));

        assertTrue(intSelector.startsWith(intSelector2));
        assertTrue(intSelector.startsWith(intSelector));
        assertTrue(!intSelector.startsWith(Selector.fromInt(4)));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testRemoveLast() throws Exception {
        Selector selector = Selector.fromString("one.two.three");
        selector.removeLast();

        assertEquals(Selector.fromString("one.two"), selector);
        selector.removeLast();
        assertEquals(Selector.fromString("one"), selector);

        try {
            selector.removeLast();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() { }
}
