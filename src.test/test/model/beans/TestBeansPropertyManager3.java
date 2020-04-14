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
 * $Id: TestBeansPropertyManager3.java,v 1.8 2002/11/20 00:19:58 ludovicc Exp $
 */
package test.model.beans;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import junit.framework.TestCase;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.model.beans.BeansPropertyManager;
import org.scopemvc.model.collection.ListModel;
import org.scopemvc.model.util.CompoundSelectorIterator;
import org.scopemvc.model.util.IntIndexSelectorIterator;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.8 $ $Date: 2002/11/20 00:19:58 $
 */
public final class TestBeansPropertyManager3 extends TestCase {

    private ModelObject m;
    private PropertyManager manager;


    /**
     * Constructor for the TestBeansPropertyManager3 object
     *
     * @param inName Name of the test
     */
    public TestBeansPropertyManager3(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testIsReadOnly() throws Exception {
        assertTrue(!manager.isReadOnly(m, Selector.fromString("stringProperty")));
        assertTrue(!manager.isReadOnly(m, Selector.fromString("intProperty")));
        assertTrue(!manager.isReadOnly(m, Selector.fromString("stringIndexedProperty")));
        assertTrue(!manager.isReadOnly(m, Selector.fromString("stringIndexedProperty.0")));
        assertTrue(!manager.isReadOnly(m, Selector.fromString("stringNonIndexedProperty")));
        assertTrue(!manager.isReadOnly(m, Selector.fromString("stringNonIndexedProperty.0")));
        assertTrue(!manager.isReadOnly(m, Selector.fromString("stringNonIndexedProperty2")));
        assertTrue(!manager.isReadOnly(m, Selector.fromString("stringNonIndexedProperty2.0")));
        assertTrue(manager.isReadOnly(m, Selector.fromString("readOnlyStringProperty")));

//        try {
        manager.isReadOnly(m, Selector.fromString("unknown"));
//            fail();
//        } catch (Exception e) {
//            // expected
//        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testHasProperty() throws Exception {
        assertTrue(manager.hasProperty(m, Selector.fromString("stringProperty")));
        assertTrue(manager.hasProperty(m, Selector.fromString("intProperty")));
        assertTrue(!manager.hasProperty(m, Selector.fromString("unknown")));
        assertTrue(manager.hasProperty(m, Selector.fromString("stringIndexedProperty")));
        assertTrue(manager.hasProperty(m, Selector.fromString("stringIndexedProperty.0")));
        assertTrue(!manager.hasProperty(m, Selector.fromString("stringIndexedProperty.0.name")));
        assertTrue(!manager.hasProperty(m, Selector.fromString("stringIndexedProperty.99")));
        assertTrue(manager.hasProperty(m, Selector.fromString("stringNonIndexedProperty")));
        assertTrue(manager.hasProperty(m, Selector.fromString("stringNonIndexedProperty.0")));
        assertTrue(!manager.hasProperty(m, Selector.fromString("stringNonIndexedProperty.99")));
        assertTrue(manager.hasProperty(m, Selector.fromString("stringNonIndexedProperty2")));
        assertTrue(manager.hasProperty(m, Selector.fromString("stringNonIndexedProperty2.0")));
        assertTrue(!manager.hasProperty(m, Selector.fromString("stringNonIndexedProperty2.99")));
        assertTrue(!manager.hasProperty(m, Selector.fromString("hiddenStringIndexedProperty")));
        assertTrue(manager.hasProperty(m, Selector.fromString("hiddenStringIndexedProperty.0")));
        assertTrue(manager.hasProperty(m, Selector.fromString("readOnlyStringProperty")));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetPropertyClass() throws Exception {
        assertTrue(manager.getPropertyClass(m, Selector.fromString("stringProperty")) == String.class);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("intProperty")) == Integer.TYPE);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("stringIndexedProperty")) == String[].class);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("stringIndexedProperty.0")) == String.class);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("stringNonIndexedProperty")) == java.util.ArrayList.class);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("stringNonIndexedProperty.0")) == Object.class);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("stringNonIndexedProperty2")) == String[].class);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("stringNonIndexedProperty2.0")) == Object.class);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("hiddenStringIndexedProperty.0")) == String.class);
        assertTrue(manager.getPropertyClass(m, Selector.fromString("readOnlyStringProperty")) == String.class);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSelectorIterator0() throws Exception {
        String[] names = {"stringProperty", "intProperty", "stringIndexedProperty", "stringNonIndexedProperty", "stringNonIndexedProperty2", "readOnlyStringProperty"};
        Set nameSet = new HashSet(Arrays.asList(names));
        for (Iterator i = manager.getSelectorIterator(m); i.hasNext(); ) {
            Selector s = (Selector) i.next();
            nameSet.remove(s.getName());
        }
        assertTrue(nameSet.toString(), nameSet.isEmpty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSelectorIterator1() throws Exception {
        manager = BeansPropertyManager.getInstance(m.getStringIndexedProperty());
        Iterator i = manager.getSelectorIterator(m.getStringIndexedProperty());
        assertTrue(i instanceof IntIndexSelectorIterator);
        int count = 0;
        while (i.hasNext()) {
            i.next();
            ++count;
        }
        assertTrue(count == m.getStringIndexedProperty().length);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSelectorIterator2() throws Exception {
        ListModel lm = new ListModel(m.getStringNonIndexedProperty());
        manager = BeansPropertyManager.getInstance(lm);
        Iterator i = manager.getSelectorIterator(lm);
        assertTrue(i instanceof CompoundSelectorIterator);

        Set names = new HashSet();
        while (i.hasNext()) {
            names.add(((Selector) i.next()).getName());
        }

        assertTrue("Not size: " + (lm.getSize() + 3) + ", " + names.toString(), names.size() == lm.getSize() + 3);

        assertTrue("Can't find size property", names.contains("size"));
        assertTrue("Can't find empty property", names.contains("empty"));
        assertTrue("Can't find list property", names.contains("list"));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSelectorIterator3() throws Exception {
        manager = BeansPropertyManager.getInstance(m.getStringNonIndexedProperty2());
        Iterator i = manager.getSelectorIterator(m.getStringNonIndexedProperty2());
        assertTrue(i instanceof IntIndexSelectorIterator);
        int count = 0;
        while (i.hasNext()) {
            i.next();
            ++count;
        }
        assertTrue(count == m.getStringNonIndexedProperty2().length);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSelectorFor() throws Exception {
        assertNull(manager.getSelectorFor(m, null));

        assertNull(manager.getSelectorFor(m, "not a property"));

        // JavaBean
        Selector s = manager.getSelectorFor(m, m.getStringProperty());
        assertEquals(s, Selector.fromString("stringProperty"));

        // Object[]
        Object o = m.getStringIndexedProperty();
        manager = BeansPropertyManager.getInstance(o);
        assertEquals(Selector.fromInt(1), manager.getSelectorFor(o, m.getStringIndexedProperty(1)));

        // java.util.List
        java.util.List l = m.getStringNonIndexedProperty();
        manager = BeansPropertyManager.getInstance(l);
        assertEquals(Selector.fromInt(1), manager.getSelectorFor(l, l.get(1)));
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        m = new ModelObject();
        manager = BeansPropertyManager.getInstance(m);
    }
}

