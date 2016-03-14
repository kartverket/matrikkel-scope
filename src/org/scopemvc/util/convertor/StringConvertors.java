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
package org.scopemvc.util.convertor;


import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.ScopeConfig;

/**
 * Factory class for creation of default convertors for arbitrary classes. <br>
 * You can use a custom factory with a different strategy for locating the
 * convertors. For that, put the class name of your factory in the
 * StringConvertors property in the Scope configuration.
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @version $Revision: 1.8 $ $Date: 2002/10/31 12:15:53 $
 * @created 15 July 2002
 */
public class StringConvertors {

    private static final Log LOG = LogFactory.getLog(StringConvertors.class);
    private static final String CONVERTORS_PROPERTY = "StringConvertors";
    private static final String CONVERTOR_PROPERTY_PREFIX = "StringConvertor.";
    private static StringConvertors instance;

    private HashMap defaultConvertors;

    /**
     * Load convertors from ScopeConfig.
     */
    static {
        try {
            Class scClass = Class.forName(ScopeConfig.getString(CONVERTORS_PROPERTY));
            instance = (StringConvertors) scClass.newInstance();
        } catch (Exception ex) {
            LOG.error("Could not create the StringConvertors factory of class "
                    + ScopeConfig.getString(CONVERTORS_PROPERTY), ex);
            instance = new StringConvertors();
        }
    }

    /**
     * Load convertors from ScopeConfig.
     */
    public StringConvertors() {
        // this constructor needs to be public for reflection
        defaultConvertors = new HashMap();
        for (Iterator iter = ScopeConfig.getKeysMatching(CONVERTOR_PROPERTY_PREFIX);
                iter.hasNext(); ) {
            String key = (String) iter.next();
            Class convertorClass = ScopeConfig.getClass(key);
            if (convertorClass == null) {
                LOG.error("Null StringConvertor class in config for: " + key);
                continue;
            }
            try {
                defaultConvertors.put(key.substring(
                        CONVERTOR_PROPERTY_PREFIX.length()),
                        convertorClass.newInstance());
            } catch (Exception e) {
                LOG.error("Failed to create StringConvertor for: " + key, e);
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("StringConvertor.<clinit>: " + defaultConvertors.size());
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
    public static StringConvertor forClass(Class inValueClass) {
        return instance.findConvertor(inValueClass);
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
    protected StringConvertor findConvertor(Class inValueClass) {
        return (StringConvertor) defaultConvertors.get(inValueClass.getName());
    }

    /**
     * Register a new convertor for the value class.
     *
     * @param inValueClass The value class with the convertor to register
     * @param inConvertor The StringConvertor instance for the value class.
     */
    protected void registerConvertor(Class inValueClass, StringConvertor inConvertor) {
        defaultConvertors.put(inValueClass.toString(), inConvertor);
    }
}
