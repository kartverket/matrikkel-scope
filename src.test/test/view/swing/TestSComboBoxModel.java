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
package test.view.swing;


import javax.swing.JComboBox;
import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.*;

/**
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.12 $ $Date: 2002/11/20 00:19:56 $
 * @created 12 September 2002
 */
public class TestSComboBoxModel extends TestCase {

    /**
     * TODO: describe of the Field
     */
    protected SComboBoxModel comboModel;
    /**
     * TODO: describe of the Field
     */
    protected SwingDummyModel model;


    /**
     * Constructor for the TestSComboBoxModel object
     *
     * @param inName Name of the test
     */
    public TestSComboBoxModel(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testUnbound() throws Exception {
        assertNull(comboModel.getBoundModel());
        assertTrue(comboModel.getSize() == 0);
        assertNull(comboModel.getElementAt(0));
        assertNull(comboModel.getBoundSelectionModel());
        assertNull(comboModel.getSelectedItem());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetup() throws Exception {
        Selector s = Selector.fromString("xyz");

        assertNull(comboModel.getSelectionSelector());
        comboModel.setSelectionSelector(s);
        assertSame(s, comboModel.getSelectionSelector());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testSetup2() throws Exception {
        Selector s = Selector.fromString("xyz");

        assertNull(comboModel.getSelectionSelector());
        comboModel.setSelectionSelector("xyz");
        assertEquals(s, comboModel.getSelectionSelector());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBind() throws Exception {
        comboModel.setSelectionSelector("stringProperty");
        comboModel.setSelector("stringNonIndexedProperty");
        comboModel.setBoundModel(model);
        SuiteViewSwing.waitForAWT();

        assertEquals(model.getStringProperty(), comboModel.getSelectedItem());
        comboModel.setSelectedItem(model.getStringNonIndexedProperty(0));
        SuiteViewSwing.waitForAWT();

        assertEquals(model.getStringNonIndexedProperty(0), comboModel.getSelectedItem());
        assertEquals(model.getStringNonIndexedProperty(0), model.getStringProperty());

        model.setStringProperty((String) model.getStringNonIndexedProperty(1));
        SuiteViewSwing.waitForAWT();

        assertEquals(model.getStringNonIndexedProperty(1), comboModel.getSelectedItem());
        assertEquals(model.getStringNonIndexedProperty(1), model.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testBindNoMCE() throws Exception {
        SwingDummyModelNoMCE noMceModel = new SwingDummyModelNoMCE();
        noMceModel.setStringProperty(noMceModel.getStringNonIndexedProperty(0));

        comboModel.setSelectionSelector("stringProperty");
        comboModel.setSelector("stringNonIndexedProperty");
        comboModel.setBoundModel(noMceModel);
        SuiteViewSwing.waitForAWT();

        assertEquals(noMceModel.getStringProperty(), comboModel.getSelectedItem());
        comboModel.setSelectedItem(model.getStringNonIndexedProperty(0));
        SuiteViewSwing.waitForAWT();

        assertEquals(noMceModel.getStringNonIndexedProperty(0), comboModel.getSelectedItem());
        assertEquals(noMceModel.getStringNonIndexedProperty(0), noMceModel.getStringProperty());

        noMceModel.setStringProperty((String) noMceModel.getStringNonIndexedProperty(1));
        SuiteViewSwing.waitForAWT();

        assertEquals(noMceModel.getStringNonIndexedProperty(0), comboModel.getSelectedItem());

        comboModel.refresh();
        SuiteViewSwing.waitForAWT();

        assertEquals(noMceModel.getStringNonIndexedProperty(1), comboModel.getSelectedItem());
        assertEquals(noMceModel.getStringNonIndexedProperty(1), noMceModel.getStringProperty());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testNullModel() throws Exception {
        comboModel.setSelectionSelector("stringProperty");
        comboModel.setSelector("stringNonIndexedProperty");

        SwingDummyModelNoMCE model = new SwingDummyModelNoMCE();

        comboModel.setBoundModel(model);
        SuiteViewSwing.waitForAWT();

        assertNotNull(comboModel.getBoundModel());
        assertNotNull(comboModel.getSelectedItem());

        comboModel.setBoundModel(null);

        assertNull(comboModel.getBoundModel());
        assertNull(comboModel.getSelectedItem());
    }

    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        comboModel = new SComboBoxModel(new JComboBox());
        model = new SwingDummyModel();
        model.setStringProperty(model.getStringNonIndexedProperty(0));
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() { }
}
