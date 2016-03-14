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
 * Beaninfo for STable
 *
 * @author ludovicc
 * @version $Revision: 1.4 $
 * @created May 28, 2002
 */

public class STableBeanInfo extends BasicBeanInfo {

    /**
     * Constructor for the STableBeanInfo object
     */
    public STableBeanInfo() {
        super(STable.class);
    }

    /**
     * Gets the property descriptors
     *
     * @return The propertyDescriptors value
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor changeSelectionControlID = new PropertyDescriptor("changeSelectionControlID",
                    getBeanClass(), "getChangeSelectionControlID", "setChangeSelectionControlID");
            changeSelectionControlID.setDisplayName("The control ID issued for a change in the selection");
            changeSelectionControlID.setShortDescription("The control ID issued for a change in the selection");
            PropertyDescriptor columnNames = new PropertyDescriptor("columnNames", getBeanClass(),
                    null, "setColumnNames");
            columnNames.setDisplayName("Names of the columns");
            columnNames.setShortDescription("");
            PropertyDescriptor columnSelectors = new PropertyDescriptor("columnSelectors", getBeanClass(),
                    null, "setColumnSelectors");
            columnSelectors.setDisplayName("Selectors for the columns");
            PropertyDescriptor columnSelectorStrings = new PropertyDescriptor("columnSelectorStrings",
                    getBeanClass(), null, "setColumnSelectorStrings");
            columnSelectorStrings.setDisplayName("Selectors for the columns");
            PropertyDescriptor doubleClickControlID = new PropertyDescriptor("doubleClickControlID",
                    getBeanClass(), "getDoubleClickControlID", "setDoubleClickControlID");
            doubleClickControlID.setDisplayName("The control ID issued for a double click");
            doubleClickControlID.setShortDescription("The control ID issued for a double click");
            PropertyDescriptor selectionSelector = new PropertyDescriptor("selectionSelector",
                    getBeanClass(), "getSelectionSelector", "setSelectionSelector");
            selectionSelector.setDisplayName("The selection for the current selected item");
            selectionSelector.setShortDescription("Identifies the property that contains the selected item");
            PropertyDescriptor selector = new PropertyDescriptor("selector", getBeanClass(),
                    "getSelector", "setSelector");
            selector.setDisplayName("The selector for the list of items");
            selector.setShortDescription("Identifies the model property containing the list of items to "
                    + "display");
            PropertyDescriptor[] pds = new PropertyDescriptor[]{
                    changeSelectionControlID,
                    columnNames,
                    columnSelectors,
                    columnSelectorStrings,
                    doubleClickControlID,
                    selectionSelector,
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
