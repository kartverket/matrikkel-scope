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
 * $Id: TestSLabel.java,v 1.9 2002/11/11 00:31:42 ludovicc Exp $
 */
package test.view.swing;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SLabel;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STextField;

/**
 * <P>
 *
 * ***** Should test validation state too. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.9 $ $Date: 2002/11/11 00:31:42 $
 * @created 24 September 2002
 */
public final class TestSLabel extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSLabel.class);

    private SLabel label;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSLabel object
     *
     * @param inName Name of the test
     */
    public TestSLabel(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        label.setSelector(Selector.fromString("stringProperty"));
        assertEquals(Selector.fromString("stringProperty"), label.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(label.getBoundModel(), model);
        assertEquals(model.getStringProperty(), label.getText());
        assertTrue(label.isEnabled());

        int width = label.getSize().width;

        model.setStringProperty("abcdefghijklmnopq");
        SuiteViewSwing.waitForAWT();
        assertTrue(label.isEnabled());
        assertEquals(model.getStringProperty(), label.getText());
        assertTrue(label.getSize().width != width);
        width = label.getSize().width;

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(label.isEnabled());
        assertNull(model.getStringProperty());
        assertTrue(label.getSize().width != width);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        label.setSelector(Selector.fromString("stringProperty"));
        assertEquals(Selector.fromString("stringProperty"), label.getSelector());

        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);

        assertSame(label.getBoundModel(), model);
        assertTrue(label.isEnabled());
        assertEquals(model.getStringProperty(), label.getText());
        String x = model.getStringProperty();

        model.setStringProperty("abc");
        SuiteViewSwing.waitForAWT();
        assertTrue(label.isEnabled());
        assertEquals(x, label.getText());

        view.refresh();
        assertTrue(label.isEnabled());
        assertEquals(model.getStringProperty(), label.getText());
        x = model.getStringProperty();

        model.setStringProperty("xyz");
        SuiteViewSwing.waitForAWT();
        assertTrue(label.isEnabled());
        assertEquals(x, label.getText());

        view.refresh();
        assertTrue(label.isEnabled());
        assertEquals(model.getStringProperty(), label.getText());

    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConvenience() throws Exception {
        label.setSelector("stringProperty");
        assertEquals(Selector.fromString("stringProperty"), label.getSelector());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testLabelFor() throws Exception {
        STextField textField = new STextField();
        textField.setSelector("readOnlyStringProperty");
        view.add(textField);

        label.setSelector(Selector.fromString("stringProperty"));
        label.setLabelFor(textField);
        assertEquals(Selector.fromString("stringProperty"), label.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        Thread.sleep(5000);
        assertSame(view.getBoundModel(), model);
        assertSame(label.getBoundModel(), model);
        assertEquals(model.getStringProperty(), label.getText());
        assertTrue(!label.isEnabled());

        textField.setSelector("stringProperty");
        SuiteViewSwing.waitForAWT();
        Thread.sleep(5000);
        assertTrue(label.isEnabled());

    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindReadonly() throws Exception {
        label.setSelector(Selector.fromString("readOnlyStringProperty"));
        assertEquals(Selector.fromString("readOnlyStringProperty"), label.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(label.getBoundModel(), model);
        assertEquals(model.getReadOnlyStringProperty(), label.getText());
        // label should not be disabled if bound property is read-only
        assertTrue(label.isEnabled());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        label = new SLabel();

        view = new SPanel();
        view.add(label);

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
