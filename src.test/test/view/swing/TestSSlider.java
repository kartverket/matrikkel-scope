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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.SSlider;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.9 $ $Date: 2002/09/25 13:53:09 $
 * @created 13 September 2002
 */
public final class TestSSlider extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSSlider.class);

    private SSlider slider;
    private SwingDummyController controller;
    private SPanel view;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSSlider object
     *
     * @param inName Name of the test
     */
    public TestSSlider(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertTrue(!slider.isEnabled());

        slider.setSelector(Selector.fromString("intProperty"));
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("intProperty"), slider.getSelector());
        assertTrue(!slider.isEnabled());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConvenience() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertTrue(!slider.isEnabled());

        slider.setSelector("intProperty");
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("intProperty"), slider.getSelector());
        assertTrue(!slider.isEnabled());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        slider.setSelector(Selector.fromString("intProperty"));
        assertEquals(Selector.fromString("intProperty"), slider.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(slider.getBoundModel(), model);
        assertTrue(slider.isEnabled());
        assertTrue(model.getIntProperty() == slider.getValue());

        model.setIntProperty(50);
        SuiteViewSwing.waitForAWT();
        assertTrue(slider.isEnabled());
        assertTrue(model.getIntProperty() == slider.getValue());

        model.setIntProperty(105);
        SuiteViewSwing.waitForAWT();
        assertTrue(slider.isEnabled());
        assertTrue(model.getIntProperty() == slider.getValue());
        assertTrue(100 == model.getIntProperty());

        slider.setValue(30);
        SuiteViewSwing.waitForAWT();
        assertTrue(slider.isEnabled());
        assertTrue(30 == model.getIntProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind2() throws Exception {
        slider.setSelector(Selector.fromString("intProperty2"));
        assertEquals(Selector.fromString("intProperty2"), slider.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(slider.getBoundModel(), model);
        assertTrue(slider.isEnabled());
        assertTrue(model.getIntProperty2().intValue() == slider.getValue());

        model.setIntProperty2(new Integer(50));
        SuiteViewSwing.waitForAWT();
        assertTrue(slider.isEnabled());
        assertTrue(model.getIntProperty2().intValue() == slider.getValue());

        model.setIntProperty2(new Integer(105));
        SuiteViewSwing.waitForAWT();
        assertTrue(slider.isEnabled());
        assertTrue(model.getIntProperty2().intValue() == slider.getValue());
        assertTrue(100 == model.getIntProperty2().intValue());

        slider.setValue(30);
        SuiteViewSwing.waitForAWT();
        assertTrue(slider.isEnabled());
        assertTrue(30 == model.getIntProperty2().intValue());

        model.setIntProperty2(null);
        SuiteViewSwing.waitForAWT();
        assertTrue(!slider.isEnabled());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBadBind() throws Exception {
        slider.setSelector(Selector.fromString("stringProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!slider.isEnabled());

        slider.setValue(50);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testReadOnlyBind() throws Exception {
        slider.setSelector(Selector.fromString("intReadOnlyProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(!slider.isEnabled());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testValidation() throws Exception {
        slider.setSelector(Selector.fromString("invalidIntProperty"));
        controller.setModel(model);
        SuiteViewSwing.waitForAWT();

        assertTrue(slider.isEnabled());
        slider.setValue(10);
        SuiteViewSwing.waitForAWT();
        assertEquals(10, model.getInvalidIntProperty());

        slider.setValue(60);
        SuiteViewSwing.waitForAWT();
        assertTrue(slider.isEnabled());
        assertEquals(10, model.getInvalidIntProperty());
        assertEquals(60, slider.getValue());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        slider.setSelector(Selector.fromString("intProperty"));
        assertEquals(Selector.fromString("intProperty"), slider.getSelector());

        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);

        assertSame(slider.getBoundModel(), model);
        assertTrue(slider.isEnabled());
        assertEquals(model.getIntProperty(), slider.getValue());

        model.setIntProperty(50);
        assertTrue(slider.isEnabled());
        assertTrue(model.getIntProperty() != slider.getValue());

        view.refresh();
        assertTrue(slider.isEnabled());
        assertEquals(model.getIntProperty(), slider.getValue());

        model.setIntProperty(70);
        assertTrue(slider.isEnabled());
        assertTrue(model.getIntProperty() != slider.getValue());

        view.refresh();
        assertTrue(slider.isEnabled());
        assertEquals(model.getIntProperty(), slider.getValue());

        slider.setValue(30);
        SuiteViewSwing.waitForAWT();
        assertTrue(slider.isEnabled());
        assertEquals(slider.getValue(), model.getIntProperty());
        assertEquals(30, model.getIntProperty());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        slider = new SSlider();

        view = new SPanel();
        view.add(slider);

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
