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
 * $Id: TestScopeConfig.java,v 1.6 2002/09/12 19:09:36 ludovicc Exp $
 */
package test.util;

import java.util.*;
import junit.framework.TestCase;

import org.scopemvc.util.ScopeConfig;

/**
 * <P>
 *
 * Can't test both the default and custom cases at the same time because loaded
 * statically. ***** Need to extend for new config overrides. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/12 19:09:36 $
 */
public final class TestScopeConfig extends TestCase {

    /**
     * Constructor for the TestScopeConfig object
     *
     * @param inName Name of the test
     */
    public TestScopeConfig(String inName) {
        super(inName);
    }


    /**
     * The JUnit setup method
     */
    @Override
    public void setUp() {
        ScopeConfig.setPropertiesName("test.util.CustomScopeConfig");
    }

    @Override
    protected void tearDown() {
        ScopeConfig.setPropertiesName(org.scopemvc.util.DefaultScopeConfig.class.getName());
    }

    /**
     * A unit test for JUnit
     */
    public void testScopeConfigDefaultString() {
        assertEquals("view", ScopeConfig.getString("org.scopemvc.controller.servlet.ScopeServlet.ViewIDParam"));
    }


    /**
     * A unit test for JUnit
     */
    public void testScopeConfigOverrideString() {
        assertEquals("0", ScopeConfig.getString("org.scopemvc.view.servlet.xml.AbstractXSLPage.shouldCacheTemplates"));
    }


    /**
     * A unit test for JUnit
     */
    public void testScopeConfigClass() {
        assertEquals(java.lang.Integer.class, ScopeConfig.getClass("classTest.1"));
        assertEquals(java.lang.Integer.class, ScopeConfig.getClass("classTest.2"));
    }


    /**
     * A unit test for JUnit
     */
    public void testScopeConfigInteger() {
        assertEquals(Integer.valueOf(99), ScopeConfig.getInteger("integerTest.1"));
        assertEquals(Integer.valueOf(99), ScopeConfig.getInteger("integerTest.2"));
    }


    /**
     * A unit test for JUnit
     */
    public void testScopeConfigMatching() {
        Iterator i = ScopeConfig.getKeysMatching("classTest.");
        assertTrue(i.hasNext());
        List l = new LinkedList();
        l.add(i.next());
        assertTrue(i.hasNext());
        l.add(i.next());
        assertTrue(!i.hasNext());
        assertTrue(l.contains("classTest.1"));
        assertTrue(l.contains("classTest.2"));
    }
}

