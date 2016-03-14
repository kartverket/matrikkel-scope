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
package samples.filefind;


import java.util.List;
import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;

/**
 * Model is passed into ctor. Expected to be a List of FileProperties.
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:48 $
 * @created 05 September 2002
 */
public class SearchResultsController extends BasicController {

    /**
     * TODO: describe of the Field
     */
    public static final String CLOSE_RESULTS_ID = "CLOSE_RESULTS";


    /**
     * Constructor for the SearchResultsController object
     *
     * @param inFileProperties TODO: Describe the Parameter
     */
    public SearchResultsController(List inFileProperties) {
        setModel(inFileProperties);
        setView(new SearchResultsView());
    }


    /**
     * TODO: document the method
     *
     * @param inControl TODO: Describe the Parameter
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        if (inControl.matchesID(CLOSE_RESULTS_ID)) {
            doCloseResults();
        }
    }


    private void doCloseResults() throws ControlException {
        shutdown();
        // on close the window, commit suicide
    }
}
