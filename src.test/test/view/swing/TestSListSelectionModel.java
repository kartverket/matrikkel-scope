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
package test.view.swing;


import java.awt.Dimension;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SList;
import org.scopemvc.view.swing.SListSelectionModel;
import org.scopemvc.view.swing.SPanel;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.10 $ $Date: 2002/11/20 00:19:56 $
 * @created 18 September 2002
 */
public final class TestSListSelectionModel extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSListSelectionModel.class);

    private SList list1;
    private SListSelectionModel listSelection1;

    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSListSelectionModel object
     *
     * @param inName Name of the test
     */
    public TestSListSelectionModel(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound1() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertNull(list1.getSelectedValue());
        assertNull(listSelection1.getViewValue());
        assertTrue(listSelection1.getMinSelectionIndex() == -1);

        listSelection1.setSelector(Selector.fromString("stringProperty"));
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("stringProperty"), listSelection1.getSelector());
        assertNull(list1.getSelectedValue());
        assertNull(listSelection1.getViewValue());
        assertTrue(listSelection1.getMinSelectionIndex() == -1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        listSelection1.setSelector(Selector.fromString("stringProperty"));
        listSelection1.setBoundModel(model);

        list1.setListModel(new String[]{"abc", "xyz"});
        SuiteViewSwing.waitForAWT();

        list1.setSelectedIndex(0);
        SuiteViewSwing.waitForAWT();
        assertEquals("abc", model.getStringProperty());

        list1.setSelectedIndex(1);
        SuiteViewSwing.waitForAWT();
        assertEquals("xyz", model.getStringProperty());

        list1.clearSelection();
        SuiteViewSwing.waitForAWT();
        assertNull(model.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConvenience() throws Exception {
        listSelection1.setSelector("stringProperty");
        assertEquals(Selector.fromString("stringProperty"), listSelection1.getSelector());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind12() throws Exception {
        listSelection1.setSelector(Selector.fromString("stringProperty"));
        listSelection1.setBoundModel(model);

        list1.setListModel(new String[]{"abc", "xyz"});
        SuiteViewSwing.waitForAWT();

        model.setStringProperty("xyz");
        SuiteViewSwing.waitForAWT();
        assertTrue(list1.getSelectedIndex() == 1);
        assertEquals("xyz", list1.getSelectedValue());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(list1.getSelectedIndex() == 0);
        assertEquals("abc", list1.getSelectedValue());

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(list1.getSelectedIndex() == -1);
        assertNull(list1.getSelectedValue());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(list1.getSelectedIndex() == 0);
        assertEquals("abc", list1.getSelectedValue());

        model.setStringProperty("def");
        SuiteViewSwing.waitForAWT();
        assertTrue("selection: " + list1.getSelectedIndex(), list1.getSelectedIndex() == -1);
        assertNull(list1.getSelectedValue());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testReadOnlyBind() throws Exception {
        list1.setListModel(new Boolean[]{Boolean.TRUE, Boolean.FALSE});
        listSelection1.setSelector(Selector.fromString("booleanReadOnlyProperty"));
        listSelection1.setBoundModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!list1.isEnabled());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        listSelection1.setSelector(Selector.fromString("stringProperty"));
        listSelection1.setBoundModel(model);

        list1.setListModel(new String[]{"abc", "xyz"});
        SuiteViewSwing.waitForAWT();

        list1.setSelectedIndex(0);
        SuiteViewSwing.waitForAWT();
        assertEquals("abc", model.getStringProperty());

        list1.setSelectedIndex(1);
        SuiteViewSwing.waitForAWT();
        assertEquals("xyz", model.getStringProperty());

        list1.clearSelection();
        SuiteViewSwing.waitForAWT();
        assertNull(model.getStringProperty());

        list1.setListModel(new String[]{"abc", "xyz"});
        SuiteViewSwing.waitForAWT();

        model.setStringProperty("xyz");
        SuiteViewSwing.waitForAWT();
        assertNull(list1.getSelectedValue());

        listSelection1.refresh();
        assertTrue(list1.getSelectedIndex() == 1);
        assertEquals("xyz", list1.getSelectedValue());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(list1.getSelectedIndex() == 1);
        assertEquals("xyz", list1.getSelectedValue());

        listSelection1.refresh();
        assertTrue(list1.getSelectedIndex() == 0);
        assertEquals("abc", list1.getSelectedValue());

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(list1.getSelectedIndex() == 0);
        assertEquals("abc", list1.getSelectedValue());

        listSelection1.refresh();
        assertTrue(list1.getSelectedIndex() == -1);
        assertNull(list1.getSelectedValue());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(list1.getSelectedIndex() == -1);
        assertNull(list1.getSelectedValue());

        listSelection1.refresh();
        assertTrue(list1.getSelectedIndex() == 0);
        assertEquals("abc", list1.getSelectedValue());

        model.setStringProperty("def");
        SuiteViewSwing.waitForAWT();
        assertTrue(list1.getSelectedIndex() == 0);
        assertEquals("abc", list1.getSelectedValue());

        listSelection1.refresh();
        assertTrue(list1.getSelectedIndex() == -1);
        assertNull(list1.getSelectedValue());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testMultipleViewToModel() throws Exception {
        listSelection1.setSelector(Selector.fromString("selections"));
        listSelection1.setBoundModel(model);

        list1.setListModel(new String[]{"abc", "def", "ghi", "xyz"});
        list1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        SuiteViewSwing.waitForAWT();

        list1.setSelectedIndex(0);
        SuiteViewSwing.waitForAWT();
        assertTrue(model.getSelections() != null);
        assertTrue(model.getSelections().size() == 1);
        assertTrue(model.getSelections().contains("abc"));

        list1.setSelectionInterval(1, 2);
        SuiteViewSwing.waitForAWT();
        assertTrue(model.getSelections() != null);
        assertTrue(model.getSelections().size() == 2);
        assertTrue(model.getSelections().contains("def"));
        assertTrue(model.getSelections().contains("ghi"));

        list1.clearSelection();
        SuiteViewSwing.waitForAWT();
        assertTrue(model.getSelections() != null);
        assertTrue("selections: " + model.getSelections(), model.getSelections().isEmpty());

        list1.setSelectionInterval(0, 3);
        SuiteViewSwing.waitForAWT();
        assertTrue(model.getSelections() != null);
        assertTrue(model.getSelections().size() == 4);
        assertTrue(model.getSelections().contains("abc"));
        assertTrue(model.getSelections().contains("def"));
        assertTrue(model.getSelections().contains("ghi"));
        assertTrue(model.getSelections().contains("xyz"));
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testMultipleIntervalViewToModel() throws Exception {
        listSelection1.setSelector(Selector.fromString("selections"));
        listSelection1.setBoundModel(model);

        list1.setListModel(new String[]{"abc", "def", "ghi", "xyz"});
        list1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        SuiteViewSwing.waitForAWT();

        list1.addSelectionInterval(0, 0);
        SuiteViewSwing.waitForAWT();
        assertTrue(model.getSelections() != null);
        assertTrue(model.getSelections().size() == 1);
        assertTrue(model.getSelections().contains("abc"));

        list1.addSelectionInterval(2, 3);
        SuiteViewSwing.waitForAWT();
        assertTrue(model.getSelections() != null);
        assertEquals(3, model.getSelections().size());
        assertTrue(model.getSelections().contains("abc"));
        assertTrue(!model.getSelections().contains("def"));
        // should not be selected
        assertTrue(model.getSelections().contains("ghi"));
        assertTrue(model.getSelections().contains("xyz"));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testMultipleModelToView1() throws Exception {
        listSelection1.setSelector(Selector.fromString("selections"));
        listSelection1.setBoundModel(model);

        list1.setListModel(new String[]{"abc", "def", "ghi", "xyz"});
        list1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        SuiteViewSwing.waitForAWT();

        assertTrue(list1.getSelectedIndex() == -1);

        HashSet set = new HashSet();
        set.add("def");
        set.add("ghi");
        model.setSelections(set);
        SuiteViewSwing.waitForAWT();

        assertTrue(listSelection1.getMinSelectionIndex() == 1);
        assertTrue(listSelection1.getMaxSelectionIndex() == 2);

        model.setSelections(new HashSet());
        SuiteViewSwing.waitForAWT();

        assertTrue(list1.getSelectedIndex() == -1);

        set = new HashSet();
        set.add("xyz");
        set.add("abc");
        model.setSelections(set);
        SuiteViewSwing.waitForAWT();

        assertTrue(listSelection1.getMinSelectionIndex() == 0);
        assertTrue(listSelection1.getMaxSelectionIndex() == 3);

        model.setSelections(null);
        SuiteViewSwing.waitForAWT();

        assertTrue(list1.getSelectedIndex() == -1);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testMultipleIntervalModelToView1() throws Exception {
        listSelection1.setSelector(Selector.fromString("selections"));
        listSelection1.setBoundModel(model);

        list1.setListModel(new String[]{"abc", "def", "ghi", "xyz"});
        list1.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        HashSet multiSet = new HashSet();
        multiSet.add("xyz");
        multiSet.add("abc");
        model.setSelections(multiSet);
        SuiteViewSwing.waitForAWT();
        assertEquals(0, listSelection1.getMinSelectionIndex());
        assertEquals(3, listSelection1.getMaxSelectionIndex());
        assertTrue(listSelection1.isSelectedIndex(0));
        assertTrue(!listSelection1.isSelectedIndex(1));
        // should not be selected
        assertTrue(!listSelection1.isSelectedIndex(2));
        // should not be selected
        assertTrue(listSelection1.isSelectedIndex(3));

    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testMultipleRefresh() throws Exception {
        listSelection1.setSelector(Selector.fromString("selections"));
        listSelection1.setBoundModel(model);

        list1.setListModel(new String[]{"abc", "def", "ghi", "xyz"});
        list1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        model.setSelections(new HashSet());
        SuiteViewSwing.waitForAWT();

        assertTrue(list1.getSelectedIndex() == -1);

        model.getSelections().add("def");
        model.getSelections().add("ghi");
        listSelection1.refresh();
        SuiteViewSwing.waitForAWT();

        assertTrue(listSelection1.getMinSelectionIndex() == 1);
        assertTrue(listSelection1.getMaxSelectionIndex() == 2);

        model.getSelections().clear();
        listSelection1.refresh();
        SuiteViewSwing.waitForAWT();

        assertTrue(list1.getSelectedIndex() == -1);

        model.getSelections().add("xyz");
        model.getSelections().add("abc");
        listSelection1.refresh();
        SuiteViewSwing.waitForAWT();

        assertTrue(listSelection1.getMinSelectionIndex() == 0);
        assertTrue(listSelection1.getMaxSelectionIndex() == 3);
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        list1 = new SList();
        list1.setPreferredSize(new Dimension(50, 70));
        listSelection1 = new SListSelectionModel(list1, true);
        list1.setSelectionModel(listSelection1);

        view = new SPanel();
        view.add(list1);

        controller = new SwingDummyController();
        controller.setView(view);
        controller.startup();
        // does showView()

        model = new SwingDummyModel();
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        controller.shutdown();
    }
}
