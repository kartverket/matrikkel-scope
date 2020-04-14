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
 * $Id: STableTextCellEditor.java,v 1.8 2002/09/25 13:53:08 ludovicc Exp $
 */
package org.scopemvc.view.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;

/**
 * Cell editor for {@link org.scopemvc.view.swing.STable STable} or {@link
 * javax.swing.JTable JTable} using {@link javax.swing.JTextField JTextField}.
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.8 $ $Date: 2002/09/25 13:53:08 $
 * @created 05 September 2002
 */
public class STableTextCellEditor extends STextCellEditor implements TableCellEditor {

    private static final Log LOG = LogFactory.getLog(STableTextCellEditor.class);
    private static final Border BLACK_BORDER = new LineBorder(Color.black);


    /**
     * Constructor for the STableTextCellEditor object
     *
     * @param inClass The class of the values to be edited
     * @throws IllegalArgumentException If the StringConvertor could not be
     *      found for the given class
     */
    public STableTextCellEditor(Class inClass) throws IllegalArgumentException {
        super(StringConvertors.forClass(inClass));
    }


    /**
     * Constructor for the STableTextCellEditor object
     *
     * @param inConvertor The StringConvertor for the values to edit
     */
    public STableTextCellEditor(StringConvertor inConvertor) {
        super(inConvertor);
    }

    /**
     * Sets an initial <code>value</code> for the editor. This will cause the
     * editor to <code>stopEditing</code> and lose any partially edited value if
     * the editor is editing when this method is called. <p>
     *
     * Returns the component that should be added to the client's <code>Component</code>
     * hierarchy. Once installed in the client's hierarchy this component will
     * then be able to draw and receive user input.
     *
     * @param inTable the <code>JTable</code> that is asking the editor to edit;
     *      can be <code>null</code>
     * @param inValue the value of the cell to be edited; it is up to the
     *      specific editor to interpret and draw the value. For example, if
     *      value is the string "true", it could be rendered as a string or it
     *      could be rendered as a check box that is checked. <code>null</code>
     *      is a valid value
     * @param inSelected true if the cell is to be rendered with highlighting
     * @param inRow the row of the cell being edited
     * @param inColumn the column of the cell being edited
     * @return the component for editing
     */
    public Component getTableCellEditorComponent(JTable inTable, Object inValue,
            boolean inSelected,
            int inRow, int inColumn) {
        super.setValue(inValue);
        JTextComponent component = getTextComponent();
        component.setBorder(BLACK_BORDER);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getTableCellEditorComponent: " + component);
        }
        return component;
    }

}

