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


import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:46 $
 * @created 05 September 2002
 */
public class ActivesubmodelModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector PERSON = Selector.fromString("person");
    /**
     * TODO: describe of the Field
     */
    public static final Selector NEW_TOY = Selector.fromString("newToy");
    /**
     * TODO: describe of the Field
     */
    public static final Selector SELECTED_TOY = Selector.fromString("selectedToy");

    private PersonModel person;
    private String newToy;
    private String selectedToy;


    /**
     * Constructor for the ActivesubmodelModel object
     */
    public ActivesubmodelModel() {
        person = new PersonModel();
        listenNewSubmodel(PERSON);
    }


    /**
     * Gets the person
     *
     * @return The person value
     */
    public PersonModel getPerson() {
        return person;
    }


    /**
     * Gets the new toy
     *
     * @return The newToy value
     */
    public String getNewToy() {
        return newToy;
    }


    /**
     * Gets the selected toy
     *
     * @return The selectedToy value
     */
    public String getSelectedToy() {
        return selectedToy;
    }


    /**
     * Sets the new toy
     *
     * @param inNewToy The new newToy value
     */
    public void setNewToy(String inNewToy) {
        newToy = inNewToy;
        fireModelChange(VALUE_CHANGED, NEW_TOY);
    }


    /**
     * Sets the selected toy
     *
     * @param inSelectedToy The new selectedToy value
     */
    public void setSelectedToy(String inSelectedToy) {
        selectedToy = inSelectedToy;
        fireModelChange(VALUE_CHANGED, SELECTED_TOY);
    }


    /**
     * Adds an element to the Toy attribute of the ActivesubmodelModel object
     */
    public void addToy() {
        if (getNewToy() == null) {
            throw new IllegalArgumentException("null newToy");
        }
        getPerson().getPet().addToy(getNewToy());
    }


    /**
     * TODO: document the method
     */
    public void removeToy() {
        getPerson().getPet().removeToy(selectedToy);
    }


    /**
     * TODO: document the method
     */
    public void clearToys() {
        getPerson().getPet().clearToys();
    }
}
