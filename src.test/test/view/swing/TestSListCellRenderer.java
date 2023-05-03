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
 * $Id: TestSListCellRenderer.java,v 1.7 2002/11/20 00:19:56 ludovicc Exp $
 */
package test.view.swing;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.core.Selector;
import org.scopemvc.view.swing.SListCellRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/11/20 00:19:56 $
 * @created 24 September 2002
 */
public final class TestSListCellRenderer extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestSListCellRenderer.class);

    private SListCellRenderer renderer;
    private JList list;
    private SwingDummyModel model;


    /**
     * Constructor for the TestSListCellRenderer object
     *
     * @param inName Name of the test
     */
    public TestSListCellRenderer(String inName) {
        super(inName);
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testRendererDefault1() throws Exception {
        Component c = renderer.getListCellRendererComponent(list, "test", 0, false, false);
        assertNotNull(c);
        assertEquals("test", renderer.getText());
        assertNull(renderer.getIcon());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testRendererDefault2() throws Exception {
        ImageIcon icon = new ImageIcon();
        Component c = renderer.getListCellRendererComponent(list, icon, 0, false, false);
        assertNotNull(c);
        assertEquals("", renderer.getText());
        assertSame(icon, renderer.getIcon());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testRendererText() throws Exception {
        renderer.setTextSelector(Selector.fromString("stringProperty"));
        Component c = renderer.getListCellRendererComponent(list, model, 0, false, false);
        assertNotNull(c);
        assertEquals(model.getStringProperty(), renderer.getText());
        assertNull(renderer.getIcon());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testRendererIcon() throws Exception {
        renderer.setIconSelector(Selector.fromString("iconProperty"));
        Component c = renderer.getListCellRendererComponent(list, model, 0, false, false);
        assertNotNull(c);
        assertSame(model.getIconProperty(), renderer.getIcon());
        assertEquals("", renderer.getText());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testRendererTextIcon() throws Exception {
        renderer.setIconSelector(Selector.fromString("iconProperty"));
        renderer.setTextSelector(Selector.fromString("stringProperty"));
        Component c = renderer.getListCellRendererComponent(list, model, 0, false, false);
        assertNotNull(c);
        assertEquals(model.getStringProperty(), renderer.getText());
        assertSame(model.getIconProperty(), renderer.getIcon());
    }


    /**
     * A unit test for JUnit
     *
     * @throws Exception Any abnormal exception
     */
    public void testConvenience() throws Exception {
        renderer.setIconSelector("iconProperty");
        renderer.setTextSelector("stringProperty");
        Component c = renderer.getListCellRendererComponent(list, model, 0, false, false);
        assertNotNull(c);
        assertEquals(model.getStringProperty(), renderer.getText());
        assertSame(model.getIconProperty(), renderer.getIcon());
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {

        renderer = new SListCellRenderer();

        list = new JList();
        list.setCellRenderer(renderer);

        model = new SwingDummyModel();
    }


    /**
     * The teardown method for JUnit
     */
    protected void tearDown() { }
}
