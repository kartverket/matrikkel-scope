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
 * $Id: TestSTable.java,v 1.15 2002/11/20 00:19:56 ludovicc Exp $
 */
package test.view.swing;


import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
//import junit.extensions.jfcunit.JTableMouseEventData;
import junit.extensions.jfcunit.TestHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.Selector;
import org.scopemvc.model.collection.ListModel;
import org.scopemvc.view.swing.SListSelectionModel;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STable;
import org.scopemvc.view.swing.STableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * <P>
 *
 * </P>
 *
 * Changes:
 *  - Not using RobotTestHelper fixes flaky test {@link #testControlIssue()}
 *
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.15 $ $Date: 2002/11/20 00:19:56 $
 * @created 18 September 2002
 */
public final class TestSTable extends JFCTestCase {

    private static final Log LOG = LogFactory.getLog(TestSTable.class);

    private STable table;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;
    private TestHelper helper;


    /**
     * Constructor for the TestSTable object
     *
     * @param inName Name of the test
     */
    public TestSTable(String inName) {
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
        assertTrue(table.getModel() instanceof STableModel);
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

        assertEquals(0, ((STableModel) table.getModel()).getColumnCount());
        table.setColumnSelectors(new String[]{"a", "b", "c"});
        assertEquals(3, ((STableModel) table.getModel()).getColumnCount());

        table.setColumnSelectors(new Selector[]{s});
        assertEquals(1, ((STableModel) table.getModel()).getColumnCount());

        table.setColumnNames(new String[]{"a", "b", "c"});
        assertEquals("a", ((STableModel) table.getModel()).getColumnName(0));
        assertEquals("b", ((STableModel) table.getModel()).getColumnName(1));
        assertEquals("c", ((STableModel) table.getModel()).getColumnName(2));
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
        assertEquals(Selector.fromString("subModels"), ((STableModel) table.getModel()).getSelector());
        assertNull(((STableModel) table.getModel()).getShownModel());
        assertEquals(0, ((STableModel) table.getModel()).getRowCount());
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
        assertEquals(Selector.fromString("subModels"), ((STableModel) table.getModel()).getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, table.getBoundModel());
        assertSame(model.getSubModels(), ((STableModel) table.getModel()).getShownModel());
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
        assertSame(model.getSubModels(), ((STableModel) table.getModel()).getShownModel());
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
        assertEquals(Selector.fromString("subModels"), ((STableModel) table.getModel()).getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, table.getBoundModel());
        assertSame(model.getSubModels(), ((STableModel) table.getModel()).getShownModel());
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
     * @todo This is broken: see STableModel.setShownModel
     * @throws Exception TODO: Describe the Exception
     */
    public void testKeepSelection1() throws Exception {
//        table.setSelector(Selector.fromString("stringNonIndexedProperty"));
//        table.setSelectionSelector(Selector.fromString("stringProperty"));
//        controller.setModel(model);
//        SuiteViewSwing.waitForAWT();
//
//        table.setRowSelectionInterval(1, 1);
//        java.util.ArrayList newList = (ArrayList)model.getStringNonIndexedProperty().clone();
//        model.setStringNonIndexedProperty(newList);
//        SuiteViewSwing.waitForAWT();
//        assertTrue("getSelectedIndex == " + table.getSelectedRow(), table.getSelectedRow() == 1);
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


//    /**
//     * A unit test for JUnit
//     *
//     * @throws Exception Any abnormal exception
//     */
//    public void testControlIssue() throws Exception {
//        table.setSelector(Selector.fromString("subModels"));
//        //table.setSelectionSelector("stringProperty");
//        table.setColumnSelectors(new String[]{"stringProperty", "intProperty"});
//        table.setColumnNames(new String[]{"stringProperty", "intProperty"});
//        table.setChangeSelectionControlID("changeSelection");
//        table.setDoubleClickControlID("doubleClick");
//        // force the columns to be non-editable
//        ((STableModel) table.getModel()).setEditableColumns(new boolean[]{false, false});
//        assertEquals(Selector.fromString("subModels"), ((STableModel) table.getModel()).getSelector());
//
//        controller.setModel(model);
//        SuiteViewSwing.waitForAWT();
//
//        helper.enterClickAndLeave(new JTableMouseEventData(this, table, 0, 0, 1));
//        helper.enterClickAndLeave(new JTableMouseEventData(this, table, 1, 0, 1));
//
//        SuiteViewSwing.waitForAWT();
//        assertEquals(new Control("changeSelection"), controller.lastControl);
//
//        Thread.sleep(1000);
//        helper.enterClickAndLeave(new JTableMouseEventData(this, table, 1, 0, 2));
//
//        SuiteViewSwing.waitForAWT();
//        Thread.sleep(1000);
//
//        assertEquals(new Control("doubleClick"), controller.lastControl);
//
//    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        table.setSelector(Selector.fromString("subModels"));
        //table.setSelectionSelector("stringProperty");
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
        assertTrue(model.getSubModels() != ((STableModel) table.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) table.getSelectionModel()).getBoundModel());

        view.refresh();

        assertSame(model.getSubModels(), ((STableModel) table.getModel()).getShownModel());
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getStringProperty(), table.getValueAt(0, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getStringProperty(), table.getValueAt(1, 0));
        assertEquals(((SwingDummyModel) model.getSubModels().get(0)).getIntProperty(), ((Integer) table.getValueAt(0, 1)).intValue());
        assertEquals(((SwingDummyModel) model.getSubModels().get(1)).getIntProperty(), ((Integer) table.getValueAt(1, 1)).intValue());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        helper = new JFCTestHelper();

        table = new STable();
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

//    private void testRenderer0(Class valClass, Class convClass)
//            throws Exception {
//        Object rend;
//        rend = table.getDefaultRenderer(valClass);
//        assertTrue(rend != null);
//        assertEquals(rend.getClass(), SDefaultTableCellRenderer.class);
//        StringConvertor conv = ((SDefaultTableCellRenderer)rend)
//                                        .getStringConvertor();
//        assertTrue(conv != null);
//        assertEquals(conv.getClass(), convClass);
//
//    }

//    public void testDefaultCellRenderers() throws Exception {
//        testRenderer0(java.util.Date.class, DateStringConvertor.class);
//        testRenderer0(org.scopemvc.util.DateTime.class, DateTimeStringConvertor.class);
//        testRenderer0(org.scopemvc.util.Time.class, TimeStringConvertor.class);
//        testRenderer0(Double.class, DoubleStringConvertor.class);
//        testRenderer0(Float.class, FloatStringConvertor.class);
//        testRenderer0(Long.class, LongStringConvertor.class);
//        testRenderer0(Integer.class, IntegerStringConvertor.class);
//        testRenderer0(String.class, StringStringConvertor.class);
//        testRenderer0(java.math.BigDecimal.class, BigDecimalStringConvertor.class);
//        testRenderer0(java.math.BigInteger.class, BigIntegerStringConvertor.class);
//    }

//    private void testEditor0(Class valClass, Class convClass)
//            throws Exception {
//        Object ed;
//        ed = table.getDefaultEditor(valClass);
//        assertTrue(ed != null);
//        assertEquals(ed.getClass(), STableTextCellEditor.class);
//        StringConvertor conv = ((STableTextCellEditor)ed)
//                                        .getStringConvertor();
//        assertTrue(conv != null);
//        assertEquals(conv.getClass(), convClass);
//
//    }
//
//    public void testDefaultCellEditors() throws Exception {
//        testEditor0(java.util.Date.class, DateStringConvertor.class);
//        testEditor0(org.scopemvc.util.DateTime.class, DateTimeStringConvertor.class);
//        testEditor0(org.scopemvc.util.Time.class, TimeStringConvertor.class);
//        testEditor0(Double.class, DoubleStringConvertor.class);
//        testEditor0(Float.class, FloatStringConvertor.class);
//        testEditor0(Long.class, LongStringConvertor.class);
//        testEditor0(Integer.class, IntegerStringConvertor.class);
//        testEditor0(String.class, StringStringConvertor.class);
//        testEditor0(java.math.BigDecimal.class, BigDecimalStringConvertor.class);
//        testEditor0(java.math.BigInteger.class, BigIntegerStringConvertor.class);
//    }

}
