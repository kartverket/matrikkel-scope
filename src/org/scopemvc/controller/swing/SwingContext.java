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
package org.scopemvc.controller.swing;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.View;
import org.scopemvc.util.Debug;
import org.scopemvc.util.ResourceLoader;
import org.scopemvc.util.ScopeConfig;
import org.scopemvc.view.awt.AWTUtilities;
import org.scopemvc.view.swing.SMenuItem;
import org.scopemvc.view.swing.SwingUtil;
import org.scopemvc.view.swing.SwingView;
import org.scopemvc.controller.basic.ViewContext;

/**
 * <P>
 *
 * Swing implementation of {@link org.scopemvc.controller.basic.ViewContext} to
 * show views inside JFrames or JDialogs and show errors using JOptionPanes.
 * <br>
 * Also allows Views to take ownership of SMenuItems from their containing
 * Window. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.21 $ $Date: 2002/11/11 23:57:29 $
 * @created 05 August 2002
 * @see org.scopemvc.controller.basic.ViewContext
 * @todo synchronized (rootpanes) is needed because threading is not handled
 *      well. This could be removed if we use foxtrot.
 */
public class SwingContext extends ViewContext {

    /**
     * The property in ScopeConfig for the delay before showing the wait cursor
     * when startProgress once startProgress() has been called, value in
     * milliseconds
     */
    public static final String PROGRESS_START_DELAY_PROPERTY =
            "org.scopemvc.controller.swing.SwingContext.progress_start_delay";

    private static final Log LOG = LogFactory.getLog(SwingContext.class);

    private static final long PROGRESS_START_DELAY =
            ScopeConfig.getInteger(PROGRESS_START_DELAY_PROPERTY).longValue();

    // ----------------- Shared null frame ----------------

    /**
     * The parent of views that get shown when no parent can be found. Ensures
     * everything has the application icon.
     */
    private Frame sharedNullFrame;

    // ----------------- Visible View management ----------------

    /**
     * Keep track of all open Frames and Dialogs by keeping references to their
     * JRootPane. Don't bother tracking Message Boxes because they're modal and
     * don't interact with Controllers directly. This is an ordered list: the
     * last opened rootpane is at the end.
     */
    private LinkedList rootpanes = new LinkedList();

    /**
     * The timer used to delay the display of the wait cursor when {@link
     * #startProgress} is called
     */
    private Timer progressStartTimer = new Timer(true);
    private TimerTask task;


    /**
     * Constructor for the SwingContext object
     */
    public SwingContext() { }


    /**
     * Find the SMenuItem in the menu.
     *
     * @param inMenu A menu
     * @param inControlID The control ID of the menu item to find
     * @return The SMenuItem matching the control ID, or null if not found
     */
    protected static SMenuItem findMenuItemInMenu(JMenu inMenu, String inControlID) {
        if (inControlID == null) {
            throw new IllegalArgumentException("ControlID is null");
        }

        for (int i = 0; i < inMenu.getItemCount(); i++) {
            JMenuItem item = inMenu.getItem(i);
            if (item instanceof JMenu) {
                SMenuItem result = findMenuItemInMenu((JMenu) item, inControlID);
                if (result != null) {
                    return result;
                }
                continue;
            } else if (item instanceof SMenuItem
                    && inControlID.equals(((SMenuItem) item).getControlID())) {
                return (SMenuItem) item;
            }
        }
        return null;
    }


    /**
     * Find the rootpane that currently contains the focussed component, or null
     * if none. This is useful in finding the parent for a new Dialog.
     *
     * @return The focussedRootPane value
     */
    public JRootPane getFocussedRootPane() {
        synchronized (rootpanes) {
            for (Iterator i = rootpanes.iterator(); i.hasNext(); ) {
                Object o = i.next();
                if (Debug.ON) {
                    Debug.assertTrue(o instanceof JRootPane, "not JRootPane: " + o.getClass());
                }
                JRootPane r = (JRootPane) o;
                Container w = r.getParent();
                if (Debug.ON) {
                    Debug.assertTrue(w instanceof Window, "not Window: " + w.getClass());
                }
                if (isFocusOwner((Window) w)) {
                    return r;
                }
            }
        }
        return null;
    }


    /**
     * Show the view. <br>
     * Depending on the display mode of the view, the view is displayed in a
     * frame or a dialog.
     *
     * @param inView The view to show. Must be a subclass of SwingView
     */
    public void showView(View inView) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("showView: " + inView);
        }

        if (!(inView instanceof SwingView)) {
            throw new IllegalArgumentException("Can only show SwingViews: " + inView);
        }

        // Get topmost SwingView container
        Container parentContainer = getTopmostContainer((SwingView) inView);
        if (!(parentContainer instanceof SwingView)) {
            throw new IllegalArgumentException("Can only show SwingViews: " + parentContainer);
        }
        SwingView view = (SwingView) parentContainer;

        // Try to find the Dialog or Frame that holds View
        // If one exists, bring it to the front and return.
        JRootPane rootpane = findRootPaneFor(view);
        if (rootpane != null) {
            Container window = rootpane.getParent();
            rootpane.setDefaultButton(view.getDefaultButton());
            if (Debug.ON) {
                Debug.assertTrue(window instanceof Window, "window not window: " + window.getClass());
            }
            ((Window) window).toFront();
            return;
        }

        // OK, doesn't exist so create a new Dialog or Frame and show it
        if (view.getDisplayMode() == SwingView.PRIMARY_WINDOW) {
            showViewInPrimaryWindow(view);
        } else {
            showViewInDialog(view);
        }
        if (Debug.ON) {
            Debug.assertTrue(findRootPaneFor(view) != null, "null findRootPaneFor");
        }
    }


    /**
     * Find the Window that is showing this View and then close it on Swing's
     * event-handling thread. Does nothing if the passed View isn't showing.
     * <br>
     * This method disposes the Window from memory.
     *
     * @param inView The view to hide. Must be a subclass of SwingView
     */
    public void hideView(View inView) {
        if (!(inView instanceof SwingView)) {
            throw new IllegalArgumentException("Can only hide SwingViews: " + inView);
        }

        // Get topmost SwingView container
        Container parentContainer = getTopmostContainer((SwingView) inView);
        if (!(parentContainer instanceof SwingView)) {
            throw new IllegalArgumentException("Can only hide SwingViews: " + parentContainer);
        }
        SwingView view = (SwingView) parentContainer;

        JRootPane rootpane = findRootPaneFor(view);
        if (rootpane == null) {
            LOG.warn("Found no root pane for view " + view + ". View has not been shown");
            return;
        }

        // Save bounds of window for when reshown
        if (Debug.ON) {
            Debug.assertTrue(rootpane.getParent() != null, "parent: " + rootpane.getParent());
        }
        view.setViewBounds(rootpane.getParent().getBounds());

        hideRootPane(rootpane);
    }


    /**
     * Hide all open Views.
     */
    public void hideAllViews() {
        synchronized (rootpanes) {
            while (!rootpanes.isEmpty()) {
                JRootPane rootpane = (JRootPane) rootpanes.getLast();
                hideRootPane(rootpane);
            }
        }
    }


    /**
     * Are there any Views left open?
     *
     * @return true if all views are closed
     */
    public boolean areAllViewsClosed() {
        return (rootpanes.size() < 1);
    }


    // ----------------- Message boxes ----------------

    /**
     * Show an error message in a JOptionPane
     *
     * @param inErrorTitle The title for the error message
     * @param inErrorMessage The error message
     */
    public void showError(final String inErrorTitle, final String inErrorMessage) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("showError: " + inErrorTitle + ", " + inErrorMessage);
        }

        SwingUtil.runFromSwingEventThread(
            new Runnable() {
                public void run() {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("showError: " + inErrorTitle);
                    }
                    JOptionPane.showMessageDialog(getFocussedRootPane(), inErrorMessage, inErrorTitle,
                            JOptionPane.ERROR_MESSAGE);
                }
            });
    }


    /**
     * Show a message in a JOptionPane
     *
     * @param inTitle The title for the message
     * @param inMessage The message
     */
    public void showMessage(final String inTitle, final String inMessage) {
        SwingUtil.runFromSwingEventThread(
            new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(getFocussedRootPane(), inMessage, inTitle,
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });
    }


//     protected Object showOKCancelWarning(final String inTitle, final String inMessage) {
//         OKCancelOptionDialogShower s = new OKCancelOptionDialogShower(parentComponent, inTitle, inMessage,
//             JOptionPane.WARNING_MESSAGE);
//         Display.runFromSwingEventThread(s);
//         return s.result;
//     }
//
//
//     protected Object showYesNoWarning(final String inTitle, final String inMessage) {
//         YesNoOptionDialogShower s = new YesNoOptionDialogShower(parentComponent, inTitle, inMessage,
//             JOptionPane.WARNING_MESSAGE);
//         Display.runFromSwingEventThread(s);
//         return s.result;
//     }


    /**
     * Exit the application
     */
    public void exit() {
        hideAllViews();
        // default Swing operation
        System.exit(0);
    }


    /**
     * Start a progress indicator to notify the user that a long operation is
     * running. <br>
     * In the SwingContext, the progress indicator is a wait cursor, and it is
     * displayed after a delay of n milliseconds defined in the Scope property
     * 'org.scopemvc.controller.swing.SwingContext.progress_start_delay'
     */
    public void startProgress() {
        if (task != null) {
            task.cancel();
        }
        task =
            new TimerTask() {
                public void run() {
                    getDefaultParentWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }
            };
        progressStartTimer.schedule(task, PROGRESS_START_DELAY);
    }


    /**
     * Stop the progress indicator
     */
    public void stopProgress() {
        if (task != null) {
            task.cancel();
        }
        getDefaultParentWindow().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }


    // ------------------------------------ Menu handling ------------------------------

    /**
     * Hook a menu item to a view. <br>
     * The view and its bound controller will receive the Controls issued by the
     * menu item.
     *
     * @param inView the view to hook the menu item to.
     * @param inControlID The control ID used to identity the menu item
     * @param inEnabled Set to true to enable the menu item
     */
    public void hookMenuItem(SwingView inView, String inControlID, boolean inEnabled) {
        SMenuItem item = findMenuItem(inView, inControlID);
        if (item == null) {
            LOG.error("Can't find menuitem for: " + inControlID, new Throwable());
            return;
        }

        item.setOwner(inView);
        item.setEnabled(inEnabled);
    }


    /**
     * Find the Rootpane that contains the SwingView or null if none can be
     * found.
     *
     * @param inView The View to get the RootPane from
     * @return The parent RootPane, or null
     */
    public JRootPane findRootPaneFor(View inView) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("findRootPaneFor: " + inView);
        }
        if (Debug.ON) {
            Debug.assertTrue(inView instanceof JComponent, "not JComponent: " + inView.getClass().getName());
        }
        synchronized (rootpanes) {
            for (Iterator i = rootpanes.iterator(); i.hasNext(); ) {
                Object o = i.next();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("findRootPaneFor: looking at: " + o);
                }
                if (Debug.ON) {
                    Debug.assertTrue(o instanceof JRootPane, "not JRootPane: " + o.getClass());
                }
                JRootPane r = (JRootPane) o;
                if (r.isAncestorOf((JComponent) inView)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("findRootPaneFor: got it");
                    }
                    return r;
                }
            }
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("findRootPaneFor: not found");
        }
        return null;
    }


    /**
     * Get the frame used as default parent for the views. <br>
     * This frame uses the default application icon.
     *
     * @return The sharedNullFrame value
     */
    protected Frame getSharedNullFrame() {
        if (sharedNullFrame == null) {
            try {
                sharedNullFrame = new NullFrame();
                sharedNullFrame.setIconImage(getDefaultWindowIcon());
                AWTUtilities.centreOnScreen(sharedNullFrame);
            } catch (UnsupportedOperationException ignore) {
                // thrown by JDK 1.4
            }
        }
        return sharedNullFrame;
    }


    /**
     * Get the default icon. <br>
     * The icon is initialised from the property
     * 'org.scopemvc.controller.swing.SwingContext.window_icon' defined in the
     * Scope configuration. This property should contain the location of the
     * icon resource. The icon is loaded with the ResourceLoader.
     *
     * @return The defaultWindowIcon value
     * @see org.scopemvc.util.ResourceLoader
     */
    protected Image getDefaultWindowIcon() {
        String iconPath = ScopeConfig.getString("org.scopemvc.controller.swing.SwingContext.window_icon");
        if (iconPath == null || iconPath.length() < 1) {
            return null;
        }
        try {
            return ResourceLoader.getImage(iconPath);
        } catch (RuntimeException e) {
            LOG.warn("getDefaultWindowIconPath: (" + iconPath + ")", e);
            return null;
        }
    }


    /**
     * <p>
     *
     * Returns true if the window owns the focus. </p> <p>
     *
     * Swing loses focus from the current component when a menu is pulled down
     * so for a Window to have focus we say it needs to either contain the focus
     * owner OR parent a Window (not a Dialog/Frame) that owns the focus.</p>
     *
     * @param inWindow The Window to test for focus
     * @return True if the window or one of its child owns the focus
     */
    protected boolean isFocusOwner(Window inWindow) {
        if (inWindow.getFocusOwner() != null) {
            return true;
        }
        Window[] children = inWindow.getOwnedWindows();
        if (children == null) {
            return false;
        }
        for (int i = 0; i < children.length; i++) {
            Window child = children[i];
            if (child instanceof Dialog || child instanceof Frame) {
                continue;
            }
            if (isFocusOwner(child)) {
                return true;
            }
        }
        return false;
    }


    /**
     * For dialogs to be opened, find the currently focussed window, or if none,
     * the last opened window or if none, the shared null Frame.
     *
     * @return The defaultParentWindow value
     */
    protected Window getDefaultParentWindow() {

        // The currently focussed window
        JRootPane r = getFocussedRootPane();
        if (r != null) {
            Container c = r.getParent();
            if (Debug.ON) {
                Debug.assertTrue(c instanceof Window, "not Window: " + c.getClass());
            }
            return (Window) c;
        }

        // if no open windows, the shared null frame
        if (rootpanes.size() == 0) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("null frame");
            }
            return getSharedNullFrame();
        }

        // the last opened window
        if (Debug.ON) {
            Debug.assertTrue(rootpanes.getLast() instanceof JRootPane);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("last shown frame: " + (Window) ((JRootPane) rootpanes.getLast()).getParent());
        }
        return (Window) ((JRootPane) rootpanes.getLast()).getParent();
    }


    /**
     * Finds the topmost Container, stopping before any Window structure.
     *
     * @param inView The View to get the topmost Container from
     * @return The topmost Container
     */
    protected Container getTopmostContainer(SwingView inView) {

        // Already in a JRootPane? Then find the content pane so we know when to stop below
        Container contentPane = null;
        JRootPane parentRootpane = inView.getRootPane();
        if (parentRootpane != null) {
            contentPane = parentRootpane.getContentPane();
        }

        Container result = inView;
        while (result.getParent() != null && result.getParent() != contentPane) {
            result = result.getParent();
        }
        return result;
    }


    /**
     * Setup the window: insert the view in the window, setup the title and the
     * menubar in the window and position the window on the screen.
     *
     * @param inRootPane The RootPane where to add the View
     * @param inView The View to setup
     * @param inCentreWindow If true, centre the window on the screen or on its
     *      parent window
     */
    protected void setupWindow(JRootPane inRootPane, SwingView inView, boolean inCentreWindow) {

        Container c = inRootPane.getParent();
        if (Debug.ON) {
            Debug.assertTrue(c instanceof Window, "not Window: " + c.getClass());
        }
        final Window window = (Window) c;

        synchronized (rootpanes) {
            // Keep track of the new RootPane
            rootpanes.add(inRootPane);
        }

        // Put the View in the content pane
        inRootPane.getContentPane().setLayout(new BorderLayout());
        inRootPane.getContentPane().add(BorderLayout.CENTER, inView);

        // Attach the View's menubar to the dialog
        inRootPane.setJMenuBar(inView.getMenuBar());

        // Define the default button
        inRootPane.setDefaultButton(inView.getDefaultButton());

        // Restore last bounds of this View
        Rectangle lastShownBounds = inView.getViewBounds();
        if (lastShownBounds == null) {
            window.pack();
            if (inCentreWindow) {
                if (Debug.ON) {
                    Debug.assertTrue(window.getParent() instanceof Window, "no window parent");
                }
                AWTUtilities.centreOnWindow((Window) window.getParent(), window);
            }
        } else if (lastShownBounds == SwingView.CENTRED) {
            window.pack();
            AWTUtilities.centreOnScreen(window);
        } else {
            window.setBounds(lastShownBounds);
        }
        // If too big for screen, adjust
        AWTUtilities.fitOnScreen(window);

        // WindowCloser to issue close Control
        window.addWindowListener(new WindowCloser(inView));
        // Don't let Swing handle the window close. Nasty... Swing needs an interface here
        if (window instanceof JFrame) {
            ((JFrame) window).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        } else if (window instanceof JDialog) {
            ((JDialog) window).setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        }

        // Display safely from event queue
        SwingUtil.runFromSwingEventThread(
            new Runnable() {
                public void run() {
                    window.setVisible(true);
                }
            });
    }


    /**
     * Hide the root pane and dispose of the parent window.
     *
     * @param inRootPane The RootPane to hide
     */
    protected void hideRootPane(JRootPane inRootPane) {
        synchronized (rootpanes) {
            rootpanes.remove(inRootPane);
        }
        Container window = inRootPane.getParent();
        if (window == sharedNullFrame) {
            sharedNullFrame = null;
        }

        if (Debug.ON) {
            Debug.assertTrue(window instanceof Window, "not Window: " + window);
        }
        hideWindow((Window) window);
        if (areAllViewsClosed() && sharedNullFrame != null) {
            hideWindow(sharedNullFrame);
            sharedNullFrame = null;
        }
    }

    /**
     * Hide the window safely from the Swing event thread.
     *
     * @param inWindow The window to close
     */
    protected void hideWindow(final Window inWindow) {
        SwingUtil.runFromSwingEventThread(
            new Runnable() {
                public void run() {
                    inWindow.dispose();
                }
            });
    }

    /**
     * Make a dialog (modal or modeless determined by {@link
     * org.scopemvc.view.swing.SwingView#getDisplayMode the display mode})
     * parented to either currently focussed window, or last shown, or null
     * shared frame.
     *
     * @param inView The View to show
     */
    protected void showViewInDialog(SwingView inView) {

        // Make a JDialog to contain the view.
        Window parentWindow = getDefaultParentWindow();
        if (Debug.ON) {
            Debug.assertTrue(parentWindow != null, "null parentWindow");
        }

        final JDialog dialog;
        if (parentWindow instanceof Dialog) {
            dialog = new JDialog((Dialog) parentWindow);
        } else {
            if (Debug.ON) {
                Debug.assertTrue(parentWindow instanceof Frame);
            }
            dialog = new JDialog((Frame) parentWindow);
        }

        // Set title, modality, resizability
        if (inView.getTitle() != null) {
            dialog.setTitle(inView.getTitle());
        }
        if (inView.getDisplayMode() == SwingView.MODAL_DIALOG) {
            dialog.setModal(true);
        } else {
            dialog.setModal(false);
        }
        dialog.setResizable(inView.isResizable());

        setupWindow(dialog.getRootPane(), inView, true);
    }


    /**
     * Show the view in a new frame
     *
     * @param inView The View to show
     */
    protected void showViewInPrimaryWindow(SwingView inView) {

        // Make a JFrame to contain the view
        final JFrame frame = new JFrame();

        // Set its title, icon, resizability
        if (inView.getTitle() != null) {
            frame.setTitle(inView.getTitle());
        }
        // All JFrames take the same icon
        Image icon = getDefaultWindowIcon();
        if (icon != null) {
            frame.setIconImage(icon);
        }
        frame.setResizable(inView.isResizable());

        setupWindow(frame.getRootPane(), inView, false);
    }


    /**
     * Unhook a menu item from a view.
     *
     * @param inView the view where the menu item was hooked to.
     * @param inControlID The control ID used to identity the menu item
     */
    protected void unhookMenuItemImpl(String inControlID, SwingView inView) {
        SMenuItem item = findMenuItem(inView, inControlID);
        if (item == null) {
            LOG.error("Can't find menuitem for: " + inControlID, new Throwable());
            return;
        }

        item.unsetOwner(inView);
    }


    /**
     * Find the SMenuItem in the menu bar associated with the frame of the view.
     *
     * @param inView A view contained in a frame containing a menubar
     * @param inControlID The control ID of the menu item to find
     * @return The SMenuItem matching the control ID, or null
     */
    protected SMenuItem findMenuItem(SwingView inView, String inControlID) {
        JRootPane rootPane = findRootPaneFor(inView);
        if (rootPane == null) {
            return null;
        }

        JMenuBar menuBar = rootPane.getJMenuBar();
        if (menuBar == null) {
            return null;
        }

        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            SMenuItem result = findMenuItemInMenu(menu, inControlID);
            if (result != null) {
                return result;
            }
        }
        return null;
    }


    static class NullFrame extends Frame {
        /**
         * Sets the visible attribute
         *
         * @param inVisible The new visible value
         */
        public void setVisible(boolean inVisible) {
            // Don't ever show!
        }
    }


    // ----------------- Show/hide Views ----------------

    /**
     * Attached to windows opened by this manager so that they issue a Control
     * on closing.
     *
     * @author smefroy
     * @version $Revision: 1.21 $
     * @created 05 August 2002
     */
    static class WindowCloser extends WindowAdapter {
        private SwingView view;

        /**
         * Constructor for the WindowCloser object
         *
         * @param inView The View to watch for close events.
         */
        WindowCloser(SwingView inView) {
            view = inView;
        }

        /**
         * Notifies that the Window is closing
         *
         * @param inEvent the Window event
         */
        public void windowClosing(WindowEvent inEvent) {
            view.issueControl(view.getCloseControl());
        }
    }
}
