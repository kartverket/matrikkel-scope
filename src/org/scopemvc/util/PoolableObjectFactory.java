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
package org.scopemvc.util;


/**
 * Interface for creating poolable objects.
 *
 * @author <A HREF="mailto:haruki_zaemon@users.sourceforge.net">Simon Harris</A>
 * @version $Revision: 1.4 $
 * @created 05 September 2002
 * @todo Replace with the Object pool classes from Jakarta (commons-pool
 *      library), that will be less code to maintain (ludovicc)
 */
public interface PoolableObjectFactory {
    /**
     * Creates and returns a poolable object.
     *
     * @return TODO: Describe the Return Value
     */
    public Object createObject();

    /**
     * Peforms whatever processing is required prior to destruction of a pooled
     * object.
     *
     * @param object TODO: Describe the Parameter
     */
    public void destroyObject(Object object);

    /**
     * Peforms whatever processing is required prior to activating a pooled
     * object.
     *
     * @param object TODO: Describe the Parameter
     */
    public void activateObject(Object object);

    /**
     * Peforms whatever processing is required prior to passivating a pooled
     * object.
     *
     * @param object TODO: Describe the Parameter
     */
    public void passivateObject(Object object);
}
