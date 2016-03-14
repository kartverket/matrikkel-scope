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

/**
 * <p>
 *
 * Specifies contract for all classes converting object values to <code>String</code>
 * representation and back from <code>String</code> into object instances.
 * Convertors should be based on <code>Locale</code>. </p> <p>
 *
 * Default <code>StringConvertor</code>s can be obtained with class <code>StringConvertors.</code>
 * </p>
 *
 * @author <A HREF="mailto:danmi@users.sourceforge.net">Daniel Michalik</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.7 $ $Date: 2002/10/17 02:07:00 $
 * @created 05 September 2002
 * @see StringConvertors
 */
public interface StringConvertor {

    /**
     * Formats object into <code>String</code>. It never return a null.
     * Implementing class can offer posibility to set string representation of
     * <code>null</code> (such as <code>"null"</code> or <code>"<null>"</code>),
     * however default representation should be <code>""</code>. Slightly
     * different null handling is performed in {@link StringStringConvertor
     * StringStringConvertor} class.
     *
     * @param inValue The object to convert
     * @return The string representation of the value object
     * @exception IllegalArgumentException this convertor can't express the
     *      value as String.
     */
    String valueAsString(Object inValue) throws IllegalArgumentException;


    /**
     * Parses <code>String</code> to produce corresponding object. Empty or
     * <code>null</code> string will be typically converted into <code>null</code>
     * (unlike standard <code>java.text</code> formats which throws exceptions
     * in such situations).
     *
     * @param inString The string to parse
     * @return An object of the supported type initialised with the parsed
     *      string
     * @exception IllegalArgumentException can't convert from String.
     */
    Object stringAsValue(String inString) throws IllegalArgumentException;


    /**
     * Returns true if the convertor supports the stringAsValue() operation.
     * <br>
     * This method is useful when you want to display a string for complex
     * object but you don't want to convert a string to a value object in any
     * case. Editors can use this method to become automatically read-only when
     * the returned value is false.
     *
     * @return true in most cases, false if the StringConvertor cannot parse
     *      strings
     */
    boolean supportsStringAsValue();

}

