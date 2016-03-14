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
package test.model.beans;


import java.util.ArrayList;
import junit.framework.TestCase;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.model.beans.BeansPropertyManager;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/12 19:09:35 $
 * @created 05 September 2002
 */
public final class TestBeansPropertyManager2 extends TestCase {

    private ModelObject m;
    private PropertyManager manager;


    /**
     * Constructor for the TestBeansPropertyManager2 object
     *
     * @param inName Name of the test
     */
    public TestBeansPropertyManager2(String inName) {
        super(inName);
    }


    // ------------- Simple sets

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSet0() throws Exception {
        manager.set(m, Selector.fromString("stringProperty"), "test");
        assertEquals("test", m.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSet1() throws Exception {
        manager.set(m, Selector.fromString("intProperty"), new Integer(68));
        assertTrue(68 == m.getIntProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSet2() throws Exception {
        String[] s = {"a", "b", "c"};
        manager.set(m, Selector.fromString("stringIndexedProperty"), s);
        assertSame(s, m.getStringIndexedProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSet3() throws Exception {
        ArrayList l = new ArrayList();
        l.add("a");
        l.add("b");
        manager.set(m, Selector.fromString("stringNonIndexedProperty"), l);
        assertSame(l, m.getStringNonIndexedProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSet4() throws Exception {
        String[] a = new String[2];
        a[0] = "x";
        a[1] = "y";
        manager.set(m, Selector.fromString("stringNonIndexedProperty2"), a);
        assertSame(a, m.getStringNonIndexedProperty2());
    }


    // -------------------- Unknown simple property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetUnknown() throws Exception {
        try {
            manager.set(m, Selector.fromString("unknown"), "test");
            fail();
        } catch (Exception e) {
            // expected
        }
    }


    // -------------------- Read only simple property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetReadOnly() throws Exception {
        try {
            manager.set(m, Selector.fromString("readOnlyStringProperty"), "test");
            fail();
        } catch (Exception e) {
            // expected
        }
    }


    // -------------------- Indexed property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetIndexed0() throws Exception {
        manager.set(m, Selector.fromString("stringIndexedProperty.0"), "test");
        String s1 = m.getStringIndexedProperty(0);
        assertSame("test", s1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetIndexed1() throws Exception {
        manager.set(m, Selector.fromString("stringIndexedProperty.1"), "test1");
        String s1 = m.getStringIndexedProperty(1);
        assertSame("test1", s1);
    }


    // -------------------- Unknown indexed property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetIndexedUnknown0() throws Exception {
        try {
            manager.set(m, Selector.fromString("stringIndexedProperty.99"), "test");
            fail();
        } catch (Exception e) {
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetIndexedUnknown1() throws Exception {
        try {
            manager.set(m, Selector.fromString("unknown.0"), "test");
            fail();
        } catch (Exception e) {
            // expected
        }
    }


    // -------------------- NonIndexed List property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetNonIndexed0() throws Exception {
        manager.set(m, Selector.fromString("stringNonIndexedProperty.0"), "test");
        Object s1 = m.getStringNonIndexedProperty().get(0);
        assertSame("test", s1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetNonIndexed1() throws Exception {
        manager.set(m, Selector.fromString("stringNonIndexedProperty.1"), "test1");
        Object s1 = m.getStringNonIndexedProperty().get(1);
        assertSame("test1", s1);
    }


    // -------------------- NonIndexed Array property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetNonIndexed2() throws Exception {
        manager.set(m, Selector.fromString("stringNonIndexedProperty2.0"), "test2");
        Object s1 = m.getStringNonIndexedProperty2()[0];
        assertSame("test2", s1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetNonIndexed3() throws Exception {
        manager.set(m, Selector.fromString("stringNonIndexedProperty2.1"), "test3");
        Object s1 = m.getStringNonIndexedProperty2()[1];
        assertSame("test3", s1);
    }


    // -------------------- Unknown indexed property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetNonIndexedUnknown0() throws Exception {
        try {
            manager.set(m, Selector.fromString("stringNonIndexedProperty.99"), "test");
            fail();
        } catch (Exception e) {
        }
    }


    // -------------------- Hidden Indexed property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetHiddenIndexed0() throws Exception {
        manager.set(m, Selector.fromString("hiddenStringIndexedProperty.0"), "test");
        String s1 = m.getHiddenStringIndexedProperty(0);
        assertSame("test", s1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetHiddenIndexed1() throws Exception {
        manager.set(m, Selector.fromString("hiddenStringIndexedProperty.1"), "test1");
        String s1 = m.getHiddenStringIndexedProperty(1);
        assertSame("test1", s1);
    }


    // -------------------- Unknown hidden indexed property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetHiddenIndexedUnknown0() throws Exception {
        try {
            manager.set(m, Selector.fromString("hiddenStringIndexedProperty.99"), "test");
            fail();
        } catch (Exception e) {
        }
    }


    // -------------------- Submodel access

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetSubmodel0() throws Exception {
        ModelObject o = new ModelObject();
        m.setSubModel(o);

        manager.set(m, Selector.fromString("subModel.stringProperty"), "test3");
        assertSame("test3", o.getStringProperty());

        manager.set(m, Selector.fromString("subModel.stringIndexedProperty.1"), "test4");
        assertSame("test4", o.getStringIndexedProperty(1));
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        m = new ModelObject();
        manager = BeansPropertyManager.getInstance(m);
    }
}

