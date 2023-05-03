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
 * $Id: SLabelBeanInfo.java,v 1.4 2002/09/25 13:53:08 ludovicc Exp $
 */
package org.scopemvc.view.swing;

import org.scopemvc.view.swing.beaninfo.BasicBeanInfo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Bean info for SLabel
 *
 * @author ludovicc
 * @version $Revision: 1.4 $
 * @created May 28, 2002
 */

public class SLabelBeanInfo extends BasicBeanInfo {
    /**
     * Constructor for the SLabelBeanInfo object
     */
    public SLabelBeanInfo() {
        super(SLabel.class);
    }

    /**
     * Gets the property descriptors
     *
     * @return The propertyDescriptors value
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor disableOnNull = new PropertyDescriptor("disableOnNull", getBeanClass(),
                    "isDisableOnNull", "setDisableOnNull");
            disableOnNull.setDisplayName("Disable this field if the model property it is bound becomes null");
            disableOnNull.setShortDescription("Disable this field if the model property it is bound "
                    + "becomes null");
            PropertyDescriptor selector = new PropertyDescriptor("selector", getBeanClass(),
                    "getSelector", "setSelector");
            selector.setDisplayName("The selector for the model property");
            selector.setShortDescription("The selector identifying the model property containing the text "
                    + "to display");
            PropertyDescriptor[] pds = new PropertyDescriptor[]{
                    disableOnNull,
                    selector};
            return pds;
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the additional bean info
     *
     * @return The additionalBeanInfo value
     */
    public BeanInfo[] getAdditionalBeanInfo() {
        Class superclass = getBeanClass().getSuperclass();
        try {
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[]{superBeanInfo};
        } catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
