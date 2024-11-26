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
 * $Id: TestSTextArea.java,v 1.14 2002/11/20 00:16:30 ludovicc Exp $
 */
package test.view.swing;


import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
//import junit.extensions.jfcunit.MouseEventData;
import junit.extensions.jfcunit.TestHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Selector;
import org.scopemvc.util.convertor.StringStringConvertor;
import org.scopemvc.view.swing.ControlIssuer;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STextArea;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.14 $ $Date: 2002/11/20 00:16:30 $
 * @created 13 September 2002
 */
public final class TestSTextArea extends JFCTestCase {

    private static final Log LOG = LogFactory.getLog(TestSTextArea.class);

    private STextArea textarea;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;
    private TestHelper helper;


    /**
     * Constructor for the TestSTextArea object
     *
     * @param inName Name of the test
     */
    public TestSTextArea(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertTrue(!textarea.isEnabled());
//        assertTrue(textarea.getText().length() < 1);

        textarea.setSelector(Selector.fromString("stringProperty"));
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("stringProperty"), textarea.getSelector());
        assertTrue(!textarea.isEnabled());
//        assertTrue(textarea.getText().length() < 1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        textarea.setSelector(Selector.fromString("stringProperty"));
        assertEquals(Selector.fromString("stringProperty"), textarea.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);

        assertSame(textarea.getBoundModel(), model);
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());

        model.setStringProperty("xyz");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("xyz", textarea.getText());

        textarea.setText("gui");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("gui", textarea.getText());
        SuiteViewSwing.waitForAWT();
        assertEquals("gui", model.getStringProperty());

        textarea.setText("");
        SuiteViewSwing.waitForAWT();
        assertTrue(!textarea.isEnabled());
        assertNull(model.getStringProperty());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(!textarea.isEnabled());
        assertNull(model.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConvenience() throws Exception {
        textarea.setSelector("stringProperty");
        assertEquals(Selector.fromString("stringProperty"), textarea.getSelector());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNull() throws Exception {
        textarea.setDisableOnNull(false);
        textarea.setSelector(Selector.fromString("stringProperty"));
        assertEquals(Selector.fromString("stringProperty"), textarea.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textarea.getBoundModel(), model);
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());

        model.setStringProperty("xyz");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());

        textarea.setText("gui");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("gui", textarea.getText());
        SuiteViewSwing.waitForAWT();
        assertEquals("gui", model.getStringProperty());

        textarea.setText("");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertTrue(model.getStringProperty() == null);

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertNull(model.getStringProperty());
        assertTrue(textarea.getText().length() < 1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBadBind() throws Exception {
        textarea.setSelector(Selector.fromString("subModel"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textarea.isEnabled());
//        assertTrue(! textarea.isSelected());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBadBind2() throws Exception {
        textarea.setDisableOnNull(false);
        textarea.setSelector(Selector.fromString("subModel"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textarea.isEnabled());
//        assertTrue(! textarea.isSelected());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testReadOnlyBind() throws Exception {
        textarea.setSelector(Selector.fromString("readOnlyStringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textarea.isEnabled());
        assertEquals(model.getReadOnlyStringProperty(), textarea.getText());
    }


    /**
     * Test issuing controls after losing focus
     *
     * @throws Exception Any abnormal exception
     */
//    public void testControlIssue2() throws Exception {
//        textarea.setSelector(Selector.fromString("stringProperty"));
//        controller.setModel(model);
//        textarea.setControlID("test1");
//        SuiteViewSwing.waitForAWT();
//        // select the textarea
//        helper.enterClickAndLeave(new MouseEventData(this, textarea, 1));
//
//        assertNull(controller.lastControl);
//
//        // select the view, exit the textfield
//        helper.enterClickAndLeave(new MouseEventData(this, view, 1));
//        SuiteViewSwing.waitForAWT();
//        assertTrue(controller.controlMatches("test1"));
//
//        // select the textarea
//        helper.enterClickAndLeave(new MouseEventData(this, textarea, 1));
//        textarea.setControlID("test1a");
//        // select the view, exit the textarea
//        helper.enterClickAndLeave(new MouseEventData(this, view, 1));
//        SuiteViewSwing.waitForAWT();
//        assertTrue(controller.controlMatches("test1a"));
//    }

//    /**
//     * Test the 'smart' way of issuing controls: control should be issued only
//     * if the data changed
//     *
//     * @throws Exception Any abnormal exception
//     */
//    public void testControlIssue3() throws Exception {
//        textarea.setControlSettings(ControlIssuer.ISSUE_CONTROL_ON_ENTER_KEY
//                | ControlIssuer.ISSUE_CONTROL_ON_LOST_FOCUS
//                | ControlIssuer.ISSUE_CONTROL_ONLY_ON_CHANGE);
//        textarea.setSelector(Selector.fromString("stringProperty"));
//        controller.setModel(model);
//        textarea.setControlID("test1");
//        SuiteViewSwing.waitForAWT();
//        // select the textarea
//        helper.enterClickAndLeave(new MouseEventData(this, textarea, 1));
//
//        assertNull(controller.lastControl);
//
//        // select the view, exit the textfield
//        helper.enterClickAndLeave(new MouseEventData(this, view, 1));
//        SuiteViewSwing.waitForAWT();
//        assertNull(controller.lastControl);
//
//        // select the textarea
//        helper.enterClickAndLeave(new MouseEventData(this, textarea, 1));
//        textarea.setText("newvalue");
//        // select the view, exit the textarea
//        helper.enterClickAndLeave(new MouseEventData(this, view, 1));
//        SuiteViewSwing.waitForAWT();
//        assertTrue(controller.controlMatches("test1"));
//    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBooleanBind() throws Exception {
        textarea.setSelector(Selector.fromString("booleanProperty1"));
        assertEquals(Selector.fromString("booleanProperty1"), textarea.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textarea.getBoundModel(), model);

        model.setBooleanProperty1(Boolean.TRUE);
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("" + model.getBooleanProperty1(), textarea.getText());

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("" + model.getBooleanProperty1(), textarea.getText());

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();
        textarea.setText("true");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("true", textarea.getText());
        assertEquals("" + model.getBooleanProperty1(), textarea.getText());

        model.setBooleanProperty1(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(!textarea.isEnabled());
        assertNull(model.getBooleanProperty1());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testInvalidBooleanBind() throws Exception {
        textarea.setSelector(Selector.fromString("booleanProperty1"));
        assertEquals(Selector.fromString("booleanProperty1"), textarea.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textarea.getBoundModel(), model);

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();

        textarea.setText("rubbish");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("rubbish", textarea.getText());
        assertEquals(Boolean.FALSE, model.getBooleanProperty1());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testInvalidBooleanBind2() throws Exception {
        textarea.setSelector(Selector.fromString("booleanProperty1"));
        assertEquals(Selector.fromString("booleanProperty1"), textarea.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textarea.getBoundModel(), model);

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();

        textarea.setText("rubbish");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("rubbish", textarea.getText());
        assertEquals(Boolean.FALSE, model.getBooleanProperty1());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        textarea.setSelector(Selector.fromString("stringProperty"));
        assertEquals(Selector.fromString("stringProperty"), textarea.getSelector());

        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);

        assertSame(textarea.getBoundModel(), model);
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());
        String x = model.getStringProperty();

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals(x, textarea.getText());

        view.refresh();
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());
        x = model.getStringProperty();

        model.setStringProperty("xyz");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals(x, textarea.getText());

        view.refresh();
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());

        textarea.setText("gui");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals("gui", textarea.getText());
        assertEquals(model.getStringProperty(), textarea.getText());

        textarea.setText("");
        SuiteViewSwing.waitForAWT();
//        assertTrue(! textarea.isEnabled());
        assertNull(model.getStringProperty());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        view.refresh();
        assertTrue(!textarea.isEnabled());
        assertNull(model.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testForcedStringConvertor() throws Exception {
        textarea.setSelector(Selector.fromString("stringProperty"));
        StringStringConvertor s = new StringStringConvertor();
        s.setNullAsString("xyz");
        textarea.setStringConvertor(s);
        textarea.setDisableOnNull(false);
        assertEquals(Selector.fromString("stringProperty"), textarea.getSelector());

        controller.setModel(model);
        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textarea.getBoundModel(), model);
//        assertTrue(textarea.isEnabled());
        assertEquals("xyz", textarea.getText());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(textarea.isEnabled());
        assertEquals(model.getStringProperty(), textarea.getText());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testInitialNullBind() throws Exception {
        textarea.setSelector("subModel.stringProperty");
        model.setSubModel(null);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textarea.isEnabled());
        assertEquals("", textarea.getText());

        SwingDummyModel submodel = new SwingDummyModel();
        submodel.setStringProperty("test");
        model.setSubModel(submodel);
        SuiteViewSwing.waitForAWT();

        assertTrue(textarea.isEnabled());
        assertEquals("test", textarea.getText());
    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        helper = new JFCTestHelper();

        textarea = new STextArea();
        textarea.setColumns(10);
        textarea.setRows(5);
        textarea.setDisableOnNull(true);

        view = new SPanel();
        view.add(textarea);

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

    // ***** setDisableOnNull(false); and test empty string, null, boolean
}
