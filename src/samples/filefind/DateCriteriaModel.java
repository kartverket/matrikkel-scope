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
package samples.filefind;


import java.util.Calendar;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;

/**
 * Part of the SearchViewModel.
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:19:58 $
 * @created 18 September 2002
 */
public class DateCriteriaModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector DATE_FROM =
            Selector.fromString("dateFrom");
    /**
     * TODO: describe of the Field
     */
    public static final Selector DATE_TO =
            Selector.fromString("dateTo");
    /**
     * TODO: describe of the Field
     */
    public static final Selector LAST_DAYS =
            Selector.fromString("lastDays");
    /**
     * TODO: describe of the Field
     */
    public static final Selector LAST_MONTHS =
            Selector.fromString("lastMonths");
    /**
     * TODO: describe of the Field
     */
    public static final Selector LAST_DAYS_ENABLED =
            Selector.fromString("lastDaysEnabled");
    /**
     * TODO: describe of the Field
     */
    public static final Selector LAST_MONTHS_ENABLED =
            Selector.fromString("lastMonthsEnabled");
    /**
     * TODO: describe of the Field
     */
    public static final Selector INTERVAL_ENABLED =
            Selector.fromString("intervalEnabled");

    private static final Log LOG = LogFactory.getLog(DateCriteriaModel.class);

    private Date dateFrom;
    private Date dateTo;
    private int lastDays = 1;
    private int lastMonths = 1;
    private boolean lastDaysEnabled;
    private boolean lastMonthsEnabled = true;
    private boolean intervalEnabled;


    /**
     * Constructor for the DateCriteriaModel object
     */
    DateCriteriaModel() {
        Calendar c = Calendar.getInstance();
        dateTo = c.getTime();
        c.add(Calendar.MONTH, -1);
        dateFrom = c.getTime();
    }


    /**
     * Gets the date from
     *
     * @return The dateFrom value
     */
    public Date getDateFrom() {
        return dateFrom;
    }


    /**
     * Gets the date to
     *
     * @return The dateTo value
     */
    public Date getDateTo() {
        return dateTo;
    }


    /**
     * Gets the last days
     *
     * @return The lastDays value
     */
    public int getLastDays() {
        return lastDays;
    }


    /**
     * Gets the last months
     *
     * @return The lastMonths value
     */
    public int getLastMonths() {
        return lastMonths;
    }


    /**
     * Gets the last days enabled
     *
     * @return The lastDaysEnabled value
     */
    public boolean isLastDaysEnabled() {
        return lastDaysEnabled;
    }


    /**
     * Gets the last months enabled
     *
     * @return The lastMonthsEnabled value
     */
    public boolean isLastMonthsEnabled() {
        return lastMonthsEnabled;
    }


    /**
     * Gets the interval enabled
     *
     * @return The intervalEnabled value
     */
    public boolean isIntervalEnabled() {
        return intervalEnabled;
    }


    /**
     * Sets the date from
     *
     * @param dateFrom The new dateFrom value
     */
    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
        fireModelChange(VALUE_CHANGED, DATE_FROM);
    }


    /**
     * Sets the date to
     *
     * @param dateTo The new dateTo value
     */
    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
        fireModelChange(VALUE_CHANGED, DATE_TO);
    }


    /**
     * Sets the last days
     *
     * @param lastDays The new lastDays value
     */
    public void setLastDays(int lastDays) {
        this.lastDays = lastDays;
        fireModelChange(VALUE_CHANGED, LAST_DAYS);
    }


    /**
     * Sets the last months
     *
     * @param lastMonths The new lastMonths value
     */
    public void setLastMonths(int lastMonths) {
        this.lastMonths = lastMonths;
        fireModelChange(VALUE_CHANGED, LAST_MONTHS);
    }


    /**
     * Sets the last days enabled
     *
     * @param lastDaysEnabled The new lastDaysEnabled value
     */
    public void setLastDaysEnabled(boolean lastDaysEnabled) {
        this.lastDaysEnabled = lastDaysEnabled;
        if (lastDaysEnabled) {
            setLastMonthsEnabled(false);
            setIntervalEnabled(false);
        }
        fireModelChange(VALUE_CHANGED, LAST_DAYS_ENABLED);
    }


    /**
     * Sets the last months enabled
     *
     * @param lastMonthsEnabled The new lastMonthsEnabled value
     */
    public void setLastMonthsEnabled(boolean lastMonthsEnabled) {
        this.lastMonthsEnabled = lastMonthsEnabled;
        if (lastMonthsEnabled) {
            setLastDaysEnabled(false);
            setIntervalEnabled(false);
        }
        fireModelChange(VALUE_CHANGED, LAST_MONTHS_ENABLED);
    }


    /**
     * Sets the interval enabled
     *
     * @param intervalEnabled The new intervalEnabled value
     */
    public void setIntervalEnabled(boolean intervalEnabled) {
        this.intervalEnabled = intervalEnabled;
        if (intervalEnabled) {
            setLastDaysEnabled(false);
            setLastMonthsEnabled(false);
        }
        fireModelChange(VALUE_CHANGED, INTERVAL_ENABLED);
    }


    /**
     * TODO: document the method
     *
     * @param filter TODO: Describe the Parameter
     */
    void prepareFilter(SearchViewModel.ComplexFileFilter filter) {
        if (isIntervalEnabled()) {
            filter.dateFrom = getDateFrom();
            filter.dateTo = getDateTo();
            return;
        }
        Calendar cal = Calendar.getInstance();
        filter.dateTo = null;
        filter.dateFrom = null;
        if (isLastDaysEnabled()) {
            cal.add(Calendar.DATE, -getLastDays());
        } else if (isLastMonthsEnabled()) {
            cal.add(Calendar.MONTH, -getLastMonths());
        } else {
            throw new IllegalStateException("No DateCriteria style is enabled");
        }
        filter.dateFrom = cal.getTime();
    }
}
