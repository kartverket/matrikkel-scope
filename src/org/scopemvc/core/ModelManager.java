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
 * $Id: ModelManager.java,v 1.8 2002/09/16 10:49:17 ludovicc Exp $
 */
package org.scopemvc.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.ScopeConfig;

/**
 * <P>
 *
 * ModelManagers provide decoupled operations on model objects including access
 * to properties and invocation of actions. A manager instance for a specific
 * model object is obtained by a static factory method in the manager subclass.
 * </P> <P>
 *
 * Other managers can be plugged in as appropriate to provide different
 * functionality required on model objects. The base Scope implementation
 * provides {@link PropertyManager}, {@link ActionManager} and {@link
 * EditorManager} implementations. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.8 $ $Date: 2002/09/16 10:49:17 $
 */
public abstract class ModelManager {

    private static final Log LOG = LogFactory.getLog(ModelManager.class);

    private Class modelClass;

    /**
     * Create a manager by looking up the class from ScopeConfig by the manager
     * name.
     *
     * @param inManagerName The name of the manager
     * @param inModelClass The class of the models that can be managed by the
     *      new manager instance
     * @return A new manager instance
     */
    protected static ModelManager make(String inManagerName, Class inModelClass) {
        Class managerClass = ScopeConfig.getClass(inManagerName);
        if (managerClass == null) {
            LOG.fatal("No Class for: " + inManagerName);
        }
        try {
            ModelManager result = (ModelManager) managerClass.newInstance();
            result.modelClass = inModelClass;
            return result;
        } catch (Exception e) {
            LOG.fatal("Can't create: " + managerClass, e);
            return null;
        }
    }

    /**
     * Gets the class of the models managed by this manager.
     *
     * @return The modelClass value
     */
    public Class getModelClass() {
        return modelClass;
    }
}
