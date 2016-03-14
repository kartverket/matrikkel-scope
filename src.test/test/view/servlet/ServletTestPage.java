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
package test.view.servlet;

import java.io.Writer;
import org.scopemvc.view.servlet.Page;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.2 $ $Date: 2002/11/20 00:19:57 $
 * @created October 7, 2003
 */

final class ServletTestPage extends Page {
    private String type;

    /**
     * Constructor for the ServletTestPage object
     *
     * @param inID TODO: Describe the Parameter
     */
    public ServletTestPage(String inID) {
        super(inID);
    }

    /**
     * Gets the content type
     *
     * @return The contentType value
     */
    public String getContentType() {
        return type;
    }

    /**
     * Sets the content type
     *
     * @param inType The new contentType value
     */
    public void setContentType(String inType) {
        type = inType;
    }

    /**
     * TODO: document the method
     *
     * @param writer TODO: Describe the Parameter
     * @throws Exception TODO: Describe the Exception
     */
    public void streamView(Writer writer) throws Exception {
        writer.write(getID());
    }

    /**
     * TODO: document the method
     *
     * @param inPropertyID TODO: Describe the Parameter
     * @param inValue TODO: Describe the Parameter
     * @throws Exception TODO: Describe the Exception
     */
    public void populateBoundModelProperty(String inPropertyID, String inValue) throws Exception {
        // noop
    }
}
