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
 * $Id: SListCellRenderer.java,v 1.9 2002/09/25 13:53:08 ludovicc Exp $
 * Changes:
 * - implements ListCellRendererSelector (scope 2.0)
 */
package org.scopemvc.view.swing;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;

import javax.swing.*;
import java.awt.*;

/**
 * <P>
 *
 * A javax.swing.DefaultListCellRenderer that tries to draw model objects as
 * text with an icon, both from properties on the displayed model object
 * identified by Selectors. <br>
 * If both Selectors don't work then it has the same behaviour as
 * DefaultListCellRenderer. <br>
 * If the string convertor is not set then the default one is used. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.9 $ $Date: 2002/09/25 13:53:08 $
 * @created 05 September 2002
 * @todo SListCellRenderer should extend from SLabel, so it will be a view and
 *      will be useable from EditorManager.getView(), then we can have a
 *      renderer configurable in ScopeConfig for the SList and the SComboBox.
 *      See how it's done for the editor in SComboBox (ludovicc)
 */
public class SListCellRenderer extends DefaultListCellRenderer implements ListCellRendererSelector {

    private static final Log LOG = LogFactory.getLog(SListCellRenderer.class);

    private Selector textSelector;
    private Selector iconSelector;
    private StringConvertor convertor;

    /**
     * Gets the selector for the text
     *
     * @return The Selector
     */
    public final Selector getTextSelector() {
        return textSelector;
    }


    /**
     * Gets the selector for the icon
     *
     * @return The Selector
     */
    public final Selector getIconSelector() {
        return iconSelector;
    }


    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell. If it is necessary to compute the dimensions of a list
     * because the list cells do not have a fixed size, this method is called to
     * generate a component on which <code>getPreferredSize</code> can be
     * invoked.
     *
     * @param inList The SList we're painting.
     * @param inValue The value returned by list.getModel().getElementAt(index).
     * @param inIndex The cells index.
     * @param inSelected True if the specified cell was selected.
     * @param inCellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     */
    public Component getListCellRendererComponent(
            JList inList,
            Object inValue,
            int inIndex,
            boolean inSelected,
            boolean inCellHasFocus) {
        // A lot of this is copied from DefaultListCellRenderer in JDK1.3
        if (LOG.isDebugEnabled()) {
            LOG.debug("getListCellRendererComponent: " + inValue);
        }

        if (inList == null) {
            setText("");
            setIcon(null);
            return this;
        }

        setComponentOrientation(inList.getComponentOrientation());

        if (inSelected) {
            setBackground(inList.getSelectionBackground());
            setForeground(inList.getSelectionForeground());
        } else {
            setBackground(inList.getBackground());
            setForeground(inList.getForeground());
        }

        // value can be null, becouse we can be used also in combobox
        // Try to get text and/or icon properties from the passed value
        PropertyManager manager = null;
        if (inValue != null) {
            manager = PropertyManager.getInstance(inValue);
        }

        String text = null;
        Icon icon = null;

        // ***** Need to cache some of this for common case where List contains models of same Class

        // text
        if (manager != null && textSelector != null) {
            try {
                if (convertor == null) {
                    convertor = StringConvertors.forClass(manager.getPropertyClass(inValue, textSelector));
                }
                Object textProperty = manager.get(inValue, textSelector);
                if (convertor != null) {
                    text = convertor.valueAsString(textProperty);
                } else {
                    text = (textProperty == null) ? "" : textProperty.toString();
                }
            } catch (Exception e) {
                // ignore and leave text == null
            }
        }

        // icon
        if (manager != null && iconSelector != null) {
            try {
                Object iconProperty = manager.get(inValue, iconSelector);
                if (iconProperty instanceof Icon) {
                    icon = (Icon) iconProperty;
                }
            } catch (Exception e) {
                // ignore and leave icon == null
            }
        }

        if (text == null && icon == null) {
            // Act like DefaultListCellRenderer
            if (inValue instanceof Icon) {
                setIcon((Icon) inValue);
                setText("");
            } else {
                setIcon(null);
                setText((inValue == null) ? "" : inValue.toString());
            }
        } else {
            if (text == null) {
                text = "";
            }
            setText(text);
            setIcon(icon);
        }

        setEnabled(inList.isEnabled());
        setFont(inList.getFont());
        setBorder((inCellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

        return this;
    }


    /**
     * Sets the text selector
     *
     * @param inSelector The new textSelector value
     */
    public final void setTextSelector(Selector inSelector) {
        textSelector = inSelector;
    }


    /**
     * Sets the text selector string
     *
     * @param inSelectorString The new textSelectorString value
     */
    public final void setTextSelector(String inSelectorString) {
        if (inSelectorString == null) {
            setTextSelector((Selector) null);
        } else {
            setTextSelector(Selector.fromString(inSelectorString));
        }
    }

    /**
     * Sets the text selector string
     *
     * @param inSelectorString The new textSelectorString value
     * @deprecated Use setTextSelector(String);
     */
    public final void setTextSelectorString(String inSelectorString) {
        setTextSelector(inSelectorString);
    }

    /**
     * Sets the icon selector
     *
     * @param inSelector The new iconSelector value
     */
    public final void setIconSelector(Selector inSelector) {
        iconSelector = inSelector;
    }

    /**
     * Sets the icon selector string
     *
     * @param inSelectorString The new iconSelectorString value
     */
    public final void setIconSelector(String inSelectorString) {
        if (inSelectorString == null) {
            setIconSelector((Selector) null);
        } else {
            setIconSelector(Selector.fromString(inSelectorString));
        }
    }

    /**
     * Sets the icon selector string
     *
     * @param inSelectorString The new iconSelectorString value
     * @deprecated Use setIconSelector(String)
     */
    public final void setIconSelectorString(String inSelectorString) {
        setIconSelector(inSelectorString);
    }

    /**
     * Sets the string convertor
     *
     * @param inConvertor The new stringConvertor value
     * @throws IllegalArgumentException If the convertor is null
     */
    public final void setStringConvertor(StringConvertor inConvertor)
             throws IllegalArgumentException {
        if (inConvertor == null) {
            throw new IllegalArgumentException("StringConvertor argument cannot"
                    + " be null");
        }
        convertor = inConvertor;
    }

}
