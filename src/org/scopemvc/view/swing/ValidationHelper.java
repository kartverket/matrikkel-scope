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
package org.scopemvc.view.swing;


import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;

import org.scopemvc.util.ScopeConfig;

/**
 * <P>
 *
 * Handles {@link org.scopemvc.view.util.ModelBindable#validationFailed} and
 * {@link org.scopemvc.view.util.ModelBindable#validationSuccess} by setting the
 * background colour of the parent component to a new colour and being able to
 * generate an "error tooltip" that is coloured and contains the localized error
 * message from the validation failure. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @author <a href="mailto:mlee@thoughtworks.com">Michael Lee</a>
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:14:01 $
 * @created 05 September 2002
 * @todo The validation failed color should be configurable in ScopeConfig
 *      (ludovicc)
 */
public class ValidationHelper {

    /**
     * The color used as background when the validation fails
     */
    public static final Color DEFAULT_VALIDATION_FAILED_COLOR = Color.PINK;

    /**
     * The localized error text from the exception that caused the validation
     * failure.
     */
    private String validationError;

    /**
     * Owning JComponent of this helper.
     */
    private JComponent parent;

    /**
     * Keep the parent's tooltip text so we can restore it on validation
     * success.
     */
    private String originalTooltipText;

    /**
     * Keep the parent's original background colour so it can be restored when
     * validation succeeds.
     */
    private Color originalBackground;


    /**
     * Constructor for ValidationHelper
     *
     * @param inComponent parent JComponent.
     */
    public ValidationHelper(JComponent inComponent) {
        parent = inComponent;
    }


    /**
     * Signals that the validation has failed.
     *
     * @param inException what caused the validation failure. Should contain a
     *      user-readable message in getLocalizedMessage().
     */
    public void validationFailed(Exception inException) {
        if (validationError == null) {
            originalBackground = parent.getBackground();
            originalTooltipText = parent.getToolTipText();
        }
        validationError = inException.getLocalizedMessage();

        parent.setBackground(getValidationFailedColor());
        parent.setToolTipText(validationError);
        clearTooltip();
    }

    /**
     * Signals that the validation has succeeded.
     */
    public void validationSuccess() {
        if (validationError != null) {
            clearTooltip();
            parent.setBackground(originalBackground);
            parent.setToolTipText(originalTooltipText);
            validationError = null;
        }
    }


    /**
     * Parent should call this in its createTooltip() to allow helper to
     * substitute an error tooltip.
     *
     * @param inTip normal tooltip created by parent.
     * @return tooltip that may be an "error tooltip" rather than the passed
     *      default tooltip.
     */
    public JToolTip createToolTip(JToolTip inTip) {
        if (inTip != null && validationError != null) {
            inTip.setBackground(getValidationFailedColor());
        }
        return inTip;
    }

    private Color getValidationFailedColor() {
        Object value = ScopeConfig.getObject(ScopeConfig.VALIDATIONHELPER_VALIDATION_FAILED_COLOR);
        if (value == null) {
            return DEFAULT_VALIDATION_FAILED_COLOR;
        }
        if (value instanceof Color) {
            return (Color) value;
        }
        if (value instanceof String) {
            return Color.decode((String) value);
        }
        return DEFAULT_VALIDATION_FAILED_COLOR;
    }


    /**
     * Marvellous hack to hide any shown tooltips.
     */
    private void clearTooltip() {
        ToolTipManager.sharedInstance().mousePressed(new MouseEvent(parent, 0, 0, 0, 0, 0, 0, false));
    }
}

