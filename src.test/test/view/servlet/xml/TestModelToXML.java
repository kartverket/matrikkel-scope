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
package test.view.servlet.xml;


import junit.framework.TestCase;
import org.scopemvc.core.Selector;
import org.scopemvc.view.servlet.xml.ModelToXML;
import org.scopemvc.view.servlet.xml.PropertyIDGenerator;

/**
 * <P>
 *
 * ***** This is all dependent on Xalan's XPath API. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net>Steve Meyfroidt</A>
 * @version $Revision: 1.6 $ $Date: 2002/09/12 19:09:36 $
 * @created 05 September 2002
 */
public final class TestModelToXML extends TestCase {

    private PropertyIDGenerator propertyIDGenerator;
    private ModelToXML modelToXML;


    /**
     * Constructor for the TestModelToXML object
     *
     * @param inName Name of the test
     */
    public TestModelToXML(String inName) {
        super(inName);
    }

    /**
     * Empty test to make junit happy, to remove once the other tests are
     * uncommented
     */
    public void testEmpty() {
        // noop
    }


    /**
     * The JUnit setup method
     *
     * @throws Exception Any abnormal exception
     */
    protected void setUp() throws Exception {
        propertyIDGenerator =
            new PropertyIDGenerator() {
                public String getPropertyID() {
                    return Selector.asString(currentPropertySelector);
                }
            };
        modelToXML = new ModelToXML();
    }

    /**
     * [?xml ...] [data id="_0"] [a id="a"]99[/a] [name id="name"]Steve[/name]
     * [/data]
     */
//    public void testSimpleModel() throws Exception {
//        Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//        ContentHandler domBuilder = new SAXResult(doc); new DOMBuilder(doc);
//
//        Object model = new SimpleModel();
//
//        modelToXML.modelToXML(model, domBuilder, propertyIDGenerator);
//
//        Element root = doc.getDocumentElement();
//        assertEquals("data", root.getNodeName());
//        assertEquals("_0", XPathAPI.selectSingleNode(doc, "data/@id").getNodeValue());
//
//        assertEquals("99", XPathAPI.selectSingleNode(doc, "data/a").getFirstChild().getNodeValue());
//        assertEquals("a", XPathAPI.selectSingleNode(doc, "data/a/@id").getNodeValue());
//
//        assertEquals("Steve", XPathAPI.selectSingleNode(doc, "data/name").getFirstChild().getNodeValue());
//        assertEquals("name", XPathAPI.selectSingleNode(doc, "data/name/@id").getNodeValue());
//    }


    /**
     * [?xml ...] [data id="_0"] [a id="a"]99[/a] [name id="name"]Steve[/name]
     * [submodel id="submodel"] [data id="_1"] [a id="submodel.a"]99[/a] [name
     * id="submodel.name"]Steve[/name] [/data] [/submodel] [/data]
     */
//    public void testContainerModel1() throws Exception {
//        Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//        ContentHandler domBuilder = new DOMBuilder(doc);
//
//        ContainerModel model = new ContainerModel();
//        model.setSubmodel(new ContainerModel());
//
//        modelToXML.modelToXML(model, domBuilder, propertyIDGenerator);
//
//        Element root = doc.getDocumentElement();
//        assertEquals("data", root.getNodeName());
//        assertEquals("_0", root.getAttribute("id"));
//
//        assertEquals("99", XPathAPI.selectSingleNode(doc, "data/a").getFirstChild().getNodeValue());
//        assertEquals("a", XPathAPI.selectSingleNode(doc, "data/a/@id").getNodeValue());
//
//        assertEquals("Steve", XPathAPI.selectSingleNode(doc, "data/name").getFirstChild().getNodeValue());
//        assertEquals("name", XPathAPI.selectSingleNode(doc, "data/name/@id").getNodeValue());
//
//        assertEquals("submodel", XPathAPI.selectSingleNode(doc, "data/submodel/@id").getNodeValue());
//        assertEquals("_1", XPathAPI.selectSingleNode(doc, "data/submodel/data/@id").getNodeValue());
//
//        assertEquals("99", XPathAPI.selectSingleNode(doc, "data/submodel/data/a").getFirstChild().getNodeValue());
//        assertEquals("submodel.a", XPathAPI.selectSingleNode(doc, "data/submodel/data/a/@id").getNodeValue());
//
//        assertEquals("Steve", XPathAPI.selectSingleNode(doc, "data/submodel/data/name").getFirstChild().getNodeValue());
//        assertEquals("submodel.name", XPathAPI.selectSingleNode(doc, "data/submodel/data/name/@id").getNodeValue());
//    }


    /**
     * [?xml ...] [data id="_0"] [a id="a"]99[/a] [name id="name"]Steve[/name]
     * [submodel id="submodel"] [data idref="_0" /] [/submodel] [/data]
     */
//    public void testContainerModel2() throws Exception {
//        Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//        ContentHandler domBuilder = new DOMBuilder(doc);
//
//        ContainerModel model = new ContainerModel();
//        model.setSubmodel(model);
//
//        modelToXML.modelToXML(model, domBuilder, propertyIDGenerator);
//
//        Element root = doc.getDocumentElement();
//        assertEquals("data", root.getNodeName());
//        assertEquals("_0", root.getAttribute("id"));
//
//        assertEquals("99", XPathAPI.selectSingleNode(doc, "data/a").getFirstChild().getNodeValue());
//        assertEquals("a", XPathAPI.selectSingleNode(doc, "data/a/@id").getNodeValue());
//
//        assertEquals("Steve", XPathAPI.selectSingleNode(doc, "data/name").getFirstChild().getNodeValue());
//        assertEquals("name", XPathAPI.selectSingleNode(doc, "data/name/@id").getNodeValue());
//
//        assertEquals("submodel", XPathAPI.selectSingleNode(doc, "data/submodel/@id").getNodeValue());
//        assertEquals("_0", XPathAPI.selectSingleNode(doc, "data/submodel/data/@idref").getNodeValue());
//    }


    /**
     * [?xml ...] [data id="_0"] [element index="0" id="0"]a[/element] [element
     * index="1" id="1"]b[/element] [element index="2" id="2"] [data id="_1"] [a
     * id="2.a"]99[/a] [name id="2.name"]Steve[/a] [/data] [/element] [element
     * index="3" id="3"] [data idref="_1" /] [/element] [/data]
     */
//    public void testList() throws Exception {
//        Document doc= DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
//        ContentHandler domBuilder = new DOMBuilder(doc);
//
//        List model = Arrays.asList(new Object[]{"a","b","c","d"});
//        SimpleModel simpleModel = new SimpleModel();
//        model.set(2, simpleModel);
//        model.set(3, simpleModel);
//
//        modelToXML.modelToXML(model, domBuilder, propertyIDGenerator);
//
//        Element root = doc.getDocumentElement();
//        assertEquals("data", root.getNodeName());
//        assertEquals("_0", root.getAttribute("id"));
//
//        assertEquals("a", XPathAPI.selectSingleNode(doc, "data/element[@index='0']").getFirstChild().getNodeValue());
//        assertEquals("0", XPathAPI.selectSingleNode(doc, "data/element[@index='0']/@id").getNodeValue());
//
//        assertEquals("b", XPathAPI.selectSingleNode(doc, "data/element[@index='1']").getFirstChild().getNodeValue());
//        assertEquals("1", XPathAPI.selectSingleNode(doc, "data/element[@index='1']/@id").getNodeValue());
//
//        assertEquals("2", XPathAPI.selectSingleNode(doc, "data/element[@index='2']/@id").getNodeValue());
//        assertEquals("_1", XPathAPI.selectSingleNode(doc, "data/element[@index='2']/data/@id").getNodeValue());
//
//        assertEquals("2.a", XPathAPI.selectSingleNode(doc, "data/element[@index='2']/data/a/@id").getNodeValue());
//        assertEquals("2.name", XPathAPI.selectSingleNode(doc, "data/element[@index='2']/data/name/@id").getNodeValue());
//
//        assertEquals("99", XPathAPI.selectSingleNode(doc, "data/element[@index='2']/data/a").getFirstChild().getNodeValue());
//        assertEquals("Steve", XPathAPI.selectSingleNode(doc, "data/element[@index='2']/data/name").getFirstChild().getNodeValue());
//
//        assertEquals("3", XPathAPI.selectSingleNode(doc, "data/element[@index='3']/@id").getNodeValue());
//        assertEquals("_1", XPathAPI.selectSingleNode(doc, "data/element[@index='3']/data/@idref").getNodeValue());
//    }
}
