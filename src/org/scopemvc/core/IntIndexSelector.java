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
 * $Id: IntIndexSelector.java,v 1.6 2002/09/11 19:12:29 ludovicc Exp $
 */
package org.scopemvc.core;


/**
 * <P>
 *
 * An implementation of {@link Selector} that identifies a property by its
 * <CODE>int</CODE> index within the parent model object. This is used to access
 * properties in java.util.List and Object[]. </P> <P>
 *
 * Created by {@link Selector#fromInt(int)} or {@link
 * Selector#fromString(String)}. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/11 19:12:29 $
 */
public final class IntIndexSelector extends Selector {

    /**
     * The <CODE>int</CODE> index of the selected property.
     */
    private int index;


    /**
     * Package private for factory to create.
     *
     * @param inIndex The index of the property to select
     */
    IntIndexSelector(int inIndex) {
        setIndex(inIndex);
    }


    /**
     * Gets the index of the property in the list or Object[]
     *
     * @return The index value
     */
    public int getIndex() {
        return index;
    }


    /**
     * Gets the name of the selector
     *
     * @return The name value
     */
    public String getName() {
        return Integer.toString(getIndex());
    }


    /**
     * Sets the index
     *
     * @param inIndex The new index value
     */
    public void setIndex(int inIndex) {
        index = inIndex;
    }


    /**
     * Returns the hashCode
     *
     * @return the hashCode
     */
    public int hashCode() {
        return getIndex();
    }


    /**
     * Creates a shallow copy of this selector
     *
     * @return a shallow copy of this selector
     */
    protected Selector getShallowCopy() {
        return Selector.fromInt(index);
    }


    /**
     * Returns true if this Selector is equals to the given Selector when
     * omitting the chained Selectors
     *
     * @param inSelector Another Selector to test against
     * @return true if we have shallow equality
     */
    protected boolean shallowEquals(Selector inSelector) {
        if (!(inSelector instanceof IntIndexSelector)) {
            return false;
        }
        return getIndex() == ((IntIndexSelector) inSelector).getIndex();
    }
}

