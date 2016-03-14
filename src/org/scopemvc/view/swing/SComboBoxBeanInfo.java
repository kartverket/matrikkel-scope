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
package org.scopemvc.view.swing;

import java.beans.*;
import org.scopemvc.view.swing.beaninfo.BasicBeanInfo;

/**
 * Beaninfo for SComboBox
 *
 * @author ludovicc
 * @version $Revision: 1.4 $
 * @created May 28, 2002
 */

public class SComboBoxBeanInfo extends BasicBeanInfo {

    /**
     * Constructor for the SComboBoxBeanInfo object
     */
    public SComboBoxBeanInfo() {
        super(SComboBox.class);
    }

    /**
     * Gets the property descriptors
     *
     * @return The propertyDescriptors value
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor changeSelectionControlID = new PropertyDescriptor("changeSelectionControlID",
                    getBeanClass(), null, "setChangeSelectionControlID");
            changeSelectionControlID.setDisplayName("The control ID issued when the selection changes");
            changeSelectionControlID.setShortDescription("The ID of the Control that is issued when the "
                    + "selection changes. If null no Control will be issued.");
            PropertyDescriptor rendererIconSelector = new PropertyDescriptor("rendererIconSelector",
                    getBeanClass(), null, "setRendererIconSelector");
            rendererIconSelector.setDisplayName("The selector for the list cell renderer to get an Icon");
            rendererIconSelector.setShortDescription("Identifies the property that will be rendered as an icon");
            PropertyDescriptor rendererSelector = new PropertyDescriptor("rendererSelector", getBeanClass(),
                    null, "setRendererSelector");
            rendererSelector.setDisplayName("The selector for the list cell renderer");
            rendererSelector.setShortDescription("Identifies the propertythat will be shown in a list cell "
                    + "(converted to a String)");
            PropertyDescriptor selectionSelector = new PropertyDescriptor("selectionSelector", getBeanClass(),
                    null, "setSelectionSelector");
            selectionSelector.setDisplayName("The selection for the current selected item");
            selectionSelector.setShortDescription("Identifies the property that contains the selected item");
            PropertyDescriptor selector = new PropertyDescriptor("selector", getBeanClass(), null,
                    "setSelector");
            selector.setDisplayName("The selector for the list of items");
            selector.setShortDescription("Identifies the model property containing the list of items to display");
            PropertyDescriptor sizeSelector = new PropertyDescriptor("sizeSelector", getBeanClass(),
                    null, "setSizeSelector");
            sizeSelector.setDisplayName("The Selector for the property that is the size of the items list.");
            sizeSelector.setShortDescription("Identifies the model property containing the size of the "
                    + "items list (optional)");
            PropertyDescriptor[] pds = new PropertyDescriptor[]{
                    changeSelectionControlID,
                    rendererIconSelector,
                    rendererSelector,
                    selectionSelector,
                    selector,
                    sizeSelector};
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
