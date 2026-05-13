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
 * $Id: TestScopeServlet.java,v 1.6 2002/11/20 00:19:58 ludovicc Exp $
 */
package test.controller.servlet;


import junit.framework.TestCase;
import org.scopemvc.controller.basic.ViewContext;

/**
 * <P>
 *
 * ***** Needs to test handlePopulateException and ServletException. ***** Needs
 * to test redirect. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:19:58 $
 */
public class TestScopeServlet extends TestCase {

    /**
     * TODO: describe of the Field
     */
    protected DummyServlet servlet;


    /**
     * Constructor for the TestScopeServlet object
     *
     * @param inName Name of the test
     */
    public TestScopeServlet(String inName) {
        super(inName);
    }


    /**
     * The main program for the TestScopeServlet class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestScopeServlet.class);
    }


    /**
     * Default request should do startup of the AppController.
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefault() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("0", response.getContent());
        assertTrue(!AppController.doneTest1Control);
        assertTrue(!AppController.doneTest1aControl);
        assertTrue(!SubController.doneTest2Control);
    }


    /**
     * Route a Test1Control to viewid=1.
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlRouting1() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "1"},
                {"action", "Test1Control"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("TestModel", response.getContent());
        assertTrue(AppController.doneTest1Control);
        assertTrue(!SubController.doneTest2Control);
    }


    /**
     * Route a Test1Control to viewid=1a.
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlRouting4() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "1a"},
                {"action", "Test1Control"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("TestModel", response.getContent());
        assertTrue(AppController.doneTest1Control);
        assertTrue(!SubController.doneTest2Control);
    }


    /**
     * Route a Test1aControl to viewid=1 to set "1a" visible and then let
     * ScopeServlet show the view because the Controller doesn't.
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefaultShowView() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "1"},
                {"action", "Test1aControl"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);
        assertNotNull(response);

//        assertEquals("0", response.getContent());
        assertTrue(AppController.doneTest1aControl);
        assertTrue(!AppController.doneTest1Control);
        assertTrue(!SubController.doneTest2Control);
    }


    /**
     * Route a Test1Control to viewid=1 which is the default Page.
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefaultPage() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"action", "Test1Control"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("TestModel", response.getContent());
        assertTrue(AppController.doneTest1Control);
        assertTrue(!SubController.doneTest2Control);
    }


    /**
     * Route a Test1Control to viewid=NONSENSE should find app controller as
     * first child of context.
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefaultPage2() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "NONSENSE"},
                {"action", "Test1Control"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("TestModel", response.getContent());
        assertTrue(AppController.doneTest1Control);
        assertTrue(!SubController.doneTest2Control);
    }


    /**
     * Route a Test1Control to viewid=2 to see the chain of responsibility is
     * OK.
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlRouting2() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "2"},
                {"action", "Test1Control"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("TestModel", response.getContent());
        assertTrue(AppController.doneTest1Control);
        assertTrue(!SubController.doneTest2Control);
    }


    /**
     * Route a Test2Control to viewid=2.
     *
     * @throws Exception Any abnormal exception
     */
    public void testControlRouting3() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "2"},
                {"action", "Test2Control"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("2TestModel2", response.getContent());
        assertTrue(!AppController.doneTest1Control);
        assertTrue(SubController.doneTest2Control);
    }


    /**
     * Route a Test1Control to viewid=1 and try to populate the view's model.
     *
     * @throws Exception Any abnormal exception
     */
    public void testPopulateModel1() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "1"},
                {"action", "Test1Control"},
                {".name", "NewName1"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("NewName1", response.getContent());
    }


    /**
     * Route a Test1Control to viewid=1 and try to populate the view's model but
     * omitting the viewid from the propertyid so should default to viewid=1
     *
     * @throws Exception Any abnormal exception
     */
    public void testPopulateModel3() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "1"},
                {"action", "Test1Control"},
                {".name", "NewName2"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("NewName2", response.getContent());
    }


    /**
     * Route a Test2Control to viewid=2 and try to populate the view's model.
     *
     * @throws Exception Any abnormal exception
     */
    public void testPopulateModel2() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "2"},
                {"action", "Test2Control"},
                {".name", "NewName3"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("NewName3", response.getContent());
    }


    /**
     * Route a Test2Control to viewid=2 and try to populate the view's model but
     * omitting the viewid from the propertyid so should default to viewid=2
     *
     * @throws Exception Any abnormal exception
     */
    public void testPopulateModel4() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "2"},
                {"action", "Test2Control"},
                {".name", "NewName4"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertEquals("NewName4", response.getContent());
    }


    /**
     * Route a Test2Control to viewid=2 and try to populate the view's model but
     * with a bad selectorid so get a validation error.
     *
     * @throws Exception Any abnormal exception
     */
    public void testPopulateModel5() throws Exception {
        DummyHTTPRequest request = new DummyHTTPRequest(new String[][]{
                {"view", "2"},
                {"action", "Test2Control"},
                {".x", "NewName5"},
                });
        DummyHTTPResponse response = servlet.handlePost(request);

        assertTrue(response.getContent().indexOf("Error") > 0);
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        servlet = new DummyServlet();
        AppController.doneTest1Control = false;
        AppController.doneTest1aControl = false;
        SubController.doneTest2Control = false;
        ViewContext.setGlobalContext(null);
        ViewContext.clearThreadContext();
    }
}
