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
package test.view.servlet.xml;


import java.net.URL;
import java.util.HashMap;
import java.util.List;
import junit.framework.TestCase;
import org.scopemvc.view.servlet.ServletView;
import org.scopemvc.view.servlet.xml.XSLPage;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/09/19 18:09:34 $
 * @created 05 September 2002
 */
public final class TestServletXSLView extends TestCase {

    private ServletView sv;
    private XSLPage view;
    private SimpleModel model;


    /**
     * Constructor for the TestServletXSLView object
     *
     * @param inName Name of the test
     */
    public TestServletXSLView(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testPopulate() throws Exception {
        HashMap params = new HashMap();
        params.put(".a", "-77");
        params.put(".name", "xyz");

        view.populateModel(params);

        assertEquals(-77, model.getA());
        assertEquals("xyz", model.getName());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testPopulateException() throws Exception {
        HashMap params = new HashMap();
        params.put(".xxx", "-77");
        params.put(".a", "yyy");
        params.put(".name", "xyz");

        List l = view.populateModel(params);
        assertTrue(l.size() == 2);
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        view = new XSLPage(null, null);
        URL xslURL = getClass().getResource("/test/view/servlet/xml/test1.xsl");
        assertTrue(xslURL != null);
        view.setXslURI(xslURL.toString());

        model = new SimpleModel();

        sv = new ServletView();
        sv.addPage(view);
        sv.setBoundModel(model);
    }

    // This fails with a LinkageError trying to load org.xml.sax.ContentHandler
//    public void testStream1() throws Exception {
//        StringWriter writer = new StringWriter();
//        view.streamView(writer);
//        String output = writer.toString();
//
//        assertTrue(output.indexOf("<HTML>") > -1);
//        assertTrue(output.indexOf("abc") > -1);
//        assertTrue(output.indexOf("xyz") > -1);
//    }
}
