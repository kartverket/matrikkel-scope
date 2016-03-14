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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.KeyEventData;
import junit.extensions.jfcunit.MouseEventData;
import junit.extensions.jfcunit.TestHelper;
import org.scopemvc.core.Selector;
import org.scopemvc.util.convertor.StringStringConvertor;
import org.scopemvc.view.swing.ControlIssuer;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STextField;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.16 $ $Date: 2002/11/20 00:16:30 $
 * @created 24 September 2002
 */
public final class TestSTextField extends JFCTestCase {

    private static final Log LOG = LogFactory.getLog(TestSTextField.class);

    private STextField textfield;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;
    private TestHelper helper;


    /**
     * Constructor for the TestSTextField object
     *
     * @param inName Name of the test
     */
    public TestSTextField(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertTrue(!textfield.isEnabled());
//        assertTrue(textfield.getText().length() < 1);

        textfield.setSelector(Selector.fromString("stringProperty"));
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("stringProperty"), textfield.getSelector());
        assertTrue(!textfield.isEnabled());
//        assertTrue(textfield.getText().length() < 1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        textfield.setSelector(Selector.fromString("stringProperty"));
        assertEquals(Selector.fromString("stringProperty"), textfield.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textfield.getBoundModel(), model);
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());

        model.setStringProperty("xyz");
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());

        textfield.setText("gui");
        textfield.postActionEvent();
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals("gui", textfield.getText());
        assertEquals(model.getStringProperty(), textfield.getText());

        textfield.setText("");
        textfield.postActionEvent();
        SuiteViewSwing.waitForAWT();
        assertTrue(!textfield.isEnabled());
        assertNull(model.getStringProperty());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(!textfield.isEnabled());
        assertNull(model.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConvenience() throws Exception {
        textfield.setSelector("stringProperty");
        assertEquals(Selector.fromString("stringProperty"), textfield.getSelector());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNull() throws Exception {
        textfield.setDisableOnNull(false);
        textfield.setSelector(Selector.fromString("stringProperty"));
        assertEquals(Selector.fromString("stringProperty"), textfield.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textfield.getBoundModel(), model);
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals("", textfield.getText());

        textfield.setText("gui");
        textfield.postActionEvent();
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals("gui", textfield.getText());
        assertEquals(model.getStringProperty(), textfield.getText());

        textfield.setText("");
        textfield.postActionEvent();
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertTrue(model.getStringProperty() == null);

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertNull(model.getStringProperty());
        assertTrue(textfield.getText().length() < 1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBadBind() throws Exception {
        textfield.setSelector(Selector.fromString("subModel"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textfield.isEnabled());
//        assertTrue(! textfield.isSelected());

        textfield.postActionEvent();
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBadBind2() throws Exception {
        textfield.setDisableOnNull(false);
        textfield.setSelector(Selector.fromString("subModel"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textfield.isEnabled());
//        assertTrue(! textfield.isSelected());

        textfield.postActionEvent();
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testReadOnlyBind() throws Exception {
        textfield.setSelector(Selector.fromString("readOnlyStringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textfield.isEnabled());
        assertEquals(model.getReadOnlyStringProperty(), textfield.getText());
    }


    /**
     * Test issuing controls after an action event (issued by pressing Enter
     * key)
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlIssue1() throws Exception {
        textfield.setSelector(Selector.fromString("stringProperty"));
        textfield.setControlID("test1");
        model.setStringProperty("");
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertNull(controller.lastControl);

        helper.sendKeyAction(new KeyEventData(this, textfield, KeyEvent.VK_A));
        SuiteViewSwing.waitForAWT();
        assertNull(controller.lastControl);
        assertEquals("", model.getStringProperty());
        assertTrue(textfield.hasFocus());

        helper.sendKeyAction(new KeyEventData(this, textfield, KeyEvent.VK_B));
        SuiteViewSwing.waitForAWT();
        assertNull(controller.lastControl);
        assertEquals("", model.getStringProperty());
        assertTrue(textfield.hasFocus());

        helper.sendKeyAction(new KeyEventData(this, textfield, KeyEvent.VK_ENTER));
        SuiteViewSwing.waitForAWT();
        assertEquals("ab", model.getStringProperty());
        assertTrue(controller.controlMatches("test1"));
        assertTrue(textfield.hasFocus());

        textfield.setControlID("test1a");
        helper.sendKeyAction(new KeyEventData(this, textfield, KeyEvent.VK_ENTER));
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test1a"));
        assertTrue(textfield.hasFocus());
    }


    /**
     * Test issuing controls after losing focus
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlIssue2() throws Exception {
        textfield.setSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        textfield.setControlID("test1");
        SuiteViewSwing.waitForAWT();
        // select the textfield
        helper.enterClickAndLeave(new MouseEventData(this, textfield, 1));

        assertNull(controller.lastControl);

        // select the view, exit the textfield
        helper.enterClickAndLeave(new MouseEventData(this, view, 1));
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test1"));

        // select the textfield
        helper.enterClickAndLeave(new MouseEventData(this, textfield, 1));
        textfield.setControlID("test1a");
        // select the view, exit the textfield
        helper.enterClickAndLeave(new MouseEventData(this, view, 1));
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test1a"));
    }

    /**
     * Test the 'smart' way of issuing controls: control should be issued only
     * if the data changed
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlIssue3() throws Exception {
        textfield.setControlSettings(ControlIssuer.ISSUE_CONTROL_ON_ENTER_KEY
                | ControlIssuer.ISSUE_CONTROL_ON_LOST_FOCUS
                | ControlIssuer.ISSUE_CONTROL_ONLY_ON_CHANGE);
        textfield.setSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        textfield.setControlID("test1");
        SuiteViewSwing.waitForAWT();
        // select the textfield
        helper.enterClickAndLeave(new MouseEventData(this, textfield, 1));

        assertNull(controller.lastControl);

        // select the view, exit the textfield
        helper.enterClickAndLeave(new MouseEventData(this, view, 1));
        SuiteViewSwing.waitForAWT();
        assertNull(controller.lastControl);

        // select the textfield
        helper.enterClickAndLeave(new MouseEventData(this, textfield, 1));
        textfield.setText("newvalue");
        // select the view, exit the textfield
        helper.enterClickAndLeave(new MouseEventData(this, view, 1));
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test1"));
    }

    /**
     * Test issuing controls after an action event
     *
     * @throws Exception Any abnormal exception
     */
    public void testKeyEnter() throws Exception {
        JButton button = new JButton("OK");
        ButtonListener buttonListener = new ButtonListener();
        button.addActionListener(buttonListener);
        button.setDefaultCapable(true);
        view.add(button);
        view.setDefaultButton(button);
        view.invalidate();
        view.validate();
        model.setStringProperty("");
        textfield.setSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);

        // No control ID, to force default behaviour
        textfield.setControlID(null);
        SuiteViewSwing.waitForAWT();
        assertNull(controller.lastControl);

        helper.sendKeyAction(new KeyEventData(this, textfield, KeyEvent.VK_A));
        SuiteViewSwing.waitForAWT();
        assertNull(controller.lastControl);
        assertTrue(!buttonListener.gotAction);
        assertEquals("", model.getStringProperty());
        assertTrue(textfield.hasFocus());

        helper.sendKeyAction(new KeyEventData(this, textfield, KeyEvent.VK_B));
        SuiteViewSwing.waitForAWT();
        assertNull(controller.lastControl);
        assertEquals("", model.getStringProperty());
        assertTrue(textfield.hasFocus());

        helper.sendKeyAction(new KeyEventData(this, textfield, KeyEvent.VK_ENTER));
        SuiteViewSwing.waitForAWT();
        assertEquals("ab", model.getStringProperty());
        assertTrue(buttonListener.gotAction);
        assertNull(controller.lastControl);

        buttonListener.gotAction = false;
        textfield.setControlID("test1");
        helper.sendKeyAction(new KeyEventData(this, textfield, KeyEvent.VK_ENTER));
        SuiteViewSwing.waitForAWT();
        assertTrue(!buttonListener.gotAction);
        assertTrue(controller.controlMatches("test1"));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBooleanBind() throws Exception {
        textfield.setSelector(Selector.fromString("booleanProperty1"));
        assertEquals(Selector.fromString("booleanProperty1"), textfield.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textfield.getBoundModel(), model);

        model.setBooleanProperty1(Boolean.TRUE);
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals("" + model.getBooleanProperty1(), textfield.getText());

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals("" + model.getBooleanProperty1(), textfield.getText());

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();
        textfield.setText("true");
        textfield.postActionEvent();
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals("true", textfield.getText());
        assertEquals("" + model.getBooleanProperty1(), textfield.getText());

        model.setBooleanProperty1(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(!textfield.isEnabled());
        assertNull(model.getBooleanProperty1());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testInvalidBooleanBind() throws Exception {
        textfield.setSelector(Selector.fromString("booleanProperty1"));
        assertEquals(Selector.fromString("booleanProperty1"), textfield.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textfield.getBoundModel(), model);

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();

        textfield.setText("rubbish");
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals("rubbish", textfield.getText());
        assertEquals(Boolean.FALSE, model.getBooleanProperty1());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testInvalidBooleanBind2() throws Exception {
        textfield.setSelector(Selector.fromString("booleanProperty1"));
        assertEquals(Selector.fromString("booleanProperty1"), textfield.getSelector());

        textfield.setControlID("test1");
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textfield.getBoundModel(), model);

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();

        textfield.setText("rubbish");
        textfield.postActionEvent();
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test1"));
        assertTrue(textfield.isEnabled());
        assertEquals("rubbish", textfield.getText());
        assertEquals(Boolean.FALSE, model.getBooleanProperty1());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        textfield.setSelector(Selector.fromString("stringProperty"));
        assertEquals(Selector.fromString("stringProperty"), textfield.getSelector());

        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);

        assertSame(textfield.getBoundModel(), model);
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());
        String x = model.getStringProperty();

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals(x, textfield.getText());

        view.refresh();
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());
        x = model.getStringProperty();

        model.setStringProperty("xyz");
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals(x, textfield.getText());

        view.refresh();
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());

        textfield.setText("gui");
        textfield.postActionEvent();
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals("gui", textfield.getText());
        assertEquals(model.getStringProperty(), textfield.getText());

        textfield.setText("");
        textfield.postActionEvent();
        SuiteViewSwing.waitForAWT();
//        assertTrue(! textfield.isEnabled());
        assertNull(model.getStringProperty());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        view.refresh();
        assertTrue(!textfield.isEnabled());
        assertNull(model.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testForcedStringConvertor() throws Exception {
        textfield.setSelector(Selector.fromString("stringProperty"));
        StringStringConvertor s = new StringStringConvertor();
        s.setNullAsString("xyz");
        textfield.setStringConvertor(s);
        textfield.setDisableOnNull(false);
        assertEquals(Selector.fromString("stringProperty"), textfield.getSelector());

        controller.setModel(model);
        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(textfield.getBoundModel(), model);
//        assertTrue(textfield.isEnabled());
        assertEquals("xyz", textfield.getText());

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(textfield.isEnabled());
        assertEquals(model.getStringProperty(), textfield.getText());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testInitialNullBind() throws Exception {
        textfield.setSelector("subModel.stringProperty");
        model.setSubModel(null);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textfield.isEnabled());
        assertEquals("", textfield.getText());

        SwingDummyModel submodel = new SwingDummyModel();
        submodel.setStringProperty("test");
        model.setSubModel(submodel);
        SuiteViewSwing.waitForAWT();

        assertTrue(textfield.isEnabled());
        assertEquals("test", textfield.getText());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testNullNavigation() throws Exception {
        textfield.setSelector("subModel.stringProperty");
        controller.setModel(model);

        SwingDummyModel submodel = new SwingDummyModel();
        submodel.setStringProperty("test");
        model.setSubModel(submodel);
        SuiteViewSwing.waitForAWT();

        assertTrue(textfield.isEnabled());
        assertEquals("test", textfield.getText());

        model.setSubModel(null);
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!textfield.isEnabled());
        assertEquals("", textfield.getText());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        helper = new JFCTestHelper();

        textfield = new STextField();
        textfield.setColumns(10);
        textfield.setDisableOnNull(true);

        view = new SPanel();
        view.add(textfield);

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

    class ButtonListener implements ActionListener {
        boolean gotAction = false;

        /**
         * TODO: document the method
         *
         * @param e TODO: Describe the Parameter
         */
        public void actionPerformed(ActionEvent e) {
            gotAction = true;
        }
    }
}

