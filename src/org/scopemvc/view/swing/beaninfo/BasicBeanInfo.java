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
 * $Id: BasicBeanInfo.java,v 1.3 2002/09/19 15:18:00 ludovicc Exp $
 */
package org.scopemvc.view.swing.beaninfo;

import java.awt.Image;
import java.beans.*;

/**
 * Base class for the component BeanInfos
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.3 $
 * @created May 28, 2002
 */
public class BasicBeanInfo extends SimpleBeanInfo {

    private Class beanClass;
    private Image iconColor16x16;
    private Image iconColor32x32;
    private Image iconMono16x16;
    private Image iconMono32x32;

    /**
     * Constructor for the BasicBeanInfo object
     *
     * @param inBeanClass The class of the bean
     */
    public BasicBeanInfo(Class inBeanClass) {
        this.beanClass = inBeanClass;
    }

    /**
     * Returns the class of the bean described by this BeanInfo.
     *
     * @return the class of the bean
     */
    public Class getBeanClass() {
        return beanClass;
    }

    /**
     * Gets the icon
     *
     * @param inIconKind Description of the Parameter
     * @return The icon value
     */
    public Image getIcon(int inIconKind) {
        Image icon = null;
        switch (inIconKind) {
            case ICON_COLOR_16x16:
                icon = iconColor16x16;
                break;
            case ICON_COLOR_32x32:
                icon = iconColor32x32;
                break;
            case ICON_MONO_16x16:
                icon = iconMono16x16;
                break;
            case ICON_MONO_32x32:
                icon = iconMono32x32;
                break;
        }
        if (icon == null && beanClass != null) {
            String resource = getDefaultIconResource(inIconKind);
            System.out.println("Resource" + resource);
            icon = loadImage(resource);
            if (icon != null) {
                switch (inIconKind) {
                    case ICON_COLOR_16x16:
                        iconColor16x16 = icon;
                        break;
                    case ICON_COLOR_32x32:
                        iconColor32x32 = icon;
                        break;
                    case ICON_MONO_16x16:
                        iconMono16x16 = icon;
                        break;
                    case ICON_MONO_32x32:
                        iconMono32x32 = icon;
                        break;
                }
            }
        }
        return icon;
    }

    /**
     * Gets the default icon resource
     *
     * @param inIconKind Description of the Parameter
     * @return The defaultIconResource value
     */
    protected String getDefaultIconResource(int inIconKind) {
        String beanName = beanClass.getName().substring(beanClass.getName().lastIndexOf('.') + 1);
        String resource = "beaninfo/images/".concat(beanName);
        switch (inIconKind) {
            case ICON_COLOR_16x16:
                resource = String.valueOf(resource).concat("Color16");
                break;
            case ICON_COLOR_32x32:
                resource = String.valueOf(resource).concat("Color32");
                break;
            case ICON_MONO_16x16:
                resource = String.valueOf(resource).concat("Mono16");
                break;
            case ICON_MONO_32x32:
                resource = String.valueOf(resource).concat("Mono32");
                break;
        }
        return String.valueOf(resource).concat(".gif");
    }

}
