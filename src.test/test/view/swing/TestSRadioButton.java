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
 * $Id: TestSRadioButton.java,v 1.9 2002/10/24 00:31:56 ludovicc Exp $
 */
package test.view.swing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.SRadioButton;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.9 $ $Date: 2002/10/24 00:31:56 $
 * @created 24 September 2002
 */
public final class TestSRadioButton extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSCheckBox.class);

    private SRadioButton radiobutton;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSRadioButton object
     *
     * @param inName Name of the test
     */
    public TestSRadioButton(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertTrue(!radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        radiobutton.setSelector(Selector.fromString("booleanProperty"));
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("booleanProperty"), radiobutton.getSelector());
        assertTrue(!radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConvenience() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertTrue(!radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        radiobutton.setSelector("booleanProperty");
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("booleanProperty"), radiobutton.getSelector());
        assertTrue(!radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        radiobutton.setSelector(Selector.fromString("booleanProperty"));
        assertEquals(Selector.fromString("booleanProperty"), radiobutton.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(radiobutton.getBoundModel(), model);
        assertTrue(radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        model.setBooleanProperty(true);
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());

        model.setBooleanProperty(false);
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        radiobutton.doClick();
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());
        assertTrue(model.getBooleanProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind2() throws Exception {
        radiobutton.setSelector(Selector.fromString("booleanProperty1"));
        assertEquals(Selector.fromString("booleanProperty1"), radiobutton.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(radiobutton.getBoundModel(), model);
        assertTrue(radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        model.setBooleanProperty1(Boolean.TRUE);
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());

        model.setBooleanProperty1(Boolean.FALSE);
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        radiobutton.doClick();
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());
        assertEquals(Boolean.TRUE, model.getBooleanProperty1());

        model.setBooleanProperty1(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isSelected());
        assertTrue(!radiobutton.isEnabled());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testValidation() throws Exception {
        radiobutton.setSelector(Selector.fromString("invalidBooleanProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        radiobutton.doClick();
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());
        assertTrue(!model.getInvalidBooleanProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBadBind() throws Exception {
        radiobutton.setSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        radiobutton.doClick();
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testReadOnlyBind() throws Exception {
        radiobutton.setSelector(Selector.fromString("booleanReadOnlyProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlIssue1() throws Exception {
        radiobutton.setSelector(Selector.fromString("booleanProperty"));
        controller.setModel(model);
        radiobutton.setControlID("test1");
        SuiteViewSwing.waitForAWT();

        assertNull(controller.lastControl);

        radiobutton.doClick();
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test1"));

        radiobutton.setControlID("test1a");
        radiobutton.doClick();
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test1a"));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlIssue2() throws Exception {
        radiobutton.setSelector(Selector.fromString("booleanProperty"));
        controller.setModel(model);
        radiobutton.setControlID("test2");
        SuiteViewSwing.waitForAWT();

        assertNull(controller.lastControl);

        model.setBooleanProperty(true);
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test2"));

        radiobutton.setControlID("test2a");
        model.setBooleanProperty(false);
        SuiteViewSwing.waitForAWT();
        assertTrue(controller.controlMatches("test2a"));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        radiobutton.setSelector(Selector.fromString("booleanProperty"));
        assertEquals(Selector.fromString("booleanProperty"), radiobutton.getSelector());

        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);

        assertSame(radiobutton.getBoundModel(), model);
        assertTrue(radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        model.setBooleanProperty(true);
        assertTrue(radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        view.refresh();
        assertTrue(radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());

        model.setBooleanProperty(false);
        assertTrue(radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());

        view.refresh();
        assertTrue(radiobutton.isEnabled());
        assertTrue(!radiobutton.isSelected());

        radiobutton.doClick();
        SuiteViewSwing.waitForAWT();
        assertTrue(radiobutton.isEnabled());
        assertTrue(radiobutton.isSelected());
        assertTrue(model.getBooleanProperty());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        radiobutton = new SRadioButton();

        view = new SPanel();
        view.add(radiobutton);

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
