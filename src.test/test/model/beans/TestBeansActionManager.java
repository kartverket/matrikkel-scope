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
package test.model.beans;


import junit.framework.TestCase;
import org.scopemvc.core.ActionManager;
import org.scopemvc.core.ModelAction;
import org.scopemvc.model.beans.BeansActionManager;

/**
 * <P>
 *
 * ***** Need to test DynamicInvokable </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/12 19:09:35 $
 * @created 05 September 2002
 */
public final class TestBeansActionManager extends TestCase {

    private ModelObject m;
    private ActionManager manager;


    /**
     * Constructor for the TestBeansActionManager object
     *
     * @param inName Name of the test
     */
    public TestBeansActionManager(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testNoargsAction() throws Exception {
        ModelAction ma = new ModelAction("action1");
        assertTrue(manager.canDoAction(m, ma));
        assertTrue(!manager.canDoAction(m, new ModelAction("abc")));

        ModelAction ma1 = new ModelAction("action1", String.class);
        assertTrue(!manager.canDoAction(m, ma1));

        assertTrue(!m.doneAction1);
        manager.doAction(m, ma, null);
        assertTrue(m.doneAction1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testPrimitiveArgAction() throws Exception {
        ModelAction ma = new ModelAction("action2", Integer.TYPE);
        assertTrue(manager.canDoAction(m, ma));

        ModelAction ma1 = new ModelAction("action2", String.class);
        assertTrue(!manager.canDoAction(m, ma1));

        ModelAction ma2 = new ModelAction("action2");
        assertTrue(!manager.canDoAction(m, ma2));

        try {
            manager.doAction(m, ma, null);
            fail("Did action with wrong number of parameters");
        } catch (Exception e) {
            // expected
        }

        try {
            manager.doAction(m, ma, new Object[]{"abc"});
            fail("Did action with wrong type of parameters");
        } catch (Exception e) {
            // expected
        }

        manager.doAction(m, ma, new Object[]{new Integer(99)});
        assertTrue(m.action2 == 99);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testReturnAction() throws Exception {
        ModelAction ma = new ModelAction("action3", Long.TYPE);
        Long l = new Long(999);
        assertEquals(l, manager.doAction(m, ma, new Object[]{l}));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testCantDoAction() throws Exception {
        ModelAction ma = new ModelAction("noAction");
        try {
            manager.doAction(m, ma, null);
            fail("Did action that can't do");
        } catch (Exception e) {
        }
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        m = new ModelObject();
        manager = BeansActionManager.getInstance(m);
    }
}

