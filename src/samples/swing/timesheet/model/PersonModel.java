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

import java.util.List;
import org.scopemvc.core.ControlException;

import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;
import org.scopemvc.model.collection.ListModel;

/**
 * <p>
 *
 * Simple Person model which allows them to enter a name</p>
 *
 * @author <a href="mailto:steve.jones@netdecisions.co.uk>Steve Jones</a>
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:50 $
 * @created 05 September 2002
 * @since Scope0.8 v1.0
 */
public class PersonModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector NAME = Selector.fromString("name");
    /**
     * TODO: describe of the Field
     */
    public static final Selector PROJECTS = Selector.fromString("projects");
    /**
     * TODO: describe of the Field
     */
    public static final Selector TIMESHEET = Selector.fromString("timesheet");

    private String name;
    private List projects;
    private TimesheetModel timesheet;


    /**
     * Constructor for the PersonModel object
     */
    public PersonModel() {
        name = "Steve";
        projects = new ListModel();
        timesheet = new TimesheetModel();

        listenNewSubmodel(PROJECTS);
        listenNewSubmodel(TIMESHEET);
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
     * Gets the projects
     *
     * @return The projects value
     */
    public List getProjects() {
        return projects;
    }

    /**
     * Gets the timesheet
     *
     * @return The timesheet value
     */
    public TimesheetModel getTimesheet() {
        return timesheet;
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
     * TODO: document the method
     */
    public void clearProjects() {
        projects.clear();
    }


    /**
     * Adds an element to the Project attribute of the PersonModel object
     *
     * @param inProject The element to be added to the Project attribute
     * @throws ControlException TODO: Describe the Exception
     */
    public void addProject(String inProject) throws ControlException {
        // check that it isn't already in there
        if (projects.contains(inProject)) {
            throw new ControlException(inProject + " already exists");
        } else if (inProject == null || inProject.trim().length() == 0) {
            throw new ControlException("A project must have a name");
        }
        projects.add(inProject);
    }


    /**
     * TODO: document the method
     *
     * @param inProject TODO: Describe the Parameter
     */
    public void removeProject(String inProject) {
        if (inProject == null) {
            throw new IllegalArgumentException("null project");
        }
        projects.remove(inProject);
    }

}
