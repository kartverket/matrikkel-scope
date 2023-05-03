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
 * $Id: SwingBoundModel.java,v 1.10 2002/10/17 02:02:32 ludovicc Exp $
 */
package org.scopemvc.view.swing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.view.util.ActiveBoundModel;
import org.scopemvc.view.util.ModelBindable;

import javax.swing.*;
import java.beans.Beans;

/**
 * <P>
 *
 * An {@link org.scopemvc.view.util.ActiveBoundModel ActiveBoundModel} that
 * forces updates to the parent View onto the AWTEvent thread because Swing
 * isn't thread-safe. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.10 $ $Date: 2002/10/17 02:02:32 $
 * @created 05 September 2002
 */
public class SwingBoundModel extends ActiveBoundModel {

    private static final Log LOG = LogFactory.getLog(SwingBoundModel.class);


    /**
     * Constructor for the SwingBoundModel object
     *
     * @param inView The view to bind to the model
     */
    public SwingBoundModel(ModelBindable inView) {
        super(inView);
    }


    /**
     * Calls parent View's {org.scopemvc.view.util.ModelBindable#validationSuccess}
     * to clear any previous validation failure from the Swing event thread.
     *
     * @param inEventType The type of the change event, one of the {@link
     *      org.scopemvc.core.ModelChangeTypes ModelChangeTypes} values
     */
    public void updateFromModel(final int inEventType) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromModel: eventType: " + inEventType);
        }

        if (Beans.isDesignTime()) {
            // no model can be set at design time, ignore all changes
            return;
        }

        if (SwingUtilities.isEventDispatchThread()) {
            superUpdateFromModel(inEventType);
        } else {
            SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        try {
                            superUpdateFromModel(inEventType);
                        } catch (Exception ex) {
                            LOG.error("updateFromModel", ex);
                        }
                    }
                });
        }
    }


    private void superUpdateFromModel(int inEventType) {
        super.updateFromModel(inEventType);
    }
}

