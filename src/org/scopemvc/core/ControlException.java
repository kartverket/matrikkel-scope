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
 * $Id: ControlException.java,v 1.4 2002/09/05 15:41:45 ludovicc Exp $
 */
package org.scopemvc.core;


import org.scopemvc.util.LocalizedException;
import org.scopemvc.util.UIStrings;

/**
 * <P>
 *
 * {@link Controller}s throw ControlExceptions while responding to {@link
 * Control}s if something goes wrong that must be reported to the user. A
 * ControlException is a {@link org.scopemvc.util.LocalizedException}. </P> <P>
 *
 * ControlExceptions contain a Control ID that can be used by an error handler
 * to identify the Control that caused the exception. For example, the Control
 * ID could be used to get a String title from UIStrings for a Swing error
 * dialog. The error handling implementation in BasicController automatically
 * populates the Control ID. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 August 2002
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:45 $
 */
public final class ControlException extends LocalizedException {

    private String sourceControlID;


    /**
     * Constructor for the ControlException object
     *
     * @param inMessageID a message ID that identifies the localised
     *      user-readable message in {@link org.scopemvc.util.UIStrings
     *      UIStrings}
     */
    public ControlException(String inMessageID) {
        super(inMessageID);
    }


    /**
     * Constructor for the ControlException object
     *
     * @param inMessageID a message ID that identifies the localised
     *      user-readable message in {@link org.scopemvc.util.UIStrings
     *      UIStrings}
     * @param inMessageParameter one parameter to be substituted in the message
     *      as {@link java.text.MessageFormat}
     */
    public ControlException(String inMessageID, Object inMessageParameter) {
        super(inMessageID, inMessageParameter);
    }


    /**
     * Constructor for the ControlException object
     *
     * @param inMessageID a message ID that identifies the localised
     *      user-readable message in {@link org.scopemvc.util.UIStrings
     *      UIStrings}
     * @param inMessageParameters a set of parameters to be substituted in the
     *      message as {@link java.text.MessageFormat}
     */
    public ControlException(String inMessageID, Object[] inMessageParameters) {
        super(inMessageID, inMessageParameters);
    }


    /**
     * Use the source Control ID to get a localised name from the {@link
     * org.scopemvc.util.UIStrings UIStrings}.
     *
     * @return Localized name of Control ID or empty String if no Control ID
     *      set.
     */
    public final String getLocalizedSourceControlName() {
        if (sourceControlID == null) {
            return "";
        }

        return UIStrings.get(sourceControlID);
    }


    /**
     * Sets the ID of the Control being executed when the error was detected.
     * <br>
     * For use by a ControlException handler only, not for application writers.
     *
     * @param inSourceControlID the ID of the {@link org.scopemvc.core.Control
     *      Control} executing when the error was detected.
     */
    public final void setSourceControlID(String inSourceControlID) {
        sourceControlID = inSourceControlID;
    }
}

