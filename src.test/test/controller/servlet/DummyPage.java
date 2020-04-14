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
 * $Id: DummyPage.java,v 1.8 2002/11/20 00:19:58 ludovicc Exp $
 */
package test.controller.servlet;


import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.view.servlet.xml.XSLPage;

/**
 * <P>
 *
 * A ServletView that sends the bound model property into the output stream.
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 18 September 2002
 * @version $Revision: 1.8 $ $Date: 2002/11/20 00:19:58 $
 */
class DummyPage extends XSLPage {
    private static final Log LOG = LogFactory.getLog(DummyPage.class);
    // support test of streamView
    private Selector selector;

    /**
     * Constructor for the DummyPage object
     *
     * @param inID TODO: Describe the Parameter
     */
    public DummyPage(String inID) {
        super(inID, null);
    }

    /**
     * Gets the content type
     *
     * @return The contentType value
     */
    public String getContentType() {
        return "text/text";
    }

    /**
     * Gets the selector
     *
     * @return The selector value
     */
    public Selector getSelector() {
        return selector;
    }

    /**
     * Sets the selector
     *
     * @param inSelector The new selector value
     */
    public void setSelector(Selector inSelector) {
        selector = inSelector;
    }

    /**
     * TODO: document the method
     *
     * @param stream TODO: Describe the Parameter
     * @throws Exception TODO: Describe the Exception
     */
    public void streamView(OutputStream stream) throws Exception {
        LOG.debug("streamView");
        PropertyManager manager = PropertyManager.getInstance(getBoundModel());
        Object o = manager.get(getBoundModel(), getSelector());
        if (LOG.isDebugEnabled()) {
            LOG.debug("streamView: " + o);
        }
        PrintStream ps = new PrintStream(stream);
        ps.print(o.toString());
        ps.flush();
    }
}

