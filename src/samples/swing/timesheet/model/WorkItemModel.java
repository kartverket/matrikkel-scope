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
import org.scopemvc.util.DateTime;
import org.scopemvc.util.Time;

/**
 * <p>
 *
 * A single item of work.</p>
 *
 * @author <a href="mailto:steve.jones@netdecisions.co.uk>Steve Jones</a>
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:50 $
 * @created 05 September 2002
 * @since Scope v0.8
 */
public class WorkItemModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector PROJECT = Selector.fromString("project");
    /**
     * TODO: describe of the Field
     */
    public static final Selector START = Selector.fromString("start");
    /**
     * TODO: describe of the Field
     */
    public static final Selector DURATION = Selector.fromString("duration");

    private String project;
    private DateTime start;
    private Time duration;

    /**
     * Constructor for the WorkItemModel object
     *
     * @param project TODO: Describe the Parameter
     * @param start TODO: Describe the Parameter
     * @param duration TODO: Describe the Parameter
     */
    public WorkItemModel(String project, DateTime start, Time duration) {
        this.project = project;
        this.start = start;
        this.duration = duration;
    }

    /**
     * Gets the project
     *
     * @return The project value
     */
    public String getProject() {
        return project;
    }

    /**
     * Gets the start
     *
     * @return The start value
     */
    public DateTime getStart() {
        return start;
    }

    /**
     * Gets the duration
     *
     * @return The duration value
     */
    public Time getDuration() {
        return duration;
    }

    /**
     * Sets the project
     *
     * @param project The new project value
     */
    public void setProject(String project) {
        this.project = project;
        fireModelChange(VALUE_CHANGED, PROJECT);
    }

    /**
     * Sets the start
     *
     * @param start The new start value
     */
    public void setStart(DateTime start) {
        this.start = start;
        fireModelChange(VALUE_CHANGED, START);

    }

    /**
     * Sets the duration
     *
     * @param duration The new duration value
     */
    public void setDuration(Time duration) {
        this.duration = duration;
        fireModelChange(VALUE_CHANGED, DURATION);
    }

    /**
     * Increase the duration of the work item
     */
    public void incrementDuration() {
        this.duration.setTime(System.currentTimeMillis() - this.start.getTime());
        fireModelChange(VALUE_CHANGED, DURATION);
    }

}
