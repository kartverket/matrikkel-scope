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
 * $Id: ConvertorScopeConfig.java,v 1.6 2002/11/20 00:19:57 ludovicc Exp $
 */
package test.util.convertor;

import java.text.SimpleDateFormat;
import java.util.ListResourceBundle;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.6 $ $Date: 2002/11/20 00:19:57 $
 */
public class ConvertorScopeConfig extends ListResourceBundle {

    final static Object[][] contents = {
            {"org.scopemvc.util.convertor.DateStringConvertor.formatter", new SimpleDateFormat("MM-dd-yyyy")},
            {"org.scopemvc.util.convertor.DateStringConvertor.parser.0", new SimpleDateFormat("MM-dd-yyyy")},
            {"org.scopemvc.util.convertor.DateStringConvertor.parser.1", new SimpleDateFormat("MM/dd/yyyy")},
            {"org.scopemvc.util.convertor.DateStringConvertor.parser.2", new SimpleDateFormat("MM.dd.yyyy")},
            {"org.scopemvc.util.convertor.DateStringConvertor.parser.3", new SimpleDateFormat("MMddyyyy")},
            };


    /**
     * Gets the contents
     *
     * @return The contents value
     */
    public Object[][] getContents() {
        return contents;
    }
}

