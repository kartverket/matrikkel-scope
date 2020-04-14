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
 * $Id: Debug.java,v 1.7 2002/09/25 13:53:07 ludovicc Exp $
 */
package org.scopemvc.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <P>
 *
 * A (very simple) general purpose Debug class for assertion checking. e.g.
 * <PRE>
 * if (Debug.ON) Debug.assert(thing != null);
 * if (Debug.ON) Debug.assert(thing != null, "thing is null");
 * </PRE> The ON boolean is a <CODE>static final</CODE> so that setting it to
 * false and rebuilding the project will strip all debug code from the binaries.
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/09/25 13:53:07 $
 * @created 05 September 2002
 */
public final class Debug {

    /**
     * Global debug flag to switch debug code on and off. This must be a <CODE>static final</CODE>
     * for the compiler to dead-strip all debug statements, which must be
     * prefixed with: <CODE>if (Debug.ON)</CODE>
     */
    public static final boolean ON = true;

    private static final Log LOG = LogFactory.getLog(Debug.class);


    /**
     * Don't create a Debug -- use static methods.
     */
    private Debug() { }


    /**
     * Test preconditions and postconditions like this (but don't include code
     * in the assertion that changes the state of anything because it'll be
     * stripped in non-debug versions): <br>
     * <pre>
     * if (Debug.ON) Debug.assert(thing != null);
     * </pre>
     *
     * @param inCondition The boolean condition. Must be true else the assert
     *      fails and throws an exception
     */
    public static void assertTrue(boolean inCondition) {
        if (!inCondition) {
            LOG.fatal(">>> Assertion failed >>>");
            LOG.fatal(">>> at >>>", new Throwable());
            throw new RuntimeException("Assertion failure");
        }
    }


    /**
     * Test preconditions and postconditions like this (but don't include code
     * in the assertion that changes the state of anything because it'll be
     * stripped in non-debug versions): <br>
     * <pre>
     * if (Debug.ON) Debug.assert(thing != null);
     * </pre>
     *
     * @param inCondition The boolean condition. Must be true else the assert
     *      fails and throws an exception
     * @param inMessage The error message if the condition is false
     */
    public static void assertTrue(boolean inCondition, String inMessage) {
        if (!inCondition) {
            LOG.fatal(">>> Assertion failed: " + inMessage);
            LOG.fatal(">>> at >>>", new Throwable());
            throw new RuntimeException("Assertion failure: " + inMessage);
        }
    }

}

