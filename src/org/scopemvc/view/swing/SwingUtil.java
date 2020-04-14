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
 * $Id: SwingUtil.java,v 1.8 2002/10/23 12:38:46 ludovicc Exp $
 */
package org.scopemvc.view.swing;


import java.awt.Component;
import javax.swing.SwingUtilities;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Control;
import org.scopemvc.core.View;
import org.scopemvc.view.awt.AWTUtilities;

/**
 * <P>
 *
 * Utilities dependent on Java Swing and Scope core and view.awt packages only.
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.8 $ $Date: 2002/10/23 12:38:46 $
 * @created 05 September 2002
 */
public final class SwingUtil {

    private static final Log LOG = LogFactory.getLog(SwingUtil.class);


    private SwingUtil() { }


    // --------------------------------- View helper

    /**
     * Issue a Control to the Controller bound to the component or one of its
     * parent components.
     *
     * @param inView The Component
     * @param inControl The Control to issue
     */
    public static void issueControl(Component inView, final Control inControl) {
        final View parent = AWTUtilities.findControlIssuer(inView);
        if (LOG.isDebugEnabled()) {
            LOG.debug("issueControl: parent controller: " + parent);
        }
        if (parent != null) {
//                         runFromSwingEventThread(new Runnable() {
//                                 public void run() {
            try {
                parent.getController().handleControl(inControl);
            } catch (Exception e) {
                LOG.warn("SwingUtilities.issueControl: ", e);
            }
//                                 }
//                         });
        } else {
            LOG.error("View has no Controller to issue Controls to: " + inView);
        }
    }


    // --------------------------------- Swing thread safety

    /**
     * Ensure a Runnable is run on the Swing event-handling thread.
     *
     * @param inRunnable The code to run in the Swing event thread
     */
    public static void runFromSwingEventThread(Runnable inRunnable) {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                inRunnable.run();
            } else {
                SwingUtilities.invokeAndWait(inRunnable);
            }
        } catch (Exception e) {
            LOG.warn("SwingUtilities.runFromSwingEventThread: ", e);
        }
    }
}

