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
 * $Id: SDefaultTableCellRenderer.java,v 1.9 2002/09/19 18:08:02 ludovicc Exp $
 */
package org.scopemvc.view.swing;

import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;

/**
 * The render for cell elements used by STable. <br>
 * The values are converted to text by using a StringConvertor.
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.9 $ $Date: 2002/09/19 18:08:02 $
 * @created 05 September 2002
 */
public class SDefaultTableCellRenderer extends DefaultTableCellRenderer {

    private static final Log LOG = LogFactory.getLog(SDefaultTableCellRenderer.class);

    private StringConvertor convertor;

    /**
     * Constructor for the SDefaultTableCellRenderer object. <br>
     * The StringConvertor used to convert the column values to strings is the
     * once defined by default in StringConvertors.
     *
     * @param inClass The class of the values in the column.
     * @see org.scopemvc.util.convertor.StringConvertors
     */
    public SDefaultTableCellRenderer(Class inClass) {
        if (inClass == null) {
            throw new IllegalArgumentException("Can't create a renderer for null class");
        }
        convertor = StringConvertors.forClass(inClass);
        if (convertor == null) {
            throw new IllegalArgumentException("Can't create a renderer for: " + inClass);
        }
    }

    /**
     * Constructor for the SDefaultTableCellRenderer object
     *
     * @param inConvertor The StringConvertor to use to convert the column
     *      values to strings
     */
    public SDefaultTableCellRenderer(StringConvertor inConvertor) {
        convertor = inConvertor;
    }

    /**
     * Returns the StringConvertor used to convert the values in the column.
     *
     * @return a StringConvertor
     */
    public final StringConvertor getStringConvertor() {
        return convertor;
    }


    /**
     * Returns a string representation of this object
     *
     * @return a string representation of this object
     */
    public String toString() {
        return "SDefaultTableCellRenderer (" + convertor + ")";
    }

    /**
     * Overriden method, which uses Scope's String converters to convert value
     * to text.
     *
     * @param inValue the value for this cell
     * @see javax.swing.table.DefaultTableCellRenderer#setValue
     */
    protected void setValue(Object inValue) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setValue: " + inValue);
        }
        if (convertor == null) {
            super.setValue(inValue);
        } else {
            setText(convertor.valueAsString(inValue));
        }
    }
}
