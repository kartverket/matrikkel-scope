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
package test.controller.servlet;


import junit.framework.TestCase;
import org.scopemvc.controller.servlet.xml.XSLServletContext;
import org.scopemvc.view.servlet.Page;
import org.scopemvc.view.servlet.ServletView;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/12 19:09:34 $
 * @created 05 September 2002
 */
public class TestServletContext extends TestCase {

    private DummyHTTPResponse response;
    private DummyHTTPRequest request;
    private ServletView servletView;
    private Page page;
    private XSLServletContext context;
    private DummyServlet servlet;


    /**
     * Constructor for the TestServletContext object
     *
     * @param inName Name of the test
     */
    public TestServletContext(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConstructor() throws Exception {
        assertSame(servlet, context.getServlet());
        assertSame(request, context.getHttpRequest());
        assertSame(response, context.getHttpResponse());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testShowView() throws Exception {
        assertTrue(!context.hasShownView());
        context.showView(servletView);
        assertEquals("test", response.getContent());
        assertTrue(context.hasShownView());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testShowError() throws Exception {
        assertTrue(!context.hasShownView());
        context.showError("abc", "uvw\nxyz");
        String content = response.getContent();
        assertTrue(content.indexOf("Error") != 0);
        assertTrue(content.indexOf("abc") != 0);
        assertTrue(content.indexOf("uvw<BR />xyz") != 0);
        assertTrue(context.hasShownView());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testInternalError1() throws Exception {
        servletView.setBoundModel(
            new Object() {
                public String toString() {
                    throw new UnsupportedOperationException();
                }
            });
        assertTrue(!context.hasShownView());
        context.showView(servletView);
        String content = response.getContent();
        assertTrue(content.indexOf("Internal error") != 0);
        assertTrue(content.indexOf("UnsupportedOperationException") != 0);
        assertTrue(context.hasShownView());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testInternalError2() throws Exception {
        servletView = new ServletView();
        // no Pages to show!
        assertTrue(!context.hasShownView());
        context.showView(servletView);
        String content = response.getContent();
        assertTrue(content.indexOf("Internal error") != 0);
        assertTrue(content.indexOf("UnsupportedOperationException") != 0);
        assertTrue(content.indexOf("No visible Page to stream.") != 0);
        assertTrue(context.hasShownView());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        response = new DummyHTTPResponse();
        servletView = new ServletView();
        page = new DummyPage(null);
        servletView.addPage(page);
        servletView.setBoundModel("test");
        servlet = new DummyServlet();
        context = new XSLServletContext(servlet, request, response, null);
    }
}
