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


import java.util.ArrayList;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:50 $
 * @created 05 September 2002
 */
public final class ModelObject {

    // --------------------- Some actions -----------------------

    boolean doneAction1;
    int action2;

    private String stringProperty = "sp";
    private int intProperty = 1;
    private String[] stringIndexedProperty = {"sip0", "sip1"};
    private String[] hiddenStringIndexedProperty = {"hsip0", "hsip1"};
    private ArrayList stringNonIndexedProperty = new ArrayList();
    private String readOnlyStringProperty = "rosp";
    private String[] stringNonIndexedProperty2 = new String[2];

    private ModelObject subModel;

    /**
     * Constructor for the ModelObject object
     */
    public ModelObject() {
        stringNonIndexedProperty.add("snip0");
        stringNonIndexedProperty.add("snip1");
        stringNonIndexedProperty2[0] = "snip20";
        stringNonIndexedProperty2[1] = "snip21";
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
     * Gets the string non indexed property
     *
     * @return The stringNonIndexedProperty value
     */
    public ArrayList getStringNonIndexedProperty() {
        return stringNonIndexedProperty;
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
    public ModelObject getSubModel() {
        return subModel;
    }


    /**
     * Sets the string property
     *
     * @param inString The new stringProperty value
     */
    public void setStringProperty(String inString) {
        stringProperty = inString;
    }


    /**
     * Sets the int property
     *
     * @param inInt The new intProperty value
     */
    public void setIntProperty(int inInt) {
        intProperty = inInt;
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
    public void setSubModel(ModelObject inModel) {
        subModel = inModel;
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

