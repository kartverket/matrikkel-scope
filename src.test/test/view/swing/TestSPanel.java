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

import javax.swing.JPanel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SCheckBox;
import org.scopemvc.view.swing.SPanel;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/25 13:53:09 $
 * @created 24 September 2002
 */
public final class TestSPanel extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSPanel.class);

    private SCheckBox checkbox1;
    private SCheckBox checkbox2;
    private SwingDummyController controller;
    private SPanel view;
    private SPanel view2;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSPanel object
     *
     * @param inName Name of the test
     */
    public TestSPanel(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        SuiteViewSwing.waitForAWT();
        assertNull(view.getBoundModel());
        assertNull(view.getShownModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSelector() throws Exception {
        view.setSelector(Selector.fromString("any"));
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("any"), view.getSelector());
        assertNull(view.getBoundModel());
        assertNull(view.getShownModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConvenience() throws Exception {
        view.setSelector("any");
        SuiteViewSwing.waitForAWT();
        assertEquals(Selector.fromString("any"), view.getSelector());
        assertNull(view.getBoundModel());
        assertNull(view.getShownModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind1() throws Exception {
        checkbox1.setSelector(Selector.fromString("booleanProperty"));
        assertEquals(Selector.fromString("booleanProperty"), checkbox1.getSelector());
        checkbox2.setSelector(Selector.fromString("booleanProperty"));
        assertEquals(Selector.fromString("booleanProperty"), checkbox2.getSelector());

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(model, view.getBoundModel());
        assertSame(model, view.getShownModel());
        assertSame(model, checkbox1.getBoundModel());
        assertSame(model, checkbox2.getBoundModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind2() throws Exception {
        SwingDummyModel submodel = new SwingDummyModel();
        model.setSubModel(submodel);
        view2.setSelector(Selector.fromString("subModel"));

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame("is: " + view2.getBoundModel(), model, view2.getBoundModel());
        assertSame(submodel, view2.getShownModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlIssueAndSync() throws Exception {
        SwingDummyModel submodel = new SwingDummyModel();
        model.setSubModel(submodel);
        view2.setSelector(Selector.fromString("subModel"));

        SwingDummyController c = new SwingDummyController();
        c.setView(view2);
        c.setModel(submodel);

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view2.getBoundModel(), model);
        assertSame(view2.getShownModel(), submodel);
        assertSame("submodel: " + submodel + " getModel: " + c.getModel(), submodel, c.getModel());

        submodel = new SwingDummyModel();
        model.setSubModel(submodel);
        SuiteViewSwing.waitForAWT();
        assertSame("submodel: " + submodel + " getModel: " + c.getModel(), submodel, c.getModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlIssueAndSyncNoMCE() throws Exception {
        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();
        SwingDummyModelNoMCE submodel = new SwingDummyModelNoMCE();
        model.setSubModel(submodel);
        view2.setSelector(Selector.fromString("subModel"));

        SwingDummyController c = new SwingDummyController();
        c.setView(view2);
        c.setModel(submodel);

        controller.setModel(model);
        SuiteViewSwing.waitForAWT();
        assertSame(view.getBoundModel(), model);
        assertSame(view2.getBoundModel(), model);
        assertSame(view2.getShownModel(), submodel);
        assertSame("submodel: " + submodel + " getModel: " + c.getModel(), submodel, c.getModel());

        SwingDummyModelNoMCE submodel2 = new SwingDummyModelNoMCE();
        model.setSubModel(submodel2);
        assertSame(submodel, c.getModel());

        view.refresh();
        assertSame(view.getBoundModel(), model);
        assertSame(view2.getBoundModel(), model);
        assertSame(view2.getShownModel(), submodel2);
        assertSame("submodel: " + submodel2 + " getModel: " + c.getModel(), submodel2, c.getModel());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        checkbox1 = new SCheckBox();
        checkbox2 = new SCheckBox();

        view = new SPanel();
        view.add(checkbox1);
        JPanel p = new JPanel();
        p.add(checkbox2);
        view.add(p);

        view2 = new SPanel();
        view.add(view2);

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
