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
 * $Id: ObjectPoolManager.java,v 1.6 2002/09/25 13:53:07 ludovicc Exp $
 */
package org.scopemvc.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages pooling of instances of different classes.
 *
 * @author <A HREF="mailto:haruki_zaemon@users.sourceforge.net">Simon Harris</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/25 13:53:07 $
 * @created 05 September 2002
 * @todo Replace with the Object pool classes from Jakarta (commons-pool
 *      library), that will be less code to maintain (ludovicc)
 */
public class ObjectPoolManager {

    private static final Log LOG = LogFactory.getLog(ObjectPoolManager.class);

    /**
     * The singleton instance of this class
     */
    private static final ObjectPoolManager INSTANCE = new ObjectPoolManager();

    /**
     * Object pools keyed by class
     */
    private final Map poolByClass = new HashMap();

    /**
     * Constructor.
     */
    private ObjectPoolManager() { }

    /**
     * Obtains the singleton instance of this class.
     *
     * @return The instance value
     */
    public static ObjectPoolManager getInstance() {
        return INSTANCE;
    }

    /**
     * Obtains the pool for the given class
     *
     * @param clazz TODO: Describe the Parameter
     * @return The objectPool value
     */
    public final ObjectPool getObjectPool(Class clazz) {
        synchronized (poolByClass) {
            ObjectPool pool = (ObjectPool) poolByClass.get(clazz);
            if (pool == null) {
                pool = new BasicObjectPool(new BasicPoolableObjectFactory(clazz));
                addObjectPool(clazz, pool);
            }
            return pool;
        }
    }

    /**
     * Obtains an instance of the sepcified class from the pool. Blocks if no
     * free instances.
     *
     * @param clazz TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public final Object borrowObject(Class clazz) {
        return getObjectPool(clazz).borrowObject();
    }

    /**
     * Obtains an instance of the specified class from the pool. Returns null if
     * no free instances.
     *
     * @param clazz TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public final Object borrowObjectIfExists(Class clazz) {
        return getObjectPool(clazz).borrowObjectIfExists();
    }

    /**
     * Returns an instance to the pool.
     *
     * @param object TODO: Describe the Parameter
     */
    public final void returnObject(Object object) {
        getObjectPool(object.getClass()).returnObject(object);
    }

    /**
     * Adds a pool to be managed.
     *
     * @param clazz The element to be added to the ObjectPool attribute
     * @param pool The element to be added to the ObjectPool attribute
     */
    public final void addObjectPool(Class clazz, ObjectPool pool) {
        synchronized (poolByClass) {
            // REDTAG: Need to ensure it doesn't exist already
            poolByClass.put(clazz, pool);
        }
    }
}
