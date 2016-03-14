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
package org.scopemvc.model.collection;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.core.ModelChangeTypes;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;

/**
 * <P>
 *
 * Used to present a list of {@link ListModelSource}s as an active BasicModel.
 * Useful when initialising {@link org.scopemvc.view.swing.SComboBox} or {@link
 * org.scopemvc.view.swing.SList} with a static list of data. </P> <P>
 *
 * This will propagate ModelChangeEvents from sublists that are
 * ModelChangeEventSources. Note that this adaptor registers as a listener with
 * such sublists: it may be necessary to manually unregister with {@link
 * #removeModelChangeListeners} if an adaptor is no longer needed. </P> <P>
 *
 * The adaptor can present its ListModelSources as a sorted list if a Comparator
 * is passed to {@link #setComparator} or all list elements implement Comparable
 * and {@link #setSorted} is called. </P>
 *
 * @author Roytman, Alex
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/12 10:51:03 $
 * @created 05 September 2002
 */
public class ListModelAdaptor extends BasicModel
         implements ModelChangeListener {

    /**
     * TODO: describe of the Field
     */
    public static final Selector LIST_SELECTOR = Selector.fromString("list");
    /**
     * TODO: describe of the Field
     */
    public static final Selector LIST_SIZE_SELECTOR = Selector.fromString("size");

    private List list;
    private ListModelSource listSources[];

    private boolean sorted;
    private Comparator comparator;


    /**
     * Constructor for the ListModelAdaptor object
     *
     * @param listSources TODO: Describe the Parameter
     */
    public ListModelAdaptor(ListModelSource listSources[]) {
        this.listSources = listSources;
        for (int i = 0; i < listSources.length; i++) {
            if (listSources[i].isModelBased()) {
                listSources[i].getListSourceModel().addModelChangeListener(this);
            }
        }
    }


    /**
     * Constructor for the ListModelAdaptor object
     *
     * @param listSource TODO: Describe the Parameter
     */
    public ListModelAdaptor(ListModelSource listSource) {
        this(new ListModelSource[]{listSource});
    }


    /**
     * Gets the list
     *
     * @return The list value
     */
    public List getList() {
        if (list == null) {
            loadList();
        }
        return list;
    }


    /**
     * Gets the size
     *
     * @return The size value
     */
    public int getSize() {
        if (list == null) {
            loadList();
        }
        return list.size();
    }


    /**
     * Gets the element at
     *
     * @param inIndex TODO: Describe the Parameter
     * @return The elementAt value
     */
    public Object getElementAt(int inIndex) {
        if (list == null) {
            loadList();
        }
        if (list == null) {
            return null;
        } else {
            return list.get(inIndex);
        }
    }


    /**
     * Gets the sorted
     *
     * @return The sorted value
     */
    public boolean isSorted() {
        return sorted;
    }


    /**
     * Sets the sorted
     *
     * @param inSorted The new sorted value
     */
    public void setSorted(boolean inSorted) {
        sorted = true;
    }


    /**
     * Sets the comparator
     *
     * @param inComparator The new comparator value
     */
    public void setComparator(Comparator inComparator) {
        if (inComparator == null) {
            setSorted(false);
            comparator = null;
            list = null;
        } else {
            setSorted(true);
            comparator = inComparator;
            list = null;
        }
        fireModelChange(ModelChangeTypes.VALUE_CHANGED, LIST_SELECTOR);
    }


    /**
     * TODO: document the method
     */
    public void removeModelChangeListeners() {
        if (listSources != null) {
            for (int i = 0; i < listSources.length; i++) {
                if (listSources[i].isModelBased()) {
                    listSources[i].getListSourceModel().removeModelChangeListener(this);
                }
            }
        }
    }


    /**
     * TODO: document the method
     *
     * @param inEvent TODO: Describe the Parameter
     */
    public void modelChanged(ModelChangeEvent inEvent) {
        if (list != null && isListChangeEvent(inEvent)) {
            loadList();
            fireModelChange(ModelChangeTypes.VALUE_CHANGED, LIST_SELECTOR);
        }
    }


    /**
     * TODO: document the method
     */
    protected void loadList() {
        if (listSources.length == 1 && listSources[0].isList() && !isSorted()) {
            list = (List) listSources[0].getListSource();
            return;
        }

        if (list == null) {
            list = new ArrayList();
        } else {
            list.clear();
        }

        for (int i = 0; i < listSources.length; i++) {
            listSources[i].addToList(list);
        }

        if (isSorted() && comparator != null) {
            Collections.sort(list, comparator);
        } else if (isSorted()) {
            Collections.sort(list);
        }
    }


    private boolean isListChangeEvent(ModelChangeEvent inEvent) {
        for (int i = 0; i < listSources.length; i++) {
            if (listSources[i].isModelBased() &&
                    inEvent.getModel().equals(listSources[i].getListSourceModel()) &&
                    (inEvent.getSelector() == null || inEvent.getSelector().equals(listSources[i].getListSourceSelector()))) {
                return true;
            }
        }
        return false;
    }
}
