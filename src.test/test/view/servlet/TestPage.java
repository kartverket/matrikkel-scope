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
package test.view.servlet;

import junit.framework.TestCase;

import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.controller.servlet.ServletContext;
import org.scopemvc.core.Control;
import org.scopemvc.core.View;
import org.scopemvc.view.servlet.Page;
import org.scopemvc.view.servlet.ServletView;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:19:57 $
 * @created 18 September 2002
 */
public final class TestPage extends TestCase {

    private Page page1;
    private Page page2;
    private ServletView servletView;
    private Object model;
    private ServletTestController controller;


    /**
     * Constructor for the TestPage object
     *
     * @param inName Name of the test
     */
    public TestPage(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     */
    public void testID() {
        assertEquals("1", page1.getID());
        assertEquals("2", page2.getID());
    }


    /**
     * A unit test for JUnit
     */
    public void testEqualsID() {
        assertTrue(page1.equalsID("1"));
        assertTrue(!page1.equalsID("rubbish"));
        assertTrue(page2.equalsID("2"));
        assertTrue(!page2.equalsID("rubbish"));
    }


    /**
     * A unit test for JUnit
     */
    public void testParent() {
        assertNull(page1.getParent());
        page1.setParent(servletView);
        assertSame(servletView, page1.getParent());
    }


    /**
     * A unit test for JUnit
     */
    public void testBoundModel() {
        assertNull(page1.getBoundModel());
        assertNull(page2.getBoundModel());

        servletView.addPage(page1);
        servletView.addPage(page2);
        servletView.setBoundModel(model);

        assertSame(model, page1.getBoundModel());
        assertSame(model, page2.getBoundModel());

        try {
            page1.setBoundModel(new Object());
            fail("setBoundModel on an unparented Page");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testController() {
        assertNull(page1.getController());
        assertNull(page2.getController());

        servletView.addPage(page1);
        servletView.addPage(page2);
        servletView.setController(controller);

        assertSame(controller, page1.getController());
        assertSame(controller, page2.getController());

        try {
            page1.setController(new ServletTestController());
            fail("setController on an unparented Page");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testIssueControl() throws Exception {
        ViewContext.setThreadContext(new TestServletContext());
        Control control = new Control("TestControl");

        try {
            page1.issueControl(control);
            fail("issueControl on an unparented Page");
        } catch (UnsupportedOperationException e) {
            // expected
        }

        servletView.addPage(page1);
        servletView.setController(controller);
        page1.issueControl(control);
        assertTrue("Control not issued through parent view", controller.handledControl);
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        page1 = new ServletTestPage("1");
        page2 = new ServletTestPage("2");
        servletView = new ServletView();
        model = new Object();
        controller = new ServletTestController();
    }

    static class TestServletContext extends ServletContext {
        /**
         * Constructor for the TestServletContext object
         */
        public TestServletContext() {
            super(null, null, null, null);
        }

        /**
         * TODO: document the method
         *
         * @param inView TODO: Describe the Parameter
         */
        public void showView(View inView) { }
    }
}
