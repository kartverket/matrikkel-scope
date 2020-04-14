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
 * $Id: PropertyTag.java,v 1.6 2002/09/05 15:41:48 ludovicc Exp $
 */
package org.scopemvc.view.servlet.jsp;


import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.controller.servlet.jsp.JSPContext;
import org.scopemvc.core.PropertyManager;
import org.scopemvc.core.Selector;
import org.scopemvc.util.convertor.StringConvertor;
import org.scopemvc.util.convertor.StringConvertors;

/**
 * <P>
 *
 * JSP tag to fetch a named property from the JSPView's bound model. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.6 $ $Date: 2002/09/05 15:41:48 $
 */
public class PropertyTag extends TagSupport {

    private final static Log LOG = LogFactory.getLog(PropertyTag.class);

    private String name;


    /**
     * Sets the name
     *
     * @param inName The new name value
     */
    public void setName(String inName) {
        name = inName;
    }


    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     * @throws JspException TODO: Describe the Exception
     */
    public int doEndTag() throws JspException {
        ServletRequest request = pageContext.getRequest();
        Object model = request.getAttribute(JSPContext.BOUND_MODEL);
        if (model == null) {
            return EVAL_PAGE;
        }

        PropertyManager manager = PropertyManager.getInstance(model);
        if (manager == null) {
            LOG.error("Can't get PropertyManager for: " + model);
            return EVAL_PAGE;
        }

        try {
            Selector selector = Selector.fromString(name);
            Class propertyClass = manager.getPropertyClass(model, selector);
            Object property = manager.get(model, selector);

            StringConvertor convertor = StringConvertors.forClass(propertyClass);
            String value = convertor.valueAsString(property);

            pageContext.getOut().write(value);
        } catch (Exception e) {
            LOG.error("Can't get property: " + name, e);
        }
        return EVAL_PAGE;
    }
}
