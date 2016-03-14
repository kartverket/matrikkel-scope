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

import java.awt.BorderLayout;
import javax.swing.*;

import org.scopemvc.core.*;
import org.scopemvc.view.swing.*;
import org.scopemvc.view.swing.*;
import org.scopemvc.view.swing.SPanel;

/**
 * Bound model is expected to be a List of FileProperties.
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.9 $ $Date: 2002/11/20 00:19:58 $
 * @created 05 September 2002
 */
public class SearchResultsView extends SPanel {

//    private STableModel tableModel = new STableModel();

    /**
     * Constructor for the SearchResultsView object
     */
    public SearchResultsView() {

        setLayout(new BorderLayout());

        STable table = new STable();
        add(new JScrollPane(table));

        table.setColumnNames(new String[]{
                "Name",
                "Length",
                "Type",
                "Path",
                "Last Modified"
                });
        table.setColumnSelectors(new String[]{
                "name",
                "length",
                "suffix",
                "path",
                "lastModified"
                });

        // Sort the results table by making the TableModel sorted.
        // Note: FileProperties model implements Comparable
        // ... so we don't bother supplying a Comparator here
        ((STableModel) table.getModel()).setSorted(true);

        SLabel fileCountLabel = new SLabel();
        fileCountLabel.setSelector("size");
        add(fileCountLabel, BorderLayout.SOUTH);
    }


    /**
     * Gets the close control
     *
     * @return The closeControl value
     */
    public Control getCloseControl() {
        return new Control(SearchResultsController.CLOSE_RESULTS_ID, this);
    }


    /**
     * Gets the title
     *
     * @return The title value
     */
    public String getTitle() {
        return "Find File Results";
    }
}
