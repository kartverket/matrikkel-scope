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
 * $Id: TestBasicModel.java,v 1.5 2002/11/20 00:19:58 ludovicc Exp $
 */
package test.model.basic;

import junit.framework.TestCase;

import org.scopemvc.core.*;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:58 $
 */
public final class TestBasicModel extends TestCase implements ModelChangeListener {

    private BasicTestModel model1;
    private boolean modelChanged;
    private String modelChangedName;


    /**
     * Constructor for the TestBasicModel object
     *
     * @param inName Name of the test
     */
    public TestBasicModel(String inName) {
        super(inName);
    }


    // ------------------------------ model change event -------------------------------

    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSimpleModelChangeEvent() throws Exception {

        model1.addModelChangeListener(this);
        model1.setName("model1x");

        assertTrue(modelChanged);
        assertTrue(modelChangedName.lastIndexOf("name") != -1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSubModelChangeEvent() throws Exception {

        model1.addModelChangeListener(this);
        BasicTestModel model2 = new BasicTestModel("model2");
        model1.setSubModel(model2);

        assertTrue(modelChanged);
        assertTrue(modelChangedName.lastIndexOf("subModel") != -1);

        modelChanged = false;
        modelChangedName = "";
        model2.setName("model2x");

        assertTrue(modelChanged);
        assertTrue(modelChangedName.lastIndexOf("subModel.name") != -1);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testActivation() throws Exception {

        model1.deactivate();

        model1.addModelChangeListener(this);
        BasicTestModel model2 = new BasicTestModel("model2");
        model1.setSubModel(model2);

        assertTrue(!modelChanged);

        model1.activate();

        modelChanged = false;
        modelChangedName = "";
        model2.setName("model2x");

        assertTrue(modelChanged);
        assertTrue(modelChangedName.lastIndexOf("subModel.name") != -1);
    }


    /**
     * TODO: document the method
     *
     * @param inEvent TODO: Describe the Parameter
     */
    public void modelChanged(ModelChangeEvent inEvent) {
        modelChanged = true;
        modelChangedName = inEvent.toString();
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        model1 = new BasicTestModel("model1");
        modelChanged = false;
        modelChangedName = "";
    }
}
