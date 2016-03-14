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
package samples.swing.activesubmodel;


import java.util.List;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;
import org.scopemvc.model.collection.ListModel;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:46 $
 * @created 05 September 2002
 */
public class PetModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector NAME = Selector.fromString("name");
    /**
     * TODO: describe of the Field
     */
    public static final Selector FOOD = Selector.fromString("food");
    /**
     * TODO: describe of the Field
     */
    public static final Selector TOYS = Selector.fromString("toys");

    private String name;
    private String food;
    private List toys;


    /**
     * Constructor for the PetModel object
     */
    public PetModel() {
        name = "Trevor";
        food = "Chicken";
        toys = new ListModel();
        toys.add("Ball");
        toys.add("Fluffy mouse");
        listenNewSubmodel(TOYS);
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
     * Gets the food
     *
     * @return The food value
     */
    public String getFood() {
        return food;
    }


    /**
     * Gets the toys
     *
     * @return The toys value
     */
    public List getToys() {
        return toys;
    }


    /**
     * Sets the name
     *
     * @param inName The new name value
     */
    public void setName(String inName) {
        name = inName;
        fireModelChange(VALUE_CHANGED, NAME);
    }


    /**
     * Sets the food
     *
     * @param inFood The new food value
     */
    public void setFood(String inFood) {
        food = inFood;
        fireModelChange(VALUE_CHANGED, FOOD);
    }


    /**
     * TODO: document the method
     */
    public void clearToys() {
        toys.clear();
    }


    /**
     * Adds an element to the Toy attribute of the PetModel object
     *
     * @param inToy The element to be added to the Toy attribute
     */
    public void addToy(String inToy) {
        toys.add(inToy);
    }


    /**
     * TODO: document the method
     *
     * @param inToy TODO: Describe the Parameter
     */
    public void removeToy(String inToy) {
        if (inToy == null) {
            throw new IllegalArgumentException("null toy");
        }
        toys.remove(inToy);
    }
}
