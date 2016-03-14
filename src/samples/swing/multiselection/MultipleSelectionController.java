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
package samples.swing.multiselection;


import org.scopemvc.controller.basic.BasicController;
import org.scopemvc.core.Control;
import org.scopemvc.core.ControlException;

/**
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:58 $
 * @created 05 September 2002
 */
public class MultipleSelectionController extends BasicController {

    /**
     * TODO: describe of the Field
     */
    public static final String SELECTION_CHANGED = "SelectionChanged";

    private MultipleSelectionViewModel myModel = new MultipleSelectionViewModel();
    private MultipleSelectionView myView = new MultipleSelectionView();

    /**
     * Constructor for the MultipleSelectionController object
     */
    public MultipleSelectionController() {
        setModel(myModel);
        setView(myView);
    }


    /**
     * Respond to SELECTION_CHANGED.
     *
     * @param inControl TODO: Describe the Parameter
     * @throws ControlException TODO: Describe the Exception
     */
    protected void doHandleControl(Control inControl) throws ControlException {
        if (inControl.matchesID(SELECTION_CHANGED)) {
            doSelected();
        }
    }


    /**
     * On SELECTED, check that the user has selected at least two customer or
     * display a little help message.
     */
    protected void doSelected() {
        boolean selected = myModel.getSelectionCount() > 1;
        myView.setSelectedManyCustomers(selected);
    }
}
