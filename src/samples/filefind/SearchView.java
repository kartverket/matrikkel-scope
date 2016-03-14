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


import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import org.scopemvc.core.Control;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.view.swing.SButton;
import org.scopemvc.view.swing.SCheckBox;
import org.scopemvc.view.swing.SPanel;
import org.scopemvc.view.swing.STable;
import org.scopemvc.view.swing.STextField;
import samples.util.GridBagHelper;

/**
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.5 $ $Date: 2002/09/25 18:07:45 $
 * @created 05 September 2002
 */
public final class SearchView extends SPanel {

    /**
     * A ModelChangeListener used to show and hide the DateCriteriaView
     * depending on whether isDateCriteriaEnabled() of bound model.
     */
    ModelChangeListener myModelChangeListener =
        new ModelChangeListener() {
            public void modelChanged(ModelChangeEvent e) {
                SearchViewModel m = (SearchViewModel) e.getModel();
                dateCriteriaView.setVisible(m.isDateCriteriaEnabled());
            }
        };

    private DateCriteriaView dateCriteriaView;


    /**
     * Constructor for the SearchView object
     */
    public SearchView() {

        GridBagLayout gridbag = new GridBagLayout();
        gridbag.columnWidths = new int[]{12, 0, 11, 0, 11, 0, 0, 11};
        gridbag.columnWeights = new double[]{0, 0, 0, 0, 1, 0};
        gridbag.rowHeights = new int[]{12, 0, 5, 0, 11, 0, 0, 0, 0, 0, 11};
        gridbag.rowWeights = new double[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0};
        setLayout(gridbag);
        GridBagHelper hlp = new GridBagHelper();

        add(new JLabel("Search files with name (PERL regular expression):"), hlp.xywh(3, 1, 3, 1, "w"));

        STextField t = new STextField();
        t.setSelector(SearchViewModel.FILE_NAME_PATTERN);
        add(t, hlp.xywh(3, 3, 4, 1, "we"));

        SCheckBox b = new SCheckBox();
        b.setText("Date");
        b.setSelector(SearchViewModel.DATE_CRITERIA_ENABLED);
        add(b, hlp.xy(3, 5, "wen"));

        dateCriteriaView = new DateCriteriaView();
        add(dateCriteriaView, hlp.xy(5, 5));

        SButton searchBtn = new SButton("Search");
        searchBtn.setControlID(SearchController.SEARCH_CONTROL_ID);
        add(searchBtn, hlp.xy(3, 8));

        STable table = new STable();
        table.setSelector("fsRoots.fileSystemRoots");
        table.setColumnNames(new String[]{
                "",
                "",
                });
        table.setColumnSelectors(new String[]{
                "enabled",
                "name",
                });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(80, 100));
        add(scroll, hlp.xywh(1, 1, 1, 9, "news"));
//        add(new FSRootsView(), hlp.xywh(1, 1, 1, 9, "news"));
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(SearchController.EXIT_CONTROL_ID, this);
    }


    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "Find File";
    }


    /**
     * Register a private ModelChangeListener to show and hide the
     * DateCriteriaView depending on whether isDateCriteriaEnabled() of bound
     * model.
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        super.setBoundModel(inModel);
        ModelChangeEventSource m = (ModelChangeEventSource) inModel;
        m.addModelChangeListener(myModelChangeListener);
    }
}
