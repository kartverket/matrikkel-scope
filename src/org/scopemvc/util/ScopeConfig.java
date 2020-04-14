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
 * $Id: ScopeConfig.java,v 1.17 2002/11/20 00:14:00 ludovicc Exp $
 */
package org.scopemvc.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <P>
 *
 * Loads the Scope config in such a way that custom properties can be set from
 * the Java command line (-D switch) or from a custom ResourceBundle. Any
 * properties not supplied are loaded with the default settings in {@link
 * DefaultScopeConfig}. </P> <P>
 *
 * A custom ResourceBundle name can be specified with {@link #setPropertiesName}
 * during application initialisation, although the default "scope.config"
 * resource is always loaded if it can be found. </P> <P>
 *
 * Property definitions have the following priority (from highest to lowest):
 *
 * <OL>
 *   <LI> System properties</LI>
 *   <LI> setPropertiesName() properties</LI>
 *   <LI> scope.properties</LI>
 *   <LI> {@link DefaultScopeConfig}</LI>
 * </OL>
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.17 $ $Date: 2002/11/20 00:14:00 $
 * @created 04 September 2002
 * @see DefaultScopeConfig
 * @todo Create a constant for each property name defined in DefaultScopeConfig,
 *      and document it using javadoc
 */
public final class ScopeConfig {

    // Standard properties

    /**
     * Property that defines defines the image resource used as the icon for
     * JDialogs and JFrames.<br>
     * The value is a path to the icon image, using the ResourceLoader. <br>
     * Default: /org/scopemvc/images/window_icon.gif
     */
    public static final String SWINGCONTEXT_WINDOW_ICON_PROPERTY =
            "org.scopemvc.controller.swing.SwingContext.window_icon";

    /**
     * Property that defines the milliseconds delay before
     * ViewContext.startBusy() is called (ie delay before busy cursor shows in
     * Swing if a Controller takes a long time to handle a Control). <br>
     * Default: 500
     */
    public static final String SWINGCONTEXT_PROGRESS_START_DELAY_PROPERTY =
            "org.scopemvc.controller.swing.SwingContext.progress_start_delay";

    /**
     * Property that determines whether the STextField component fires a
     * Control. <br>
     * The value is a coma-separated list containing:
     * <ul>
     *   <li> onEnter: fires a control when the user presses the Enter key</li>
     *
     *   <li> onLostFocus: fires a control when the component loses the focus
     *   </li>
     *   <li> onlyOnChange: fires a control only if the value of the component
     *   changed and there was an event (enter pressed or lost focus) on the
     *   component</li>
     * </ul>
     * or a combination of those values<br>
     * Default: onEnter,onLostFocus
     */
    public static final String STEXTFIELD_CONTROL_SETTINGS_PROPERTY =
            "org.scopemvc.view.swing.STextField.control_settings";

    /**
     * Property that determines whether the SPasswordField component fires a
     * Control. <br>
     * The value is a coma-separated list containing:
     * <ul>
     *   <li> onEnter: fires a control when the user presses the Enter key</li>
     *
     *   <li> onLostFocus: fires a control when the component loses the focus
     *   </li>
     *   <li> onlyOnChange: fires a control only if the value of the component
     *   changed and there was an event (enter pressed or lost focus) on the
     *   component</li>
     * </ul>
     * or a combination of those values<br>
     * Default: onEnter,onLostFocus
     */
    public static final String SPASSWORDFIELD_CONTROL_SETTINGS_PROPERTY =
            "org.scopemvc.view.swing.SPasswordField.control_settings";

    /**
     * Property that determines whether the STextArea component fires a Control.
     * <br>
     * The value is a coma-separated list containing:
     * <ul>
     *   <li> onLostFocus: fires a control when the component loses the focus
     *   </li>
     *   <li> onlyOnChange: fires a control only if the value of the component
     *   changed and there was an event (enter pressed or lost focus) on the
     *   component</li>
     * </ul>
     * or a combination of those values<br>
     * Default: onLostFocus
     */
    public static final String STEXTAREA_CONTROL_SETTINGS_PROPERTY =
            "org.scopemvc.view.swing.STextArea.control_settings";

    /**
     * Property that defines if the SLabel enabled state should be synchronized
     * with the enabled state of the component associated to a SLabel with the
     * setLabelFor() method<br>
     * Values are true or false. <br>
     * Default: true
     */
    public static final String SLABEL_USE_LABELFOR_COMPONENT_ENABLED_STATE_PROPERTY =
            "org.scopemvc.view.swing.SLabel.use_labelFor_component_enabled_state";

    /**
     * Property that defines the background color of a component in case of
     * failure. <br>
     * Values are instances of the Color class, or numeric value of the RGB
     * color code, parsable by {link Intege#decode Integer.decode()}, eg. 0xB2B
     * <br>
     * Default: Color.PINK
     */
    public static final String VALIDATIONHELPER_VALIDATION_FAILED_COLOR =
            "org.scopemvc.view.swing.ValidationHelper-validation.failed.color";

    private static final Log LOG = LogFactory.getLog(ScopeConfig.class);

    private static final String DEFAULT_CUSTOM_CONFIG_NAME = "scope";
    private static final String DEFAULT_CONFIG_NAME = "org.scopemvc.util.DefaultScopeConfig";
    private static final String DEFAULT_PROPERTY_PREFIX = "org.scopemvc.";

    // -------------------- Initialisation ----------------------------------

    private static ScopeConfig instance;

    /**
     * The Scope config properties loaded at first access of config.
     */
    private HashMap properties = new HashMap();


    private ScopeConfig() {
        initialise();
    }


    // --------------------------- Get properties API ----------------------

    /**
     * Gets the instance of the Scope config
     *
     * @return The singleton of the ScopeConfig
     */
    public static ScopeConfig getInstance() {
        if (instance == null) {
            instance = new ScopeConfig();
        }
        return instance;
    }


    /**
     * Gets the string value of the property
     *
     * @param inKey The property name
     * @return The string value of the property
     */
    public static String getString(String inKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getString: " + inKey + ", result " + getProperties().get(inKey));
        }
        Object value = getProperties().get(inKey);
        return (value == null) ? null : value.toString();
    }


    /**
     * Gets the object value of the property
     *
     * @param inKey The property name
     * @return The object value of the property
     */
    public static Object getObject(String inKey) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getObject: " + inKey + ", result " + getProperties().get(inKey));
        }
        return getProperties().get(inKey);
    }


    /**
     * Gets the char value of the property
     *
     * @param inKey The property name
     * @return The char value of the property
     */
    public static char getChar(String inKey) {
        String string = getProperties().get(inKey).toString();
        char result = 0;
        if (string == null || string.length() < 1) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getChar: no char property for: " + inKey);
            }
        } else {
            result = string.charAt(0);
        }

        if (LOG.isErrorEnabled() && string.length() > 1) {
            LOG.error("Char property for (" + inKey + ") longer than a single char: " + string);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("getChar: " + inKey + ", result " + result);
        }
        return result;
    }


    /**
     * Gets the class value of the property
     *
     * @param inKey The property name
     * @return The class value of the property
     */
    public static Class getClass(String inKey) {
        Object o = getProperties().get(inKey);

        if (o == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getClass: " + inKey + ", result null");
            }
            return null;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("getClass: " + inKey + ", result " + o);
        }
        if (o instanceof Class) {
            return (Class) o;
        }
        if (!(o instanceof String)) {
            throw new IllegalArgumentException("Config property: " + inKey
                    + " is not a Class or String: " + o.getClass());
        }
        try {
            return Class.forName((String) o);
        } catch (Exception e) {
            LOG.warn("Class not found: " + o + " for property " + inKey);
            return null;
        }
    }


    /**
     * Gets the integer value of the property
     *
     * @param inKey The property name
     * @return The integer value of the property
     */
    public static Integer getInteger(String inKey) {
        Object o = getProperties().get(inKey);

        if (o == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getInteger: " + inKey + ", result  null");
            }
            return null;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("getInteger: " + inKey + ", result " + o);
        }
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (!(o instanceof String)) {
            throw new IllegalArgumentException("Config property: " + inKey
                    + " is not an Integer or String: " + o.getClass());
        }
        try {
            return new Integer((String) o);
        } catch (Exception e) {
            LOG.warn("Not an integer value: " + o + " for property " + inKey);
            return null;
        }
    }


    /**
     * Gets the keys starting with the prefix
     *
     * @param inKeyPrefix The prefix of the keys to match
     * @return an Iterator over the matching keys
     */
    public static Iterator getKeysMatching(String inKeyPrefix) {
        HashSet matchingKeys = new HashSet();
        for (Iterator i = getProperties().keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            if (key.startsWith(inKeyPrefix)) {
                matchingKeys.add(key);
            }
        }
        return matchingKeys.iterator();
    }


    // ------------------ Set custom properties API -------------------------

    /**
     * Set name of custom properties that will override the DefaultScopeConfig
     * and "scope.properties", but not override any System properties.
     *
     * @param inName The new propertiesName value
     */
    public static void setPropertiesName(String inName) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setPropertiesName: " + inName);
        }
        if (inName == null) {
            throw new IllegalArgumentException("Can't set PropertiesName to null. Pass in a resource name.");
        }

        ResourceBundle bundle = ResourceLoader.getProperties(inName);
        if (bundle != null) {
            getInstance().putAll(bundle);
        }

        // Load system properties over the top
        getInstance().loadSystemConfig();
    }


    /**
     * Gets the properties
     *
     * @return The properties value
     */
    protected static HashMap getProperties() {
        return getInstance().properties;
    }


    /**
     * Returns the name of the default custom configuration bundle.
     *
     * @return The defaultCustomConfigName value
     */
    protected String getDefaultCustomConfigName() {
        return DEFAULT_CUSTOM_CONFIG_NAME;
    }


    /**
     * Returns the name of the default configuration bundle.
     *
     * @return The defaultConfigName value
     */
    protected String getDefaultConfigName() {
        return DEFAULT_CONFIG_NAME;
    }


    /**
     * Returns the property name prefix.
     *
     * @return The propertyPrefix value
     */
    protected String getPropertyPrefix() {
        return DEFAULT_PROPERTY_PREFIX;
    }


    /**
     * Initialises the class by loading in the configuration.
     */
    protected void initialise() {
        loadDefaultConfig();
        loadCustomConfig();
        loadSystemConfig();
        if (LOG.isDebugEnabled()) {
            LOG.debug("--- Scope properties ---");
            for (Iterator i = properties.keySet().iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                Object value = properties.get(key);
                LOG.debug(key + " = " + value);
            }
            LOG.debug("------------------------");
        }
    }


    /**
     * Load Scope config properties from System properties.
     */
    private void loadSystemConfig() {
        try {
            Properties systemProperties = System.getProperties();
            for (Enumeration e = systemProperties.keys(); e.hasMoreElements(); ) {
                String key = (String) e.nextElement();
                if (key.startsWith(DEFAULT_PROPERTY_PREFIX)) {
                    Object value = systemProperties.getProperty(key);
                    properties.put(key, value);
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Override Scope property " + key + " with " + value);
                    }
                }
            }
        } catch (SecurityException e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Can't access System properties", e);
            }
        }
    }


    /**
     * Attempt to load a custom ResourceBundle called "scope.properties" if it
     * can be found.
     */
    private void loadCustomConfig() {
        ResourceBundle bundle = ResourceLoader.getProperties(DEFAULT_CUSTOM_CONFIG_NAME);
        if (bundle != null) {
            putAll(bundle);
            LOG.info("Load custom Scope config from " + DEFAULT_CUSTOM_CONFIG_NAME + ".properties");
        } else {
            LOG.warn("Can't load custom Scope config from " + DEFAULT_CUSTOM_CONFIG_NAME + ".properties");
        }
    }


    /**
     * Load the default config.
     */
    private void loadDefaultConfig() {
        ResourceBundle bundle = ResourceLoader.getProperties(DEFAULT_CONFIG_NAME);
        if (bundle != null) {
            putAll(bundle);
        } else {
            LOG.error("Can't load default Scope config from: " + DEFAULT_CONFIG_NAME);
        }
    }


    /**
     * Put properties from the ResourceBundle.
     *
     * @param inBundle The resource bundle to get the properties from.
     */
    private void putAll(ResourceBundle inBundle) {
        for (Enumeration i = inBundle.getKeys(); i.hasMoreElements(); ) {
            String key = (String) i.nextElement();
            Object value = inBundle.getObject(key);
            properties.put(key, value);
        }
    }
}
