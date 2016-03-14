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
import java.util.ArrayList;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.JTableHeaderMouseEventData;
import junit.extensions.jfcunit.TestHelper;
import org.scopemvc.core.Selector;
import org.scopemvc.model.collection.ListModel;
import org.scopemvc.view.swing.*;

/**
 * Unit test for SSortTable
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.12 $ $Date: 2002/09/25 13:53:10 $
 * @created 18 September 2002
 */
public final class TestSSortTable extends JFCTestCase {

    private static final Log LOG = LogFactory.getLog(TestSSortTable.class);

    private SSortTable table;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;
    private TestHelper helper;


    /**
     * Constructor for the TestSSortTable object
     *
     * @param inName Name of the test
     */
    public TestSSortTable(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCreateSelectionModel1() throws Exception {
        assertTrue(table.getSelectionModel() instanceof DefaultListSelectionModel);

        table.setSelectionSelector("a");
        assertTrue(table.getSelectionModel() instanceof SListSelectionModel);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCreateSelectionModel2() throws Exception {
        assertTrue(table.getSelectionModel() instanceof DefaultListSelectionModel);

        table.setSelectionSelector(Selector.fromString("a"));
        assertTrue(table.getSelectionModel() instanceof SListSelectionModel);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCreateTableModel() throws Exception {
        assertTrue(table.getModel() instanceof SSortTableModel);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetup() throws Exception {
        Selector s = Selector.fromString("xyz");

        table.setSelectionSelector(s);
        assertSame(s, ((SListSelectionModel) table.getSelectionModel()).getSelector());

        assertEquals(0, ((SSortTableModel) table.getModel()).getColumnCount());
        table.setColumnSelectors(new String[]{"a", "b", "c"});
        assertEquals(3, ((SSortTableModel) table.getModel()).getColumnCount());

        table.setColumnSelectors(new Selector[]{s});
        assertEquals(1, ((SSortTableModel) table.getModel()).getColumnCount());

        table.setColumnNames(new String[]{"a", "b", "c"});
        assertEquals("a", ((SSortTableModel) table.getModel()).getColumnName(0));
        assertEquals("b", ((SSortTableModel) table.getModel()).getColumnName(1));
        assertEquals("c", ((SSortTableModel) table.getModel()).getColumnName(2));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFindIndexFor() throws Exception {
        table.setSelector("stringNonIndexedProperty");
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertEquals(0, table.findIndexFor(model.getStringNonIndexedProperty().get(0)));
        assertEquals(1, table.findIndexFor(model.getStringNonIndexedProperty().get(1)));
        assertEquals(-1, table.findIndexFor(null));
        assertEquals(-1, table.findIndexFor("x"));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFindElementAt() throws Exception {
        table.setSelector("stringNonIndexedProperty");
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertEquals(model.getStringNonIndexedProperty().get(0), table.findElementAt(0));
        assertEquals(model.getStringNonIndexedProperty().get(1), table.findElementAt(1));
        try {
            assertNull(table.findElementAt(20));
        } catch (IndexOutOfBoundsException okEx) {
            // expected
        }
        try {
            assertNull(table.findElementAt(-1));
        } catch (IndexOutOfBoundsException okEx) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertNull(table.getBoundModel());

        table.setSelector(Selector.fromString("subModels"));
        table.setColumnSelectors(new String[]{"stringProperty", "intProperty"});
        table.setColumnNames(new String[]{"stringProperty", "intProperty"});
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("subModels"), ((SSortTableModel) table.getModel()).getSelector());
        assertNull(((SSortTableModel) table.getModel()).getShownModel());
        assertEquals(0, ((SSortTableModel) table.getModel()).getRowCount());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        table.setSelector(Selector.fromString("subModels"));
        table.setSelectionSelector("stringProperty");
        table.setColumnSelectors(new String[]{"stringProperty", "intProperty"});
        table.setColumnNames(new String[]{"stringProperty", "intProperty"});
        assertEquals(Selector.fromString("subModels"), ((SSortTableModel) table.getModel()).getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, table.getBoundModel());
        assertSame(model.getSubModels(), ((SSortTableModel) table.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) table.getSelectionModel()).getBoundModel());

        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind2() throws Exception {
        table.setSelector(Selector.fromString("subModels"));
        table.setSelectionSelector("stringProperty");
        table.setColumnSelectors(new String[]{"stringProperty", "intProperty"});
        table.setColumnNames(new String[]{"stringProperty", "intProperty"});
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        ListModel newModels = new ListModel();
        newModels.add(new SwingDummyModel("aaa", 111));
        newModels.add(new SwingDummyModel("bbb", 222));

        model.setSubModels(newModels);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, table.getBoundModel());
        assertSame(model.getSubModels(), ((SSortTableModel) table.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) table.getSelectionModel()).getBoundModel());

        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind3() throws Exception {
        table.setSelector(Selector.fromString("subModels"));
        table.setSelectionSelector("stringProperty");
        table.setColumnSelectors(new String[]{"stringProperty", "intProperty"});
        table.setColumnNames(new String[]{"stringProperty", "intProperty"});
        assertEquals(Selector.fromString("subModels"), ((SSortTableModel) table.getModel()).getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, table.getBoundModel());
        assertSame(model.getSubModels(), ((SSortTableModel) table.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) table.getSelectionModel()).getBoundModel());

        model.getSubModels().add(new SwingDummyModel("xyz", 123));
        SuiteViewSwing.waitForAWT();

        assertEquals(((SwingDummyModel) model.getSubModels().get(3)).getStringProperty(), table.getValueAt(3, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(3)).getIntProperty(), ((Integer) table.getValueAt(3, 1)).intValue());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testValidation() throws Exception {
        model.getStringNonIndexedProperty().add("Illegal");
        table.setSelector(Selector.fromString("stringNonIndexedProperty"));
        table.setSelectionSelector(Selector.fromString("stringProperty"));
        model.setStringProperty(null);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        table.setRowSelectionInterval(2, 2);
        SuiteViewSwing.waitForAWT();
        assertTrue(table.getSelectedRow() == 2);
        assertTrue("getStringProperty <" + model.getStringProperty() + ">", model.getStringProperty() == null);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testKeepSelection2() throws Exception {
        table.setSelector(Selector.fromString("stringNonIndexedProperty"));
        table.setSelectionSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        table.setRowSelectionInterval(1, 1);
        ArrayList newList = new ArrayList(java.util.Arrays.asList(new String[]{"1", "2", "3"}));
        model.setStringNonIndexedProperty(newList);
        SuiteViewSwing.waitForAWT();
        assertTrue("getSelectedIndex == " + table.getSelectedRow(), table.getSelectedRow() == -1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        table.setSelector(Selector.fromString("subModels"));
        table.setSelectionSelector("stringProperty");
        table.setColumnSelectors(new String[]{"stringProperty", "intProperty"});
        table.setColumnNames(new String[]{"stringProperty", "intProperty"});
        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();
        model.initSubModels();
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        ArrayList newModels = new ArrayList();
        newModels.add(new SwingDummyModel("aaa", 111));
        newModels.add(new SwingDummyModel("bbb", 222));

        model.setSubModels(newModels);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, table.getBoundModel());
        assertTrue(model.getSubModels() != ((SSortTableModel) table.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) table.getSelectionModel()).getBoundModel());

        view.refresh();

        assertSame(model.getSubModels(), ((SSortTableModel) table.getModel()).getShownModel());
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSort() throws Exception {
        table.setSelector(Selector.fromString("subModels"));
        table.setSelectionSelector("stringProperty");
        table.setColumnSelectors(new String[]{"stringProperty", "intProperty"});
        table.setColumnNames(new String[]{"stringProperty", "intProperty"});
        assertEquals(Selector.fromString("subModels"), ((SSortTableModel) table.getModel()).getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, table.getBoundModel());
        assertSame(model.getSubModels(), ((SSortTableModel) table.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) table.getSelectionModel()).getBoundModel());

        // First column (string property)
        // first click: ascending
        helper.enterClickAndLeave(new JTableHeaderMouseEventData(this, table.getTableHeader(), 0, 1));

        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getStringProperty(), table.getValueAt(2, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getIntProperty(), ((Integer) table.getValueAt(2, 1)).intValue());

        // second click: descending
        helper.enterClickAndLeave(new JTableHeaderMouseEventData(this, table.getTableHeader(), 0, 1));

        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(2, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(2, 1)).intValue());

        // third click: no sort
        helper.enterClickAndLeave(new JTableHeaderMouseEventData(this, table.getTableHeader(), 0, 1));

        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getStringProperty(), table.getValueAt(2, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getIntProperty(), ((Integer) table.getValueAt(2, 1)).intValue());

        // Second column (int property)
        // first click: ascending
        helper.enterClickAndLeave(new JTableHeaderMouseEventData(this, table.getTableHeader(), 1, 1));

        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getStringProperty(), table.getValueAt(2, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getIntProperty(), ((Integer) table.getValueAt(2, 1)).intValue());

        // second click: descending
        helper.enterClickAndLeave(new JTableHeaderMouseEventData(this, table.getTableHeader(), 1, 1));

        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(2, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(2, 1)).intValue());

        // third click: no sort
        helper.enterClickAndLeave(new JTableHeaderMouseEventData(this, table.getTableHeader(), 1, 1));

        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getStringProperty(), table.getValueAt(2, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(2)).getIntProperty(), ((Integer) table.getValueAt(2, 1)).intValue());

    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        helper = new JFCTestHelper();

        table = new SSortTable();
        table.setPreferredSize(new Dimension(100, 60));

        view = new SPanel();
        view.add(new JScrollPane(table));

        controller = new SwingDummyController();
        controller.setView(view);
        controller.startup();
        // does showView()

        model = new SwingDummyModel();
        model.initSubModels();
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        controller.shutdown();
    }

}
