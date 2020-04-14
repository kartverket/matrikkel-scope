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
 * $Id: SwingDummyModelNoMCE.java,v 1.8 2002/09/12 18:26:54 ludovicc Exp $
 */
package test.view.swing;


import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <P>
 *
 * A model for testing that doesn't fire ModelChangeEvents (MCE) </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 12 September 2002
 * @version $Revision: 1.8 $ $Date: 2002/09/12 18:26:54 $
 */
public final class SwingDummyModelNoMCE {

    private static final Log LOG = LogFactory.getLog(SwingDummyModelNoMCE.class);

    private String stringProperty = "sp";
    private String readOnlyStringProperty = "rosp";
    private boolean booleanProperty = false;
    private int intProperty = 1;

    private String[] stringIndexedProperty = {"sip0", "sip1"};
    private ArrayList stringNonIndexedProperty = new ArrayList();

    private SwingDummyModelNoMCE subModel;

    private ArrayList subModels;


    /**
     * Constructor for the SwingDummyModelNoMCE object
     */
    public SwingDummyModelNoMCE() {
        stringNonIndexedProperty.add("snip0");
        stringNonIndexedProperty.add("snip1");
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
     * Gets the string property
     *
     * @return The stringProperty value
     */
    public String getStringProperty() {
        return stringProperty;
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
     * Gets the boolean read only property
     *
     * @return The booleanReadOnlyProperty value
     */
    public boolean getBooleanReadOnlyProperty() {
        return true;
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
    public SwingDummyModelNoMCE getSubModel() {
        return subModel;
    }


    /**
     * Gets the sub models
     *
     * @return The subModels value
     */
    public ArrayList getSubModels() {
        return subModels;
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
     * Sets the string property
     *
     * @param inString The new stringProperty value
     */
    public void setStringProperty(String inString) {
        stringProperty = inString;
    }


    /**
     * Sets the boolean property
     *
     * @param inValue The new booleanProperty value
     */
    public void setBooleanProperty(boolean inValue) {
        booleanProperty = inValue;
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
     * Sets the string non indexed property
     *
     * @param inList The new stringNonIndexedProperty value
     */
    public void setStringNonIndexedProperty(ArrayList inList) {
        stringNonIndexedProperty = inList;
    }


    /**
     * Sets the sub model
     *
     * @param inModel The new subModel value
     */
    public void setSubModel(SwingDummyModelNoMCE inModel) {
        subModel = inModel;
    }


    /**
     * Sets the sub models
     *
     * @param inList The new subModels value
     */
    public void setSubModels(ArrayList inList) {
        subModels = inList;
    }


    /**
     * TODO: document the method
     */
    public void initSubModels() {
        subModels = new ArrayList();
        subModels.add(new SwingDummyModel("a", 1));
        subModels.add(new SwingDummyModel("b", 2));
        subModels.add(new SwingDummyModel("c", 3));
    }
}

