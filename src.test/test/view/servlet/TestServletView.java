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
 * $Id: TestServletView.java,v 1.7 2002/11/20 00:19:57 ludovicc Exp $
 */
package test.view.servlet;

import java.io.StringWriter;
import junit.framework.TestCase;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.controller.servlet.ServletContext;
import org.scopemvc.core.Control;
import org.scopemvc.core.View;
import org.scopemvc.view.servlet.Page;
import org.scopemvc.view.servlet.ServletView;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/11/20 00:19:57 $
 * @created 18 September 2002
 */
public final class TestServletView extends TestCase {

    private ServletView view;
    private Object model;
    private ServletTestController controller;


    /**
     * Constructor for the TestServletView object
     *
     * @param inName Name of the test
     */
    public TestServletView(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSetBoundModel() throws Exception {
        assertNull(view.getBoundModel());
        view.setBoundModel(model);
        assertSame(model, view.getBoundModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSetController() throws Exception {
        assertNull(view.getController());
        view.setController(controller);
        assertSame(controller, view.getController());
    }


    /**
     * A unit test for JUnit
     */
    public void testControlIssue() {
        ViewContext.setThreadContext(new TestServletContext());
        view.setController(controller);

        Control control = new Control("TestControl");
        view.issueControl(control);
        assertTrue("Control not issued through parent view", controller.handledControl);
    }


    /**
     * A unit test for JUnit
     */
    public void testPages() {
        Page page1 = new ServletTestPage("1");
        Page page2 = new ServletTestPage("2");

        view.addPage(page1);
        view.addPage(page2);

        assertSame(view, page1.getParent());
        assertSame(view, page2.getParent());

        assertSame(page1, view.findPageByID("1"));
        assertSame(page2, view.findPageByID("2"));

        assertSame(page1, view.getVisible());
        assertSame(page1, view.getFirstPage());

        view.setVisible("2");
        assertSame(page2, view.getVisible());
        assertSame(page1, view.getFirstPage());

        view.setVisible("1");
        assertSame(page1, view.getVisible());
        assertSame(page1, view.getFirstPage());

        try {
            view.setVisible("rubbish");
            fail("Set visible to an ID that doesn't exist");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testStreamView() throws Exception {
        Page page1 = new ServletTestPage("1");
        Page page2 = new ServletTestPage("2");

        view.addPage(page1);
        view.addPage(page2);

        StringWriter writer = new StringWriter();
        ((ServletTestPage) view.getVisible()).streamView(writer);
        assertEquals("1", writer.toString());

        writer = new StringWriter();
        view.setVisible("2");
        ((ServletTestPage) view.getVisible()).streamView(writer);
        assertEquals("2", writer.toString());

        writer = new StringWriter();
        view.setVisible("1");
        ((ServletTestPage) view.getVisible()).streamView(writer);
        assertEquals("1", writer.toString());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testStreamViewError() throws Exception {
        StringWriter writer = new StringWriter();
        try {
            ((ServletTestPage) view.getVisible()).streamView(writer);
            fail("Streamed a view when no Pages exist.");
        } catch (NullPointerException e) {
            // expected
        }
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetContentType() throws Exception {
        ServletTestPage page1 = new ServletTestPage("1");
        ServletTestPage page2 = new ServletTestPage("2");

        view.addPage(page1);
        view.addPage(page2);

        page1.setContentType("abc");
        page2.setContentType("xyz");

        assertEquals("abc", ((ServletTestPage) view.getVisible()).getContentType());

        view.setVisible("2");
        assertEquals("xyz", ((ServletTestPage) view.getVisible()).getContentType());
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        view = new ServletView();
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

