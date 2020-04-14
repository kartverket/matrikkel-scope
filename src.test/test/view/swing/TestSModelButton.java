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
 * $Id: TestSModelButton.java,v 1.1 2002/09/13 17:12:12 ludovicc Exp $
 */
package test.view.swing;

import java.lang.Integer;
import junit.framework.TestCase;
import org.scopemvc.core.Control;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SModelButton;
import org.scopemvc.view.swing.SPanel;

/**
 * TODO: document the class
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @created 12 September 2002
 */

public class TestSModelButton extends TestCase {

    private SwingDummyModel model;
    private SModelButton button;
    private SwingDummyController controller;
    private SPanel panel;

    /**
     * Constructor for the TestSModelButton object
     *
     * @param inName Name of the test
     */
    public TestSModelButton(String inName) {
        super(inName);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControl() throws Exception {
        button = new SModelButton("Test", Selector.fromString("stringProperty"));
        panel.add(button);
        controller.setModel(model);

        assertEquals("Test", button.getText());
        SuiteViewSwing.waitForAWT();
        assertTrue(button.isEnabled());

        button.doClick();
        SuiteViewSwing.waitForAWT();
        assertEquals(new Control("Test", model.getStringProperty()), controller.lastControl);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testDisabledControl() throws Exception {
        button = new SModelButton("Test", Selector.fromString("stringProperty"));
        panel.add(button);
        controller.setModel(model);

        model.setStringProperty(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(!button.isEnabled());

        model.setStringProperty("notnull");
        SuiteViewSwing.waitForAWT();
        assertTrue(button.isEnabled());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testValueTest() throws Exception {
        button = new SModelButton("Test", Selector.fromString("intProperty"));
        panel.add(button);
        controller.setModel(model);

        // should be enabled if the bound value is an integer less than 1
        button.setValueTest(new Integer(1));

        model.setIntProperty(0);
        SuiteViewSwing.waitForAWT();
        assertTrue(button.isEnabled());

        model.setIntProperty(1);
        SuiteViewSwing.waitForAWT();
        assertTrue(!button.isEnabled());

        model.setIntProperty(2);
        SuiteViewSwing.waitForAWT();
        assertTrue(!button.isEnabled());

    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        model = new SwingDummyModel();
        panel = new SPanel();

        controller = new SwingDummyController();
        controller.setView(panel);

        controller.startup();
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        controller.shutdown();
    }

}
