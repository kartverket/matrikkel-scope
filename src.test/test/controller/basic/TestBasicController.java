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
 * $Id: TestBasicController.java,v 1.5 2002/11/20 00:19:58 ludovicc Exp $
 */
package test.controller.basic;

import junit.framework.*;
import org.scopemvc.controller.basic.*;

import org.scopemvc.core.*;
import org.scopemvc.model.basic.*;

/**
 * <P>
 *
 * ***** Need to add tests for showView(View) and hideView(View). </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:58 $
 */
public final class TestBasicController extends TestCase {

    static ControlException controlException;

    private Object model1, model2;
    private View view1, view2;
    private BasicController controller1, controller2;
    private DummyContext context;


    /**
     * Constructor for the TestBasicController object
     *
     * @param inName Name of the test
     */
    public TestBasicController(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     */
    public void testNewController() {
        assertNull(controller1.getParent());
        assertNull(controller1.getView());
        assertNull(controller1.getModel());
    }


    /**
     * A unit test for JUnit
     */
    public void testHookupModelAndView() {
        controller1.setModelAndView(model1, view1);

        assertNull(controller1.getParent());
        assertSame(controller1.getView(), view1);
        assertSame(controller1.getModel(), model1);
        assertSame(view1.getBoundModel(), model1);
        assertSame(view1.getController(), controller1);
    }


    /**
     * A unit test for JUnit
     */
    public void testHookupModelFirst() {
        // Set a model and a view for controller to hookup with setModel first
        controller1.setModel(model1);
        controller1.setView(view1);

        assertNull(controller1.getParent());
        assertSame(controller1.getView(), view1);
        assertSame(controller1.getModel(), model1);
        assertSame(view1.getBoundModel(), model1);
        assertSame(view1.getController(), controller1);
    }


    /**
     * A unit test for JUnit
     */
    public void testHookupViewFirst() {
        // Set a model and a view for controller to hookup with setModel first
        controller1.setView(view1);
        controller1.setModel(model1);

        assertNull(controller1.getParent());
        assertSame(controller1.getView(), view1);
        assertSame(controller1.getModel(), model1);
        assertSame(view1.getBoundModel(), model1);
        assertSame(view1.getController(), controller1);
    }


    /**
     * A unit test for JUnit
     */
    public void testStartup() {
        controller1.setModelAndView(model1, view1);
        controller1.startup();
        assertSame(view1, context.lastShownView);
    }


    /**
     * A unit test for JUnit
     */
    public void testShutdown() {
        controller1.setModelAndView(model1, view1);
        controller1.startup();
        assertSame(view1, context.lastShownView);
        controller1.shutdown();
        assertSame(view1, context.lastHiddenView);
    }


    /**
     * A unit test for JUnit
     */
    public void testExit() {
        assertTrue(!context.exited);
        controller1.addChild(controller2);
        controller2.handleControl(new Control(BasicController.EXIT_CONTROL_ID));
        assertTrue(context.exited);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testChildController() throws Exception {
        controller1.setModelAndView(model1, view1);
        controller2.addChild(controller1);

        assertSame(controller1.getParent(), controller2);
        assertSame(controller1.getView(), view1);
        assertSame(view1.getController(), controller1);
        assertSame(model1, controller1.getModel());
        assertSame(model1, view1.getBoundModel());

        assertTrue(controller2.getChildren().contains(controller1));

        controller2.removeChild(controller1);
        assertTrue(!controller2.getChildren().contains(controller1));
        assertTrue(controller1.getParent() == null);
    }


    /**
     * A unit test for JUnit
     */
    public void testChangeModel1() {
        controller1.setView(view1);
        controller1.setModel(model1);

        assertSame(view1, controller1.getView());
        assertSame(model1, controller1.getModel());

        Control control = new Control(BasicController.CHANGE_MODEL_CONTROL_ID, model2);
        controller1.handleControl(control);

        assertSame(view1, controller1.getView());
        assertSame(model2, controller1.getModel());
    }


    /**
     * A unit test for JUnit
     */
    public void testControlError() {
        assertNull(controlException);
        Control control = new Control(TestController.MAKE_EXCEPTION);
        controller1.handleControl(control);
        assertEquals(TestController.MAKE_EXCEPTION, controlException.getLocalizedSourceControlName());
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        context = new DummyContext();
        ViewContext.clearThreadContext(); //flaky test fix
        ViewContext.setGlobalContext(context);

        model1 = new DummyModel("Model1");
        model2 = new DummyModel("Model2");
        view1 = new DummyView("View1");
        view2 = new DummyView("View2");
        controller1 = new TestController("Controller1");
        controller2 = new TestController("Controller2");
        controlException = null;
    }


    // ------------------------ Inner classes ----------------------------

    class TestController extends BasicController {
        final static String MAKE_EXCEPTION = "makeError";
        private String name;

        /**
         * Constructor for the TestController object
         *
         * @param inName Name of the test
         */
        TestController(String inName) {
            super();
            name = inName;
        }

        /**
         * TODO: document the method
         *
         * @param inControl TODO: Describe the Parameter
         * @throws ControlException Any abnormal exception
         */
        protected void doHandleControl(Control inControl) throws ControlException {
            if (inControl.matchesID(MAKE_EXCEPTION)) {
                throw new ControlException("test");
            }
        }

        /**
         * TODO: document the method
         *
         * @param inException TODO: Describe the Parameter
         */
        protected void handleControlException(ControlException inException) {
            controlException = inException;
        }

        /**
         * Gets the name
         *
         * @return The name value
         */
        String getName() {
            return name;
        }
    }


    class DummyModel extends BasicModel {
        private String name;

        /**
         * Constructor for the DummyModel object
         *
         * @param inName Name of the test
         */
        DummyModel(String inName) {
            name = inName;
        }

        /**
         * TODO: document the method
         *
         * @return TODO: Describe the Return Value
         */
        public String toString() {
            return getName();
        }

        /**
         * Gets the name
         *
         * @return The name value
         */
        String getName() {
            return name;
        }
    }


    class DummyView implements View, ModelChangeListener {
        private String name;
        private Controller controller;
        private Object model;

        /**
         * Constructor for the DummyView object
         *
         * @param inName Name of the test
         */
        DummyView(String inName) {
            name = inName;
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
         * Gets the bound model
         *
         * @return The boundModel value
         */
        public Object getBoundModel() {
            return model;
        }

        /**
         * TODO: document the method
         *
         * @param inControl TODO: Describe the Parameter
         */
        public void issueControl(Control inControl) { }

        /**
         * Sets the controller
         *
         * @param inController The new controller value
         */
        public void setController(Controller inController) {
            controller = inController;
        }

        /**
         * Sets the bound model
         *
         * @param inModel The new boundModel value
         */
        public void setBoundModel(Object inModel) {
            model = inModel;
        }

        /**
         * TODO: document the method
         *
         * @param inEvent TODO: Describe the Parameter
         */
        public void modelChanged(ModelChangeEvent inEvent) { }

        /**
         * TODO: document the method
         *
         * @return TODO: Describe the Return Value
         */
        public String toString() {
            return getName();
        }

        /**
         * Gets the name
         *
         * @return The name value
         */
        String getName() {
            return name;
        }
    }
}

