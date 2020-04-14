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
 * $Id: TestArrayModel.java,v 1.6 2002/09/12 19:09:35 ludovicc Exp $
 * Changes:
 *  - added generics signature to ArrayModel
 */
package test.model.collection;


import junit.framework.TestCase;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.model.collection.ArrayModel;
import test.model.basic.BasicTestModel;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/12 19:09:35 $
 */
public final class TestArrayModel extends TestCase implements ModelChangeListener {

    private ArrayModel<Object> arrayModel;
    private BasicTestModel model, submodel;
    private Object[] array;

    private boolean modelChanged;
    private String modelChangedName;


    /**
     * Constructor for the TestArrayModel object
     *
     * @param inName Name of the test
     */
    public TestArrayModel(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelDefaultConstructor() throws Exception {
        ArrayModel m = new ArrayModel();
        assertTrue(m.getSize() == 0);
        try {
            m.get(0);
        } catch (NullPointerException e) {
            // We expect this
            try {
                m.set(1, "test");
                fail("get/set on no array");
            } catch (NullPointerException e1) {
                // expected
            }
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelBooleanConstructor1() throws Exception {
        ArrayModel m = new ArrayModel(false);
        assertTrue(m.getSize() == 0);
        try {
            m.get(0);
            fail("get/set on no array");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            m.set(1, "test");
            fail("get/set on no array");
        } catch (NullPointerException e1) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelBooleanConstructor2() throws Exception {
        ArrayModel m = new ArrayModel(true);
        assertTrue(m.getSize() == 0);
        try {
            m.get(0);
            fail("get/set on no array");
        } catch (NullPointerException e) {
            // expected
        }
        try {
            m.set(-1, "test");
            fail("get/set on no array");
        } catch (NullPointerException e1) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelIntConstructor() throws Exception {
        ArrayModel m = new ArrayModel(10);
        assertTrue(m.getSize() == 10);
        m.set(0, "test");
        assertEquals("test", m.get(0));
        m.set(9, "test1");
        assertEquals("test1", m.get(9));
        try {
            m.get(10);
            fail("get/set beyond bounds of array");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            m.set(-1, "test");
            fail("get/set beyond bounds of array");
        } catch (ArrayIndexOutOfBoundsException e1) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelBooleanIntConstructor1() throws Exception {
        ArrayModel m = new ArrayModel(true, 10);
        assertTrue(m.getSize() == 10);
        m.set(0, "test");
        assertEquals("test", m.get(0));
        m.set(9, "test1");
        assertEquals("test1", m.get(9));
        try {
            m.get(10);
            fail("get/set beyond bounds of array");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            m.set(-1, "test");
            fail("get/set beyond bounds of array");
        } catch (ArrayIndexOutOfBoundsException e1) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelBooleanIntConstructor2() throws Exception {
        ArrayModel m = new ArrayModel(false, 10);
        assertTrue(m.getSize() == 10);
        m.set(0, "test");
        assertEquals(m.get(0), "test");
        m.set(9, "test1");
        assertEquals(m.get(9), "test1");
        try {
            m.get(10);
            fail("get/set beyond bounds of array");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        try {
            m.set(-1, "test");
            fail("get/set beyond bounds of array");
        } catch (ArrayIndexOutOfBoundsException e1) {
            // expected
        }
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelArrayConstructorWithouthGenerics() throws Exception {
        ArrayModel m = new ArrayModel(array);
        assertTrue(m.getSize() == array.length);
        for (int i = 0; i < m.getSize(); ++i) {
            assertEquals(m.get(i), array[i]);
        }
        try {
            m.get(array.length + 1);
        } catch (IndexOutOfBoundsException e) {
            // We expect this
            try {
                m.set(-1, "test");
            } catch (ArrayIndexOutOfBoundsException e1) {
                // We expect this
                return;
            }
        }
        fail("get/set beyond bounds of array");
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelArrayConstructor() throws Exception {
        ArrayModel m = new ArrayModel<>(array);
        assertTrue(m.getSize() == array.length);
        for (int i = 0; i < m.getSize(); ++i) {
            assertEquals(m.get(i), array[i]);
        }
        try {
            m.get(array.length + 1);
        } catch (IndexOutOfBoundsException e) {
            // We expect this
            try {
                m.set(-1, "test");
            } catch (ArrayIndexOutOfBoundsException e1) {
                // We expect this
                return;
            }
        }
        fail("get/set beyond bounds of array");
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelBooleanArrayConstructor1() throws Exception {
        ArrayModel m = new ArrayModel(true, array);
        assertTrue(m.getSize() == array.length);
        for (int i = 0; i < m.getSize(); ++i) {
            assertEquals(m.get(i), array[i]);
        }
        try {
            m.get(array.length + 1);
        } catch (IndexOutOfBoundsException e) {
            // We expect this
            try {
                m.set(-1, "test");
            } catch (ArrayIndexOutOfBoundsException e1) {
                // We expect this
                return;
            }
        }
        fail("get/set beyond bounds of array");
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelBooleanArrayConstructor2() throws Exception {
        ArrayModel m = new ArrayModel(false, array);
        assertTrue(m.getSize() == array.length);
        for (int i = 0; i < m.getSize(); ++i) {
            assertEquals(m.get(i), array[i]);
        }
        try {
            m.get(array.length + 1);
        } catch (IndexOutOfBoundsException e) {
            // We expect this
            try {
                m.set(-1, "test");
            } catch (ArrayIndexOutOfBoundsException e1) {
                // We expect this
                return;
            }
        }
        fail("get/set beyond bounds of array");
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelSelectors() throws Exception {
        for (int i = 0; i < arrayModel.size(); ++i) {
            assertEquals(arrayModel.get(i), new Integer(i));
        }
        for (int i = 0; i < arrayModel.size(); ++i) {
            arrayModel.set(i, new Integer(i + 100));
        }
        for (int i = 0; i < arrayModel.size(); ++i) {
            assertEquals(arrayModel.get(i), new Integer(i + 100));
        }
        arrayModel.setArray(array);
        assertSame(arrayModel.getArray(), array);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelSetArray() throws Exception {
        Integer[] o = new Integer[]{
                new Integer(99),
                };
        arrayModel.setArray(o);
        assertTrue(arrayModel.getSize() == 1);
        assertEquals(arrayModel.get(0), new Integer(99));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayModelChangeEventPropagation() throws Exception {
        arrayModel.set(0, model);
        arrayModel.addModelChangeListener(this);

        modelChanged = false;
        modelChangedName = "";

        submodel.setName("xxx");

        assertTrue(modelChangedName, modelChangedName.lastIndexOf("0.subModel.name") != -1);
    }


    /**
     * TODO: document the method
     *
     * @param inEvent TODO: Describe the Parameter
     */
    public void modelChanged(ModelChangeEvent inEvent) {
        modelChanged = true;
        modelChangedName = "" + inEvent;
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        arrayModel = new ArrayModel<>(10);
        for (int i = 0; i < arrayModel.getSize(); ++i) {
            arrayModel.set(i, new Integer(i));
        }

        model = new BasicTestModel("model");
        submodel = new BasicTestModel("submodel");
        model.setSubModel(submodel);

        array = new Object[10];
        for (int i = 0; i < array.length; ++i) {
            array[i] = "test" + i;
        }
        modelChanged = false;
        modelChangedName = "";
    }
}

