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
 * $Id: StringIndexSelector.java,v 1.4 2002/09/05 15:41:45 ludovicc Exp $
 */
package org.scopemvc.core;


/**
 * <P>
 *
 * An implementation of {@link Selector} that identifies a property by its
 * <CODE>String</CODE> index within the parent model object. </P> <P>
 *
 * Create by {@link Selector#fromString(String)}. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:45 $
 */
public final class StringIndexSelector extends Selector {

    /**
     * The <CODE>String</CODE> index of the selected property. Immutable after
     * construction.
     */
    private String index;


    /**
     * Package private for factory to create.
     *
     * @param inIndex The index (name) of the property to select
     */
    StringIndexSelector(String inIndex) {
        setIndex(inIndex);
    }


    /**
     * Gets the index (i.e the name of the property selected)
     *
     * @return The index value
     * @see #getName
     */
    public final String getIndex() {
        return index;
    }


    /**
     * Gets the name of the property selected
     *
     * @return The name value
     */
    public final String getName() {
        return getIndex();
    }


    /**
     * Sets the index
     *
     * @param inIndex The new index value
     */
    public void setIndex(String inIndex) {
        index = inIndex;
    }


    /**
     * Returns the hashCode
     *
     * @return the hashCode of this object
     */
    public int hashCode() {
        return index.hashCode();
    }


    /**
     * Creates a shallow copy of this selector
     *
     * @return a shallow copy of this selector
     */
    protected Selector getShallowCopy() {
        return new StringIndexSelector(index);
    }


    /**
     * Returns true if this Selector is equals to the given Selector when
     * omitting the chained Selectors
     *
     * @param inSelector Another Selector to test against
     * @return true if we have shallow equality
     */
    protected final boolean shallowEquals(Selector inSelector) {
        if (!(inSelector instanceof StringIndexSelector)) {
            return false;
        }
        return getIndex().equals(((StringIndexSelector) inSelector).getIndex());
    }
}

