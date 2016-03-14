package org.scopemvc.view.swing;

import org.scopemvc.core.Selector;
import org.scopemvc.util.convertor.StringConvertor;

/**
 * Interface for ListCellRenderes som alle som skal brukes av SList må implementere
 *
 * @author Christian A. Rektorli
 */
public interface ListCellRendererSelector {
    public Selector getTextSelector();
    public Selector getIconSelector();
    public void setTextSelector(Selector inSelector);
    public void setTextSelector(String inSelectorString);
    public void setTextSelectorString(String inSelectorString);
    public void setIconSelector(Selector inSelector);
    public void setIconSelector(String inSelectorString);
    public void setIconSelectorString(String inSelectorString);
    public void setStringConvertor(StringConvertor inConvertor);
}
