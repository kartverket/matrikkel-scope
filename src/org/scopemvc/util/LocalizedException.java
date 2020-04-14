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
 * $Id: LocalizedException.java,v 1.6 2002/09/25 13:53:07 ludovicc Exp $
 */
package org.scopemvc.util;

import java.text.MessageFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <P>
 *
 * An Exception that implements {@link #getLocalizedMessage} by looking up its
 * message as an ID that is looked up in {@link org.scopemvc.util.UIStrings
 * UIStrings} to present the user with a localised message: message parameters
 * passed in are substituted in the message as java.text.MessageFormat. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/25 13:53:07 $
 * @created 05 September 2002
 */
public class LocalizedException extends Exception {

    private static final Log LOG = LogFactory.getLog(LocalizedException.class);

    private Object[] messageParameters;


    /**
     * Create with a message id, but no message parameters.
     *
     * @param inMessageID a message ID that identifies the localised
     *      user-readable message in {@link org.scopemvc.util.UIStrings
     *      UIStrings}
     */
    public LocalizedException(String inMessageID) {
        this(inMessageID, null);
    }


    /**
     * Create with a message id and one message parameter.
     *
     * @param inMessageID a message ID that identifies the localised
     *      user-readable message in {@link org.scopemvc.util.UIStrings
     *      UIStrings}
     * @param inMessageParameter one parameter to be substituted in the message
     *      as {@link java.text.MessageFormat}
     */
    public LocalizedException(String inMessageID, Object inMessageParameter) {
        super(inMessageID);
        messageParameters = new Object[]{inMessageParameter};
    }


    /**
     * Create with a message id and message parameters.
     *
     * @param inMessageID a message ID that identifies the localised
     *      user-readable message in {@link org.scopemvc.util.UIStrings
     *      UIStrings}
     * @param inMessageParameters a set of parameters to be substituted in the
     *      message as {@link java.text.MessageFormat}
     */
    public LocalizedException(String inMessageID, Object[] inMessageParameters) {
        super(inMessageID);
        messageParameters = inMessageParameters;
    }


    /**
     * Use the message as an ID to a message pattern in the {@link
     * org.scopemvc.util.UIStrings UIStrings} and use {@link
     * java.text.MessageFormat} to format it with the message parameters.
     *
     * @return The localizedMessage value
     */
    public final String getLocalizedMessage() {
        String messagePattern = UIStrings.get(getMessage());
        if (messagePattern == null) {
            LOG.error("Got null UIString for: " + getMessage());
            messagePattern = "";
        }
        return MessageFormat.format(messagePattern, messageParameters);
    }
}

