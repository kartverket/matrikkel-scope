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
 * $Id: SwingDummyModel.java,v 1.11 2002/09/12 18:26:54 ludovicc Exp $
 */
package test.view.swing;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;
import org.scopemvc.model.collection.ListModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 10 September 2002
 * @version $Revision: 1.11 $ $Date: 2002/09/12 18:26:54 $
 */
public final class SwingDummyModel extends BasicModel {

    private static final Log LOG = LogFactory.getLog(SwingDummyModel.class);

    private static final ImageIcon ICON = new ImageIcon();

    // --------------------- Some actions -----------------------

    boolean doneAction1;
    int action2;

    private String stringProperty = "sp";
    private String readOnlyStringProperty = "rosp";
    private int intProperty = 1;
    private Integer intProperty2 = 1;
    private int invalidIntProperty = 0;
    private boolean booleanProperty = false;
    private Boolean booleanProperty1 = Boolean.FALSE;

    private String[] stringIndexedProperty = {"sip0", "sip1"};
    private String[] hiddenStringIndexedProperty = {"hsip0", "hsip1"};
    private ArrayList stringNonIndexedProperty = new ArrayList();
    private String[] stringNonIndexedProperty2 = new String[2];

    private HashSet selections;

    private SwingDummyModel subModel;

    private ListModel subModels;

    /**
     * Constructor for the SwingDummyModel object
     */
    public SwingDummyModel() {
        stringNonIndexedProperty.add("snip0");
        stringNonIndexedProperty.add("snip1");
        stringNonIndexedProperty2[0] = "snip20";
        stringNonIndexedProperty2[1] = "snip21";
    }


    /**
     * Constructor for the SwingDummyModel object
     *
     * @param inString TODO: Describe the Parameter
     * @param inInt TODO: Describe the Parameter
     */
    public SwingDummyModel(String inString, int inInt) {
        this();
        setStringProperty(inString);
        setIntProperty(inInt);
    }


    /**
     * Gets the string property
     *
     * @return The stringProperty value
     */
    public String getStringProperty() {
        return stringProperty;
    }


    /**
     * Gets the int property
     *
     * @return The intProperty value
     */
    public int getIntProperty() {
        return intProperty;
    }


    /**
     * Gets the int property2
     *
     * @return The intProperty2 value
     */
    public Integer getIntProperty2() {
        return intProperty2;
    }


    /**
     * Gets the invalid int property
     *
     * @return The invalidIntProperty value
     */
    public int getInvalidIntProperty() {
        return invalidIntProperty;
    }


    /**
     * Gets the read only int property
     *
     * @return The readOnlyIntProperty value
     */
    public int getReadOnlyIntProperty() {
        return 70;
    }


    /**
     * Gets the boolean property
     *
     * @return The booleanProperty value
     */
    public boolean getBooleanProperty() {
        return booleanProperty;
    }


    /**
     * Gets the boolean property1
     *
     * @return The booleanProperty1 value
     */
    public Boolean getBooleanProperty1() {
        return booleanProperty1;
    }


    /**
     * Gets the boolean read only property
     *
     * @return The booleanReadOnlyProperty value
     */
    public boolean getBooleanReadOnlyProperty() {
        return true;
    }


    /**
     * Gets the invalid boolean property
     *
     * @return The invalidBooleanProperty value
     */
    public boolean getInvalidBooleanProperty() {
        return false;
    }


    /**
     * Gets the string indexed property
     *
     * @return The stringIndexedProperty value
     */
    public String[] getStringIndexedProperty() {
        return stringIndexedProperty;
    }


    /**
     * Gets the string indexed property
     *
     * @param inIndex TODO: Describe the Parameter
     * @return The stringIndexedProperty value
     */
    public String getStringIndexedProperty(int inIndex) {
        return stringIndexedProperty[inIndex];
    }


    /**
     * Gets the hidden string indexed property
     *
     * @param inIndex TODO: Describe the Parameter
     * @return The hiddenStringIndexedProperty value
     */
    public String getHiddenStringIndexedProperty(int inIndex) {
        return hiddenStringIndexedProperty[inIndex];
    }


    /**
     * Gets the hidden string indexed property size
     *
     * @return The hiddenStringIndexedPropertySize value
     */
    public int getHiddenStringIndexedPropertySize() {
        return hiddenStringIndexedProperty.length;
    }


    /**
     * Gets the string non indexed property
     *
     * @return The stringNonIndexedProperty value
     */
    public ArrayList getStringNonIndexedProperty() {
        return stringNonIndexedProperty;
    }

    /**
     * Gets the string non indexed property
     *
     * @param inIndex TODO: Describe the Parameter
     * @return The stringNonIndexedProperty value
     */
    public String getStringNonIndexedProperty(int inIndex) {
        return (String) getStringNonIndexedProperty().get(inIndex);
    }


    /**
     * Gets the string non indexed property2
     *
     * @return The stringNonIndexedProperty2 value
     */
    public String[] getStringNonIndexedProperty2() {
        return stringNonIndexedProperty2;
    }


    /**
     * Gets the read only string property
     *
     * @return The readOnlyStringProperty value
     */
    public String getReadOnlyStringProperty() {
        return readOnlyStringProperty;
    }


    /**
     * Gets the sub model
     *
     * @return The subModel value
     */
    public SwingDummyModel getSubModel() {
        return subModel;
    }

    /**
     * Gets the icon property
     *
     * @return The iconProperty value
     */
    public ImageIcon getIconProperty() {
        return ICON;
    }


    /**
     * Gets the selections
     *
     * @return The selections value
     */
    public HashSet getSelections() {
        return selections;
    }


    /**
     * Gets the sub models
     *
     * @return The subModels value
     */
    public ListModel getSubModels() {
        return subModels;
    }


    /**
     * Sets the string property
     *
     * @param inString The new stringProperty value
     */
    public void setStringProperty(String inString) {
        if ("Illegal".equals(inString)) {
            throw new IllegalArgumentException();
        }
        stringProperty = inString;
        fireModelChange(VALUE_CHANGED, Selector.fromString("stringProperty"));
    }


    /**
     * Sets the int property
     *
     * @param inInt The new intProperty value
     */
    public void setIntProperty(int inInt) {
        intProperty = inInt;
        fireModelChange(VALUE_CHANGED, Selector.fromString("intProperty"));
    }


    /**
     * Sets the int property2
     *
     * @param inInt The new intProperty2 value
     */
    public void setIntProperty2(Integer inInt) {
        intProperty2 = inInt;
        fireModelChange(VALUE_CHANGED, Selector.fromString("intProperty2"));
    }


    /**
     * Sets the invalid int property
     *
     * @param inInt The new invalidIntProperty value
     */
    public void setInvalidIntProperty(int inInt) {
        if (inInt > 50) {
            throw new IllegalArgumentException();
        }
        invalidIntProperty = inInt;
        fireModelChange(VALUE_CHANGED, Selector.fromString("invalidIntProperty"));
    }


    /**
     * Sets the boolean property
     *
     * @param inValue The new booleanProperty value
     */
    public void setBooleanProperty(boolean inValue) {
        booleanProperty = inValue;
        fireModelChange(VALUE_CHANGED, Selector.fromString("booleanProperty"));
    }


    /**
     * Sets the boolean property1
     *
     * @param inValue The new booleanProperty1 value
     */
    public void setBooleanProperty1(Boolean inValue) {
        booleanProperty1 = inValue;
        fireModelChange(VALUE_CHANGED, Selector.fromString("booleanProperty1"));
    }


    /**
     * Sets the invalid boolean property
     *
     * @param inValue The new invalidBooleanProperty value
     */
    public void setInvalidBooleanProperty(boolean inValue) {
        if (inValue) {
            throw new IllegalArgumentException();
        }
        fireModelChange(VALUE_CHANGED, Selector.fromString("invalidBooleanProperty"));
    }


    /**
     * Sets the string indexed property
     *
     * @param inStrings The new stringIndexedProperty value
     */
    public void setStringIndexedProperty(String[] inStrings) {
        stringIndexedProperty = inStrings;
    }


    /**
     * Sets the string indexed property
     *
     * @param inIndex The new stringIndexedProperty value
     * @param inString The new stringIndexedProperty value
     */
    public void setStringIndexedProperty(int inIndex, String inString) {
        stringIndexedProperty[inIndex] = inString;
    }


    /**
     * Sets the hidden string indexed property
     *
     * @param inIndex The new hiddenStringIndexedProperty value
     * @param inString The new hiddenStringIndexedProperty value
     */
    public void setHiddenStringIndexedProperty(int inIndex, String inString) {
        hiddenStringIndexedProperty[inIndex] = inString;
    }

    /**
     * Sets the string non indexed property
     *
     * @param inList The new stringNonIndexedProperty value
     */
    public void setStringNonIndexedProperty(ArrayList inList) {
        stringNonIndexedProperty = inList;
        fireModelChange(VALUE_CHANGED, Selector.fromString("stringNonIndexedProperty"));
    }


    /**
     * Sets the string non indexed property2
     *
     * @param inArray The new stringNonIndexedProperty2 value
     */
    public void setStringNonIndexedProperty2(String[] inArray) {
        stringNonIndexedProperty2 = inArray;
    }


    /**
     * Sets the sub model
     *
     * @param inModel The new subModel value
     */
    public void setSubModel(SwingDummyModel inModel) {
        unlistenOldSubmodel(Selector.fromString("subModel"));
        subModel = inModel;
        listenNewSubmodel(Selector.fromString("subModel"));
        fireModelChange(VALUE_CHANGED, Selector.fromString("subModel"));
    }


    /**
     * Sets the selections
     *
     * @param inHash The new selections value
     */
    public void setSelections(HashSet inHash) {
        selections = inHash;
        fireModelChange(VALUE_CHANGED, Selector.fromString("selections"));
    }


    /**
     * Sets the sub models
     *
     * @param inList The new subModels value
     */
    public void setSubModels(ListModel inList) {
        subModels = inList;
        fireModelChange(VALUE_CHANGED, Selector.fromString("subModels"));
    }


    /**
     * TODO: document the method
     */
    public void initSubModels() {
        subModels = new ListModel();
        subModels.add(new SwingDummyModel("a", 1));
        subModels.add(new SwingDummyModel("b", 2));
        subModels.add(new SwingDummyModel("c", 3));
    }


    /**
     * TODO: document the method
     */
    public void action1() {
        doneAction1 = true;
    }


    /**
     * TODO: document the method
     *
     * @param inParameter TODO: Describe the Parameter
     */
    public void action2(int inParameter) {
        action2 = inParameter;
    }


    /**
     * TODO: document the method
     *
     * @param inParameter TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public long action3(long inParameter) {
        return inParameter;
    }
}

