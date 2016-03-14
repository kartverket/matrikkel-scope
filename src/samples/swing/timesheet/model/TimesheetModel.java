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

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;
import org.scopemvc.model.collection.ListModel;
import org.scopemvc.util.DateTime;
import org.scopemvc.util.Time;

/**
 * <p>
 *
 * </p>
 *
 * @author <a href="mailto:steve.jones@netdecisions.co.uk>Steve Jones</a>
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:50 $
 * @created 05 September 2002
 * @since Scope v0.8
 */
public class TimesheetModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector WORK_ITEMS = Selector.fromString("workItems");
    /**
     * TODO: describe of the Field
     */
    public static final Selector TOTAL_WORK = Selector.fromString("totalWork");
    /**
     * TODO: describe of the Field
     */
    public static final Selector CURRENT_PROJECT = Selector.fromString("currentProject");
    /**
     * TODO: describe of the Field
     */
    public static final Selector CURRENT_ITEM = Selector.fromString("currentItem");
    /**
     * TODO: describe of the Field
     */
    public static final Selector PROJECT_TOTAL = Selector.fromString("projectTotal");

    private ListModel workItems;
    private Time totalWork;
    private String currentProject;
    private Time projectTotal;
    private WorkItemModel currentItem;

    /**
     * The timer stuff
     */
    private Timer timer;


    /**
     * Constructor for the TimesheetModel object
     *
     * @param workItems TODO: Describe the Parameter
     * @param totalWork TODO: Describe the Parameter
     * @param currentProject TODO: Describe the Parameter
     * @param projectTotal TODO: Describe the Parameter
     */
    public TimesheetModel(ListModel workItems, Time totalWork, String currentProject, Time projectTotal) {
        this.workItems = workItems;
        this.totalWork = totalWork;
        this.currentProject = currentProject;
        this.projectTotal = projectTotal;
        this.listenNewSubmodel(WORK_ITEMS);
    }

    /**
     * Constructor for the TimesheetModel object
     */
    public TimesheetModel() {
        this(new ListModel(), new Time(0), null, new Time(0));
    }

    /**
     * Gets the work items
     *
     * @return The workItems value
     */
    public ListModel getWorkItems() {
        return workItems;
    }


    /**
     * Gets the total work
     *
     * @return The totalWork value
     */
    public Time getTotalWork() {
        return totalWork;
    }

    /**
     * Gets the current project
     *
     * @return The currentProject value
     */
    public String getCurrentProject() {
        return currentProject;
    }

    /**
     * Gets the project total
     *
     * @return The projectTotal value
     */
    public Time getProjectTotal() {
        return projectTotal;
    }

    /**
     * Sets the work items
     *
     * @param workItems The new workItems value
     */
    public void setWorkItems(ListModel workItems) {
        this.unlistenOldSubmodel(WORK_ITEMS);
        this.workItems = workItems;
        this.listenNewSubmodel(WORK_ITEMS);
        this.fireModelChange(VALUE_CHANGED, WORK_ITEMS);
    }

    /**
     * Sets the total work
     *
     * @param totalWork The new totalWork value
     */
    public void setTotalWork(Time totalWork) {
        this.totalWork = totalWork;
        this.fireModelChange(VALUE_CHANGED, TOTAL_WORK);
    }

    /**
     * Sets the current project
     *
     * @param currentProject The new currentProject value
     */
    public void setCurrentProject(String currentProject) {
        this.currentProject = currentProject;
        if (currentProject != null) {
            // also need to create a new work item
            WorkItemModel newItem = new WorkItemModel(currentProject, new DateTime(), new Time(0));
            workItems.add(newItem);
            this.currentItem = newItem;
        } else {
            currentItem = null;
        }
        this.runTimer();
        this.fireModelChange(VALUE_CHANGED, CURRENT_PROJECT);
        recalculate();
    }

    /**
     * Sets the project total
     *
     * @param projectTotal The new projectTotal value
     */
    public void setProjectTotal(Time projectTotal) {
        this.projectTotal = projectTotal;
        this.fireModelChange(VALUE_CHANGED, PROJECT_TOTAL);
    }

    /**
     * <p>
     *
     * Overide of the model change event, this is done so additional processing
     * can be done on the model change when the work items changes. </p>
     *
     * @param inEvent the event received from a child ModelChangeEventSource.
     */
    public void modelChanged(ModelChangeEvent inEvent) {
        recalculate();

        super.modelChanged(inEvent);
    }

    /**
     * Recalculate the information in the timesheet reporting fields.
     */
    private void recalculate() {
        Time newProjectTotal = new Time(0);
        Time newTotalWork = new Time(0);

        for (Iterator iterator = workItems.iterator(); iterator.hasNext(); ) {
            WorkItemModel workItemModel = (WorkItemModel) iterator.next();
            newTotalWork.setTime(newTotalWork.getTime() + workItemModel.getDuration().getTime());
            if (workItemModel.getProject().equals(this.getCurrentProject())) {
                newProjectTotal.setTime(newProjectTotal.getTime() + workItemModel.getDuration().getTime());
            }
        }
        this.setProjectTotal(newProjectTotal);
        this.setTotalWork(newTotalWork);
    }

    /**
     * Start the timer if not already started
     */
    private void runTimer() {
        if (timer == null) {
            timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimesheetTimerTask(), 1000, 1000);
        }
    }

    /**
     * Inner class to handle the timer task stuff
     *
     * @author lclaude
     * @version $Revision: 1.4 $
     * @created 05 September 2002
     */
    class TimesheetTimerTask extends TimerTask {
        /**
         * Main processing method for the TimesheetTimerTask object
         */
        public void run() {
            if (currentItem != null) {
                currentItem.incrementDuration();
            }
        }
    }
}
