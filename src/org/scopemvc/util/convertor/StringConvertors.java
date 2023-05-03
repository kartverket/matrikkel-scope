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
 * $Id: StringConvertors.java,v 1.8 2002/10/31 12:15:53 ludovicc Exp $
 */
package org.scopemvc.util.convertor;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.ScopeConfig;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Factory class for creation of default convertors for arbitrary classes. <br>
 * You can use a custom factory with a different strategy for locating the
 * convertors. For that, put the class name of your factory in the
 * StringConvertors property in the Scope configuration.
 *
 * Changes:
 *  - Added {@link #updateLocale()} for ability to change locale based format for registered StringConvertor types.
 *  - Added {@link #initConvertors()} to use in subclasses for custom strategies.
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.8 $ $Date: 2002/10/31 12:15:53 $
 * @created 15 July 2002
 */
public class StringConvertors {

    private static final Log LOG = LogFactory.getLog(StringConvertors.class);
    private static final String CONVERTORS_PROPERTY = "StringConvertors";
    private static final String CONVERTOR_PROPERTY_PREFIX = "StringConvertor.";
    private static volatile StringConvertors instance;

    private final HashMap<String, StringConvertor> defaultConvertors = new HashMap<>();

    /**
     * Load convertors from ScopeConfig.
     */
    public static StringConvertors getInstance() {
        if (instance == null) {
            synchronized (StringConvertors.class) {
                try {
                    Class<?> scClass = Class.forName(ScopeConfig.getString(CONVERTORS_PROPERTY));
                    instance = (StringConvertors) scClass.getDeclaredConstructor().newInstance();
                } catch (Exception ex) {
                    LOG.error("Could not create the StringConvertors factory of class "
                            + ScopeConfig.getString(CONVERTORS_PROPERTY), ex);
                    instance = new StringConvertors();
                }
            }
        }
        return instance;
    }

    /**
     * Updates convertors with default format locale.
     * <p>
     *    Before calling this method, make sure to set default locale: <br><br>
     *    {@code Locale.setDefault(Locale.Category.FORMAT, locale);}
     */
    public static void updateLocale() {
        getInstance().initConvertors();
    }


    /**
     * Load convertors from ScopeConfig.
     */
    public StringConvertors() {
        // this constructor needs to be public for reflection
        initConvertors();
        if (LOG.isDebugEnabled()) {
            LOG.debug("StringConvertors.<clinit>: " + defaultConvertors.size());
        }
    }

    protected void initConvertors() {
        for (Iterator<?> iter = ScopeConfig.getKeysMatching(CONVERTOR_PROPERTY_PREFIX);
                iter.hasNext(); ) {
            String key = (String) iter.next();
            Class<?> convertorClass = ScopeConfig.getClass(key);
            if (convertorClass == null) {
                LOG.error("Null StringConvertor class in config for: " + key);
                continue;
            }
            try {
                defaultConvertors.put(key.substring(
                        CONVERTOR_PROPERTY_PREFIX.length()),
                        (StringConvertor) convertorClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                LOG.error("Failed to create StringConvertor for: " + key, e);
            }
        }
    }

    /**
     * Return a {@link StringConvertor} for the passed Class, else return null.
     * <br>
     * Note that this doesn't work for subclasses of datatypes types: the type
     * must match exactly.
     *
     * @param inValueClass Description of the Parameter
     * @return Description of the Return Value
     */
    public static StringConvertor forClass(Class<?> inValueClass) {
        return getInstance().findConvertor(inValueClass);
    }

    /**
     * Return a {@link StringConvertor} for the passed Class, else return null.
     * <br>
     * The default implementation doesn't work for subclasses of datatypes
     * types: the type must match exactly.
     *
     * @param inValueClass Description of the Parameter
     * @return Description of the Return Value
     */
    protected StringConvertor findConvertor(Class<?> inValueClass) {
        return defaultConvertors.get(inValueClass.getName());
    }

    /**
     * Register a new convertor for the value class.
     *
     * @param inValueClass The value class with the convertor to register
     * @param inConvertor The StringConvertor instance for the value class.
     */
    protected void registerConvertor(Class<?> inValueClass, StringConvertor inConvertor) {
        defaultConvertors.put(inValueClass.getName(), inConvertor);
    }
}
