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
 * $Id: SComboBox.java,v 1.20 2002/11/06 00:31:57 ludovicc Exp $
 * Changes:
 *  - added setPointer() (scope 2.0)
 *  - added setRendererPointer() (scope 2.0)
 *  - added setSelectionPointer() (scope 2.0)
 *  - added setSizePointer() (scope 2.0)
 */
package org.scopemvc.view.swing;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.Pointer;
import org.scopemvc.core.Control;
import org.scopemvc.core.Controller;
import org.scopemvc.core.EditorManager;
import org.scopemvc.core.Selector;
import org.scopemvc.core.View;
import org.scopemvc.util.convertor.StringConvertor;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;

/**
 * <P>
 *
 * A JComboBox that uses a {@link SComboBoxModel} to bind to model properties
 * for the list of items and to bind the selected item to a property. Uses a
 * {@link SListCellRenderer} to draw items in the list. Uses a {@link
 * SComboBoxEditor} if it's editable. </P> <P>
 *
 * SComboBox can issue Controls on selection changes. </P> <P>
 *
 * SComboBox doesn't bind itself to a model: it delegates all binding to its
 * SComboBoxModel. </P> <P>
 *
 * Examples for SComboBox use can be found in <code>samples.swing.combodemo</code>
 * package, and see {@link SComboBoxModel} and {@link SAbstractListModel}. </P>
 * <p>
 *
 * Elements (rows) in the data model can be arbitrary class (e.g. Person,
 * Employee etc.). Scope's combobox renderer can have set Selector for desired
 * element attribute (e.g. "name"). <br>
 * If selector is ommited, then toString() value is drawn. <br>
 * If elements are of types for which exist StringConvertor, the combo box can
 * be editable (exactly not the elements, but selected item property, however it
 * makes sense to have elements and selected item of the same type.) </p> <p>
 *
 * <i>Data model</i> will be typically array (<code>Object[]</code>) or a list
 * derived from <code>java.util.List</code>. For this case there is no need to
 * set the size model. For example: <pre>
 *     class SelectedPerson {
 *         ...
 *         public void setSelectedPerson(Person p) {...}
 *         public Person getSelectedPerson() {...}
 *     }
 *     ...
 *     List persons = getSomePersons();
 *     combo.setDataModel(persons);
 *     combo.setSelectionSelector("selectedPerson");  // a property of the bound view model
 * </pre> </p> <p>
 *
 * Sometimes the data elements of combobox changes during combobox life cycle.
 * <br>
 * For this case is better to use a single model for both selection and element
 * list. The list is treated as model property and can be changed: <pre>
 *     class PersonSelection extends BasicModel {
 *         public final static Selector PERSONS = Selector.fromString("persons");
 *         ...
 *         public void setSelectedPerson(Person p) {...}
 *         public Person getSelectedPerson() {...}
 *         public List getPersons() {...}
 *
 *         ...
 *             // in some method
 *             persons = fetchOtherPersons();
 *             super.fireModelChange(ModelChangeEvent.VALUE_CHANGED, PERSONS);
 *             // now are data in combobox refreshed
 *         ...
 *     }
 *     ...
 *     SComboBox combo = new SComboBox();
 *     combo.setSelector(PERSONS);
 *     combo.setSelectionSelector("selectedPerson");
 *     ...
 * </pre> <br>
 * Note that since <i>persons</i> attribute is of type <code>java.util.List</code>
 * , there is again no need to set size selector. </p> <p>
 *
 * Finally, the following example shows where the size property and selector is
 * needed. The Java Beans specification says that indexed properties can be
 * exposed as a single array value. However, this is not a requirement, as
 * SComboBox supports the use of index properties when the size of the list of
 * elements is given: <pre>
 *     class Persons  {
 *         ...
 *         public Person getPerson(int i) {...}
 *         public void setPerson(int i, Person p) {...}
 *
 *         public int getPersonsCount() {...}
 *     }
 *     ...
 *     SComboBox combo = new SComboBox();
 *     combo.setSelector("person");
 *     combo.setSizeSelector("personsCount");
 *     ...
 * </pre> </p>
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <a href="mailto:ludovicc@users.sourceforge.net">Ludovic Claude</a>
 * @version $Revision: 1.20 $ $Date: 2002/11/06 00:31:57 $
 * @created 05 September 2002
 * @see SComboBoxModel
 * @see SAbstractListModel
 * @see SList
 * @todo The comboBox should define the stringConvertor for editing the selected
 *      item as soon as the model is bound to it. It's editable only if there is
 *      a selector for the selected item, and if the bound model contains the
 *      property defined by the selector, and also if the string convertor for
 *      the class of the selected item allows convertion string to value.
 *      (ludovicc)
 */
public class SComboBox extends JComboBox
         implements View, Refreshable {

    /**
     * The view type for the combobox editor.
     *
     * @see org.scopemvc.core.EditorManager
     */
    public static final String VIEW_TYPE = "scombobox";

    private static final Log LOG = LogFactory.getLog(SComboBox.class);

    // -------------------- Controls -----------------------

    /**
     * Control to issue on selection change.
     */
    private String selectionControlID;

    // ------------- Support validation failures from selection -------------

    /**
     * Helper to manage validation state.
     */
    private ValidationHelper validationHelper = new ValidationHelper(this);

    /**
     * True if the bound property is read-only. This property affects the
     * enabled state of the component.
     */
    private boolean readOnly = false;

    /**
     * Allows the user to define the enabled state of this component. <br>
     * The component cannot be forced to enabled=true if the bouund property is
     * read-only.
     */
    private boolean userEnabled = true;

    /**
     * Constructor for the SComboBox object
     */
    public SComboBox() {
        // 'this' cannot be called while calling super, so the model is initialised
        // with a null combobox, and the real value will be set on the next line
        super(new SComboBoxModel(null));
        SComboBoxModel model = (SComboBoxModel) getModel();
        model.setComboBox(this);
        setRenderer(new SListCellRenderer());
        setEditor(new SComboBoxEditor());
        addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent inEvent) {
                    issueChangeSelectionControl();
                }
            });
        setReadOnly(!Beans.isDesignTime());
        updateUI();
    }

    // ---------- Implement View by delegation to SListModel and SListSelectionModel ----------

    /**
     * Gets the bound model
     *
     * @return The boundModel value
     */
    public final Object getBoundModel() {
        return ((SComboBoxModel) getModel()).getBoundModel();
    }


    /**
     * Gets the Selector used to identify the property that this component will
     * be bound to. <br>
     * The component will present this property to the user. <br>
     * The bound property should be an array (<code>Object[]</code>) or a list
     * derived from <code>java.util.List</code>, or the JavaBean indexed
     * property for one element of the list (eg. getPerson(int)).
     *
     * @return A selector.
     */
    public final Selector getSelector() {
        return ((SComboBoxModel) getModel()).getSelector();
    }


    /**
     * Gets the selector for the selected item
     *
     * @return The selector for the selected item
     */
    public final Selector getSelectionSelector() {
        return ((SComboBoxModel) getModel()).getSelectionSelector();
    }


    /**
     * Gets the selector for the item to render in the SComboBox list.
     *
     * @return The selector for the item to render
     * @see SListCellRenderer
     */
    public final Selector getRendererSelector() {
        SListCellRenderer renderer = (SListCellRenderer) getRenderer();
        return renderer.getTextSelector();
    }


    /**
     * Gets the selector for the icon to render in the SComboBox list.
     *
     * @return The selector for the icon to render
     * @see SListCellRenderer
     */
    public final Selector getRendererIconSelector() {
        SListCellRenderer renderer = (SListCellRenderer) getRenderer();
        return renderer.getIconSelector();
    }


    /**
     * Gets the selector for the property giving the size of the list of items.
     *
     * @return The selector for the list size.
     */
    public final Selector getSizeSelector() {
        return ((SComboBoxModel) getModel()).getSizeSelector();
    }

    /**
     * Gets the control ID to be issued when the selection changes.
     *
     * @return The control ID
     */
    public final String getChangeSelectionControlID() {
        return selectionControlID;
    }


    // -------------------- Controls -----------------------

    /**
     * Gets the Controller for this View. <br>
     * Don't assign a Controller to this component, instead delegate to the
     * containing SwingView that has a parent Controller.
     *
     * @return The controller value - always null here
     */
    public Controller getController() {
        return null;
    }

    /**
     * Issues a control notifying that the selection has changed.
     */
    public void issueChangeSelectionControl() {
        if (selectionControlID != null) {
            Control control = new Control(selectionControlID);
            issueControl(control);
        }
    }


    /**
     * Issue a Control to the View's parent (owner) Controller.
     *
     * @param inControl The Control to issue
     */
    public void issueControl(Control inControl) {
        SwingUtil.issueControl(this, inControl);
    }


    /**
     * Sets the bound model. <br>
     * Updates also the combobox editor to match the type of the selected item.
     *
     * @param inModel The new boundModel value
     * @see org.scopemvc.core.EditorManager
     */
    public final void setBoundModel(Object inModel) {
        SComboBoxModel comboBoxModel = (SComboBoxModel) getModel();
        comboBoxModel.setBoundModel(inModel);
        if (inModel != null) {
            // init the editor according to the type of the selected item to edit
            EditorManager manager = EditorManager.getInstance(inModel);
            View editor = manager.getEditor(VIEW_TYPE, comboBoxModel.getBoundSelectionModel(),
                    comboBoxModel.getSelectionSelector());
            if (editor == null) {
                LOG.warn("Combobox editor not found for property "
                        + Selector.asString(comboBoxModel.getSelectionSelector()));
            } else if (!(editor instanceof ComboBoxEditor)) {
                LOG.warn("Editor of class " + editor.getClass() + " doesn't implement ComboBoxEditor");
            } else {
                setEditor((ComboBoxEditor) editor);
            }
        } else {
            setEditor(null);
        }
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setPointer(String pointerPath) {
        setSelector(pointerPath);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointer a pointer to the model
     */
    public void setPointer(Pointer pointer) {
        setSelector(pointer.getSelector());
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setSelectionPointer(String pointerPath) {
        setSelectionSelector(pointerPath);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointer a pointer to the model
     */
    public void setSelectionPointer(Pointer pointer) {
        setSelectionSelector(pointer.getSelector());
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setSizePointer(String pointerPath) {
        setSizeSelector(pointerPath);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setSelector(..)</code> for now.
     *
     * @param pointer a pointer to the model
     */
    public void setSizePointer(Pointer pointer) {
        setSizeSelector(pointer.getSelector());
    }


    /**
     * Set the Selector for the list data. Should be a java.util.List or an
     * Object[] or have a "size" property and properties accessible by an
     * IntIndexedSelector.
     *
     * @param inSelector The new selector value
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSelector(Selector inSelector) {
        ((SComboBoxModel) getModel()).setSelector(inSelector);
    }


    /**
     * Set the Selector for the list data. Should be a java.util.List or an
     * Object[] or have a "size" property and properties accessible by an
     * IntIndexedSelector.
     *
     * @param inSelectorString The string representation of the selector
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSelector(String inSelectorString) {
        ((SComboBoxModel) getModel()).setSelector(inSelectorString);
    }

    /**
     * Set the Selector for the list data. Should be a java.util.List or an
     * Object[] or have a "size" property and properties accessible by an
     * IntIndexedSelector.
     *
     * @param inSelectorString The string representation of the selector
     * @deprecated Use setSelector(String) instead
     */
    public final void setSelectorString(String inSelectorString) {
        setSelector(inSelectorString);
    }

    /**
     * Set the Selector for the list selection: this property will be bound to
     * the list's single selection.
     *
     * @param inSelector The new selectionSelector value
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSelectionSelector(Selector inSelector) {
        ((SComboBoxModel) getModel()).setSelectionSelector(inSelector);
    }


    /**
     * Set the Selector for the list selection: this property will be bound to
     * the list's single selection.
     *
     * @param inSelectorString The string representation of the
     *      selectionSelector
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSelectionSelector(String inSelectorString) {
        ((SComboBoxModel) getModel()).setSelectionSelectorString(inSelectorString);
    }

    /**
     * Set the Selector for the list selection: this property will be bound to
     * the list's single selection.
     *
     * @param inSelectorString The string representation of the
     *      selectionSelector
     * @deprecated Use setSelectionSelector(String) instead
     */
    public final void setSelectionSelectorString(String inSelectorString) {
        setSelectionSelector(inSelectorString);
    }

    /**
     * Optional: set the Selector for the property that is the size of the items
     * list. Not needed for lists that are of type Object[] or java.util.List.
     *
     * @param inSelector The new sizeSelector value
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSizeSelector(Selector inSelector) {
        ((SComboBoxModel) getModel()).setSizeSelector(inSelector);
    }


    /**
     * Optional: set the Selector for the property that is the size of the items
     * list. Not needed for lists that are of type Object[] or java.util.List.
     *
     * @param inSelectorString The string representation of the sizeSelector
     * @deprecated Will be removed in Scope 2.0
     */
    public final void setSizeSelector(String inSelectorString) {
        ((SComboBoxModel) getModel()).setSizeSelector(inSelectorString);
    }

    /**
     * Optional: set the Selector for the property that is the size of the items
     * list. Not needed for lists that are of type Object[] or java.util.List.
     *
     * @param inSelectorString The string representation of the sizeSelector
     * @deprecated Use setSizeSelector(String) instead
     */
    public final void setSizeSelectorString(String inSelectorString) {
        setSizeSelector(inSelectorString);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setRendererSelector(..)</code> for now.
     *
     * @param pointerPath the path for a pointer to the model
     */
    public void setRendererPointer(String pointerPath) {
        setRendererSelector(pointerPath);
    }

    /**
     * For forward compability with Scope 2.0. Delegates to <code>setRendererSelector(..)</code> for now.
     *
     * @param pointer a pointer to the model
     */
    public void setRendererPointer(Pointer pointer) {
        setRendererSelector(pointer.getSelector());
    }

    /**
     * Set the Selector for the list cell renderer: this is the property that
     * will be shown in a list cell (converted to a String).
     *
     * @param inSelector The new rendererSelector value
     * @deprecated Will be removed from Scope 2.0
     */
    public final void setRendererSelector(Selector inSelector) {
        SListCellRenderer renderer = (SListCellRenderer) getRenderer();
        renderer.setTextSelector(inSelector);
    }

    /**
     * Set the Selector for the list cell renderer: this is the property that
     * will be shown in a list cell (converted to a String).
     *
     * @param inSelectorString The string representation of the rendererSelector
     * @deprecated Will be removed from Scope 2.0
     */
    public final void setRendererSelector(String inSelectorString) {
        SListCellRenderer renderer = (SListCellRenderer) getRenderer();
        renderer.setTextSelector(inSelectorString);
    }


    /**
     * Set the Selector for the list cell renderer: this is the property that
     * will be shown in a list cell (converted to a String).
     *
     * @param inSelectorString The string representation of the rendererSelector
     * @deprecated Use setRendererSelector(String) instead
     */
    public final void setRendererSelectorString(String inSelectorString) {
        setRendererSelector(inSelectorString);
    }


    /**
     * Optional: Set the Selector for the list cell renderer to get an Icon:
     * this is the property that will be shown as an Icon in a list cell.
     *
     * @param inSelector The new rendererIconSelector value
     */
    public final void setRendererIconSelector(Selector inSelector) {
        SListCellRenderer renderer = (SListCellRenderer) getRenderer();
        renderer.setIconSelector(inSelector);
    }


    /**
     * Optional: Set the Selector for the list cell renderer to get an Icon:
     * this is the property that will be shown as an Icon in a list cell.
     *
     * @param inSelectorString The string representation of the
     *      rendererIconSelector
     */
    public final void setRendererIconSelector(String inSelectorString) {
        SListCellRenderer renderer = (SListCellRenderer) getRenderer();
        renderer.setIconSelector(inSelectorString);
    }

    /**
     * Optional: Set the Selector for the list cell renderer to get an Icon:
     * this is the property that will be shown as an Icon in a list cell.
     *
     * @param inSelectorString The string representation of the
     *      rendererIconSelector
     * @deprecated Use setRendererIconSelector(String) instead
     */
    public final void setRendererIconSelectorString(String inSelectorString) {
        setRendererIconSelector(inSelectorString);
    }

    /**
     * Set the Control ID for the Control that will be issued when the selection
     * is changed. If null no Control will be issued.
     *
     * @param inControlID The new changeSelectionControlID value
     */
    public final void setChangeSelectionControlID(String inControlID) {
        selectionControlID = inControlID;
    }

    /**
     * Enables or disables this component, depending on the value of the
     * parameter <code>b</code>. An enabled component can respond to user input
     * and generate events. Components are enabled initially by default.
     *
     * @param inEnabled If <code>true</code>, this component is enabled;
     *      otherwise this component is disabled.
     */
    public void setEnabled(boolean inEnabled) {
        userEnabled = inEnabled;
        super.setEnabled(userEnabled && !readOnly);
    }

    /**
     * Sets the look and feel (L&amp;F) object that renders this component. <br>
     * Overriden here because there is no createDefaultCellRenderer() method in
     * JComboBox.
     *
     * @param inUI the ComboBoxUI L&amp;F object
     * @see javax.swing.UIDefaults#getUI
     */
    public void setUI(ComboBoxUI inUI) {
        if (getRenderer() == null) {
            setRenderer(new SListCellRenderer());
        }
        super.setUI(inUI);
    }

    /**
     * <p>
     *
     * Sets the renderer that paints the item selected from the list in the
     * SComboBox field. The renderer is used if the SComboBox is not editable.
     * If it is editable, the editor is used to render and edit the selected
     * item. </p> <p>
     *
     * You need to use a SListCellRenderer in the SComboBox, as this renderer
     * uses the bound model to display the information. </p>
     *
     * @param inRenderer the <code>SListCellRenderer</code> that displays the
     *      selected item
     * @see #setRendererSelector
     */
    public void setRenderer(ListCellRenderer inRenderer) {
        if (!(inRenderer instanceof SListCellRenderer)) {
            throw new IllegalArgumentException("Renderer must be an instance of SListCellRenderer");
        }
        super.setRenderer(inRenderer);
    }


    /**
     * <p>
     *
     * This method is overriden to fix JDK 1.2.x,1.3.x bug: after setting model
     * combobox sets element 0 as selected. This is violation of MVC paradigm -
     * model describes some state and should not be modified by view. JDK 1.4
     * beta has this bug fixed. </p> <p>
     *
     * Model must be an instance of SComboBoxModel.</p>
     *
     * @param inModel The new model value
     * @see SComboBoxModel#setSelectedItem(Object)
     */
    public void setModel(ComboBoxModel inModel) {
        if (!(inModel instanceof SComboBoxModel)) {
            throw new IllegalArgumentException("Model must be an instance of SComboBoxModel");
        }
        SComboBoxModel m = (SComboBoxModel) inModel;
        m.setJdk13FixIgnoreSetSelectedItem(true);
        super.setModel(m);
        m.setJdk13FixIgnoreSetSelectedItem(false);
    }

    /**
     * Force use of this StringConvertor instead of automatically finding one to
     * match the datatype being edited.
     *
     * @param inConvertor The new stringConvertor value
     */
    public void setStringConvertor(StringConvertor inConvertor) {
        if (getEditor() instanceof SComboBoxEditor) {
            SComboBoxEditor editor = (SComboBoxEditor) getEditor();
            editor.setStringConvertor(inConvertor);
        }
    }

    // --------- Set up list binding by delegation -------------

    /**
     * Can use this method to specify a static list model for the contents of
     * the list rather than binding to a dynamic property of some view model.
     *
     * @param inModel The new listModel value
     * @see org.scopemvc.model.collection.ListModelAdaptor
     */
    public void setListModel(Object inModel) {
        ((SComboBoxModel) getModel()).setListModel(inModel);
    }

    /**
     * Don't assign a Controller to this component, instead delegate to the
     * containing SwingView that has a parent Controller.
     *
     * @param inController The new controller value
     */
    public void setController(Controller inController) {
        throw new UnsupportedOperationException("Can't assign a Controller to a " + getClass());
    }

    // ------------------ Refreshable -------------------------

    /**
     * Updates the component with the current state of the bound model.
     */
    public void refresh() {
        ((SComboBoxModel) getModel()).refresh();
    }


    /**
     * Validation failed while getting a value from this component into the
     * bound model object.
     *
     * @param inException The exception causing the validation failure
     */
    public void validationFailed(Exception inException) {
        validationHelper.validationFailed(inException);
    }


    /**
     * Clears previous validation failure.
     */
    public void validationSuccess() {
        validationHelper.validationSuccess();
    }


    /**
     * Returns the instance of JToolTip that should be used to display the
     * tooltip. <br>
     * In case of failure, the error message contained in the validation
     * exception is displayed, else the standard tooltip for the component is
     * used.
     *
     * @return The tooltip for the component
     * @see #validationFailed(Exception)
     */
    public JToolTip createToolTip() {
        return validationHelper.createToolTip(super.createToolTip());
    }

    /**
     * Defines if the bound property is read-only in the model. <br>
     * This affects the enabled state of the component
     *
     * @param inReadOnly true if the bound property is read-only in the model
     */
    void setReadOnly(boolean inReadOnly) {
        readOnly = inReadOnly;
        super.setEnabled(userEnabled && !readOnly);
    }
}

