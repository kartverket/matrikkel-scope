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
 * $Id: SAbstractListModel.java,v 1.20 2002/10/03 15:38:15 ludovicc Exp $
 */
package org.scopemvc.view.swing;


import java.util.Collection;
import java.util.Comparator;
import javax.swing.AbstractListModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.IntIndexSelector;
import org.scopemvc.core.ModelChangeEvent;
import org.scopemvc.core.ModelChangeEventSource;
import org.scopemvc.core.ModelChangeListener;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.model.collection.ListModelAdaptor;
import org.scopemvc.model.collection.ListModelSource;
import org.scopemvc.util.Debug;
import org.scopemvc.view.util.ModelBindable;

/**
 * <P>
 *
 * A javax.swing.AbstractListModel bound to a property of a model used by {@link
 * SList}, {@link SComboBox}, {@link STable}. The property should have elements
 * accessible using IntIndexedSelectors and needs to fulfill one of the
 * following criteria:
 * <OL>
 *   <LI> be a java.util.List</LI>
 *   <LI> be an Object[]</LI>
 *   <LI> optional: have an accessible 'size' property in the parent view model
 *   (see below) </LI>
 * </OL>
 * Unfortunately this means that JavaBeans indexed properties cannot be bound
 * directly to an SAbstractListModel because there is no way to discover the
 * size of such a list, <B>unless</B> a separate property can be accessed to
 * provide the size of the list using {@link #setSizeSelector} or {@link
 * #setSizeSelectorString}. </P> <P>
 *
 * If required, the model for items can be specified as a static
 * ListModelAdapter when it is more convenient to specify the list model at
 * initialisation of (for example) an SComboBox rather than include it in the
 * view model for active binding. </P> <P>
 *
 * The list can present its elements as a sorted list if a Comparator is passed
 * to {@link #setSorted(java.util.Comparator)} or all list elements implement
 * Comparable and {@link #setSorted(boolean)} is called. </P> <P>
 *
 * ***** This implementation makes no provisions for thread-safety. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.20 $ $Date: 2002/10/03 15:38:15 $
 * @created 05 September 2002
 * @see SList
 * @see STable
 * @see SComboBox
 * @see org.scopemvc.model.collection.ListModelAdaptor
 * @todo Check that the implementation is thread-safe; there is a partial
 *      solution with the modelChanged() method, but more may be needed
 *      (ludovicc)
 */
public abstract class SAbstractListModel extends AbstractListModel
         implements ModelBindable, Refreshable, ModelChangeListener {

    private static final Log LOG = LogFactory.getLog(SAbstractListModel.class);

    /**
     * Share this instance for all fetches from the list. Synchronize on it
     * first!
     */
    private static final IntIndexSelector INDEX_SELECTOR = Selector.fromInt(0);

    /**
     * The Selector for the size property in the model, may be null
     */
    private Selector sizeSelector;

    // ------------------ Allow fixed list model to be set -------------------

    /**
     * The bound model containing the list of items
     */
    private Object listModel;

    private boolean sorted;
    private Comparator comparator;

    // ------------------- Delegate to SwingBoundModel -------------------

    /**
     * Helper to manage model to view binding.
     */
    private SwingBoundModel boundModel = new SwingBoundModel(this);

    // --------------------- shownModel -----------------------

    /**
     * The (list) model object that the SListModel presents, which may be a
     * property of the bound model if a Selector is specified. May be null if
     * the bound property is a JavaBeans indexed property.
     */
    private Object shownModel;

    /**
     * The property manager for the bound (list) model.
     */
    private PropertyManager manager;


    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return boundModel.getBoundModel();
    }


    /**
     * Gets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The component will present this property to the user.
     *
     * @return A selector.
     */
    public final Selector getSelector() {
        return boundModel.getSelector();
    }


    /**
     * Get the current value (what would be set as a property of the bound model
     * object) being presented on the View.
     *
     * @return property's value from the UI.
     */
    public final Object getViewValue() {
        return getShownModel();
    }


    /**
     * Get the (list) model object that the SListModel presents, which may be a
     * property of the bound model if a Selector is specified. <br>
     * May be null if the bound property is a JavaBeans indexed property.
     *
     * @return The shownModel value
     */
    public final Object getShownModel() {
        return shownModel;
    }

    // ----------- Allow size of list to come from independent Selector --------

    /**
     * Gets the size selector
     *
     * @return The sizeSelector value
     */
    public Selector getSizeSelector() {
        return sizeSelector;
    }


    // ---------------- Allow list to be sorted -----------------------

    /**
     * Returns true if the list is sorted
     *
     * @return true if the list is sorted
     */
    public boolean isSorted() {
        return sorted;
    }


    // ------------------------- Implement javax.swing.AbstractListModel ----------------------------

    /**
     * Gets the size of the list
     *
     * @return The size of the list
     */
    public int getSize() {
        /*
         * if (getBoundModel() == null) {
         * if (LOG.isDebugEnabled()) LOG.debug("getSize: null getBoundModel(): 0");
         * return 0;
         * } else
         */
        if (sizeSelector != null) {
            try {
                PropertyManager manager = boundModel.getPropertyManager();
                if (manager == null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("getSize: no manager for: " + shownModel + ": size 0");
                    }
                    return 0;
                }
                Integer size = (Integer) manager.get(getBoundModel(), sizeSelector);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getSize: from (" + sizeSelector + "): size " + size.intValue());
                }
                return size.intValue();
            } catch (Exception e) {
                LOG.warn("getSize: Can't get size using sizeSelector: " + sizeSelector, e);
                return 0;
            }
        } else if (shownModel instanceof java.util.List) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getSize: from java.util.List: size " + ((java.util.List) shownModel).size());
            }
            return ((java.util.List) shownModel).size();
        } else if (shownModel instanceof Object[]) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getSize: from Object[]: size " + ((Object[]) shownModel).length);
            }
            return ((Object[]) shownModel).length;
        } else if (shownModel instanceof ListModelAdaptor) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getSize: from adaptor: size " + ((ListModelAdaptor) shownModel).getSize());
            }
            return ((ListModelAdaptor) shownModel).getSize();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("getSize: from null shownModel: size 0");
            }
            return 0;
        }
    }

    // ------------------ implement AstractListModel -------------------------

    /**
     * Returns the value at the specified index. <br>
     * Note that if the get() fails for any reason, a null is returned.
     *
     * @param inIndex The index of the element in the list
     * @return The value at the specified index.
     */
    public Object getElementAt(int inIndex) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getElementAt: " + inIndex);
        }

        if (shownModel == null && getBoundModel() == null) {
            LOG.warn("getElementAt: null model trying to get element: " + inIndex);
            return null;
        } else if (shownModel instanceof java.util.List) {
            return ((java.util.List) shownModel).get(inIndex);
        } else if (shownModel instanceof Object[]) {
            return ((Object[]) shownModel)[inIndex];
        } else if (shownModel instanceof ListModelAdaptor) {
            return ((ListModelAdaptor) shownModel).getElementAt(inIndex);
        } else {
            try {
                synchronized (INDEX_SELECTOR) {
                    INDEX_SELECTOR.setIndex(inIndex);
                    if (shownModel == null) {
                        // JavaBeans indexed property... this is ugly
                        Selector s = boundModel.getSelector().deepClone();
                        s.chain(INDEX_SELECTOR);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Indexed: " + s);
                        }
                        return boundModel.getPropertyManager().get(getBoundModel(), s);
                    } else {
                        if (manager != null) {
                            return manager.get(shownModel, INDEX_SELECTOR);
                        }
                    }
                }
            } catch (Exception e) {
                LOG.warn("Can't get element " + inIndex + " from: " + shownModel, e);
            }
        }
        return null;
    }


    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelector The new selector to use
     */
    public final void setSelector(Selector inSelector) {
        boundModel.setSelector(inSelector);
    }


    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelectorString The string representation of the selector
     * @see Selector#fromString
     */
    public void setSelector(String inSelectorString) {
        boundModel.setSelector(inSelectorString);
    }

    /**
     * Sets the Selector used to identify the property that this component will
     * be bound to. <br>
     * This component will present this property to the user.
     *
     * @param inSelectorString The string representation of the selector
     * @deprecated Use setSelector(String) instead
     */
    public void setSelectorString(String inSelectorString) {
        setSelector(inSelectorString);
    }


    /**
     * Sets the selector for the size property
     *
     * @param inSelector The new sizeSelector value
     */
    public void setSizeSelector(Selector inSelector) {
        sizeSelector = inSelector;
    }


    /**
     * Sets the selector for the size property
     *
     * @param inSelectorString The string representation of the selector for the
     *      size property
     */
    public void setSizeSelector(String inSelectorString) {
        if (inSelectorString == null) {
            setSizeSelector((Selector) null);
        } else {
            setSizeSelector(Selector.fromString(inSelectorString));
        }
    }


    /**
     * Sets the selector for the size property
     *
     * @param inSelectorString The string representation of the selector for the
     *      size property
     * @deprecated Use setSizeSelector(String) instead
     */
    public void setSizeSelectorString(String inSelectorString) {
        setSizeSelector(inSelectorString);
    }


    /**
     * Can use this to specify a static list model for the contents of the list
     * rather than binding to a dynamic property of some view model.
     *
     * @param inModel The new listModel value
     * @see org.scopemvc.model.collection.ListModelAdaptor
     */
    public void setListModel(Object inModel) {
        listModel = inModel;
        setShownModel(listModel);
    }


    /**
     * Sets if the list is sorted. <br>
     * This uses the natural comparator on the list items if true.
     *
     * @param inSorted The new sorted value
     */
    public void setSorted(boolean inSorted) {
        sorted = true;
        boundModel.updateFromModel(ModelChangeEvent.VALUE_CHANGED);
    }


    /**
     * Sets if the list is sorted. <br>
     * This uses the passed comparator on the list items if not null.
     *
     * @param inComparator The comparator to use to sort the list, or null to
     *      have no sorting
     */
    public void setSorted(Comparator inComparator) {
        if (inComparator == null) {
            setSorted(false);
            comparator = null;
        } else {
            setSorted(true);
            comparator = inComparator;
        }
        boundModel.updateFromModel(ModelChangeEvent.VALUE_CHANGED);
    }


    /**
     * @param inComparator The new comparator value
     * @deprecated see setSorted(Comparator)
     */
    public void setComparator(Comparator inComparator) {
        setSorted(inComparator);
    }


    /**
     * Sets the bound model
     *
     * @param inModel The new boundModel value
     */
    public void setBoundModel(Object inModel) {
        boundModel.setBoundModel(inModel);
    }


    // --------------------- Implement ModelBindable ----------------------

    /**
     * Use the passed property value and read-only state to update the View.
     * <BR>
     * Ignores inReadOnly because it makes no sense here.
     *
     * @param inValue The new value of the property in the bound model
     * @param inReadOnly The new read-only state of the property
     */
    public void updateFromProperty(Object inValue, boolean inReadOnly) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("updateFromProperty: " + inValue + ", " + inReadOnly);
        }

        setShownModel(inValue);
    }


    /**
     * Validation failed while getting a value from this component into the
     * bound model object. <BR>
     * Does nothing here, as the component cannot change the property.
     *
     * @param inException The exception causing the validation failure
     */
    public void validationFailed(Exception inException) {
        // noop
    }

    /**
     * Clears previous validation failure. <BR>
     * Does nothing here, as the component cannot change the property.
     */
    public void validationSuccess() {
        // noop
    }


    // ------------------ Refreshable -------------------------

    /**
     * Updates this list model with the current state of the bound model.
     */
    public void refresh() {
        Object propertyValue = boundModel.getPropertyValue();
        boolean propertyReadOnly = boundModel.getPropertyReadOnly();
        updateFromProperty(propertyValue, propertyReadOnly);
    }


    // --------- Implement ModelChangeListener for the shownModel -------------

    /**
     * <P>
     *
     * Invoked to notify listeners of a change in the state of a {@link
     * ModelChangeEventSource}. </P> <P>
     *
     * ListDataEvent are fired by this method from the Swing event thread. </P>
     *
     * @param inEvent the {@link ModelChangeEvent} representing the change in
     *      state of the ModelChangeEventSource.
     */
    public void modelChanged(final ModelChangeEvent inEvent) {
        SwingUtil.runFromSwingEventThread(
            new Runnable() {
                public void run() {
                    doModelChanged(inEvent);
                }
            });
    }

    /**
     * Gets the manager for the bound (list) model.
     *
     * @return The manager value
     */
    protected final PropertyManager getManager() {
        return manager;
    }


    /**
     * Called internally from updateFromProperty().
     *
     * @param inModel The new shownModel value
     */
    protected void setShownModel(Object inModel) {

        // If we've got a static list model then don't bind to dynamic
        // property here.
        if (listModel != null && inModel != listModel) {
            return;
        }

        if (shownModel == inModel) {
            // ensure refresh does something even when bound to a non-ModelChangeSource
            // ... this might turn out to be a bad idea but I can't see another way right now.
            fireContentsChanged(this, 0, Integer.MAX_VALUE);
            //getSize() - 1);
            return;
        }

        if (shownModel instanceof ListModelAdaptor) {
            // To allow GC
            ((ListModelAdaptor) shownModel).removeModelChangeListeners();
        }

        if (shownModel instanceof ModelChangeEventSource) {
            ((ModelChangeEventSource) shownModel).removeModelChangeListener(this);
        }

        shownModel = inModel;

        if (isSorted() && shownModel instanceof Collection) {
            shownModel = new ListModelAdaptor(new ListModelSource((Collection) inModel));
            ((ListModelAdaptor) shownModel).setComparator(comparator);
            ((ListModelAdaptor) shownModel).setSorted(true);
        }

        if (shownModel != null) {
            manager = PropertyManager.getInstance(shownModel);
        }

        if (shownModel instanceof ModelChangeEventSource) {
            ((ModelChangeEventSource) shownModel).addModelChangeListener(this);
        }

        shownModelChanged();
        fireContentsChanged(this, 0, getSize() - 1);
    }


    /**
     * Notifies of a change in the shown model. <br>
     * Used by SComboBoxModel for updating its selected item
     */
    protected void shownModelChanged() {
        // noop
    }

    /**
     * Respond to a change in the model by firing the appropriate ListDataEvent
     *
     * @param inEvent the {@link ModelChangeEvent} representing the change in
     *      state of the ModelChangeEventSource.
     */
    protected void doModelChanged(ModelChangeEvent inEvent) {
        if (Debug.ON) {
            Debug.assertTrue(inEvent.getModel() == shownModel, "not my model: " + inEvent.getModel());
        }

        Selector selector = inEvent.getSelector();
        int index0 = 0;
        int index1 = getSize() - 1;
        if (selector instanceof IntIndexSelector) {
            index0 = ((IntIndexSelector) selector).getIndex();
            index1 = index0;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("modelChanged: " + inEvent.getModel() + ", selector is " + selector);
        }

        if (inEvent.getType() == ModelChangeEvent.ACCESS_CHANGED) {
            // implement when lists are editable
            return;
        } else if (inEvent.getType() == ModelChangeEvent.VALUE_ADDED) {
            fireIntervalAdded(this, index0, index1);
        } else if (inEvent.getType() == ModelChangeEvent.VALUE_REMOVED) {
            fireIntervalRemoved(this, index0, index1);
        } else if (inEvent.getType() == ModelChangeEvent.VALUE_CHANGED) {
            fireContentsChanged(this, index0, index1);
        }
    }

    /**
     * Returns true if the bound model contains the value in its list of
     * elements
     *
     * @param inValue The value to check
     * @return true if the value belongs to the list of bound items
     */
    protected boolean containsElement(Object inValue) {
        // this implementation is slow and is reserved for validation purposes only
        int n = getSize();
        for (int i = 0; i < n; i++) {
            Object elem = getElementAt(i);
            if (elem == inValue || elem != null && elem.equals(inValue)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Element " + inValue + " is contained in the bound list");
                }
                return true;
            }
        }
        LOG.debug("Element " + inValue + " isn't contained in the bound list");
        return false;
    }
}
