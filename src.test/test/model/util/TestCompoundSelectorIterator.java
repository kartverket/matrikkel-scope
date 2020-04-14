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
 * $Id: TestCompoundSelectorIterator.java,v 1.4 2002/09/12 19:09:36 ludovicc Exp $
 */
package test.model.util;

import java.util.*;
import junit.framework.TestCase;

import org.scopemvc.core.*;
import org.scopemvc.model.util.*;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.4 $ $Date: 2002/09/12 19:09:36 $
 */
public final class TestCompoundSelectorIterator extends TestCase {

    /**
     * Constructor for the TestCompoundSelectorIterator object
     *
     * @param inName Name of the test
     */
    public TestCompoundSelectorIterator(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCompoundSelectorIterator() throws Exception {

        Iterator iterator1 = new IntIndexSelectorIterator(0, 10);

        Selector[] selectors = {
                Selector.fromString("a"),
                Selector.fromString("b"),
                Selector.fromString("c"),
                Selector.fromString("d"),
                Selector.fromString("e"),
                Selector.fromString("f"),
                };
        Iterator iterator2 = new ArraySelectorIterator(selectors);

        Iterator iterator = new CompoundSelectorIterator(iterator1, iterator2);

        for (int i = 0; i <= 10; ++i) {
            assertTrue(((IntIndexSelector) iterator.next()).getIndex() == i);
        }

        for (int i = 0; i < selectors.length; ++i) {
            assertEquals(selectors[i], iterator.next());
        }

        assertTrue(!iterator.hasNext());
        try {
            iterator.next();
            fail("Beyond end of iterator");
        } catch (Exception e) {
            // expected
        }
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() { }
}

