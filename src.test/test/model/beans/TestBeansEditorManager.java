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
 * $Id: TestBeansEditorManager.java,v 1.5 2002/11/20 00:19:58 ludovicc Exp $
 */
package test.model.beans;

import junit.framework.TestCase;
import org.scopemvc.core.EditorManager;
import org.scopemvc.core.Selector;
import org.scopemvc.model.beans.BeansEditorManager;
import org.scopemvc.view.util.PropertyEditorFactory;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:58 $
 */
public final class TestBeansEditorManager extends TestCase {

    private ModelObject m;
    private EditorManager manager;


    /**
     * Constructor for the TestBeansEditorManager object
     *
     * @param inName Name of the test
     */
    public TestBeansEditorManager(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testEditor1() throws Exception {
        assertEquals(org.scopemvc.view.swing.STextField.class,
                manager.getEditor(PropertyEditorFactory.SWING, m, Selector.fromString("stringProperty")).getClass());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testViewer1() throws Exception {
        assertEquals(org.scopemvc.view.swing.SLabel.class,
                manager.getViewer(PropertyEditorFactory.SWING, m, Selector.fromString("stringProperty")).getClass());
    }


    /**
     * The JUnit setup method
     */
    protected void setUp() {
        m = new ModelObject();
        manager = BeansEditorManager.getInstance(m);
    }
}

