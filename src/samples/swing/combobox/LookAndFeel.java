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
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/19 18:09:34 $
 * @created 18 September 2002
 */
public class LookAndFeel {

    private static final Log LOG = LogFactory.getLog(LookAndFeel.class);

    private String name;
    private String implementation;


    /**
     * Constructor for the LookAndFeel object
     *
     * @param n TODO: Describe the Parameter
     * @param i TODO: Describe the Parameter
     */
    public LookAndFeel(String n, String i) {
        name = n;
        implementation = i;
    }


    /**
     * Gets the name
     *
     * @return The name value
     */
    public String getName() {
        return name;
    }


    /**
     * Gets the implementation
     *
     * @return The implementation value
     */
    public String getImplementation() {
        return implementation;
    }


    /**
     * Sets the as current
     *
     * @param root The new asCurrent value
     */
    public void setAsCurrent(final Component root) {
        SwingUtilities.invokeLater(
            new Runnable() {
                public void run() {
                    try {
                        UIManager.setLookAndFeel(implementation);
                        SwingUtilities.updateComponentTreeUI(root);
                    } catch (Exception ex) {
                        LOG.error("Cannot set look and feel " + name, ex);
                    }
                }
            });
    }


    /**
     * To enable work with Swing's default renderer.
     *
     * @return TODO: Describe the Return Value
     */
    public String toString() {
        return name;
    }
}
