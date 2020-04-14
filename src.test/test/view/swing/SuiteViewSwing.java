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
 * $Id: SuiteViewSwing.java,v 1.10 2002/10/24 00:31:56 ludovicc Exp $
 */
package test.view.swing;

import javax.swing.SwingUtilities;
import junit.framework.Test;
import junit.framework.TestCase;

import junit.framework.TestSuite;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.controller.swing.SwingContext;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.10 $ $Date: 2002/10/24 00:31:56 $
 * @created 12 September 2002
 */
public final class SuiteViewSwing extends TestCase {

    /**
     * Constructor for the SuiteViewSwing object
     *
     * @param inName TODO: Describe the Parameter
     */
    public SuiteViewSwing(String inName) {
        super(inName);
    }


    /**
     * A unit test suite for JUnit
     *
     * @return The test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(TestSTableModel.class));
        suite.addTest(new TestSuite(TestSAbstractListModel.class));
        suite.addTest(new TestSuite(TestSComboBoxModel.class));
        suite.addTest(new TestSuite(TestSComboBox.class));
        suite.addTest(new TestSuite(TestSTable.class));
        suite.addTest(new TestSuite(TestValidationHelper.class));
        suite.addTest(new TestSuite(TestSButton.class));
        suite.addTest(new TestSuite(TestSCheckBox.class));
        suite.addTest(new TestSuite(TestSTextField.class));
        suite.addTest(new TestSuite(TestSTextArea.class));
        suite.addTest(new TestSuite(TestSLabel.class));
        suite.addTest(new TestSuite(TestSMenuItem.class));
        suite.addTest(new TestSuite(TestSwingView.class));
        suite.addTest(new TestSuite(TestSPanel.class));
        suite.addTest(new TestSuite(TestSUnboundPanel.class));
        suite.addTest(new TestSuite(TestSListCellRenderer.class));
        suite.addTest(new TestSuite(TestSListSelectionModel.class));
        suite.addTest(new TestSuite(TestSList.class));
        suite.addTest(new TestSuite(TestSRadioButton.class));
        suite.addTest(new TestSuite(TestSPasswordField.class));
        suite.addTest(new TestSuite(TestSSlider.class));
        suite.addTest(new TestSuite(TestSAction.class));
        suite.addTest(new TestSuite(TestSModelAction.class));
        suite.addTest(new TestSuite(TestSModelButton.class));
        return suite;
    }

    /**
     * Wait for AWT / Swing to execute all events in its event queue
     *
     * @throws Exception TODO: Describe the Exception
     */
    public static void waitForAWT() throws Exception {
        waitForAWT(100, 2000);
        waitForAWT(100, 8000);
    }

    private static void waitForAWT(final long startDelay, final long timeout) throws Exception {
        final Object lock = new Object();
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    synchronized (lock) {
                        try {
                            lock.wait(startDelay);
                        } catch (Exception ignore) {}
                        lock.notifyAll();
                    }
                }
            });
        synchronized (lock) {
            lock.notifyAll();
            lock.wait(timeout);
        }
    }


    /**
     * The JUnit setup method
     */
    public void setUp() {
        ViewContext.setGlobalContext(new SwingContext());
        ViewContext.clearThreadContext();
    }
}

