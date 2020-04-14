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
 * $Id: TestSAction.java,v 1.1 2002/09/13 17:12:57 ludovicc Exp $
 */
package test.view.swing;

import junit.framework.TestCase;
import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SAction;
import org.scopemvc.view.swing.SPanel;

/**
 * TODO: document the class
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @created 12 September 2002
 */
public class TestSAction extends TestCase {

    private SAction action;
    private SwingDummyController controller;
    private SPanel panel;

    /**
     * Constructor for the TestSAction object
     *
     * @param inName Name of the test
     */
    public TestSAction(String inName) {
        super(inName);
    }

    /**
     * A unit test for JUnit
     */
    public void testNoControl() {
        action.actionPerformed(null);
        assertNull(controller.lastControl);
    }

    /**
     * A unit test for JUnit
     */
    public void testControl() {
        action.setControlID("Test");
        assertEquals("Test", action.getName());
        action.actionPerformed(null);
        assertEquals(new Control("Test"), controller.lastControl);
    }

    /**
     * A unit test for JUnit
     */
    public void testConstructorControl() {
        SAction a = new SAction("Test2", panel);

        assertEquals("Test2", a.getName());
        a.actionPerformed(null);
        assertEquals(new Control("Test2"), controller.lastControl);
    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        action = new SAction();

        panel = new SPanel();
        panel.addSubView(action);

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
