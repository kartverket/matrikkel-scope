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
 * $Id: TestSAbstractListModel.java,v 1.11 2002/11/20 00:19:56 ludovicc Exp $
 */
package test.view.swing;


import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import org.scopemvc.view.swing.SAbstractListModel;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.11 $ $Date: 2002/11/20 00:19:56 $
 * @created 10 September 2002
 */
public class TestSAbstractListModel extends TestCase {

    private SAbstractListModel listModel;
    private List list;
    private org.scopemvc.model.collection.ListModel list2;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSAbstractListModel object
     *
     * @param inName Name of the test
     */
    public TestSAbstractListModel(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        assertNull(listModel.getBoundModel());
        assertTrue(listModel.getSize() == 0);
        assertNull(listModel.getElementAt(0));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        listModel.setBoundModel(list);
        SuiteViewSwing.waitForAWT();

        assertSame(list, listModel.getBoundModel());
        assertTrue(listModel.getSize() == list.size());
        assertSame(list.get(0), listModel.getElementAt(0));
        assertSame(list.get(1), listModel.getElementAt(1));
        assertSame(list.get(2), listModel.getElementAt(2));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind2() throws Exception {
        listModel.setBoundModel(list2);
        SuiteViewSwing.waitForAWT();

        assertSame(list2, listModel.getBoundModel());
        assertTrue(listModel.getSize() == list2.size());
        assertSame(list2.get(0), listModel.getElementAt(0));
        assertSame(list2.get(1), listModel.getElementAt(1));
        assertSame(list2.get(2), listModel.getElementAt(2));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testModelChangeEvent() throws Exception {
        listModel.setBoundModel(list2);
        SuiteViewSwing.waitForAWT();

        Listener l = new Listener();
        listModel.addListDataListener(l);

        l.reset();
        list2.add("jkl");
        SuiteViewSwing.waitForAWT();

        assertEquals(ListDataEvent.INTERVAL_ADDED, l.type);
        assertEquals("index0: " + l.index0, 3, l.index0);
        assertEquals("index1: " + l.index1, 3, l.index1);

        l.reset();
        list2.remove("jkl");

        assertEquals(ListDataEvent.INTERVAL_REMOVED, l.type);
        assertEquals("index0: " + l.index0, 3, l.index0);
        assertEquals("index1: " + l.index1, 3, l.index1);

        l.reset();
        list2.set(1, "stu");

        assertEquals(ListDataEvent.CONTENTS_CHANGED, l.type);
        assertEquals("index0: " + l.index0, 1, l.index0);
        assertEquals("index1: " + l.index1, 1, l.index1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBadBind() throws Exception {
        listModel.setBoundModel(1);
        SuiteViewSwing.waitForAWT();

        try {
            assertTrue(listModel.getSize() == 0);
            assertNull(listModel.getElementAt(0));
        } catch (AssertionFailedError ex) {
            throw ex;
        } catch (IllegalArgumentException ok) {
            // expecting some kind of failure here!
            System.out.println("Caught excepted exception " + ok);
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testListPropertyBind() throws Exception {
        listModel.setSelector("stringNonIndexedProperty");
        listModel.setBoundModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(model.getStringNonIndexedProperty(), listModel.getShownModel());
        assertTrue(listModel.getSize() == model.getStringNonIndexedProperty().size());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testArrayPropertyBind() throws Exception {
        listModel.setSelector("stringNonIndexedProperty2");
        listModel.setBoundModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(model.getStringNonIndexedProperty2(), listModel.getShownModel());
        assertTrue("getSize: " + listModel.getSize() + ", length: " + model.getStringNonIndexedProperty2().length, listModel.getSize() == model.getStringNonIndexedProperty2().length);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        listModel.setSelector("stringNonIndexedProperty");
        listModel.setBoundModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(model, listModel.getBoundModel());
        assertSame(model.getStringNonIndexedProperty(), listModel.getShownModel());

        ArrayList oldList = model.getStringNonIndexedProperty();
        ArrayList newList = new ArrayList();
        model.setStringNonIndexedProperty(newList);

        assertSame(oldList, listModel.getShownModel());

        listModel.refresh();
        SuiteViewSwing.waitForAWT();

        assertSame(newList, listModel.getShownModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testIndexed() throws Exception {
        listModel.setSelector("hiddenStringIndexedProperty");
        listModel.setSizeSelector("hiddenStringIndexedPropertySize");
        listModel.setBoundModel(model);
        SuiteViewSwing.waitForAWT();

        assertEquals(model.getHiddenStringIndexedPropertySize(), listModel.getSize());

        assertSame(model.getHiddenStringIndexedProperty(0), listModel.getElementAt(0));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFixedListModel() throws Exception {
        listModel.setListModel(list2);
        listModel.setSelector("stringNonIndexedProperty");
        listModel.setBoundModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(list2, listModel.getShownModel());

        model.setStringNonIndexedProperty(new ArrayList());
        SuiteViewSwing.waitForAWT();

        assertSame(list2, listModel.getShownModel());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        listModel =
            new SAbstractListModel() {
            };

        Object[] strings = {"abc", "def", "xyz"};
        list = Arrays.asList(strings);

        strings = new Object[]{"123", "456", "789"};
        list2 = new org.scopemvc.model.collection.ListModel(new ArrayList(Arrays.asList(strings)));

        model = new SwingDummyModel();
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() { }


    static class Listener implements ListDataListener {
        int index0;
        int index1;
        int type;

        /**
         * TODO: document the method
         *
         * @param e TODO: Describe the Parameter
         */
        public void intervalAdded(ListDataEvent e) {
            index0 = e.getIndex0();
            index1 = e.getIndex1();
            type = e.getType();
        }

        /**
         * TODO: document the method
         *
         * @param e TODO: Describe the Parameter
         */
        public void intervalRemoved(ListDataEvent e) {
            index0 = e.getIndex0();
            index1 = e.getIndex1();
            type = e.getType();
        }

        /**
         * TODO: document the method
         *
         * @param e TODO: Describe the Parameter
         */
        public void contentsChanged(ListDataEvent e) {
            index0 = e.getIndex0();
            index1 = e.getIndex1();
            type = e.getType();
        }

        /**
         * TODO: document the method
         */
        void reset() {
            index0 = -99;
            index1 = -99;
            type = -99;
        }
    }
}
