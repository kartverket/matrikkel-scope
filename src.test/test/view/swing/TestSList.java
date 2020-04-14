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
 * $Id: TestSList.java,v 1.12 2002/11/20 00:19:56 ludovicc Exp $
 */
package test.view.swing;


import java.awt.Dimension;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SList;
import org.scopemvc.view.swing.SListCellRenderer;
import org.scopemvc.view.swing.SListModel;
import org.scopemvc.view.swing.SListSelectionModel;
import org.scopemvc.view.swing.SPanel;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.12 $ $Date: 2002/11/20 00:19:56 $
 * @created 18 September 2002
 */
public final class TestSList extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSList.class);

    private SList list;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSList object
     *
     * @param inName Name of the test
     */
    public TestSList(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCreateSelectionModel() throws Exception {
        assertTrue(list.getSelectionModel() instanceof SListSelectionModel);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCreateRenderer() throws Exception {
        assertTrue(list.getCellRenderer() instanceof SListCellRenderer);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCreateListModel() throws Exception {
        assertTrue(list.getModel() instanceof SListModel);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetup() throws Exception {
        Selector s = Selector.fromString("xyz");

        assertNull(((SListSelectionModel) list.getSelectionModel()).getSelector());
        list.setSelectionSelector(s);
        assertSame(s, ((SListSelectionModel) list.getSelectionModel()).getSelector());

        assertNull(((SListCellRenderer) list.getCellRenderer()).getTextSelector());
        list.setRendererSelector(s);
        assertSame(s, ((SListCellRenderer) list.getCellRenderer()).getTextSelector());

        assertNull(((SListCellRenderer) list.getCellRenderer()).getIconSelector());
        list.setRendererIconSelector(s);
        assertSame(s, ((SListCellRenderer) list.getCellRenderer()).getIconSelector());

        assertNull(((SListModel) list.getModel()).getSizeSelector());
        list.setSizeSelector(s);
        assertSame(s, ((SListModel) list.getModel()).getSizeSelector());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetup2() throws Exception {
        Selector s = Selector.fromString("xyz");

        assertNull(((SListSelectionModel) list.getSelectionModel()).getSelector());
        list.setSelectionSelector("xyz");
        assertEquals(s, ((SListSelectionModel) list.getSelectionModel()).getSelector());

        assertNull(((SListCellRenderer) list.getCellRenderer()).getTextSelector());
        list.setRendererSelector("xyz");
        assertEquals(s, ((SListCellRenderer) list.getCellRenderer()).getTextSelector());

        assertNull(((SListCellRenderer) list.getCellRenderer()).getIconSelector());
        list.setRendererIconSelector("xyz");
        assertEquals(s, ((SListCellRenderer) list.getCellRenderer()).getIconSelector());

        assertNull(((SListModel) list.getModel()).getSizeSelector());
        list.setSizeSelector("xyz");
        assertEquals(s, ((SListModel) list.getModel()).getSizeSelector());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetup3() throws Exception {
        Object[] l = {"a", "b"};

        assertEquals(0, list.getModel().getSize());
        list.setListModel(l);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertEquals(2, list.getModel().getSize());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFindIndexFor() throws Exception {
        Object[] l = {"a", "b"};
        list.setListModel(l);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertEquals(0, list.findIndexFor("a"));
        assertEquals(1, list.findIndexFor("b"));
        assertEquals(-1, list.findIndexFor(null));
        assertEquals(-1, list.findIndexFor("x"));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFindElementAt() throws Exception {
        Object[] l = {"a", "b"};
        list.setListModel(l);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertEquals("a", list.findElementAt(0));
        assertEquals("b", list.findElementAt(1));
        try {
            assertNull(list.findElementAt(2));
        } catch (IndexOutOfBoundsException okEx) {
            // expected
        }
        try {
            assertNull(list.findElementAt(-1));
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
        list.setSelectionModel(new SListSelectionModel(list, true));
        SuiteViewSwing.waitForAWT();
        assertTrue(!list.isEnabled());
        assertNull(list.getBoundModel());

        list.setSelector(Selector.fromString("stringNonIndexedProperty"));
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("stringNonIndexedProperty"), ((SListModel) list.getModel()).getSelector());
        assertTrue(!list.isEnabled());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        list.setSelectionModel(new SListSelectionModel(list, true));
        list.setSelector(Selector.fromString("stringNonIndexedProperty"));
        assertEquals(Selector.fromString("stringNonIndexedProperty"), ((SListModel) list.getModel()).getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, list.getBoundModel());
        assertTrue(!list.isEnabled());
        // because no selection
        assertSame(model.getStringNonIndexedProperty(), ((SListModel) list.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) list.getSelectionModel()).getBoundModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind2() throws Exception {
        list.setSelectionModel(new SListSelectionModel(list, true));
        list.setSelector(Selector.fromString("stringNonIndexedProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertTrue(!list.isEnabled());

        ArrayList newList = new ArrayList(java.util.Arrays.asList(new String[]{"1", "2", "3"}));
        model.setStringNonIndexedProperty(newList);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, list.getBoundModel());
        assertTrue(!list.isEnabled());
        // because no selection
        assertSame(newList, ((SListModel) list.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) list.getSelectionModel()).getBoundModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testValidation() throws Exception {
        model.getStringNonIndexedProperty().add("Illegal");
        list.setSelector(Selector.fromString("stringNonIndexedProperty"));
        list.setSelectionSelector(Selector.fromString("stringProperty"));
        model.setStringProperty(null);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        list.setSelectedIndex(2);
        SuiteViewSwing.waitForAWT();
        assertTrue(list.getSelectedIndex() == 2);
        assertTrue("getStringProperty <" + model.getStringProperty() + ">", model.getStringProperty() == null);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testKeepSelection1() throws Exception {
        list.setSelector(Selector.fromString("stringNonIndexedProperty"));
        list.setSelectionSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertTrue(list.isEnabled());

        list.setSelectedIndex(1);
        java.util.ArrayList newList = (ArrayList) model.getStringNonIndexedProperty().clone();
        model.setStringNonIndexedProperty(newList);
        SuiteViewSwing.waitForAWT();
        assertTrue("getSelectedIndex == " + list.getSelectedIndex(), list.getSelectedIndex() == 1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testKeepSelection2() throws Exception {
        list.setSelector(Selector.fromString("stringNonIndexedProperty"));
        list.setSelectionSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        list.setSelectedIndex(1);
        ArrayList newList = new ArrayList(java.util.Arrays.asList(new String[]{"1", "2", "3"}));
        model.setStringNonIndexedProperty(newList);
        SuiteViewSwing.waitForAWT();
        assertTrue("getSelectedIndex == " + list.getSelectedIndex() + ", " + list.getSelectedValue(), list.getSelectedIndex() == -1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlIssue() throws Exception {
        // ***** How to implement?
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        list.setSelector(Selector.fromString("stringNonIndexedProperty"));
        list.setSelectionSelector("stringProperty");
        assertEquals(Selector.fromString("stringNonIndexedProperty"), ((SListModel) list.getModel()).getSelector());

        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        list.setSelectedIndex(1);
        assertSame(view.getBoundModel(), model);
        assertSame(model, list.getBoundModel());
        assertSame(model.getStringNonIndexedProperty(), ((SListModel) list.getModel()).getShownModel());
        assertSame(model, ((SListSelectionModel) list.getSelectionModel()).getBoundModel());
        assertSame(model.getStringNonIndexedProperty().get(1), ((SListSelectionModel) list.getSelectionModel()).getViewValue());
        assertSame(model.getStringProperty(), ((SListSelectionModel) list.getSelectionModel()).getViewValue());

        ArrayList oldList = model.getStringNonIndexedProperty();
        ArrayList newList = new ArrayList();
        model.setStringNonIndexedProperty(newList);

        assertSame(oldList, ((SListModel) list.getModel()).getShownModel());
        assertSame(oldList.get(1), ((SListSelectionModel) list.getSelectionModel()).getViewValue());
        assertSame(model.getStringProperty(), ((SListSelectionModel) list.getSelectionModel()).getViewValue());

        list.refresh();
        SuiteViewSwing.waitForAWT();

        assertSame(model.getStringNonIndexedProperty(), ((SListModel) list.getModel()).getShownModel());
        assertNull(model.getStringProperty());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        list = new SList();
        list.setPreferredSize(new Dimension(50, 60));

        view = new SPanel();
        view.add(list);

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
