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
 * $Id: TestModelAction.java,v 1.6 2002/11/20 00:19:56 ludovicc Exp $
 */
package test.core;

import junit.framework.TestCase;
import org.scopemvc.core.ModelAction;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:19:56 $
 */
public final class TestModelAction extends TestCase {

    /**
     * Constructor for the TestModelAction object
     *
     * @param inName Name of the test
     */
    public TestModelAction(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     */
    public void testNoParametersModelAction() {
        ModelAction ma = new ModelAction("Test");
        assertEquals("Test", ma.getMethodName());
        assertNotNull(ma.getParameterClasses());
        assertTrue(ma.getParameterClasses().length == 0);
    }


    /**
     * A unit test for JUnit
     */
    public void testSingleParameterModelAction() {
        ModelAction ma = new ModelAction("Test2", Object.class);
        assertEquals("Test2", ma.getMethodName());
        assertTrue(ma.getParameterClasses()[0].equals(Object.class));
        assertTrue(ma.getParameterClasses().length == 1);
    }


    /**
     * A unit test for JUnit
     */
    public void testMultipleParameterModelAction() {
        ModelAction ma = new ModelAction("Test3", new Class[]{Integer.class, String.class});
        assertEquals("Test3", ma.getMethodName());
        assertTrue(ma.getParameterClasses()[0].equals(Integer.class));
        assertTrue(ma.getParameterClasses()[1].equals(String.class));
        assertTrue(ma.getParameterClasses().length == 2);
    }


    /**
     * A unit test for JUnit
     */
    public void testBadModelAction() {
        try {
            ModelAction ma = new ModelAction((String) null);
            fail("Null method name in model action " + ma);
        } catch (Throwable t) {
            // OK assertion failed
        }
    }


    /**
     * A unit test for JUnit
     */
    public void testBadModelAction2() {
        try {
            ModelAction ma = new ModelAction("a", (Class) null);
            fail("Null parameter class in model action " + ma);
        } catch (Throwable t) {
            // OK assertion failed
        }
        try {
            ModelAction ma = new ModelAction("b", (Class[]) null);
            fail("Null parameter class array in model action " + ma);
        } catch (Throwable t) {
            // OK assertion failed
        }
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() { }
}

