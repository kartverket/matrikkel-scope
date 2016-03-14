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
package org.scopemvc.view.awt;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.View;
import org.scopemvc.util.Debug;

/**
 * <P>
 *
 * Utilities dependent on Java AWT and Scope core packages only. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/09/13 17:04:38 $
 * @created 05 September 2002
 */
public final class AWTUtilities {

    private static final Log LOG = LogFactory.getLog(AWTUtilities.class);

    private AWTUtilities() { }

    /**
     * Find the next View from a Component upwards in the java.awt.Component
     * containment hierarchy that has a parent Controller that it can issue
     * Controls to.
     *
     * @param inComponent The Component to find a Controller for
     * @return The parent View bound to a non-null Controller
     */
    public static View findControlIssuer(Component inComponent) {
        Component parent = inComponent;
        while (parent != null) {
            if (parent instanceof View) {
                if (((View) parent).getController() != null) {
                    return (View) parent;
                }
            }
            parent = parent.getParent();
        }
        if (LOG.isInfoEnabled()) {
            LOG.warn("findControlIssuer: no Control Issuer for component: " + inComponent);
        }
        return null;
    }


    // --------------------------------- Window positioning

    /**
     * Center a window on the screen
     *
     * @param inWindow The Window to center
     */
    public static void centreOnScreen(Window inWindow) {
        if (inWindow == null) {
            throw new IllegalArgumentException();
        }

        Rectangle centredBounds = getCenteredBounds(inWindow.getSize());
        inWindow.setLocation(centredBounds.x, centredBounds.y);
    }


    /**
     * Center a window on a parent Window if showing, or if not assume position
     * of parent is (0, 0)
     *
     * @param inParent The parent Window used as reference if it's visible
     * @param inWindow The Window to center
     */
    public static void centreOnWindow(Window inParent, Window inWindow) {
        if (inParent == null) {
            throw new IllegalArgumentException("Can't centre on a null parent.");
        }
        if (inWindow == null) {
            throw new IllegalArgumentException("Can't centre a null Window.");
        }

        Dimension parentSize = inParent.getSize();
        Dimension windowSize = inWindow.getSize();
        Point parentLocation = new Point(0, 0);
        if (inParent.isShowing()) {
            parentLocation = inParent.getLocationOnScreen();
        }

        int x = parentLocation.x + parentSize.width / 2 - windowSize.width / 2;
        int y = parentLocation.y + parentSize.height / 2 - windowSize.height / 2;

        inWindow.setLocation(x, y);
    }


    /**
     * Make sure window fits on the screeen
     *
     * @param inWindow The Window to fit on the screen
     */
    public static void fitOnScreen(Window inWindow) {
        Dimension fullScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension screenSize = new Dimension(fullScreenSize.width, fullScreenSize.height);
        Dimension windowSize = inWindow.getSize();
        Point windowLocation = inWindow.getLocation();

        if (LOG.isDebugEnabled()) {
            LOG.debug("fitOnScreen: screenSize: " + screenSize);
            LOG.debug("fitOnScreen: windowSize: " + windowSize);
            LOG.debug("fitOnScreen: windowLocation: " + windowLocation);
        }

        Dimension newWindowSize = new Dimension(windowSize);
        Point newWindowLocation = new Point(windowLocation);

        if (windowLocation.x < 0) {
            newWindowLocation.x = 0;
        }

        if (windowLocation.y < 0) {
            newWindowLocation.y = 0;
        }

        if (newWindowLocation.x + windowSize.width > screenSize.width) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("fitOnScreen: too wide");
            }
            // First try to reposition window
            newWindowLocation.x = windowLocation.x - (windowLocation.x + windowSize.width - screenSize.width);
            if (LOG.isDebugEnabled()) {
                LOG.debug("fitOnScreen: reposition: location: " + newWindowLocation);
            }
            // Then resize it
            if (newWindowLocation.x < 0) {
                newWindowSize.width += newWindowLocation.x;
                newWindowLocation.x = 0;
                if (LOG.isDebugEnabled()) {
                    LOG.debug("fitOnScreen: reposition: size: " + newWindowSize);
                }
            }
        }

        if (newWindowLocation.y + windowSize.height > screenSize.height) {
            // First try to reposition window
            newWindowLocation.y = windowLocation.y - (windowLocation.y + windowSize.height - screenSize.height);
            // Then resize it
            if (newWindowLocation.y < 0) {
                newWindowSize.height += newWindowLocation.y;
                newWindowLocation.y = 0;
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("fitOnScreen: newWindowSize: " + newWindowSize);
            LOG.debug("fitOnScreen: newWindowLocation: " + newWindowLocation);
        }

        inWindow.setSize(newWindowSize);
        inWindow.setLocation(newWindowLocation);
    }


    private static Rectangle getCenteredBounds(Dimension inWindowSize) {
        if (Debug.ON) {
            Debug.assertTrue(inWindowSize != null);
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = screenSize.width / 2 - inWindowSize.width / 2;
        int y = screenSize.height / 2 - inWindowSize.height / 2;

        return new Rectangle(x, y, inWindowSize.width, inWindowSize.height);
    }
}

