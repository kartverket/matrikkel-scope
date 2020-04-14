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
 * $Id: TestBeansPropertyManager.java,v 1.7 2002/11/20 00:19:58 ludovicc Exp $
 */
package test.model.beans;

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
 * @created 05 September 2002
 * @version $Revision: 1.7 $ $Date: 2002/11/20 00:19:58 $
 */
public final class TestBeansPropertyManager extends TestCase {

    private ModelObject m;
    private PropertyManager manager;


    /**
     * Constructor for the TestBeansPropertyManager object
     *
     * @param inName Name of the test
     */
    public TestBeansPropertyManager(String inName) {
        super(inName);
    }

    // ------------- Simple gets

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGet0() throws Exception {
        assertSame(m.getStringProperty(), manager.get(m, Selector.fromString("stringProperty")));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGet1() throws Exception {
        Object o = manager.get(m, Selector.fromString("intProperty"));
        assertEquals(m.getIntProperty(), ((Integer) o).intValue());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGet2() throws Exception {
        Object o = manager.get(m, Selector.fromString("stringIndexedProperty"));
        assertSame(m.getStringIndexedProperty(), o);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGet3() throws Exception {
        Object o = manager.get(m, Selector.fromString("stringNonIndexedProperty"));
        assertSame(m.getStringNonIndexedProperty(), o);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGet4() throws Exception {
        Object o = manager.get(m, Selector.fromString("stringNonIndexedProperty2"));
        assertSame(m.getStringNonIndexedProperty2(), o);
    }


    // -------------------- Unknown simple property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetUnknown() throws Exception {
        try {
            Object o = manager.get(m, Selector.fromString("unknown"));
            fail("Should not get " + o);
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
    public void testGetIndexed0() throws Exception {
        String s1 = m.getStringIndexedProperty(0);
        Object s2 = manager.get(m, Selector.fromString("stringIndexedProperty.0"));
        assertSame(s1, s2);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetIndexed1() throws Exception {
        String s1 = m.getStringIndexedProperty(1);
        Object s2 = manager.get(m, Selector.fromString("stringIndexedProperty.1"));
        assertSame(s1, s2);
    }


    // -------------------- Unknown indexed property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetIndexedUnknown0() throws Exception {
        try {
            Object o = manager.get(m, Selector.fromString("stringIndexedProperty.99"));
            fail("Should not get " + o);
        } catch (Exception e) {
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetIndexedUnknown1() throws Exception {
        try {
            Object o = manager.get(m, Selector.fromString("unknown.0"));
            fail("Should not get " + o);
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
    public void testGetNonIndexed0() throws Exception {
        Object s1 = m.getStringNonIndexedProperty().get(0);
        Object s2 = manager.get(m, Selector.fromString("stringNonIndexedProperty.0"));
        assertSame(s1, s2);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetNonIndexed1() throws Exception {
        Object s1 = m.getStringNonIndexedProperty().get(1);
        Object s2 = manager.get(m, Selector.fromString("stringNonIndexedProperty.1"));
        assertSame(s1, s2);
    }


    // -------------------- NonIndexed Array property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetNonIndexed2() throws Exception {
        Object s1 = m.getStringNonIndexedProperty2()[0];
        Object s2 = manager.get(m, Selector.fromString("stringNonIndexedProperty2.0"));
        assertSame(s1, s2);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetNonIndexed3() throws Exception {
        Object s1 = m.getStringNonIndexedProperty2()[1];
        Object s2 = manager.get(m, Selector.fromString("stringNonIndexedProperty2.1"));
        assertSame(s1, s2);
    }


    // -------------------- Unknown indexed property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetNonIndexedUnknown0() throws Exception {
        try {
            Object o = manager.get(m, Selector.fromString("stringNonIndexedProperty.99"));
            fail("Should not get " + o);
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
        Object s1 = m.getHiddenStringIndexedProperty(0);
        Object s2 = manager.get(m, Selector.fromString("hiddenStringIndexedProperty.0"));
        assertSame(s1, s2);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetHiddenIndexed1() throws Exception {
        Object s1 = m.getHiddenStringIndexedProperty(1);
        Object s2 = manager.get(m, Selector.fromString("hiddenStringIndexedProperty.1"));
        assertSame(s1, s2);
    }


    // -------------------- Unknown hidden indexed property

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetHiddenIndexedUnknown0() throws Exception {
        try {
            Object o = manager.get(m, Selector.fromString("hiddenStringIndexedProperty.99"));
            fail("Should not get " + o);
        } catch (Exception e) {
        }
    }


    // -------------------- Submodel access

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSubmodel0() throws Exception {
        ModelObject o = new ModelObject();
        m.setSubModel(o);
        assertSame(o, manager.get(m, Selector.fromString("subModel")));

        assertSame(o.getStringProperty(), manager.get(m, Selector.fromString("subModel.stringProperty")));
        assertSame(o.getStringIndexedProperty(1), manager.get(m, Selector.fromString("subModel.stringIndexedProperty.1")));
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        m = new ModelObject();
        manager = BeansPropertyManager.getInstance(m);
    }

}

