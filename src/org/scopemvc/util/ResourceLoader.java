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
 * $Id: ResourceLoader.java,v 1.7 2002/11/11 00:31:42 ludovicc Exp $
 */
package org.scopemvc.util;

import java.awt.Image;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class for loading resources. <br>
 * This class fixes an issue where the Scope library wants to load a resource
 * belonging to a client jar, but cannot find it because of the resource is not
 * accessible from the scope classloader. <br>
 * The solution is to give access from Scope to the client classloader, i.e the
 * classloader used to load the client jars containing the application
 * resources. <br>
 * User code needs to set the client classloader with <code>ResourceLoader.setClientClassLoader</code>
 * before Scope can load the client resources.
 *
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.7 $ $Date: 2002/11/11 00:31:42 $
 * @created 17. juin 2002
 * @see #setClientClassLoader
 * @see java.lang.ClassLoader#getResource
 */

public class ResourceLoader {

    private static final Log LOG = LogFactory.getLog(ResourceLoader.class);

    private static ClassLoader clientClassLoader;
    private static ClassLoader scopeClassLoader = ResourceLoader.class.getClassLoader();

    private ResourceLoader() { }

    /**
     * Gets the client loader
     *
     * @return The clientLoader value
     */
    public static ClassLoader getClientLoader() {
        return clientClassLoader;
    }

    /**
     * Gets the icon
     *
     * @param inIconPath Description of the Parameter
     * @return The icon value, or null if it was not found
     */
    public static Icon getIcon(String inIconPath) {
        URL url = getResource(inIconPath);
        if (url == null) {
            return null;
        } else {
            return new ImageIcon(url);
        }
    }

    /**
     * Gets the image
     *
     * @param inImagePath Description of the Parameter
     * @return The image value, or null if it was not found
     */
    public static Image getImage(String inImagePath) {
        URL url = getResource(inImagePath);
        if (url == null) {
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    /**
     * Gets the resource
     *
     * @param inResourcePath Description of the Parameter
     * @return The resource value
     */
    public static URL getResource(String inResourcePath) {
        URL url = null;
        if (inResourcePath == null) {
            return null;
        }
        String resourcePath = inResourcePath;
        if (resourcePath.startsWith("/")) {
            resourcePath = resourcePath.substring(1);
        }

        if (getClientLoader() != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Loading " + resourcePath + " using client classloader " + getClientLoader());
            }
            url = getClientLoader().getResource(resourcePath);
        }
        if (url == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Loading " + resourcePath + " using scope classloader " + scopeClassLoader);
            }
            url = scopeClassLoader.getResource(resourcePath);
        }
        if (url == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Loading " + resourcePath + " using system classloader "
                        + ClassLoader.getSystemClassLoader());
            }
            url = ClassLoader.getSystemClassLoader().getResource(resourcePath);
        }
        return url;
    }

    /**
     * Try to return a ResourceBundle loaded from the named resource, or null if
     * can't load.
     *
     * @param inPropertiesName The name of the resource bundle (without
     *      .properties or locale suffix)
     * @return The ResourceBundle containing the properties
     */
    public static ResourceBundle getProperties(String inPropertiesName) {
        ResourceBundle properties = null;
        if (inPropertiesName != null) {
            try {
                if (clientClassLoader != null) {
                    properties = ResourceBundle.getBundle(inPropertiesName,
                            Locale.getDefault(), clientClassLoader);
                } else {
                    properties = ResourceBundle.getBundle(inPropertiesName);
                }
            } catch (MissingResourceException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Can't load config: " + inPropertiesName, e);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("getProperties: " + properties);
            }
        }
        return properties;
    }

    /**
     * Gets the client class loader.
     *
     * @return The clientClassLoader value
     */
    public static ClassLoader getClientClassLoader() {
        return clientClassLoader;
    }

    /**
     * Sets the client class loader used to load the resources. <br>
     * This class loader should be the one used to load the jars of your
     * application containing the resources. <br>
     * Typical code looks like: <br>
     * <pre>
     *    class MyLauncher {
     *      public static void main(String[] args) {
     *        ResourceLoader.setClientClassLoader(MyLauncher.class.getClassLoader());
     *        // starts Scope application
     *        MyController controller = new MyController();
     *        controller.startUp();
     *      }
     *    }
     * </pre>
     *
     * @param inClassLoader The new clientClassLoader value
     */
    public static void setClientClassLoader(ClassLoader inClassLoader) {
        clientClassLoader = inClassLoader;
    }

}
