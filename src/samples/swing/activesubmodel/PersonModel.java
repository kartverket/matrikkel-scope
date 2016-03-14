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
public class PersonModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector NAME = Selector.fromString("name");
    /**
     * TODO: describe of the Field
     */
    public static final Selector PET = Selector.fromString("pet");

    private String name;
    private PetModel pet;


    /**
     * Constructor for the PersonModel object
     */
    public PersonModel() {
        name = "Steve";
        pet = new PetModel();
        listenNewSubmodel(PET);
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
     * Gets the pet
     *
     * @return The pet value
     */
    public PetModel getPet() {
        return pet;
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
     * Sets the pet
     *
     * @param inPet The new pet value
     */
    public void setPet(PetModel inPet) {
        unlistenOldSubmodel(PET);
        pet = inPet;
        listenNewSubmodel(PET);
        fireModelChange(VALUE_CHANGED, PET);
    }
}
