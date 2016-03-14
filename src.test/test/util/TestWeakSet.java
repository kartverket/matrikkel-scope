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
package test.util;

import java.util.*;
import junit.framework.TestCase;

import org.scopemvc.util.*;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:haruki_zaemon@users.sourceforge.net">Simon Harris</A>
 * @version $Revision: 1.5 $
 * @created 05 September 2002
 */
public final class TestWeakSet extends TestCase {

    /**
     * Constructor for the TestWeakSet object
     *
     * @param inName Name of the test
     */
    public TestWeakSet(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     */
    public void testEmpty() {
        Set set = new WeakSet();
        assertTrue("New set not empty", set.isEmpty());
    }

    /**
     * A unit test for JUnit
     */
    public void testAddLots() {
        Set set = new WeakSet();
        Integer[] refHolder = new Integer[20];
        for (int i = 0; i < 20; i++) {
            refHolder[i] = new Integer(i);
            set.add(refHolder[i]);
        }
        for (int i = 0; i < 20; i++) {
            assertTrue("Entry not found: " + i, set.contains(new Integer(i)));
        }
    }

    /**
     * A unit test for JUnit
     */
    public void testAddRemove() {
        Set set = new WeakSet();
        Integer[] refHolder = new Integer[20];
        for (int i = 0; i < 20; i++) {
            refHolder[i] = new Integer(i);
            set.add(refHolder[i]);
        }

        assertTrue("size not correct", set.size() == 20);

        for (int i = 0; i < 20; i++) {
            assertTrue("Entry not found" + i, set.remove(new Integer(i)));
        }
        assertTrue("remove failed", set.isEmpty());
    }

    /**
     * A unit test for JUnit
     */
    public void testClear() {
        Set set = new WeakSet();
        Integer[] refHolder = new Integer[20];
        for (int i = 0; i < 20; i++) {
            refHolder[i] = new Integer(i);
            set.add(refHolder[i]);
        }
        set.clear();
        assertTrue("set not cleared", set.isEmpty());
    }

    /**
     * A unit test for JUnit
     */
    public void testGC() {
        Set set = new WeakSet();
        List list = new LinkedList();

        Integer[] refHolder = new Integer[20];
        for (int i = 0; i < 10; i++) {
            refHolder[i] = new Integer(i);
            set.add(refHolder[i]);
        }

        for (int i = 10; i < 20; i++) {
            Integer integer = new Integer(i);
            set.add(integer);
            list.add(integer);
        }
        assertTrue("size not correct before gc", set.size() == 20);

        refHolder = null;
        System.gc();
        assertTrue("size not correct after gc", set.size() == 10);

        list.clear();
        System.gc();
        assertTrue("size not correct after gc", set.size() == 0);
    }

    /**
     * A unit test for JUnit
     */
    public void testIterator() {
        Set set = new WeakSet();
        List list = new LinkedList();
        for (int i = 0; i < 20; i++) {
            Integer integer = new Integer(i);
            set.add(integer);
            list.add(integer);
        }

        int found = 0;
        for (Iterator i = set.iterator(); i.hasNext(); ) {
            Object o = i.next();
            assertTrue("entry was null", o != null);
            found++;
            // remove the element just found
            list.remove(o);
            // remove any object in the list other than the current one
            // since all elements found have already been removed from the list,
            // this guarantees that the element to be removed has not yet been iterated over.
            int index = list.indexOf(o);
            // to use another object
            index++;
            if (index >= list.size()) {
                index = 0;
            }
            list.remove(index);
            System.gc();
        }
        // 10 elements found because for each element found, another one should be removed from the list and GC'd
        assertEquals("iterator incorrect", 10, found);
    }

    /**
     * The JUnit setup method
     */
    protected void setUp() { }
}
