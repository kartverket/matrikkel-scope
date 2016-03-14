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
package org.scopemvc.model.util;


import java.util.Iterator;
import org.scopemvc.core.Selector;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * An Iterator that iterates over an array of Selectors. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.8 $ $Date: 2002/10/31 12:15:54 $
 * @created 05 September 2002
 */
public final class ArraySelectorIterator implements Iterator {

    private Selector[] selectors;

    private int currentIndex = -1;


    /**
     * Constructor for the ArraySelectorIterator object
     *
     * @param inSelectors The array of selectors to iterate over
     */
    public ArraySelectorIterator(Selector[] inSelectors) {
        if (inSelectors == null) {
            selectors = new Selector[0];
        } else {
            selectors = inSelectors;
        }
        currentIndex = 0;
    }


    /**
     * Returns <tt>true</tt> if the iteration has more elements.
     *
     * @return <tt>true</tt> if the iteration has more elements.
     */
    public boolean hasNext() {
        if (Debug.ON) {
            Debug.assertTrue(currentIndex > -1);
        }
        return currentIndex < selectors.length;
    }


    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     */
    public Object next() {
        if (Debug.ON) {
            Debug.assertTrue(selectors != null, "null selectors");
        }
        if (!(currentIndex > -1 && currentIndex < selectors.length)) {
            throw new RuntimeException("Iterator past end of Selector list.");
        }

        Selector result = selectors[currentIndex];
        ++currentIndex;
        return result;
    }


    /**
     * Removes from the underlying collection the last element returned by the
     * iterator. Not supported here.
     */
    public void remove() {
        throw new UnsupportedOperationException("Can't remove Selectors from a model.");
    }
}

