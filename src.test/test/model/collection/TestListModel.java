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
package test.model.collection;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import junit.framework.TestCase;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.model.collection.ListModel;
import test.model.basic.BasicTestModel;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:haruki_zaemon@users.sourceforge.net">Simon Harris</A>
 * @version $Revision: 1.9 $
 * @created 05 September 2002
 */
public final class TestListModel extends TestCase implements ModelChangeListener {

    private ListModel listModel;
    private BasicTestModel model, submodel;
    private List list;

    private boolean modelChanged;
    private String modelChangedName;


    /**
     * Constructor for the TestListModel object
     *
     * @param inName Name of the test
     */
    public TestListModel(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelDefaultConstructor() throws Exception {
        ListModel m = new ListModel(null);
        assertTrue(m.getSize() == 0);
        try {
            m.get(0);
        } catch (NullPointerException e) {
            // We expect this
            try {
                m.set(1, "test");
                fail("get/set on no list");
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
    public void testListModelBooleanConstructor1() throws Exception {
        ListModel m = new ListModel(false, null);
        assertTrue(m.getSize() == 0);
        try {
            Object o = m.get(0);
            assertNull("get/set on no list", o);
        } catch (NullPointerException e) {
            // expected
        }
        try {
            m.set(1, "test");
            fail("get/set on no list");
        } catch (IllegalStateException e1) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelBooleanConstructor2() throws Exception {
        ListModel m = new ListModel(true);
        assertTrue(m.getSize() == 0);
        try {
            Object o = m.get(0);
            assertNull("get/set on no list", o);
        } catch (NullPointerException e) {
            // expected
        }
        try {
            m.set(-1, "test");
            fail("get/set on no list");
        } catch (IllegalStateException e1) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelListConstructor() throws Exception {
        ListModel m = new ListModel(list);
        assertTrue(m.getSize() == list.size());
        for (int i = 0; i < m.getSize(); ++i) {
            assertEquals(m.get(i), list.get(i));
        }
        try {
            m.get(list.size() + 1);
        } catch (IndexOutOfBoundsException e) {
            // We expect this
            try {
                m.set(-1, "test");
            } catch (IndexOutOfBoundsException e1) {
                // We expect this
                return;
            }
        }
        fail("get/set beyond bounds of list");
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelBooleanListConstructor1() throws Exception {
        ListModel m = new ListModel(true, list);
        assertTrue(m.getSize() == list.size());
        for (int i = 0; i < m.getSize(); ++i) {
            assertEquals(m.get(i), list.get(i));
        }
        try {
            m.get(list.size() + 1);
        } catch (Exception e) {
            // We expect this
            try {
                m.set(-1, "test");
            } catch (IndexOutOfBoundsException e1) {
                // We expect this
                return;
            }
        }
        fail("get/set beyond bounds of list");
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelBooleanListConstructor2() throws Exception {
        ListModel m = new ListModel(false, list);
        assertTrue(m.getSize() == list.size());
        for (int i = 0; i < m.getSize(); ++i) {
            assertEquals(m.get(i), list.get(i));
        }
        try {
            m.get(list.size() + 1);
        } catch (Exception e) {
            // We expect this
            try {
                m.set(-1, "test");
            } catch (IndexOutOfBoundsException e1) {
                // We expect this
                return;
            }
        }
        fail("get/set beyond bounds of list");
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelSelectors() throws Exception {
        for (int i = 0; i < listModel.size(); ++i) {
            assertEquals(listModel.get(i), new Integer(i));
        }
        for (int i = 0; i < listModel.size(); ++i) {
            listModel.set(i, new Integer(i + 100));
        }
        for (int i = 0; i < listModel.size(); ++i) {
            assertEquals(listModel.get(i), new Integer(i + 100));
        }
        listModel.setList(list);
        assertEquals(listModel.getList(), list);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelSetList() throws Exception {
        List o = new ArrayList(1);
        o.add(new Integer(99));

        listModel.setList(o);
        assertTrue(listModel.getSize() == 1);
        assertEquals(listModel.get(0), new Integer(99));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelChangeEventPropagation() throws Exception {
        listModel.set(0, model);
        listModel.addModelChangeListener(this);

        modelChanged = false;
        modelChangedName = "";

        submodel.setName("xxx");

        assertTrue(modelChanged);
        assertTrue(modelChangedName, modelChangedName.lastIndexOf("0.subModel.name") != -1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation1() throws Exception {
        listModel.addModelChangeListener(this);

        modelChanged = false;
        listModel.add("new");
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelChangeEventPropagation2() throws Exception {
        listModel.set(0, model);
        listModel.addModelChangeListener(this);

        modelChanged = false;
        modelChangedName = "";

        submodel.setName("xxx");

        assertTrue(modelChanged);
        assertTrue(modelChangedName, modelChangedName.lastIndexOf("0.subModel.name") != -1);

        modelChanged = false;
        listModel.remove(0);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListModelChangeEventPropagation3() throws Exception {
        listModel.set(0, model);
        listModel.addModelChangeListener(this);

        modelChanged = false;
        modelChangedName = "";

        submodel.setName("xxx");

        assertTrue(modelChanged);
        assertTrue(modelChangedName, modelChangedName.lastIndexOf("0.subModel.name") != -1);

        modelChanged = false;
        listModel.remove(model);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation3() throws Exception {
        listModel.addModelChangeListener(this);

        modelChanged = false;
        List l = new LinkedList();
        l.add(model);
        listModel.addAll(l);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation4() throws Exception {
        listModel.addModelChangeListener(this);

        listModel.add(model);
        listModel.add(submodel);

        modelChanged = false;
        List l = new LinkedList();
        l.add(model);
        listModel.retainAll(l);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation5() throws Exception {
        listModel.addModelChangeListener(this);

        listModel.add(model);
        listModel.add(submodel);

        modelChanged = false;
        List l = new LinkedList();
        l.add(model);
        listModel.removeAll(l);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation6() throws Exception {
        listModel.addModelChangeListener(this);
        listModel.add(model);
        listModel.add(submodel);

        modelChanged = false;
        listModel.clear();
        assertTrue(modelChanged);
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
        listModel = new ListModel(new LinkedList());
        for (int i = 0; i < 10; ++i) {
            listModel.add(new Integer(i));
        }

        model = new BasicTestModel("model");
        submodel = new BasicTestModel("submodel");
        model.setSubModel(submodel);

        list = new LinkedList();
        for (int i = 0; i < 10; ++i) {
            list.add("test" + i);
        }
        modelChanged = false;
        modelChangedName = "";
    }
}
