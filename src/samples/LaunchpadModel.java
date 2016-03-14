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
package samples;


import java.util.ArrayList;
import java.util.List;

/**
 * <P>
 *
 * Javabean model object that contains a full list of examples that can be
 * launched as well as the currently selected one. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/05 15:41:48 $
 * @created 05 September 2002
 */
public class LaunchpadModel {

    private List examples;
    private ExampleModel selectedExample;


    /**
     * Constructor for the LaunchpadModel object
     */
    public LaunchpadModel() {
        examples = new ArrayList();
        examples.add(new ExampleModel("Hello World", "helloworld", "HelloController"));
        examples.add(new ExampleModel("Hello World 2", "helloworld2", "Hello2Controller"));
        examples.add(new ExampleModel("Hello World 3", "helloworld3", "Hello3Controller"));
        examples.add(new ExampleModel("Swing Menu", "swing.menu", "MenuController"));
        examples.add(new ExampleModel("Swing List", "swing.list", "ListController"));
        examples.add(new ExampleModel("Multiple Swing Views", "swing.multiview", "MultiviewController"));
        examples.add(new ExampleModel("Simple Active Model", "swing.activemodel", "ActivemodelController"));
        examples.add(new ExampleModel("Complex Active Model", "swing.activesubmodel", "ActivesubmodelController"));
        examples.add(new ExampleModel("ComboBoxes", "swing.combobox", "ComboDemoController"));
        examples.add(new ExampleModel("Dynamic Read-only Properties", "swing.readonly", "ReadOnlyController"));
        examples.add(new ExampleModel("File Finder Application", "filefind", "SearchController"));
        examples.add(new ExampleModel("Timesheet Application", "swing.timesheet.controller", "TimesheetController"));
    }


    /**
     * Gets the examples
     *
     * @return The examples value
     */
    public List getExamples() {
        return examples;
    }


    /**
     * Gets the selected example
     *
     * @return The selectedExample value
     */
    public ExampleModel getSelectedExample() {
        return selectedExample;
    }


    /**
     * Sets the selected example
     *
     * @param inSelection The new selectedExample value
     */
    public void setSelectedExample(ExampleModel inSelection) {
        selectedExample = inSelection;
    }
}

