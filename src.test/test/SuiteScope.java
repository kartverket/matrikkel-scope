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
package test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/09 17:54:34 $
 * @created 06 September 2002
 */
public final class SuiteScope extends TestCase {

    private static final Log LOG = LogFactory.getLog(SuiteScope.class);


    /**
     * Constructor for the SuiteScope object
     *
     * @param inName TODO: Describe the Parameter
     */
    public SuiteScope(String inName) {
        super(inName);
    }


    /**
     * A unit test suite for JUnit
     *
     * @return The test suite
     */
    public static Test suite() {
//        // assertion failures are expected during the test run but don't want to see them
//        Category.getRoot().removeAppender("stdout");

        TestSuite suite = new TestSuite();
        suite.addTest(test.util.convertor.SuiteConvertor.suite());
        suite.addTest(test.view.servlet.xml.SuiteViewServletXML.suite());
        suite.addTest(test.util.SuiteUtil.suite());
        suite.addTest(test.core.SuiteCore.suite());
        suite.addTest(test.controller.basic.SuiteControllerBasic.suite());
        suite.addTest(test.controller.servlet.SuiteControllerServlet.suite());
        suite.addTest(test.controller.swing.SuiteControllerSwing.suite());
        suite.addTest(test.model.util.SuiteModelUtil.suite());
        suite.addTest(test.model.beans.SuiteModelBeans.suite());
        suite.addTest(test.model.basic.SuiteModelBasic.suite());
        suite.addTest(test.model.collection.SuiteModelCollection.suite());
        suite.addTest(test.view.util.SuiteViewUtil.suite());
        suite.addTest(test.view.servlet.SuiteViewServlet.suite());
        suite.addTest(test.view.awt.SuiteViewAWT.suite());
        suite.addTest(test.view.swing.SuiteViewSwing.suite());
        return suite;
    }


    /**
     * The main program for the SuiteScope class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(SuiteScope.class);
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() { }
}
