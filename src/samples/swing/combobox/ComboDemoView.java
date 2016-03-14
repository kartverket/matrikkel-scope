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


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import org.scopemvc.core.Control;
import org.scopemvc.view.swing.SActionListener;
import org.scopemvc.view.swing.SComboBox;
import org.scopemvc.view.swing.SPanel;
import samples.util.GridBagHelper;

/**
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.9 $ $Date: 2002/11/20 00:19:56 $
 * @created 05 September 2002
 */
public class ComboDemoView extends SPanel {

    private SActionListener nextLaF = new SActionListener();


    /**
     * Constructor for the ComboDemoView object
     */
    public ComboDemoView() {
        GridBagLayout gridbag = new GridBagLayout();
        int comboWidth = 90;
        gridbag.columnWidths = new int[]{12, comboWidth, 11, comboWidth, 11, comboWidth, 11, 0, 11, 0, 11};
        gridbag.columnWeights = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        gridbag.rowHeights = new int[]{12, 0, 0, 0, 11, 0, 0, 0, 11, 0, 0, 0, 11};
        gridbag.rowWeights = new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        GridBagHelper hlp = new GridBagHelper();
        setLayout(gridbag);

        add(new JLabel("Look and Feel"), hlp.xy(1, 1, "w"));
        SComboBox lookAndFeelCombo = new SComboBox();
        add(lookAndFeelCombo, hlp.xy(1, 3, "we"));
        lookAndFeelCombo.setSelector("lookAndFeelSelection.lookAndFeels");
        lookAndFeelCombo.setSelectionSelector("lookAndFeelSelection.selectedLookAndFeel");

        JButton b = new JButton("Next");
        b.addActionListener(nextLaF);
        nextLaF.setModelActionString("nextLookAndFeel");
        add(b, hlp.xy(3, 3));

        add(new JLabel("Font Size"), hlp.xy(1, 5, "w"));
        SComboBox fontSizeCombo = new SComboBox();
        fontSizeCombo.setEditable(true);
        add(fontSizeCombo, hlp.xy(1, 7, "we"));
        fontSizeCombo.setSelector("fontSizeSelection.sizeList");
        fontSizeCombo.setSelectionSelector("fontSizeSelection.currentSize");
        fontSizeCombo.setSizeSelector("fontSizeSelection.sizeListSize");

        /*
         * date part:
         */
        SComboBox yearCombo = new SComboBox();
        yearCombo.setEditable(true);
        add(new JLabel("Year"), hlp.xy(1, 9, "w"));
        add(yearCombo, hlp.xy(1, 11, "we"));
        yearCombo.setSelector("dateModel.years");
        yearCombo.setSelectionSelector("dateModel.currentYear");

        SComboBox monthCombo = new SComboBox();
        monthCombo.setEditable(true);
        add(new JLabel("Month"), hlp.xy(3, 9, "w"));
        add(monthCombo, hlp.xy(3, 11, "we"));
        monthCombo.setSelector("dateModel.months");
        monthCombo.setSelectionSelector("dateModel.currentMonth");

        SComboBox dayCombo = new SComboBox();
        dayCombo.setEditable(true);
        add(new JLabel("Day"), hlp.xy(5, 9, "w"));
        add(dayCombo, hlp.xy(5, 11, "we"));
        dayCombo.setSelector("dateModel.days");
        dayCombo.setSelectionSelector("dateModel.currentDay");

        /*
         * static list for duplicate font combo
         */
        add(new JLabel("Duplicate Font Size"), new GridBagConstraints(
                1, 13, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0
                ));
        SComboBox duplicateFontSizeCombo = new SComboBox();
        duplicateFontSizeCombo.setEditable(false);
        duplicateFontSizeCombo.setEditable(true);
        add(duplicateFontSizeCombo, new GridBagConstraints(
                1, 15, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 8, 0), 0, 0
                ));
        Integer[] staticSizes = {new Integer(9), new Integer(10), new Integer(11)};
        duplicateFontSizeCombo.setListModel(staticSizes);
        duplicateFontSizeCombo.setSelectionSelector("fontSizeSelection.currentSize");
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(ComboDemoController.EXIT_CONTROL_ID, this);
    }


    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        super.setBoundModel(inModel);

        ComboDemoModel m = (ComboDemoModel) inModel;
        nextLaF.setBoundModel(m.getLookAndFeelSelection());
    }
}
