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
 * $Id: UIStrings.java,v 1.8 2002/11/11 00:38:34 ludovicc Exp $
 */
package org.scopemvc.util;


import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <P>
 *
 * Uses a ResourceBundle as a dictionary mapping IDs against user-readable
 * Strings that can be presented on the UI. A custom resource bundle should be
 * provided, with a call to setPropertiesName during application initialisation.
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.8 $ $Date: 2002/11/11 00:38:34 $
 * @created 05 September 2002
 */
public final class UIStrings {

    private static final Log LOG = LogFactory.getLog(UIStrings.class);

    private static ResourceBundle properties;

    private static String propertiesName;


    private UIStrings() { }


    /**
     * Get the localized string for the given key
     *
     * @param inKey The key in the dictionary, not dependent on locale
     * @return The localized string, user-readable, or inKey if the 
     * key was not found.
     */
    public static String get(String inKey) {
    	return get(inKey, inKey);
    }


    /**
     * Get the localized string for the given key
     *
     * @param inKey The key in the dictionary, not dependent on locale
     * @param inDefaultValue The default value to use if the key is not found
     * @return The localized string, user-readable
     */
    public static String get(String inKey, String inDefaultValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("get: " + inKey);
        }

        if (getProperties() == null) {
            if (inKey == null) {
                return "";
            }
            // missing resource!
            return inDefaultValue;
        }
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("get: " + inKey);
            }
            String result = getProperties().getString(inKey);
            if (LOG.isDebugEnabled()) {
                LOG.debug("get: " + result);
            }
            if (Debug.ON) {
                Debug.assertTrue(result != null);
            }
            return result;
        } catch (Throwable e) {
        	LOG.debug("Could not read key " + inKey + " from UIStrings");
            if (inKey == null) {
                return "";
            }
            return inDefaultValue;
        }
    }


    /**
     * Sets the properties name
     *
     * @param inName The new propertiesName value
     */
    public static void setPropertiesName(String inName) {
        if (inName == null) {
            throw new IllegalArgumentException("Can't set properties name to null");
        }
        propertiesName = inName;
        properties = null;
        // force reload
    }


    private static ResourceBundle getProperties() {
        if (propertiesName == null) {
            return null;
        }
        if (properties == null) {
            try {
                properties = ResourceBundle.getBundle(propertiesName);
            } catch (MissingResourceException e) {
                LOG.error("getProperties: " + propertiesName, e);
                if (Debug.ON) {
                    Debug.assertTrue(1 == 0, "Missing resources: " + propertiesName);
                }
            }
        }
        return properties;
    }
}

