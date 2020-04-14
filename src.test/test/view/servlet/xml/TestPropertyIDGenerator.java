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
 * $Id: TestPropertyIDGenerator.java,v 1.5 2002/11/20 00:19:59 ludovicc Exp $
 */
package test.view.servlet.xml;


import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.view.servlet.xml.PropertyIDGenerator;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:59 $
 */
public final class TestPropertyIDGenerator extends TestCase {

    private PropertyIDGenerator propertyIDGenerator;


    /**
     * Constructor for the TestPropertyIDGenerator object
     *
     * @param inName Name of the test
     */
    public TestPropertyIDGenerator(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testPropertyIDs1() throws Exception {
        assertEquals("", propertyIDGenerator.getPropertyID());
        assertEquals("", propertyIDGenerator.getPropertyID());

        propertyIDGenerator.startProperty(Selector.fromString("abc"));
        assertEquals("abc", propertyIDGenerator.getPropertyID());
        assertEquals("abc", propertyIDGenerator.getPropertyID());

        propertyIDGenerator.endProperty();
        assertEquals("", propertyIDGenerator.getPropertyID());
        assertEquals("", propertyIDGenerator.getPropertyID());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testPropertyIDs2() throws Exception {
        propertyIDGenerator.startProperty(Selector.fromString("abc"));
        propertyIDGenerator.startProperty(Selector.fromString("0"));
        assertEquals("abc.0", propertyIDGenerator.getPropertyID());
        assertEquals("abc.0", propertyIDGenerator.getPropertyID());

        propertyIDGenerator.endProperty();
        assertEquals("abc", propertyIDGenerator.getPropertyID());
        assertEquals("abc", propertyIDGenerator.getPropertyID());

        propertyIDGenerator.endProperty();
        assertEquals("", propertyIDGenerator.getPropertyID());
        assertEquals("", propertyIDGenerator.getPropertyID());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testPropertyIDs3() throws Exception {
        propertyIDGenerator.startProperty(Selector.fromString("abc.0"));
        assertEquals("abc.0", propertyIDGenerator.getPropertyID());
        assertEquals("abc.0", propertyIDGenerator.getPropertyID());

        propertyIDGenerator.endProperty();
        assertEquals("abc", propertyIDGenerator.getPropertyID());
        assertEquals("abc", propertyIDGenerator.getPropertyID());

        propertyIDGenerator.endProperty();
        assertEquals("", propertyIDGenerator.getPropertyID());
        assertEquals("", propertyIDGenerator.getPropertyID());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        propertyIDGenerator =
            new PropertyIDGenerator() {
                public String getPropertyID() {
                    return Selector.asString(currentPropertySelector);
                }
            };
    }
}
