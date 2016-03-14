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
package samples.swing.combobox;

import java.util.*;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;

/**
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:47 $
 * @created 05 September 2002
 */
public class DateModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector CURRENT_YEAR = Selector.fromString("currentYear");
    /**
     * TODO: describe of the Field
     */
    public static final Selector CURRENT_MONTH = Selector.fromString("currentMonth");
    /**
     * TODO: describe of the Field
     */
    public static final Selector CURRENT_DAY = Selector.fromString("currentDay");
    /**
     * TODO: describe of the Field
     */
    public static final Selector DAYS = Selector.fromString("days");
    /**
     * TODO: describe of the Field
     */
    public static final Selector YEARS = Selector.fromString("years");

    private Calendar currentDate = GregorianCalendar.getInstance();
    private Object[] years;
    private Object[] months;
    private Object[] days;


    /**
     * Constructor for the DateModel object
     */
    public DateModel() {
        months = new Object[12];
        for (int i = 0; i < 12; i++) {
            months[i] = new Integer(i + 1);
        }
        updateModels();
    }


    /**
     * Returns interval of +-2 years from now.
     *
     * @return The years value
     */
    public Object[] getYears() {
        return years;
    }


    /**
     * Gets the months
     *
     * @return The months value
     */
    public Object[] getMonths() {
        return months;
    }


    /**
     * Gets the days
     *
     * @return The days value
     */
    public Object[] getDays() {
        return days;
    }


    /**
     * Gets the current year
     *
     * @return The currentYear value
     */
    public int getCurrentYear() {
        return currentDate.get(Calendar.YEAR);
    }


    /**
     * Gets the current month
     *
     * @return The currentMonth value
     */
    public int getCurrentMonth() {
        return currentDate.get(Calendar.MONTH) + 1;
    }


    /**
     * Gets the current day
     *
     * @return The currentDay value
     */
    public int getCurrentDay() {
        return currentDate.get(Calendar.DATE);
    }


    /**
     * Sets the current year
     *
     * @param n The new currentYear value
     */
    public void setCurrentYear(int n) {
        currentDate.set(Calendar.YEAR, n);
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, CURRENT_YEAR);
        updateModels();
    }


    /**
     * Sets the current month
     *
     * @param n The new currentMonth value
     */
    public void setCurrentMonth(int n) {
        currentDate.set(Calendar.MONTH, n - 1);
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, CURRENT_MONTH);
        updateModels();
    }


    /**
     * Sets the current day
     *
     * @param n The new currentDay value
     */
    public void setCurrentDay(int n) {
        currentDate.set(Calendar.DATE, n);
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, CURRENT_DAY);
        updateModels();
    }


    /**
     * Builds lists of choices for years and days based on current date
     */
    private void updateModels() {
        years = new Object[5];
        int n = getCurrentYear() - 2;
        for (int i = 0; i < 5; i++) {
            years[i] = new Integer(n + i);
        }
        n = currentDate.getActualMaximum(Calendar.DATE);
        days = new Object[n];
        for (int i = 0; i < n; i++) {
            days[i] = new Integer(i + 1);
        }
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, YEARS);
        fireModelChange(ModelChangeEvent.VALUE_CHANGED, DAYS);
    }
}
