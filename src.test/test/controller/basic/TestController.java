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
package test.controller.basic;

import junit.framework.TestCase;
import org.scopemvc.controller.basic.*;

import org.scopemvc.core.*;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:19:58 $
 * @created 05 September 2002
 */
public final class TestController extends TestCase {

    /**
     * TODO: describe of the Field
     */
    public static boolean controlError = false;
    /**
     * TODO: describe of the Field
     */
    public static boolean doneControl = false;

    private static final String CONTROL_1_ID = "CONTROL1";
    private static final String CONTROL_2_ID = "CONTROL2";


    /**
     * Constructor for the TestController object
     *
     * @param inName Name of the test
     */
    public TestController(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     */
    public void testControllerSetup() {

        DummyController controller = new DummyController();
        DummyController parentController = new DummyController();
        Object model = new Object();
        View view = new DummyView();

        parentController.addChild(controller);
        controller.setModel(model);
        controller.setView(view);
        assertEquals(parentController, controller.getParent());
        assertEquals(model, controller.getModel());
        assertEquals(view, controller.getView());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testControllerExecute() throws Exception {
        DummyController parent = new DummyController();
        DummyController child = new DummyController();
        parent.addChild(child);

        // Neither Controller is enabled to handle any Controls
        // ... so test that the Control goes unhandled

        // No control handlers should fail silently with debug output (assertion failures)
        controlError = false;
        doneControl = false;

        child.handleControl(new Control(CONTROL_1_ID));
        assertTrue(!doneControl);

        parent.handleControl(new Control(CONTROL_1_ID));
        assertTrue(!doneControl);

        // Make child handle Control1 and execute to throw ControlException
        controlError = false;
        child.setupForControl1();
        child.handleControl(new Control(CONTROL_1_ID));
        assertTrue("Expected control exception", controlError);

        // Make parent handle Control2 and execute it
        doneControl = false;
        parent.setupForControl2();
        parent.handleControl(new Control(CONTROL_2_ID));
        assertTrue("Expected control done", doneControl);

        // Make parent handle Control2 and execute it
        doneControl = false;
        child.handleControl(new Control(CONTROL_2_ID));
        assertTrue("Expected control done (from child)", doneControl);
    }


    /**
     * A unit test for JUnit
     */
    public void testControllerSetView() {

        Object model = new Object();
        View view = new DummyView();

        Controller controller = new DummyController();
        controller.setModel(model);
        controller.setView(view);
        assertSame(view, controller.getView());
        assertSame(model, controller.getModel());
        assertSame(model, view.getBoundModel());
        assertSame(controller, view.getController());

        View view2 = new DummyView();
        controller.setView(view2);
        assertSame(view2, controller.getView());
        assertSame(model, controller.getModel());
        assertSame(model, view2.getBoundModel());
        assertSame(controller, view2.getController());

        assertNull(view.getController());
        assertNull(view.getBoundModel());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testTopController() throws Exception {
        DummyController parent = new DummyController();
        DummyController child = new DummyController();
        parent.addChild(child);

        assertSame(parent.getTopParent(), parent);
        assertSame(child.getTopParent(), parent);
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        ViewContext.setGlobalContext(new DummyContext());
    }


    class DummyController extends BasicController {
        private boolean canDoControl1 = false;
        private boolean canDoControl2 = false;

        /**
         * TODO: document the method
         */
        public void setupForControl1() {
            canDoControl1 = true;
        }

        /**
         * TODO: document the method
         */
        public void setupForControl2() {
            canDoControl2 = true;
        }

        /**
         * TODO: document the method
         *
         * @param inControl TODO: Describe the Parameter
         * @throws ControlException Any abnormal exception
         */
        protected void doHandleControl(Control inControl) throws ControlException {
            if (canDoControl1 && inControl.matchesID(CONTROL_1_ID)) {
                inControl.markMatched();
                doControl1();
            } else if (canDoControl2 && inControl.matchesID(CONTROL_2_ID)) {
                doControl2();
                inControl.markMatched();
            }
        }

        /**
         * TODO: document the method
         *
         * @param inException TODO: Describe the Parameter
         */
        protected void handleControlException(ControlException inException) {
            TestController.controlError = true;
        }

        /**
         * TODO: document the method
         *
         * @throws ControlException Any abnormal exception
         */
        protected void doControl1() throws ControlException {
            throw new ControlException("DUMMY_MESSAGE_ID");
        }

        /**
         * TODO: document the method
         */
        protected void doControl2() {
            TestController.doneControl = true;
        }
    }


    class DummyView implements View, ModelChangeListener {
        private Object model;
        private Controller controller;

        /**
         * Gets the bound model
         *
         * @return The boundModel value
         */
        public Object getBoundModel() {
            return model;
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
         * TODO: document the method
         *
         * @param inControl TODO: Describe the Parameter
         */
        public void issueControl(Control inControl) { }

        /**
         * Sets the bound model
         *
         * @param inModel The new boundModel value
         */
        public void setBoundModel(Object inModel) {
            model = inModel;
        }

        /**
         * Sets the controller
         *
         * @param inController The new controller value
         */
        public void setController(Controller inController) {
            controller = inController;
        }

        /**
         * TODO: document the method
         *
         * @param inEvent TODO: Describe the Parameter
         */
        public void modelChanged(ModelChangeEvent inEvent) { }
    }
}

