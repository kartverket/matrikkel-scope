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

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import org.scopemvc.core.*;
import org.scopemvc.view.swing.*;
import org.scopemvc.view.swing.*;
import org.scopemvc.view.swing.SPanel;
import samples.util.GridBagHelper;

/**
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/25 18:07:45 $
 * @created 05 September 2002
 */
public class DateCriteriaView extends SPanel {

    /**
     * A ModelChangeListener to enable/disable controls as the model changes
     * state.
     */
    ModelChangeListener myModelChangeListener =
        new ModelChangeListener() {
            public void modelChanged(ModelChangeEvent e) {
                SearchViewModel m = (SearchViewModel) e.getModel();
                if (!m.isDateCriteriaEnabled()) {
                    return;
                }
                DateCriteriaModel d = m.getDateCriteria();
                lastDays.setEditable(d.isLastDaysEnabled());
                lastMonths.setEditable(d.isLastMonthsEnabled());
                dateTo.setEditable(d.isIntervalEnabled());
                dateFrom.setEditable(d.isIntervalEnabled());
            }
        };

    private STextField lastDays;
    private STextField lastMonths;
    private STextField dateTo;
    private STextField dateFrom;


    /**
     * Constructor for the DateCriteriaView object
     */
    public DateCriteriaView() {

        // Bind this SPanel to the dateCriteria submodel of the SearchViewModel
        setSelector("dateCriteria");

        GridBagLayout gridbag = new GridBagLayout();
        gridbag.columnWidths = new int[]{0, 11, 30, 11, 0};
        gridbag.columnWeights = new double[]{0, 0, 0, 0, 1};
        gridbag.rowHeights = new int[]{0, 5, 0, 5, 0, 5, 0, 11};
        gridbag.rowWeights = new double[]{0, 0, 0, 0, 0, 0, 0, 1};
        setLayout(gridbag);
        GridBagHelper hlp = new GridBagHelper();

        SRadioButton b = new SRadioButton();
        b.setText("In Last");
        b.setSelector(DateCriteriaModel.LAST_MONTHS_ENABLED);
        add(b, hlp.xy(0, 0, "we"));

        lastMonths = new STextField();
        lastMonths.setSelector(DateCriteriaModel.LAST_MONTHS);
        lastMonths.setColumns(4);
        add(lastMonths, hlp.xy(2, 0, "w"));

        add(new JLabel("Months"), hlp.xywh(2, 0, 3, 1, "e"));

        b = new SRadioButton();
        b.setText("In Last");
        b.setSelector(DateCriteriaModel.LAST_DAYS_ENABLED);
        add(b, hlp.xy(0, 2, "w"));

        lastDays = new STextField();
        lastDays.setSelector(DateCriteriaModel.LAST_DAYS);
        lastDays.setColumns(4);
        add(lastDays, hlp.xy(2, 2, "w"));

        add(new JLabel("Days"), hlp.xywh(2, 2, 3, 1, "e"));

        b = new SRadioButton();
        b.setText("Between");
        b.setSelector(DateCriteriaModel.INTERVAL_ENABLED);
        add(b, hlp.xy(0, 4, "we"));

        dateFrom = new STextField();
        dateFrom.setSelector(DateCriteriaModel.DATE_FROM);
        dateFrom.setColumns(7);
        add(dateFrom, hlp.xy(2, 4, "we"));

        dateTo = new STextField();
        dateTo.setSelector(DateCriteriaModel.DATE_TO);
        dateTo.setColumns(7);
        add(dateTo, hlp.xy(2, 6, "we"));

        add(new JLabel("and"), hlp.xy(0, 6, "e"));
    }


    /**
     * Override to add a listener that will enable/disable controls as the model
     * changes state.
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        super.setBoundModel(inModel);
        ModelChangeEventSource m = (ModelChangeEventSource) inModel;
        m.addModelChangeListener(myModelChangeListener);
    }
}
