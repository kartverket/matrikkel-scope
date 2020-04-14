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
 * $Id: DynamicReadOnly.java,v 1.5 2002/09/16 10:49:17 ludovicc Exp $
 */
package org.scopemvc.model.beans;


import org.scopemvc.core.Selector;

/**
 * <P>
 *
 * {@link BeansPropertyManager} queries models that implement this for whether a
 * property is read-only before falling back on simply looking for a
 * setter/getter. </P> <P>
 *
 * If you have properties on a model that can become read-only at runtime, then
 * implement this interface on your model class. </P> Warning: This can't handle
 * JavaBeans indexed properties. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/09/16 10:49:17 $
 */
public interface DynamicReadOnly {

    /**
     * Returns true if the property is read-only with this model in its current
     * state.
     *
     * @param inSelector Selector for a property directly in this model. <CODE>inSelector.getNext()</CODE>
     *      is guaranteed to be null.
     * @return true if the property is read-only else false. If true, this
     *      overrides then the property is read-only even if there is an
     *      accessible setter for it. If false the normal rules are applied by
     *      the BeansPropertyManager.
     */
    boolean isPropertyReadOnly(Selector inSelector);
}

