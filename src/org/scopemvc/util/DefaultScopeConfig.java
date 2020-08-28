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
 * $Id: DefaultScopeConfig.java,v 1.14 2002/11/20 00:14:00 ludovicc Exp $
 */
package org.scopemvc.util;

import java.awt.Color;
import java.text.DateFormat;
import java.util.ListResourceBundle;

/**
 * <P>
 *
 * Default resources for Scope. Can be augmented with a new ResourceBundle to
 * customize Scope behaviour, in which case, see {@link
 * ScopeConfig#setPropertiesName}. Custom configuration specified this way
 * overrides the defaults in this resource. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.14 $ $Date: 2002/11/20 00:14:00 $
 * @created 05 September 2002
 */
public class DefaultScopeConfig extends ListResourceBundle {

    static final Object[][] CONTENTS = {
    // **** Swing stuff ****
            {ScopeConfig.SWINGCONTEXT_WINDOW_ICON_PROPERTY, "/org/scopemvc/images/window_icon.gif"},
            {ScopeConfig.SWINGCONTEXT_PROGRESS_START_DELAY_PROPERTY, 500},
            {ScopeConfig.STEXTFIELD_CONTROL_SETTINGS_PROPERTY, "onEnter,onLostFocus"},
            {ScopeConfig.SPASSWORDFIELD_CONTROL_SETTINGS_PROPERTY, "onEnter,onLostFocus"},
            {ScopeConfig.STEXTAREA_CONTROL_SETTINGS_PROPERTY, "onEnter"},
            {ScopeConfig.SLABEL_USE_LABELFOR_COMPONENT_ENABLED_STATE_PROPERTY, "true"},
            {"PropertyViewer.swing-java.lang.String", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-java.lang.Integer", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-int", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-java.lang.Long", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-long", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-java.lang.Float", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-float", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-java.lang.Double", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-double", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-java.lang.Boolean", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-boolean", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-java.util.Date", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-org.scopemvc.util.Time", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-org.scopemvc.util.DateTime", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-java.math.BigDecimal", "org.scopemvc.view.swing.SLabel"},
            {"PropertyViewer.swing-java.math.BigInteger", "org.scopemvc.view.swing.SLabel"},
            {"PropertyEditor.swing-java.lang.String", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-java.lang.Integer", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-int", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-java.lang.Long", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-long", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-java.lang.Float", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-float", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-java.lang.Double", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-double", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-java.lang.Boolean", "org.scopemvc.view.swing.SCheckBox"},
            {"PropertyEditor.swing-boolean", "org.scopemvc.view.swing.SCheckBox"},
            {"PropertyEditor.swing-java.util.Date", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-org.scopemvc.util.Time", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-org.scopemvc.util.DateTime", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-java.math.BigDecimal", "org.scopemvc.view.swing.STextField"},
            {"PropertyEditor.swing-java.math.BigInteger", "org.scopemvc.view.swing.STextField"},
    // the editors for scombobox
            {"PropertyEditor.scombobox-java.lang.Object", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.lang.String", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.lang.Integer", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-int", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.lang.Long", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-long", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.lang.Float", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-float", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.lang.Double", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-double", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.lang.Boolean", "org.scopemvc.view.swing.SComboBoxEditor"},
    // todo: create a scombobox editor for boolean using a JCheckBox
            {"PropertyEditor.scombobox-boolean", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.util.Date", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-org.scopemvc.util.Time", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-org.scopemvc.util.DateTime", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.math.BigDecimal", "org.scopemvc.view.swing.SComboBoxEditor"},
            {"PropertyEditor.scombobox-java.math.BigInteger", "org.scopemvc.view.swing.SComboBoxEditor"},
    // ValidationHelper
            {ScopeConfig.VALIDATIONHELPER_VALIDATION_FAILED_COLOR, Color.PINK},
    // **** Servlet stuff ****
            {"org.scopemvc.controller.servlet.ScopeServlet.ControlParam", "action"},
            {"org.scopemvc.controller.servlet.ScopeServlet.ViewIDParam", "view"},
            {"ServletFormParameter.propertyIDPrefix", "."},
            {"org.scopemvc.view.servlet.xml.AbstractXSLPage.shouldCacheTemplates", "1"},
    // or "0" during development
            {"org.scopemvc.view.servlet.xml.AbstractXSLPage.debugXMLDirectory", ""},
    // or "/tmp/xml/" during development to see XML files before XSL processing
            {"org.scopemvc.controller.servlet.ScopeServlet.maxControllerPoolSize", 10},
    // **** Model stuff ****

            {"PropertyManager", "org.scopemvc.model.beans.BeansPropertyManager"},
            {"ActionManager", "org.scopemvc.model.beans.BeansActionManager"},
            {"EditorManager", "org.scopemvc.model.beans.BeansEditorManager"},
    // **** StringConvertor definitions ****
            {"StringConvertors", "org.scopemvc.util.convertor.StringConvertors"},
            {"StringConvertor.java.lang.String", "org.scopemvc.util.convertor.StringStringConvertor"},
            {"StringConvertor.java.lang.Integer", "org.scopemvc.util.convertor.IntegerStringConvertor"},
            {"StringConvertor.int", "org.scopemvc.util.convertor.IntegerStringConvertor"},
            {"StringConvertor.java.lang.Long", "org.scopemvc.util.convertor.LongStringConvertor"},
            {"StringConvertor.long", "org.scopemvc.util.convertor.LongStringConvertor"},
            {"StringConvertor.java.lang.Float", "org.scopemvc.util.convertor.FloatStringConvertor"},
            {"StringConvertor.float", "org.scopemvc.util.convertor.FloatStringConvertor"},
            {"StringConvertor.java.lang.Double", "org.scopemvc.util.convertor.DoubleStringConvertor"},
            {"StringConvertor.double", "org.scopemvc.util.convertor.DoubleStringConvertor"},
            {"StringConvertor.java.lang.Boolean", "org.scopemvc.util.convertor.BooleanStringConvertor"},
            {"StringConvertor.boolean", "org.scopemvc.util.convertor.BooleanStringConvertor"},
            {"StringConvertor.java.util.Date", "org.scopemvc.util.convertor.DateStringConvertor"},
            {"StringConvertor.org.scopemvc.util.Time", "org.scopemvc.util.convertor.TimeStringConvertor"},
            {"StringConvertor.org.scopemvc.util.DateTime", "org.scopemvc.util.convertor.DateTimeStringConvertor"},
            {"StringConvertor.java.math.BigDecimal", "org.scopemvc.util.convertor.BigDecimalStringConvertor"},
            {"StringConvertor.java.math.BigInteger", "org.scopemvc.util.convertor.BigIntegerStringConvertor"},
    // Null formatters for DateStringConvertors (the default default is set in the convertor...)
            {"org.scopemvc.util.convertor.DateStringConvertor.formatter",
            DateFormat.getDateInstance(DateFormat.MEDIUM)},
            {"org.scopemvc.util.convertor.TimeStringConvertor.formatter",
            DateFormat.getTimeInstance(DateFormat.MEDIUM)},
            {"org.scopemvc.util.convertor.DateTimeStringConvertor.formatter",
            DateFormat.getDateTimeInstance()},
            {"org.scopemvc.util.convertor.NumberStringConvertor.substituteMinusSign", true},
            {"org.scopemvc.util.convertor.NumberStringConvertor.strict", true},
            };


    /**
     * Gets the contents
     *
     * @return The contents value
     */
    public Object[][] getContents() {
        return CONTENTS;
    }
}
