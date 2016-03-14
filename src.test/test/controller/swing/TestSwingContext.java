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

import java.awt.Cursor;

import junit.framework.*;
import org.scopemvc.controller.basic.*;
import org.scopemvc.controller.swing.*;
import org.scopemvc.util.ScopeConfig;
import org.scopemvc.view.swing.*;

/**
 * @author <A HREF="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</A>
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:56 $
 * @created 05 September 2002
 */
public final class TestSwingContext extends TestCase {

    private SwingContext context;

    /**
     * Constructor for the TestSwingContext object
     *
     * @param inName Name of the test
     */
    public TestSwingContext(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testShowWindow() throws Exception {
        SPanel window = new SPanel();
        window.setTitle("Test Window");
        window.setDisplayMode(SwingView.PRIMARY_WINDOW);
        context.showView(window);
        SuiteControllerSwing.waitForAWT();
        assertTrue(!context.areAllViewsClosed());

        context.hideAllViews();
        SuiteControllerSwing.waitForAWT();
        assertTrue(context.areAllViewsClosed());
    }

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testShowDialog() throws Exception {
        // cannot test easily modal dialogs as they block the current thread of
        // execution when calling show(), so we use modeless dialogs only
        SPanel modelessDialog = new SPanel();
        modelessDialog.setDisplayMode(SwingView.MODELESS_DIALOG);
        modelessDialog.setTitle("Test Modeless Dialog");

        context.showView(modelessDialog);
        SuiteControllerSwing.waitForAWT();
        assertTrue(!context.areAllViewsClosed());

        context.hideAllViews();
        SuiteControllerSwing.waitForAWT();
        assertTrue(context.areAllViewsClosed());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testProgress() throws Exception {
        long progressDelay = ScopeConfig.getInteger(SwingContext.PROGRESS_START_DELAY_PROPERTY).longValue();
        SPanel window = new SPanel();
        context.showView(window);
        context.startProgress();
        SuiteControllerSwing.waitForAWT();
        Thread.sleep(progressDelay * 2);
        // to pass the progress delay
        assertTrue(!context.areAllViewsClosed());
        assertEquals(window.getCursor().getType(), Cursor.WAIT_CURSOR);

        Thread.sleep(1000);

        context.stopProgress();
        SuiteControllerSwing.waitForAWT();
        assertEquals(window.getCursor().getType(), Cursor.DEFAULT_CURSOR);

        Thread.sleep(1000);

        context.hideAllViews();
        SuiteControllerSwing.waitForAWT();
        assertTrue(context.areAllViewsClosed());
    }


    /**
     * A unit test for JUnit. <br>
     * Ensures that Scope closes all its windows so the JVM can exit cleanly
     *
     * @throws Exception Any abnormal exception
     * @todo Write this test
     */
    public void testComplexInitAndClose() throws Exception { }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        context = new SwingContext();
        ViewContext.setGlobalContext(context);
        ViewContext.clearThreadContext();
    }

    /**
     * The teardown method for JUnit
     */
    protected void tearDown() {
        ViewContext.setGlobalContext(null);
    }

}
