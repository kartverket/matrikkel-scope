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
 * $Id: TestSMenuItem.java,v 1.5 2002/09/25 13:53:09 ludovicc Exp $
 */
package test.view.swing;

import junit.framework.TestCase;
import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SMenuItem;
import org.scopemvc.view.swing.SPanel;

import javax.swing.*;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/25 13:53:09 $
 * @created 24 September 2002
 */
public final class TestSMenuItem extends TestCase {

    private SMenuItem menuitem;
    private SwingDummyController controller;


    /**
     * Constructor for the TestSMenuItem object
     *
     * @param inName Name of the test
     */
    public TestSMenuItem(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testNoControl() throws Exception {
        menuitem.setControlID(null);
        menuitem.doClick();
        SuiteViewSwing.waitForAWT();
        assertNull(controller.lastControl);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControl() throws Exception {
        assertEquals("Test", menuitem.getText());
        menuitem.doClick();
        SuiteViewSwing.waitForAWT();
        assertEquals(new Control("Test"), controller.lastControl);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testOwner() throws Exception {
        assertTrue(menuitem.isEnabled());
        menuitem.doClick();
        SuiteViewSwing.waitForAWT();
        assertNotNull(controller.lastControl);
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testNoOwner() throws Exception {
        menuitem.setOwner(null);
        assertTrue(!menuitem.isEnabled());
        menuitem.doClick();
        SuiteViewSwing.waitForAWT();
        assertNull(controller.lastControl);
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        menuitem = new SMenuItem("Test");

        SPanel p =
            new SPanel() {
                public JMenuBar getMenuBar() {
                    JMenuBar menubar = new JMenuBar();
                    JMenu menu = new JMenu();
                    menu.add(menuitem);
                    menubar.add(menu);
                    return menubar;
                }
            };
        menuitem.setOwner(p);
        controller = new SwingDummyController();
        controller.setView(p);

        controller.startup();
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        controller.shutdown();
    }
}

