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


import java.util.ArrayList;
import javax.swing.JTextField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.util.convertor.StringStringConvertor;
import org.scopemvc.view.swing.SComboBox;
import org.scopemvc.view.swing.SComboBoxEditor;
import org.scopemvc.view.swing.SComboBoxModel;
import org.scopemvc.view.swing.SListCellRenderer;
import org.scopemvc.view.swing.SPanel;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.15 $ $Date: 2002/11/20 00:19:56 $
 * @created 10 September 2002
 */
public final class TestSComboBox extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSComboBox.class);

    private SComboBox combo;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSComboBox object
     *
     * @param inName Name of the test
     */
    public TestSComboBox(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCreateModel() throws Exception {
        assertTrue(combo.getModel() instanceof SComboBoxModel);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCreateRenderer() throws Exception {
        assertTrue(combo.getRenderer() instanceof SListCellRenderer);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConveniences() throws Exception {
        Selector s = Selector.fromString("xyz");

        assertNull(((SComboBoxModel) combo.getModel()).getSelectionSelector());
        combo.setSelectionSelector(s);
        assertSame(s, ((SComboBoxModel) combo.getModel()).getSelectionSelector());

        assertNull(((SListCellRenderer) combo.getRenderer()).getTextSelector());
        combo.setRendererSelector(s);
        assertSame(s, ((SListCellRenderer) combo.getRenderer()).getTextSelector());

        assertNull(((SListCellRenderer) combo.getRenderer()).getIconSelector());
        combo.setRendererIconSelector(s);
        assertSame(s, ((SListCellRenderer) combo.getRenderer()).getIconSelector());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConveniences2() throws Exception {
        Selector s = Selector.fromString("xyz");

        assertNull(((SComboBoxModel) combo.getModel()).getSelectionSelector());
        combo.setSelectionSelector("xyz");
        assertEquals(s, ((SComboBoxModel) combo.getModel()).getSelectionSelector());

        assertNull(((SListCellRenderer) combo.getRenderer()).getTextSelector());
        combo.setRendererSelector("xyz");
        assertEquals(s, ((SListCellRenderer) combo.getRenderer()).getTextSelector());

        assertNull(((SListCellRenderer) combo.getRenderer()).getIconSelector());
        combo.setRendererIconSelector("xyz");
        assertEquals(s, ((SListCellRenderer) combo.getRenderer()).getIconSelector());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertTrue(!combo.isEnabled());
        assertNull(combo.getBoundModel());

        combo.setSelector(Selector.fromString("stringNonIndexedProperty"));
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("stringNonIndexedProperty"), ((SComboBoxModel) combo.getModel()).getSelector());
        assertTrue(!combo.isEnabled());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        combo.setSelector(Selector.fromString("stringNonIndexedProperty"));
        assertEquals(Selector.fromString("stringNonIndexedProperty"), ((SComboBoxModel) combo.getModel()).getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, combo.getBoundModel());
        assertTrue(!combo.isEnabled());
        assertSame(model.getStringNonIndexedProperty(), ((SComboBoxModel) combo.getModel()).getShownModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind2() throws Exception {
        combo.setSelector(Selector.fromString("stringNonIndexedProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertTrue(!combo.isEnabled());

        ArrayList newList = new ArrayList(java.util.Arrays.asList(new String[]{"1", "2", "3"}));
        model.setStringNonIndexedProperty(newList);
        SuiteViewSwing.waitForAWT();

        assertSame(view.getBoundModel(), model);
        assertSame(model, combo.getBoundModel());
        assertSame(newList, ((SComboBoxModel) combo.getModel()).getShownModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testValidation() throws Exception {
        combo.setSelector(Selector.fromString("stringNonIndexedProperty"));
        combo.setSelectionSelector(Selector.fromString("stringProperty"));
        model.setStringProperty(null);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        combo.setSelectedItem("snip1");
        SuiteViewSwing.waitForAWT();
        assertEquals("snip1", combo.getSelectedItem());
        assertEquals("getStringProperty <" + model.getStringProperty() + ">", "snip1", model.getStringProperty());

        LOG.debug("Set Illegal selected item");
        combo.setSelectedItem("Illegal");
        assertTrue(combo.isEnabled());
        SuiteViewSwing.waitForAWT();
        SuiteViewSwing.waitForAWT();
        // should keep the previously valid selection
        assertEquals("snip1", combo.getSelectedItem());
        assertEquals("getStringProperty <" + model.getStringProperty() + ">", "snip1", model.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testKeepSelection() throws Exception {
        combo.setSelector(Selector.fromString("stringNonIndexedProperty"));
        combo.setSelectionSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertTrue(combo.isEnabled());

        combo.setSelectedItem(model.getStringNonIndexedProperty(1));
        java.util.ArrayList newList = (ArrayList) model.getStringNonIndexedProperty().clone();
        model.setStringNonIndexedProperty(newList);
        SuiteViewSwing.waitForAWT();
        assertEquals(model.getStringNonIndexedProperty(1), combo.getSelectedItem());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testEditable() throws Exception {
        combo.setSelector(Selector.fromString("stringNonIndexedProperty"));
        combo.setSelectionSelector(Selector.fromString("stringProperty"));
        combo.setEditable(true);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertTrue(combo.isEnabled());

        // change the selected item with an item not in the list
        combo.setSelectedItem("newvalue");
        SuiteViewSwing.waitForAWT();
        assertEquals("newvalue", combo.getSelectedItem());
        assertEquals("newvalue", model.getStringProperty());

        // change the list of items but keep the previously selected item
        Object oldValue = model.getStringNonIndexedProperty(1);
        combo.setSelectedItem(model.getStringNonIndexedProperty(1));
        java.util.ArrayList newList = new ArrayList();
        newList.add("x1");
        newList.add("x2");
        model.setStringNonIndexedProperty(newList);
        SuiteViewSwing.waitForAWT();
        assertEquals(oldValue, combo.getSelectedItem());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testNonEditable() throws Exception {
        combo.setSelector(Selector.fromString("stringNonIndexedProperty"));
        combo.setSelectionSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertTrue(combo.isEnabled());

        LOG.debug("Set selected item " + model.getStringNonIndexedProperty(1));
        combo.setSelectedItem(model.getStringNonIndexedProperty(1));
        LOG.debug("Set new list of items (x1,x2)");
        java.util.ArrayList newList = new ArrayList();
        newList.add("x1");
        newList.add("x2");
        model.setStringNonIndexedProperty(newList);
        SuiteViewSwing.waitForAWT();
        assertEquals(model.getStringNonIndexedProperty(0), combo.getSelectedItem());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     * @todo Implement this test
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
        combo.setSelector(Selector.fromString("stringNonIndexedProperty"));
        assertEquals(Selector.fromString("stringNonIndexedProperty"), ((SComboBoxModel) combo.getModel()).getSelector());

        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(model, combo.getBoundModel());
        assertSame(model.getStringNonIndexedProperty(), ((SComboBoxModel) combo.getModel()).getShownModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetStringConvertor() throws Exception {
        StringStringConvertor sc = new StringStringConvertor();
        sc.setNullAsString("xyz");
        combo.setStringConvertor(sc);
        SComboBoxEditor e = (SComboBoxEditor) combo.getEditor();
        e.setItem(null);
        SuiteViewSwing.waitForAWT();
        assertEquals("xyz", ((JTextField) combo.getEditor().getEditorComponent()).getText());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        combo = new SComboBox();

        view = new SPanel();
        view.add(combo);

        controller = new SwingDummyController();
        controller.setView(view);
        controller.startup();
        // does showView()

        model = new SwingDummyModel();
        model.setStringProperty(model.getStringNonIndexedProperty(0));
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        controller.shutdown();
    }
}
