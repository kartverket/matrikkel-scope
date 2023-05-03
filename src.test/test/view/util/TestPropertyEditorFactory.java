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
 * $Id: TestPropertyEditorFactory.java,v 1.4 2002/09/12 19:09:38 ludovicc Exp $
 */
package test.view.util;

import junit.framework.TestCase;
import org.scopemvc.view.util.PropertyEditorFactory;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.4 $ $Date: 2002/09/12 19:09:38 $
 */
public final class TestPropertyEditorFactory extends TestCase {

    /**
     * Constructor for the TestPropertyEditorFactory object
     *
     * @param inName Name of the test
     */
    public TestPropertyEditorFactory(String inName) {
        super(inName);
    }


    /**
     * The JUnit setup method
     */
    public void setUp() { }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefaultEditors() throws Exception {
        assertEquals(org.scopemvc.view.swing.STextField.class,
                PropertyEditorFactory.getPropertyEditor(PropertyEditorFactory.SWING, String.class).getClass());
        assertEquals(org.scopemvc.view.swing.SCheckBox.class,
                PropertyEditorFactory.getPropertyEditor(PropertyEditorFactory.SWING, Boolean.class).getClass());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefaultViewers() throws Exception {
        assertEquals(org.scopemvc.view.swing.SLabel.class,
                PropertyEditorFactory.getPropertyViewer(PropertyEditorFactory.SWING, String.class).getClass());
        assertEquals(org.scopemvc.view.swing.SLabel.class,
                PropertyEditorFactory.getPropertyViewer(PropertyEditorFactory.SWING, Boolean.class).getClass());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefaultEditors1() throws Exception {
        assertEquals(org.scopemvc.view.swing.SCheckBox.class,
                PropertyEditorFactory.getPropertyEditor(PropertyEditorFactory.SWING, Boolean.TYPE).getClass());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testDefaultViewers1() throws Exception {
        assertEquals(org.scopemvc.view.swing.SLabel.class,
                PropertyEditorFactory.getPropertyViewer(PropertyEditorFactory.SWING, Boolean.TYPE).getClass());
    }
}

