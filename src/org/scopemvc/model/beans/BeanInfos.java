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
package org.scopemvc.model.beans;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * Internal class used by the Beans ModelManagers to access BeanInfo for model
 * objects. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/09/05 15:41:45 $
 * @created 05 September 2002
 */
final class BeanInfos {

    private static final Log LOG = LogFactory.getLog(BeanInfos.class);

    private static HashMap beanInfos = new HashMap();
    private static HashMap propertyDescriptors = new HashMap();


    /**
     * Gets the bean info
     *
     * @param inModelClass TODO: Describe the Parameter
     * @return The beanInfo value
     */
    static BeanInfo getBeanInfo(Class inModelClass) {
        // Got it cached? Note that Introspector maintains a cache but
        // ... it returns a copy of cached BeanInfos. Since Scope treats
        // ... the BeanInfo as immutable we may as well cache BeanInfo
        // ... here to avoid the BeanInfo copy.
        BeanInfo result = (BeanInfo) beanInfos.get(inModelClass);
        if (result == null) {
            // Not in cache so get one in standard JavaBeans way
            try {
                result = Introspector.getBeanInfo(inModelClass, Object.class);
                if (Debug.ON) {
                    Debug.assertTrue(result != null);
                }
                beanInfos.put(inModelClass, result);
                cachePropertyDescriptors(inModelClass, result);
            } catch (IntrospectionException e) {
                LOG.fatal("Can't find BeanInfo for: " + inModelClass, e);
                if (Debug.ON) {
                    Debug.assertTrue(1 == 0, e.toString());
                }
            }
        }
        return result;
    }


    /**
     * Gets the property descriptor
     *
     * @param inModelClass TODO: Describe the Parameter
     * @param inPropertyName TODO: Describe the Parameter
     * @return The propertyDescriptor value
     */
    static PropertyDescriptor getPropertyDescriptor(Class inModelClass, String inPropertyName) {
        if (Debug.ON) {
            Debug.assertTrue(inPropertyName != null, "null inPropertyName");
        }
        Object descriptors = propertyDescriptors.get(inModelClass);
        if (descriptors == null) {
            getBeanInfo(inModelClass);
            descriptors = propertyDescriptors.get(inModelClass);
        }
        if (Debug.ON) {
            Debug.assertTrue(descriptors instanceof HashMap, "descriptors not HashMap: " + descriptors);
        }
        return (PropertyDescriptor) ((HashMap) descriptors).get(inPropertyName);
    }


    private static void cachePropertyDescriptors(Class inModelClass, BeanInfo beanInfo) {
        HashMap descriptors = new HashMap();
        PropertyDescriptor[] beanDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < beanDescriptors.length; ++i) {
            PropertyDescriptor desc = beanDescriptors[i];
            if (Debug.ON) {
                Debug.assertTrue(desc != null, "null desc");
            }
            descriptors.put(desc.getName(), desc);
        }
        propertyDescriptors.put(inModelClass, descriptors);
    }
}
