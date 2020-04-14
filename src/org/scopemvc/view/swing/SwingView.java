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
 * $Id: SwingView.java,v 1.14 2002/11/20 01:36:58 ludovicc Exp $
 */
package org.scopemvc.view.swing;


import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.controller.basic.ViewContext;
import org.scopemvc.controller.swing.SwingContext;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.View;

/**
 * <P>
 *
 * An abstract base JPanel implementation of View for use in Swing-based user
 * interfaces. {@link org.scopemvc.controller.swing.SwingContext} assumes the
 * use of SwingView subclasses, for which the following methods should be
 * overridden:
 * <UL>
 *   <LI> {@link #getTitle}</LI>
 *   <LI> {@link #getDisplayMode}</LI>
 *   <LI> {@link #getCloseControl}</LI>
 *   <LI> {@link #isResizable}</LI>
 * </UL>
 * </P> <P>
 *
 * Binding to the model object should be implemented in subclasses appropriately
 * -- see {@link SPanel} for an example. This split in functionality is to
 * support splitting Scope into a Controller/View subsystem and an independent
 * model subsystem. </P> <P>
 *
 * SwingView subclasses must issue a CHANGE_MODEL_CONTROL_ID Control when the
 * shown model object changes. This supports BasicController which needs to know
 * when its current model changes. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net>Ludovic Claude</a>
 * @version $Revision: 1.14 $ $Date: 2002/11/20 01:36:58 $
 * @created 18 June 2002
 */
public abstract class SwingView extends JPanel implements View {

    /**
     * The view type for all components in this package
     *
     * @see org.scopemvc.core.EditorManager
     */
    public static final String VIEW_TYPE = "swing";

    /**
     * Display the view in a new Frame
     *
     * @see #getDisplayMode
     */
    public static final int PRIMARY_WINDOW = 0;

    /**
     * Display the view in a modal dialog
     *
     * @see #getDisplayMode
     */
    public static final int MODAL_DIALOG = 1;

    /**
     * Display the view in a modeless dialog
     *
     * @see #getDisplayMode
     */
    public static final int MODELESS_DIALOG = 2;

    // ------------------- Support SwingContext: don't usually override these -------------------

    /**
     * Constant for positioning the screen centered on the screen
     *
     * @see #setViewBounds
     */
    public static final Rectangle CENTRED = new Rectangle();

    private static final Log LOG = LogFactory.getLog(SwingView.class);

    private List subViews = null;

    /**
     * The bounds that the view will be shown at. If null use default position.
     * The bounds are saved on hideView so the view come back at the same
     * position when shown again.
     */
    private Rectangle viewBounds;

    private int displayMode = PRIMARY_WINDOW;

    private String title = "Untitled";

    private JMenuBar menuBar;

    private Control closeControl = new Control(BasicController.HIDE_VIEW_CONTROL_ID, this);

    private JButton defaultButton;

    // -------------- implement View ------------------

    private Controller controller;

    /**
     * Automatically installs a SwingContext if SwingView is created anywhere
     * and no ViewContext has been set previously or if the ViewContext is not a
     * SwingContext.
     */
    public SwingView() {
        if (ViewContext.getViewContext() == null
                || !(ViewContext.getViewContext() instanceof SwingContext)) {
            ViewContext.clearThreadContext();
            ViewContext.setGlobalContext(new SwingContext());
        }
    }


    /**
     * Get bounds to show this view at. If null default positioning, if value is
     * {@link #CENTRED} then centre the view on the screen.
     *
     * @return The viewBounds value
     */
    public final Rectangle getViewBounds() {
        return viewBounds;
    }


    /**
     * @return The lastShownBounds value
     * @deprecated see {@link #getViewBounds}
     */
    public final Rectangle getLastShownBounds() {
        return getViewBounds();
    }


    // ------------------- Support SwingContext: override these -------------------

    /**
     * Provides the title for windows that show this view.
     *
     * @return The title value
     * @see org.scopemvc.controller.swing.SwingContext
     */
    public String getTitle() {
        return title;
    }


    /**
     * Determines the type of window used to show this view.
     *
     * @return The displayMode value
     * @see #PRIMARY_WINDOW
     * @see #MODAL_DIALOG
     * @see #MODELESS_DIALOG
     * @see org.scopemvc.controller.swing.SwingContext
     */
    public int getDisplayMode() {
        return displayMode;
    }


    /**
     * The Control issued when the window containing this View is closed.
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return closeControl;
    }


    /**
     * Should a window showing this view be resizable?
     *
     * @return The resizable value
     */
    public boolean isResizable() {
        return true;
    }


    /**
     * The menubar to attach to a window showing this view, or null if none.
     *
     * @return The menuBar value
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }


    /**
     * Gets the controller
     *
     * @return The controller value
     */
    public Controller getController() {
        return controller;
    }


    /**
     * Issue a Control to the parent Controller, or if no direct parent, search
     * upwards in the View hierarchy for the next up.
     *
     * @param inControl the Control to broadcast.
     */
    public void issueControl(Control inControl) {
        SwingUtil.issueControl(this, inControl);
    }

    /**
     * Gets the number of sub views
     *
     * @return The subViewCount value
     */
    public int getSubViewCount() {
        if (subViews != null) {
            return subViews.size();
        } else {
            return 0;
        }
    }

    /**
     * Gets the sub view at the given index
     *
     * @param inIndex The index of the view in the list of sub views
     * @return The SwingSubView
     */
    public SwingSubView getSubView(int inIndex) {
        if (subViews != null && inIndex < subViews.size()) {
            return (SwingSubView) subViews.get(inIndex);
        }
        return null;
    }

    // --------------------- JRootPane equivalent functionality -----------

    /**
     * Gets the default button
     *
     * @return The defaultButton value
     */
    public JButton getDefaultButton() {
        return defaultButton;
    }

    /**
     * The parent Controller must register itself with the SwingView via this
     * method in order to receive Control from it. Only one Controller can
     * parent a SwingView, which should be fine if a Chain of Command is used
     * between Controllers.
     *
     * @param inController the parent Controller.
     */
    public final void setController(Controller inController) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setController: " + inController);
        }

        controller = inController;
    }


    /**
     * The Control issued when the window containing this View is closed.
     *
     * @param inCloseControl The closeControl value
     */
    public void setCloseControl(Control inCloseControl) {
        closeControl = inCloseControl;
    }

    /**
     * The Control issued when the window containing this View is closed.
     *
     * @param inCloseControlID The control ID to issue - usually one of the
     *      following values defined in {@link
     *      org.scopemvc.controller.basic.BasicController BasicController}
     * @see org.scopemvc.controller.basic.BasicController#HIDE_VIEW_CONTROL_ID
     * @see org.scopemvc.controller.basic.BasicController#EXIT_CONTROL_ID
     */
    public void setCloseControl(String inCloseControlID) {
        closeControl = new Control(inCloseControlID, this);
    }

    /**
     * Defines the type of window used to show this view.
     *
     * @param inDisplayMode The new displayMode value
     * @see #PRIMARY_WINDOW
     * @see #MODAL_DIALOG
     * @see #MODELESS_DIALOG
     * @see org.scopemvc.controller.swing.SwingContext
     */
    public void setDisplayMode(int inDisplayMode) {
        displayMode = inDisplayMode;
    }


    /**
     * Provides the title for windows that show this view.
     *
     * @param inTitle The new title value
     * @see org.scopemvc.controller.swing.SwingContext
     */
    public void setTitle(String inTitle) {
        title = inTitle;
    }


    /**
     * Called when hiding a view to store last bounds of this view when visible.
     * Override to return null if the view should always be packed and placed in
     * default location. Call this during View ctor to set up initial bounds.
     *
     * @param inViewBounds The new viewBounds value
     */
    public void setViewBounds(Rectangle inViewBounds) {
        viewBounds = inViewBounds;
    }


    /**
     * @param inLastShownBounds The new lastShownBounds value
     * @deprecated see {@link #setViewBounds}
     */
    public void setLastShownBounds(Rectangle inLastShownBounds) {
        setViewBounds(inLastShownBounds);
    }

    /**
     * The menubar to attach to a window showing this view, or null if none.
     *
     * @param inMenuBar The new menuBar value
     */
    public void setMenuBar(JMenuBar inMenuBar) {
        menuBar = inMenuBar;
    }

    /**
     * Sets the default button
     *
     * @param inButton The new defaultButton value
     */
    public void setDefaultButton(JButton inButton) {
        defaultButton = inButton;
        SwingContext context = (SwingContext) SwingContext.getViewContext();
        JRootPane rootPane = context.findRootPaneFor(this);
        if (rootPane != null) {
            rootPane.setDefaultButton(defaultButton);
        }
    }


    /**
     * Returns a string description
     *
     * @return a string description
     */
    public String toString() {
        return getClass().toString() + "(" + getTitle() + ")";
    }

    /**
     * <P>
     *
     * Adds a sub view to this view. <br>
     * To use only for dependant views that are not Swing components, such as
     * Actions and MenuItem. </P> <P>
     *
     * Standard component views are automatically registered in the SwingView
     * when they are added to this container or one of its child container with
     * the add() method. </P>
     *
     * @param inSubView The sub view to be added
     * @see java.awt.Container#add
     */
    public void addSubView(SwingSubView inSubView) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("addSubView: subView: " + inSubView);
        }
        if (subViews == null) {
            subViews = new ArrayList();
        }
        if (!subViews.contains(inSubView)) {
            subViews.add(inSubView);
            inSubView.setOwner(this);
        }
    }

    /**
     * Remove a sub view from this view
     *
     * @param inSubView The sub view to remove
     */
    public void removeSubView(SwingSubView inSubView) {
        if (subViews == null || inSubView.getOwner() != this) {
            return;
        }
        subViews.remove(inSubView);
        inSubView.unsetOwner(this);
    }
}

