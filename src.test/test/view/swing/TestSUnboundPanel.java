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
import org.scopemvc.view.swing.SUnboundPanel;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/25 13:53:09 $
 * @created 24 September 2002
 */
public final class TestSUnboundPanel extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSUnboundPanel.class);

    private SCheckBox checkbox1;
    private SCheckBox checkbox2;
    private SwingDummyController controller;
    private SUnboundPanel view;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSUnboundPanel object
     *
     * @param inName Name of the test
     */
    public TestSUnboundPanel(String inName) {
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
        assertNull(view.getBoundModel());

        assertNull(checkbox1.getBoundModel());
        assertNull(checkbox2.getBoundModel());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        checkbox1 = new SCheckBox();
        checkbox2 = new SCheckBox();

        view = new SUnboundPanel();
        view.add(checkbox1);
        JPanel p = new JPanel();
        p.add(checkbox2);
        view.add(p);

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

