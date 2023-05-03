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
 * $Id: STextCellEditor.java,v 1.7 2002/09/25 13:53:08 ludovicc Exp $
 */
package org.scopemvc.view.swing;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.convertor.StringConvertor;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Base class for cell editors based on Swing's <code>JTextComponent</code>. It
 * is validating editor - it means, that {@link #stopCellEditing
 * stopCellEditing} can return <code>false</code>. <p>
 *
 * Difference between a Swing's and this cell editor is that Swing always
 * returns String value, whereas <code>STextCellEditor</code> uses {@link
 * org.scopemvc.util.convertor.StringConvertor StringConvertor} to return object
 * of desired type.</p> <p>
 *
 * <i>Note:</i> This editor fixes in unbelievable simple way Sun's editor
 * unpleasant bug - when You start cell editing with keyboard, not mouse, the
 * text field has no cursor (in 1.3.1 and 1.4 beta) </p>
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.7 $ $Date: 2002/09/25 13:53:08 $
 * @created 05 September 2002
 */
public abstract class STextCellEditor extends AbstractCellEditor {

    private static final Log LOG = LogFactory.getLog(STextCellEditor.class);

    private JTextComponent component;
    private StringConvertor convertor;
    private ValidationHelper validationHelper;
    private Object value;
    private int clickCountToStart = 2;

    /**
     * Creates new SDefaultCellRenderer
     *
     * @param inConvertor The StringConvertor for the text to edit
     * @throws IllegalArgumentException If the convertor is null
     */
    public STextCellEditor(StringConvertor inConvertor)
             throws IllegalArgumentException {
        if (inConvertor == null) {
            throw new IllegalArgumentException("Passed StringConvertor "
                    + "cannot be null");
        }
        convertor = inConvertor;
        component = createTextComponent();
        if (component == null) {
            throw new IllegalStateException("Method createTextComponent "
                    + "cannot return null component");
        }
        validationHelper = new ValidationHelper(component);
    }

    /**
     * Gets the string convertor
     *
     * @return The stringConvertor value
     */
    public final StringConvertor getStringConvertor() {
        return convertor;
    }

    /**
     * Gets the cell editor value
     *
     * @return The cellEditorValue value
     */
    public final Object getCellEditorValue() {
        return value;
    }

    /**
     * ClickCountToStart controls the number of clicks required to start
     * editing. Default value is 2.
     *
     * @return The clickCountToStart value
     */
    public final int getClickCountToStart() {
        return clickCountToStart;
    }

    /**
     * Editable for mouse click events if click count is equals or greater then
     * {@link #getClickCountToStart() getClickCountToStart()}. For all other
     * event types returns true.
     *
     * @param inEvent the event the editor should use to consider whether to
     *      begin editing or not.
     * @return The cellEditable value
     */
    public boolean isCellEditable(EventObject inEvent) {
        if (inEvent instanceof MouseEvent) {
            return ((MouseEvent) inEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param inClickCount an int specifying the number of clicks needed to
     *      start editing
     * @see #getClickCountToStart
     */
    public final void setClickCountToStart(int inClickCount) {
        clickCountToStart = inClickCount;
    }

    /**
     * Cancel editing
     */
    public final void cancelCellEditing() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("cancel Editing");
        }
        value = null;
        super.cancelCellEditing();
    }

    /**
     * Stop editing
     *
     * @return true if editing was stopped
     */
    public final boolean stopCellEditing() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("stop Editing");
        }
        boolean result = obtainValue();
        super.stopCellEditing();
        return result;
    }

    /**
     * Returns true.
     *
     * @return The validating value
     */
    protected boolean isValidating() {
        return true;
    }

    /**
     * Return the component used for editing
     *
     * @return the component used for editing
     */
    protected JTextComponent getTextComponent() {
        return component;
    }

    /**
     * Sets value for editing. Value is converted into String with <code>StringConvertor.</code>
     * For use in inherited classes.
     *
     * @param inValue The new value value
     * @throws IllegalArgumentException if converted cannot convert passed
     *      value.
     */
    protected final void setValue(Object inValue)
             throws IllegalArgumentException {
        component.setText(convertor.valueAsString(inValue));
    }

    /**
     * Create the component used for editing
     *
     * @return The component for editing
     */
    protected JTextComponent createTextComponent() {
        JTextComponent c =
            new JTextField() {
                public void addNotify() {
                    super.addNotify();
                    requestFocus();
                }
            };
        return c;
    }

    private boolean obtainValue() {
        try {
            value = convertor.stringAsValue(component.getText());
            validationHelper.validationSuccess();
            if (LOG.isDebugEnabled()) {
                LOG.debug("cell validation succeed");
            }
            return true;
        } catch (IllegalArgumentException ex) {
            value = null;
            if (LOG.isDebugEnabled()) {
                LOG.debug("cell validation failed");
            }
            validationHelper.validationFailed(ex);
            return !isValidating();
        }
    }

}

