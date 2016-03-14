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


import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;

/**
 * Used with {@link ListModelAdaptor} to wrap a Collection, List, Array,
 * Enumeration or Iterator as the source of a list of model objects. Useful when
 * initialising {@link org.scopemvc.view.swing.SComboBox} or {@link
 * org.scopemvc.view.swing.SList} with a static list of data.
 *
 * @author Roytman, Alex
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:46 $
 * @created 05 September 2002
 */
public class ListModelSource {

    private Object listSource;
    private Selector listSourceSelector;
    private PropertyManager listModelManager;


    /**
     * Constructor for the ListModelSource object
     *
     * @param listSourceModel TODO: Describe the Parameter
     * @param listSourceSelector TODO: Describe the Parameter
     */
    public ListModelSource(ModelChangeEventSource listSourceModel, Selector listSourceSelector) {
        this.listSource = listSourceModel;
        this.listSourceSelector = listSourceSelector;
        this.listModelManager = PropertyManager.getInstance(listSourceModel);
    }


    /**
     * Constructor for the ListModelSource object
     *
     * @param listSource TODO: Describe the Parameter
     */
    public ListModelSource(Collection listSource) {
        this.listSource = listSource;
    }


    /**
     * Constructor for the ListModelSource object
     *
     * @param listSource TODO: Describe the Parameter
     */
    public ListModelSource(List listSource) {
        this.listSource = listSource;
    }


    /**
     * Constructor for the ListModelSource object
     *
     * @param listSource TODO: Describe the Parameter
     */
    public ListModelSource(Iterator listSource) {
        this.listSource = listSource;
    }


    /**
     * Constructor for the ListModelSource object
     *
     * @param listSource TODO: Describe the Parameter
     */
    public ListModelSource(Enumeration listSource) {
        this.listSource = listSource;
    }


    /**
     * Gets the model based
     *
     * @return The modelBased value
     */
    public boolean isModelBased() {
        return (listModelManager != null);
    }


    /**
     * Gets the list
     *
     * @return The list value
     */
    public boolean isList() {
        return (getListSource() instanceof List);
    }


    /**
     * Gets the list source model
     *
     * @return The listSourceModel value
     */
    public ModelChangeEventSource getListSourceModel() {
        return (ModelChangeEventSource) listSource;
    }


    /**
     * Gets the list source
     *
     * @return The listSource value
     */
    public Object getListSource() {
        if (isModelBased()) {
            try {
                return (listSource == null) ? null : listModelManager.get(listSource, listSourceSelector);
            } catch (Exception ex) {
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                } else {
                    throw new RuntimeException(ex.toString());
                }
            }
        } else {
            return listSource;
        }
    }


    /**
     * Gets the list source selector
     *
     * @return The listSourceSelector value
     */
    public Selector getListSourceSelector() {
        return listSourceSelector;
    }


    /**
     * Adds an element to the ToList attribute of the ListModelSource object
     *
     * @param list The element to be added to the ToList attribute
     */
    public void addToList(List list) {
        Object ls = getListSource();
        if (ls == null) {
            return;
        }
        if (ls instanceof Collection) {
            list.addAll((Collection) ls);
        } else if (ls instanceof Iterator) {
            for (Iterator iter = (Iterator) ls; iter.hasNext(); ) {
                list.add(iter.next());
            }
        } else if (ls instanceof Enumeration) {
            for (Enumeration enumeration = (Enumeration) ls; enumeration.hasMoreElements(); ) {
                list.add(enumeration.nextElement());
            }
        } else if (ls.getClass().isArray()) {
            Object src[] = (Object[]) ls;
            for (int i = 0; i < src.length; i++) {
                list.add(src[i]);
            }
        } else {
            throw new IllegalArgumentException("Unsupported List Source: " + ls.getClass());
        }
    }
}
