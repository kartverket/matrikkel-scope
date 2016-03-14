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
package test.controller.swing;

import javax.swing.SwingUtilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <P>
 *
 * JUnit TestCase that collects all the controller.swing tests into a suite.
 * </P>
 *
 * @author <A HREF="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</A>
 * @version $Revision: 1.1 $ $Date: 2002/09/09 17:54:33 $
 * @created 05 September 2002
 */
public final class SuiteControllerSwing extends TestCase {

    /**
     * Constructor for the SuiteControllerSwing object
     *
     * @param inName TODO: Describe the Parameter
     */
    public SuiteControllerSwing(String inName) {
        super(inName);
    }


    /**
     * A unit test suite for JUnit
     *
     * @return The test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(TestSwingContext.class));
        return suite;
    }

    /**
     * Wait for AWT / Swing to execute all events in its event queue
     *
     * @throws Exception TODO: Describe the Exception
     */
    public static void waitForAWT() throws Exception {
        SwingUtilities.invokeAndWait(
            new Runnable() {
                public void run() {
                    Thread.yield();
                }
            });
        Thread.currentThread().sleep(100);
        SwingUtilities.invokeAndWait(
            new Runnable() {
                public void run() {
                    Thread.yield();
                }
            });
    }

}
