package org.scopemvc.view.swing;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * ListCellRenderer for visning av multiline-tekst
 *
 * @author Christian A. Rektorli
 */
public class SListAreaCellRenderer extends SPanel implements ListCellRenderer, ListCellRendererSelector {

   private static final Log LOG = LogFactory.getLog(SListCellRenderer.class);

   private Selector textSelector;
   private Selector iconSelector;
   private StringConvertor convertor;
   private JTextArea textArea;

   protected static Border noFocusBorder;

   public SListAreaCellRenderer() {
      super();
      textArea = new JTextArea();
      if (noFocusBorder == null) {
         noFocusBorder = new EmptyBorder(1, 1, 1, 1);
      }
      setLayout(new BorderLayout());
      textArea.setOpaque(true);
      textArea.setBorder(noFocusBorder);
      textArea.setLayout(new BorderLayout());
      add(textArea,  BorderLayout.CENTER);

   }

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
   public Component getListCellRendererComponent(JList inList, Object inValue, int inIndex, boolean inSelected, boolean inCellHasFocus) {
      // A lot of this is copied from DefaultListCellRenderer in JDK1.3
      if (LOG.isDebugEnabled()) {
         LOG.debug("getListCellRendererComponent: " + inValue);
      }

      if (inList == null) {
         textArea.setText("");
         return this;
      }

      setComponentOrientation(inList.getComponentOrientation());

      if (inSelected) {
         textArea.setBackground(inList.getSelectionBackground());
         textArea.setForeground(inList.getSelectionForeground());
      } else {
         textArea.setBackground(inList.getBackground());
         textArea.setForeground(inList.getForeground());
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
            textArea.setText("");
         } else {
            textArea.setText((inValue == null) ? "" : inValue.toString());
         }
      } else {
         if (text == null) {
            text = "";
         }
         textArea.setText(text);
      }
      textArea.setEnabled(inList.isEnabled());
      textArea.setFont(inList.getFont());
      textArea.setBorder((inCellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
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
         throw new IllegalArgumentException("StringConvertor argument cannot be null");
      }
      convertor = inConvertor;
   }

   public void setColumns(int col) {
      textArea.setColumns(col);
   }

   public Dimension getPreferredSize() {
      return textArea.getPreferredSize();
   }
}
