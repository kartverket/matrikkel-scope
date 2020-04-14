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
 * $Id: IntIndexSelectorIterator.java,v 1.6 2002/10/31 12:15:54 ludovicc Exp $
 */
package org.scopemvc.model.util;


import java.util.Iterator;
import org.scopemvc.core.IntIndexSelector;
import org.scopemvc.core.Selector;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * An Iterator that iterates between a range of IntIndexSelectors. Construct
 * with a start and end index: the iterator steps over this sequence inclusive.
 * ie (0, 0) gives a 0 Selector, but (0, -1) gives no Selector. </P> <P>
 *
 * Note: the Selectors returned must be treated as immutable: it is the same
 * Selector. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/10/31 12:15:54 $
 * @created 05 September 2002
 */
public final class IntIndexSelectorIterator implements Iterator {

    private int startIndex;
    private int endIndex;

    private int currentIndex = -1;

    private IntIndexSelector currentSelector;


    /**
     * Constructor for the IntIndexSelectorIterator object
     *
     * @param inStartIndex Start index for the selector
     * @param inEndIndex End index for the selector
     */
    public IntIndexSelectorIterator(int inStartIndex, int inEndIndex) {
        if (inStartIndex > inEndIndex) {
            int temp = inStartIndex;
            inStartIndex = inEndIndex;
            inEndIndex = temp;
        }
        startIndex = inStartIndex;
        endIndex = inEndIndex;
        currentIndex = startIndex;

        currentSelector = (IntIndexSelector) Selector.fromInt(0);
        // create shared instance
    }


    /**
     * Returns <tt>true</tt> if the iteration has more elements.
     *
     * @return <tt>true</tt> if the iteration has more elements.
     */
    public boolean hasNext() {
        if (Debug.ON) {
            Debug.assertTrue(currentIndex >= startIndex);
        }
        return currentIndex <= endIndex;
    }


    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     */
    public Object next() {
        if (!(currentIndex >= startIndex && currentIndex <= endIndex)) {
            throw new RuntimeException("Iterator past end of Selector list.");
        }

        currentSelector.setIndex(currentIndex);
        ++currentIndex;
        return currentSelector;
    }


    /**
     * Removes from the underlying collection the last element returned by the
     * iterator. Not supported here.
     */
    public void remove() {
        throw new UnsupportedOperationException("Can't remove Selectors from a model.");
    }
}
