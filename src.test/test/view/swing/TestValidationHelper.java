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

import javax.swing.*;
import junit.framework.TestCase;

import org.scopemvc.view.swing.*;

/**
 * <P>
 *
 * Tests ValidationHelper via STextField. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:14:01 $
 * @created 05 September 2002
 */
public final class TestValidationHelper extends TestCase {

    private STextField parent;
    private STextField ref;
    private JFrame f;


    /**
     * Constructor for the TestValidationHelper object
     *
     * @param inName Name of the test
     */
    public TestValidationHelper(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConstructor() throws Exception {
        JToolTip t = parent.createToolTip();
        assertEquals("original", parent.getToolTipText());
        assertEquals(ref.createToolTip().getBackground(), t.getBackground());
        assertEquals(ref.getBackground(), parent.getBackground());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSuccess() throws Exception {
        parent.validationSuccess();

        JToolTip t = parent.createToolTip();
        assertEquals("original", parent.getToolTipText());
        assertEquals(ref.createToolTip().getBackground(), t.getBackground());
        assertEquals(ref.getBackground(), parent.getBackground());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFailed() throws Exception {
        parent.validationFailed(new Exception("test"));

        JToolTip t = parent.createToolTip();
        assertEquals("test", parent.getToolTipText());
        assertEquals(ValidationHelper.DEFAULT_VALIDATION_FAILED_COLOR, t.getBackground());
        assertEquals(ValidationHelper.DEFAULT_VALIDATION_FAILED_COLOR, parent.getBackground());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testFailedSuccess() throws Exception {
        parent.validationFailed(new Exception("test1"));
        parent.validationSuccess();

        JToolTip t = parent.createToolTip();
        assertEquals("original", parent.getToolTipText());
        assertEquals(ref.createToolTip().getBackground(), t.getBackground());
        assertEquals(ref.getBackground(), parent.getBackground());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        parent = new STextField();
        parent.setToolTipText("original");
        ref = new STextField();
        ref.setToolTipText("reference");
        f = new JFrame();
        f.getContentPane().add(parent);
        f.getContentPane().add(ref);
        f.pack();
        f.setVisible(true);
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        f.setVisible(false);
        f.dispose();
    }
}

