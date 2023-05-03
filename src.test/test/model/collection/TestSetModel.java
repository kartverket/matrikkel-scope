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
 * $Id: TestSetModel.java,v 1.7 2002/11/20 00:19:57 ludovicc Exp $
 */
package test.model.collection;


import junit.framework.TestCase;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.model.collection.SetModel;
import test.model.basic.BasicTestModel;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.7 $
 */
public final class TestSetModel extends TestCase implements ModelChangeListener {

    private SetModel setModel;
    private BasicTestModel model, submodel;
    private Set set;

    private boolean modelChanged;


    /**
     * Constructor for the TestSetModel object
     *
     * @param inName Name of the test
     */
    public TestSetModel(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefaultConstructor() throws Exception {
        SetModel m = new SetModel();
        assertTrue(m.getSize() == 0);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBooleanConstructor1() throws Exception {
        SetModel m = new SetModel(false);
        assertTrue(m.getSize() == 0);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBooleanConstructor2() throws Exception {
        SetModel m = new SetModel(true);
        assertTrue(m.getSize() == 0);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetConstructor() throws Exception {
        SetModel m = new SetModel(set);
        assertTrue(m.getSize() == set.size());
        assertTrue(m.containsAll(set));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBooleanSetConstructor1() throws Exception {
        SetModel m = new SetModel(true, set);
        assertTrue(m.getSize() == set.size());
        assertTrue(m.containsAll(set));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBooleanSetConstructor2() throws Exception {
        SetModel m = new SetModel(false, set);
        assertTrue(m.getSize() == set.size());
        assertTrue(m.containsAll(set));
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelSetSet() throws Exception {
        Set<Integer> o = new HashSet();
        o.add(99);
        setModel.addModelChangeListener(this);
        modelChanged = false;

        setModel.setSet(o);
        assertTrue(setModel.getSize() == 1);
        assertTrue(setModel.containsAll(o));
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation1() throws Exception {
        setModel.addModelChangeListener(this);

        modelChanged = false;
        setModel.add("new");
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation2() throws Exception {
        setModel.addModelChangeListener(this);

        modelChanged = false;
        setModel.add(model);
        assertTrue(modelChanged);

        modelChanged = false;
        model.setName("new");
        assertTrue(modelChanged);

        modelChanged = false;
        setModel.remove(model);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation3() throws Exception {
        setModel.addModelChangeListener(this);

        modelChanged = false;
        List l = new LinkedList();
        l.add(model);
        setModel.addAll(l);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation4() throws Exception {
        setModel.addModelChangeListener(this);

        setModel.add(model);
        setModel.add(submodel);

        modelChanged = false;
        List l = new LinkedList();
        l.add(model);
        setModel.retainAll(l);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation5() throws Exception {
        setModel.addModelChangeListener(this);

        setModel.add(model);
        setModel.add(submodel);

        modelChanged = false;
        List l = new LinkedList();
        l.add(model);
        setModel.removeAll(l);
        assertTrue(modelChanged);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetModelChangeEventPropagation6() throws Exception {
        setModel.addModelChangeListener(this);
        setModel.add(model);
        setModel.add(submodel);

        modelChanged = false;
        setModel.clear();
        assertTrue(modelChanged);
    }


    /**
     * TODO: document the method
     *
     * @param inEvent TODO: Describe the Parameter
     */
    public void modelChanged(ModelChangeEvent inEvent) {
        modelChanged = true;
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        setModel = new SetModel();
        for (int i = 0; i < 10; ++i) {
            setModel.add(i);
        }

        model = new BasicTestModel("model");
        submodel = new BasicTestModel("submodel");
        model.setSubModel(submodel);

        set = new HashSet();
        for (int i = 0; i < 10; ++i) {
            set.add("test" + i);
        }
        modelChanged = false;
    }
}
