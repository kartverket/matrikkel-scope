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
 * $Id: SComboBoxEditor.java,v 1.14 2002/10/23 12:38:46 ludovicc Exp $
 */
package org.scopemvc.view.swing;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.ComboBoxEditor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;

/**
 * <P>
 *
 * Default combobox editor for SComboBox. It can be used for any value class for
 * which a StringConvertor exists. The editor is created in SComboBox
 * constructor, so it can be safely obtained with call <code>getEditor</code>.
 * </P> <p>
 *
 * The StringConvertor is obtained from {@link
 * org.scopemvc.util.convertor.StringConvertors StringConvertors} to match the
 * type of object being edited. </p>
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.14 $ $Date: 2002/10/23 12:38:46 $
 * @created 05 September 2002
 */
public class SComboBoxEditor extends STextField implements ComboBoxEditor {

    private static final Log LOG = LogFactory.getLog(SComboBoxEditor.class);

    private EditorModel model = new EditorModel();

    /**
     * Constructor for the SComboBoxEditor object
     */
    public SComboBoxEditor() {
        super();
        setBorder(null);
        setSelector("value");
        setBoundModel(model);
    }


    /**
     * Return the edited item.
     *
     * @return The item value
     * @throws IllegalStateException If the StringConvertor couldn't convert the
     *      string to an object of the same class as the selected item.
     */
    public Object getItem() throws IllegalStateException {
        return getViewValue();
    }


    /**
     * Return the component that should be added to the tree hierarchy for this
     * editor
     *
     * @return The editorComponent value
     */
    public Component getEditorComponent() {
        return this;
    }


    /**
     * Set the item that should be edited. Cancel any editing if necessary
     *
     * @param inObject The new item value
     */
    public void setItem(Object inObject) {
        model.setValue(inObject);
        refresh();
    }


    /**
     * Add an ActionListener. An action event is generated when the edited item
     * changes
     *
     * @param inListener The listener to be added
     */
    public void addActionListener(ActionListener inListener) {
        super.addActionListener(inListener);
    }


    /**
     * Ask the editor to start editing and to select everything
     */
    public void selectAll() {
        super.selectAll();
        super.requestFocus();
    }


    /**
     * Remove an ActionListener
     *
     * @param inListener The listner to be removed
     */
    public void removeActionListener(ActionListener inListener) {
        super.removeActionListener(inListener);
    }

    /**
     * Create a default StringConvertor
     *
     * @return a StringConvertor
     */
    protected StringConvertor createDefaultStringConvertor() {
        try {
            Object m = model.getValue();
            if (m != null) {
                Class clazz = m.getClass();
                return StringConvertors.forClass(clazz);
            }
        } catch (Exception e) {
            LOG.warn("setupStringConvertor", e);
        }
        return null;
    }

    /**
     * Model used internally by the editor
     *
     * @author lclaude
     * @version $Revision: 1.14 $
     * @created 23 September 2002
     */
    public static class EditorModel {
        private Object value;

        /**
         * Gets the value
         *
         * @return The value value
         */
        public Object getValue() {
            return value;
        }

        /**
         * Sets the value
         *
         * @param inValue The new value value
         */
        public void setValue(Object inValue) {
            value = inValue;
        }
    }
}
