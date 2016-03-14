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
package test.core;


import junit.framework.TestCase;
import org.scopemvc.core.ControlException;
import org.scopemvc.core.Selector;
import org.scopemvc.util.UIStrings;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/12 19:09:34 $
 * @created 05 September 2002
 */
public final class TestExceptions extends TestCase {
    private static final String MESSAGE1 = "MESSAGE1";
    private static final String MESSAGE2 = "MESSAGE2";
    private static final String CONTROL_ID = "TEST_CONTROL_ID";

    private Object model;
    private Selector selector;


    /**
     * Constructor for the TestExceptions object
     *
     * @param inName Name of the test
     */
    public TestExceptions(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     */
    public void testControlException1() {
        ControlException cex = new ControlException(MESSAGE1);
        cex.setSourceControlID(CONTROL_ID);
        assertSame(MESSAGE1, cex.getMessage());
        assertEquals("Test Control", cex.getLocalizedSourceControlName());
        assertEquals("Test Message 1", cex.getLocalizedMessage());
    }


    /**
     * A unit test for JUnit
     */
    public void testControlException2() {
        ControlException cex = new ControlException(MESSAGE2, new Object[]{"Insert"});
        cex.setSourceControlID(CONTROL_ID);
        assertSame(MESSAGE2, cex.getMessage());
        assertEquals("Test Control", cex.getLocalizedSourceControlName());
        assertEquals("Test Message 2 Insert", cex.getLocalizedMessage());
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        model = new Object();
        selector = Selector.fromInt(0);
        UIStrings.setPropertiesName("test.util.DummyUIResources");
    }
}

