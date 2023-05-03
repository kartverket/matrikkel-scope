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
 * $Id: TestControl.java,v 1.5 2002/11/20 00:19:56 ludovicc Exp $
 */
package test.core;

import junit.framework.TestCase;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:56 $
 */
public final class TestControl extends TestCase {

    /**
     * Constructor for the TestControl object
     *
     * @param inName Name of the test
     */
    public TestControl(String inName) {
        super(inName);
    }


    /**
     * Test that Control is created with an ID, and that a parameter can be set.
     */
    public void testControl() {
        Control control = new Control("ID1");
        assertTrue(!control.isMatched());
        assertTrue(control.matchesID("ID1"));
        control.setParameter("PARAM1");
        assertEquals("PARAM1", control.getParameter());
    }


    /**
     * A unit test for JUnit
     */
    public void testControlFullConstructor() {
        Control control = new Control("ID2", "PARAM2");
        assertTrue(!control.isMatched());
        assertTrue(control.matchesID("ID2"));
        assertEquals("PARAM2", control.getParameter());
    }


    /**
     * A unit test for JUnit
     */
    public void testMatch() {
        Control control = new Control("ID3");
        assertTrue(!control.isMatched());
        assertTrue(control.matchesID("ID3"));
        assertTrue(control.isMatched());
    }


    /**
     * A unit test for JUnit
     */
    public void testPopulateControlException() {
        Control control = new Control("ID4");
        ControlException e = new ControlException("hello");
        control.populateControlException(e);
        assertSame("ID4", e.getLocalizedSourceControlName());
        // assume no trans in uistrings
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() { }
}

