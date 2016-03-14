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
package samples.swing.timesheet.model;

import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;

/**
 * <p>
 *
 * The view model, this contains a person, and an additional project to be added
 * element.</p>
 *
 * @author <a href="mailto:steve.jones@netdecisions.co.uk>Steve Jones</a>
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:50 $
 * @created 05 September 2002
 * @since Scope v0.8
 */
public class PersonViewModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector PERSON = Selector.fromString("person");
    /**
     * TODO: describe of the Field
     */
    public static final Selector NEW_PROJECT = Selector.fromString("newProject");

    private PersonModel person;
    private String newProject;

    /**
     * Constructor, creates the person for this model.
     */
    public PersonViewModel() {
        person = new PersonModel();
        this.listenNewSubmodel(PERSON);
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
     * Gets the new project
     *
     * @return The newProject value
     */
    public String getNewProject() {
        return newProject;
    }

    /**
     * Sets the new project
     *
     * @param newProject The new newProject value
     */
    public void setNewProject(String newProject) {
        this.newProject = newProject;
        this.fireModelChange(VALUE_CHANGED, NEW_PROJECT);
    }
}
