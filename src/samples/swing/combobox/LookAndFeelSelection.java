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

import java.awt.Component;

import java.util.*;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;

/**
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:56 $
 * @created 05 September 2002
 */
public class LookAndFeelSelection extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector SELECTED_LF = Selector.fromString("selectedLookAndFeel");

    private LookAndFeel selectedLookAndFeel;
    private List lookAndFeels = new ArrayList();
    private Component rootComponent;

    /**
     * Constructor for the LookAndFeelSelection object
     */
    public LookAndFeelSelection() {
        lookAndFeels.add(new LookAndFeel("Metal", "javax.swing.plaf.metal.MetalLookAndFeel"));
        lookAndFeels.add(new LookAndFeel("Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
        lookAndFeels.add(new LookAndFeel("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
        selectedLookAndFeel = (LookAndFeel) lookAndFeels.get(0);
    }

    /**
     * Gets the selected look and feel
     *
     * @return The selectedLookAndFeel value
     */
    public LookAndFeel getSelectedLookAndFeel() {
        return selectedLookAndFeel;
    }

    /**
     * Gets the look and feels
     *
     * @return The lookAndFeels value
     */
    public List getLookAndFeels() {
        return lookAndFeels;
    }

    /**
     * Gets the look and feels count
     *
     * @return The lookAndFeelsCount value
     */
    public int getLookAndFeelsCount() {
        return lookAndFeels.size();
    }

    /**
     * Sets the selected look and feel
     *
     * @param l The new selectedLookAndFeel value
     */
    public void setSelectedLookAndFeel(LookAndFeel l) {
        selectedLookAndFeel = l;
        if (l != null) {
            l.setAsCurrent(rootComponent);
        }
        super.fireModelChange(ModelChangeEvent.VALUE_CHANGED, SELECTED_LF);
    }

    /**
     * Sets the root component
     *
     * @param root The new rootComponent value
     */
    public void setRootComponent(Component root) {
        rootComponent = root;
    }

    /**
     * TODO: document the method
     */
    public void nextLookAndFeel() {
        int index = 0;
        if (selectedLookAndFeel != null) {
            index = (lookAndFeels.indexOf(selectedLookAndFeel) + 1) % lookAndFeels.size();
        }
        setSelectedLookAndFeel((LookAndFeel) lookAndFeels.get(index));
    }
}
