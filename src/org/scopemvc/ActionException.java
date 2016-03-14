/*
 * ScopeMVC: a generic MVC framework for rich gui applications.
 * Copyright (c) 2000-2003, The Scope team
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
 * Neither the name "Scope", "ScopeMVC" nor the names of its contributors
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
 * $Id: ActionException.java,v 1.6 2003/06/20 00:14:42 ludovicc Exp $
 */

package org.scopemvc;


/**
 * {@link org.scopemvc.core.Controller}s throw ActionExceptions while responding to {@link
 * ActionEvent}s if something goes wrong that must be reported to the user. <P>
 *
 * ActionExceptions contain a ActionEvent ID that can be used by an error handler
 * to identify the ActionEvent that caused the exception. For example, the ActionEvent
 * ID could be used to get a String title from UIStrings for a Swing error
 * dialog. The error handling implementation in ActionController automatically
 * populates the ActionEvent ID. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <A HREF="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</A>
 * @created 05 August 2002
 * @version $Revision: 1.6 $ $Date: 2003/06/20 00:14:42 $
 */
public final class ActionException extends Exception {
    private String _actionID;
    private Object[] _messageParameters;

    /**
     * Constructor for the ActionException object
     *
     * @param messageID a message ID that identifies the localised
     *      user-readable message in {@link ControllerContext#getMessages Messages}
     */
    public ActionException(String messageID) {
        super(messageID);
    }

    /**
     * Constructor for the ActionException object
     *
     * @param messageID a message ID that identifies the localised
     *      user-readable message in {@link ControllerContext#getMessages Messages}
     * @param messageParameter one parameter to be substituted in the message
     *      as {@link java.text.MessageFormat}
     */
    public ActionException(String messageID, Object messageParameter) {
        super(messageID);
        _messageParameters = new Object[] {
                                messageParameter
                            };
    }

    /**
     * Constructor for the ActionException object
     *
     * @param messageID a message ID that identifies the localised
     *      user-readable message in {@link ControllerContext#getMessages Messages}
     * @param messageParameters a set of parameters to be substituted in the
     *      message as {@link java.text.MessageFormat}
     */
    public ActionException(String messageID, Object[] messageParameters) {
        super(messageID);
        _messageParameters = messageParameters;
    }

    /**
     * Get the message parameters
     *
     * @return a set of parameters to be substituted in the
     *      message as {@link java.text.MessageFormat}
     */
    public Object[] getMessageParameters() {
        return _messageParameters;
    }

    /**
     * Get the ID of the ActionEvent being executed when the error was detected.
     *
     * @return the ActionEvent ID
     */
    public String getActionID() {
        return _actionID;
    }

    /**
     * Sets the ID of the ActionEvent being executed when the error was detected. <br>
     * For use by a ActionException handler only, not for application writers.
     *
     * @param actionID the ID of the {@link org.scopemvc.core.ActionEvent
     *      ActionEvent} executing when the error was detected.
     */
    public final void setActionID(String actionID) {
        _actionID = actionID;
    }
}
