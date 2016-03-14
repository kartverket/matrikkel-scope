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


import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Pools objects given a factory and optional maximum pool size.
 *
 * @author <A HREF="mailto:haruki_zaemon@users.sourceforge.net">Simon Harris</A>
 * @version $Revision: 1.6 $
 * @created 05 September 2002
 * @todo Replace with the Object pool classes from Jakarta (commons-pool
 *      library), that will be less code to maintain (ludovicc)
 */
public class BasicObjectPool implements ObjectPool {

    private static final Log LOG = LogFactory.getLog(BasicObjectPool.class);

    /**
     * Used for synchronisation
     */
    private final Object lock = new Object();

    /**
     * Queue of free instances
     */
    private final LinkedList freeList = new LinkedList();

    /**
     * The factory used to create pooled objects
     */
    private final PoolableObjectFactory factory;

    /**
     * The maximum number of objects to pool
     */
    private final int maxSize;

    /**
     * Construct a basic object pool with the specified factory and a maximum
     * pool size of Integer.MAX_VALUE.
     *
     * @param inFactory TODO: Describe the Parameter
     */
    public BasicObjectPool(PoolableObjectFactory inFactory) {
        this(inFactory, Integer.MAX_VALUE);
    }

    /**
     * Construct a basic object pool with the specified factory and maximum pool
     * size.
     *
     * @param inFactory TODO: Describe the Parameter
     * @param inMaxSize TODO: Describe the Parameter
     */
    public BasicObjectPool(PoolableObjectFactory inFactory, int inMaxSize) {
        factory = inFactory;
        maxSize = inMaxSize;
    }

    /**
     * Obtains an instance of the pooled class. Blocks if no free instances.
     *
     * @return TODO: Describe the Return Value
     */
    public Object borrowObject() {
        while (true) {
            // Get an instance
            Object object = borrowObjectIfExists();

            // Was there one?
            if (object != null) {
                // Yes->
                return object;
            }

            // No, wait for one to be freed
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException ie) {
                    // Ignore it
                }
            }
        }
    }

    /**
     * Obtains an instance of the pooled class. Returns null if no free
     * instances.
     *
     * @return TODO: Describe the Return Value
     */
    public Object borrowObjectIfExists() {
        synchronized (lock) {
            Object object = null;

            // Any free objects?
            if (!freeList.isEmpty()) {
                // Yes, get the next one
                object = freeList.removeFirst();
            } else {
                // No, are we allowed to create another?
                if (freeList.size() < maxSize) {
                    // Yes, create one
                    object = factory.createObject();
                }
            }

            // Finished, return the instance (or null) to the caller->
            factory.activateObject(object);
            return object;
        }
    }

    /**
     * Returns an instance to the pool.
     *
     * @param object A previously borrowed instance.
     * @throw IllegalArgumentException if object was not previosuly borrowed
     *      from the pool.
     */
    public void returnObject(Object object) {
        synchronized (lock) {
            factory.passivateObject(object);
            freeList.addLast(object);
            lock.notifyAll();
        }
    }
}
