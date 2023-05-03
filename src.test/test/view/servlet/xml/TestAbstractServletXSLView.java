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
 * $Id: TestAbstractServletXSLView.java,v 1.9 2002/11/20 00:19:59 ludovicc Exp $
 */
package test.view.servlet.xml;

import junit.framework.TestCase;
import org.scopemvc.view.servlet.xml.AbstractXSLPage;
import org.xml.sax.ContentHandler;

import java.net.URL;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.9 $ $Date: 2002/11/20 00:19:59 $
 */
public final class TestAbstractServletXSLView extends TestCase {

    private DummyXSLView view;


    /**
     * Constructor for the TestAbstractServletXSLView object
     *
     * @param inName Name of the test
     */
    public TestAbstractServletXSLView(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSetSystemID() throws Exception {
        assertEquals("", view.getSystemID());

        view.setSystemID("a");
        assertEquals("a", view.getSystemID());

        view.setSystemID(null);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetSetXslUri() throws Exception {
        DummyXSLView v = new DummyXSLView();
        assertNull(v.getXslURI());

        assertTrue(view.getXslURI().endsWith("/test/view/servlet/xml/test.xsl"));

        view.setXslURI("xyz");
        assertEquals("xyz", view.getXslURI());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testGetContentType() throws Exception {
        assertEquals("text/html", view.getContentType());

        view = new DummyXSLView();
        assertEquals("text/xml", view.getContentType());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        view = new DummyXSLView();
        URL xslURL = getClass().getResource("/test/view/servlet/xml/test.xsl");
        assertTrue(xslURL != null);
        view.setXslURI(xslURL.toString());
    }

    // Both these fail with LinkageErrors trying load org.xmlContentHandler
//    public void testStream1() throws Exception {
//        assertEquals("", view.getSystemID());
//        StringWriter writer = new StringWriter();
//        view.streamView(writer);
//        assertTrue(writer.toString().indexOf("<HTML>") > -1);
//
//        writer = new StringWriter();
//        view.streamView(writer);
//        assertTrue(writer.toString().indexOf("<HTML>") > -1);
//    }
//
//
//    public void testStream2() throws Exception {
//        view.setCache(true);
//        StringWriter writer = new StringWriter();
//        view.streamView(writer);
//        assertTrue(writer.toString().indexOf("<HTML>") > -1);
//
//        writer = new StringWriter();
//        view.streamView(writer);
//        assertTrue(writer.toString().indexOf("<HTML>") > -1);
//    }
}

final class DummyXSLView extends AbstractXSLPage {

    /**
     * Constructor for the DummyXSLView object
     */
    DummyXSLView() {
        super(null, null);
        shouldCacheTemplates = false;
    }

    /**
     * TODO: document the method
     *
     * @param inContentHandler TODO: Describe the Parameter
     * @throws Exception Any abnormal exception
     */
    protected void generateXMLDocument(ContentHandler inContentHandler) throws Exception {
        inContentHandler.startDocument();

        inContentHandler.startElement("", "elem", "elem", null);
        inContentHandler.endElement("", "elem", "elem");

        inContentHandler.endDocument();
    }

    /**
     * Sets the cache
     *
     * @param inCache The new cache value
     */
    void setCache(boolean inCache) {
        shouldCacheTemplates = inCache;
    }
}
