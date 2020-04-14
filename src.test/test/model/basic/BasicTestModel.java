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
 * $Id: BasicTestModel.java,v 1.5 2002/09/05 15:41:46 ludovicc Exp $
 */
package test.model.basic;


import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;
import org.scopemvc.model.beans.DynamicReadOnly;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/09/05 15:41:46 $
 */
public class BasicTestModel extends BasicModel implements DynamicReadOnly {

    /**
     * TODO: describe of the Field
     */
    public static Selector NAME = Selector.fromString("name");
    /**
     * TODO: describe of the Field
     */
    public static Selector SUB_MODEL = Selector.fromString("subModel");
    /**
     * TODO: describe of the Field
     */
    public static Selector INT_PROPERTY = Selector.fromString("intProperty");
    /**
     * TODO: describe of the Field
     */
    public static Selector LONG_PROPERTY = Selector.fromString("longProperty");
    /**
     * TODO: describe of the Field
     */
    public static Selector BOOLEAN_PROPERTY = Selector.fromString("booleanProperty");
    /**
     * TODO: describe of the Field
     */
    public static Selector READ_ONLY_PROPERTY = Selector.fromString("readOnlyProperty");

    private String name;
    private BasicTestModel subModel;
    private int intProperty;
    private long longProperty;
    private boolean booleanProperty;

    private boolean nameReadOnly;


    /**
     * Constructor for the BasicTestModel object
     *
     * @param inName TODO: Describe the Parameter
     */
    public BasicTestModel(String inName) {
        setName(inName);
    }


    /**
     * Gets the name
     *
     * @return The name value
     */
    public String getName() {
        return name;
    }


    /**
     * Gets the sub model
     *
     * @return The subModel value
     */
    public BasicTestModel getSubModel() {
        return subModel;
    }


    /**
     * Gets the property read only
     *
     * @param inSelector TODO: Describe the Parameter
     * @return The propertyReadOnly value
     */
    public boolean isPropertyReadOnly(Selector inSelector) {
        if (inSelector.equals(NAME) && nameReadOnly) {
            return true;
        }
        return false;
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
     * Gets the long property
     *
     * @return The longProperty value
     */
    public long getLongProperty() {
        return longProperty;
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
     * Gets the read only property
     *
     * @return The readOnlyProperty value
     */
    public int getReadOnlyProperty() {
        return 0;
    }


    /**
     * Sets the name
     *
     * @param inName The new name value
     */
    public void setName(String inName) {
        name = inName;
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, NAME);
    }


    /**
     * Sets the sub model
     *
     * @param inSubModel The new subModel value
     */
    public void setSubModel(BasicTestModel inSubModel) {
        unlistenOldSubmodel(SUB_MODEL);
        subModel = inSubModel;
        listenNewSubmodel(SUB_MODEL);
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, SUB_MODEL);
    }


    /**
     * Sets the int property
     *
     * @param inValue The new intProperty value
     */
    public void setIntProperty(int inValue) {
        intProperty = inValue;
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, INT_PROPERTY);
    }


    /**
     * Sets the long property
     *
     * @param inValue The new longProperty value
     */
    public void setLongProperty(long inValue) {
        longProperty = inValue;
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, LONG_PROPERTY);
    }


    /**
     * Sets the boolean property
     *
     * @param inValue The new booleanProperty value
     */
    public void setBooleanProperty(boolean inValue) {
        booleanProperty = inValue;
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, BOOLEAN_PROPERTY);
    }


    /**
     * TODO: document the method
     */
    public void activate() {
        makeActive(true);
    }


    /**
     * TODO: document the method
     */
    public void deactivate() {
        makeActive(false);
    }


    /**
     * TODO: document the method
     *
     * @param inReadOnly TODO: Describe the Parameter
     */
    public void makeNameReadOnly(boolean inReadOnly) {
        nameReadOnly = inReadOnly;
        fireModelChange(ModelChangeEvent.ACCESS_CHANGED, NAME);
    }


    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public String toString() {
        return "BasicTestModel name(" + name + ")";
    }
}

