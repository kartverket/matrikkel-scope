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


import java.util.ArrayList;
import java.util.HashSet;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;
import org.scopemvc.model.basic.BasicModelChangeEvent;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.4 $ $Date: 2002/10/22 00:00:09 $
 * @created 05 September 2002
 */
public class MultipleSelectionViewModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector SELECTION_SELECTOR = Selector.fromString("selection");
    /**
     * TODO: describe of the Field
     */
    public static final Selector SELECTION_COUNT_SELECTOR = Selector.fromString("selectionCount");
    /**
     * TODO: describe of the Field
     */
    public static final Selector CUSTOMERS_SELECTOR = Selector.fromString("customers");

    private ArrayList customers = new ArrayList();
    private HashSet selection = new HashSet();


    /**
     * Constructor for the MultipleSelectionViewModel object
     */
    public MultipleSelectionViewModel() {
        addCustomer(new MultipleSelectionCustomerModel("Steve", "London", 105));
        addCustomer(new MultipleSelectionCustomerModel("Trevor", "Kendal", 3));
        addCustomer(new MultipleSelectionCustomerModel("Johan", "Kendal", 40));
        addCustomer(new MultipleSelectionCustomerModel("Bert", "Eynsham", 60));

        // make all customers selected at start
        selection.addAll(customers);
    }


    /**
     * Gets the customers
     *
     * @return The customers value
     */
    public ArrayList getCustomers() {
        return customers;
    }


    /**
     * Gets the selection
     *
     * @return The selection value
     */
    public HashSet getSelection() {
        return selection;
    }


    /**
     * Gets the selection count
     *
     * @return The selectionCount value
     */
    public int getSelectionCount() {
        return selection.size();
    }


    /**
     * Sets the selection
     *
     * @param inCustomers The new selection value
     */
    public void setSelection(HashSet inCustomers) {
        selection = inCustomers;
        fireModelChange(BasicModelChangeEvent.VALUE_CHANGED, SELECTION_SELECTOR);
        fireModelChange(BasicModelChangeEvent.VALUE_CHANGED, SELECTION_COUNT_SELECTOR);
    }


    /**
     * Adds an element to the Customer attribute of the
     * MultipleSelectionViewModel object
     *
     * @param inCustomer The element to be added to the Customer attribute
     */
    public void addCustomer(MultipleSelectionCustomerModel inCustomer) {
        customers.add(inCustomer);
        fireModelChange(BasicModelChangeEvent.VALUE_ADDED, CUSTOMERS_SELECTOR);
    }
}
