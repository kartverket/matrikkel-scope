<?xml version="1.0"?>


<!--
Scope: a generic MVC framework.
Copyright (c) 2000-2002, Steve Meyfroidt
All rights reserved.
Email: smeyfroi@users.sourceforge.net


Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.

Neither the name "Scope" nor the names of its contributors
may be used to endorse or promote products derived from this software
without specific prior written permission.


THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


$Id: FormView1.xsl,v 1.2 2002/01/12 09:35:41 smeyfroi Exp $
-->


<xsl:stylesheet 
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        version="1.0"
>


<xsl:template match="/">
<HTML>

<HEAD>
<TITLE>Scope Servlet Examples: WebForm: FormView1</TITLE>
</HEAD>

<BODY>

<H1>Scope Servlet Examples: WebForm: FormView1</H1>

<FORM ACTION="webform?action=Enter&amp;view=formview1" METHOD="post">
<INPUT TYPE="text" NAME=".name" VALUE="{/data/name}" /><BR />
<INPUT TYPE="submit" />
</FORM>

<H2>Commentary</H2>
<P>
A simple web application that accepts a String entry to populate a 
model and then switches to a second view that re-displays the data
from the model.
</P>

<H2>Servlet</H2>
<P>
The servlet extends ScopeServlet and simply provides an
override of createApplicationController to create the 
application itself.
<PRE>
public class FormServlet extends XSLScopeServlet {
	
    protected Controller createApplicationController() throws Exception {
        return new FormController();
    }
}
</PRE>
</P>

<H2>Controller</H2>
<P>
The Controller looks similar to the Swing samples in that Control
handling is the same. See the "helloworld" samples for further
details. Note that the ScopeServlet handles each HTTP request,
repopulating models and creating a regular Scope Control for
the hierarchy of Controllers to handle.
</P>
<P>
Handling of Views in a web application is a little different to the 
simpler problem of handling Swing views. In a web application 
the user can control which view is being interacted with independently
of the application logic by the use of browser "back" button etc.
An incoming request can therefore be issued by any possible
view in the entire application. To model this, Controllers in a 
web application create all the views they manage and put them
into a container analogous to a Swing "TabbedPane". The 
TabbedPane is then set as the Controller's View:
<PRE>
    public FormController() {
        setModel(new FormModel());
        
        ServletView viewContainer = new ServletView();
        viewContainer.addSubview(new FormView1());
        viewContainer.addSubview(new FormView2());
        setView(viewContainer);
    }
</PRE>
</P>
<P>
This also means that one of the Views in the container must
be chosen as the "visible" one before a showView:
<PRE>
    protected void doGotName() {
    	((ServletView)getView()).setVisible(FormView2.ID);
        showView();
    }
</PRE>
Note that by default the first view added to the container is 
set to be the "visible" one.
</P>

<H2>Views</H2>
<P>
XSLPages are very simple views that have a unique
(application-wide) ID and a URI for the XSLT that they use to
create HTML:
<PRE>
public class FormView2 extends XSLPage {

	public static final String ID = "formview2";
	private static final String XSL = "xsl/samples/servlet/xml/webform/FormView2.xsl";

    public FormView2() {
    	super(ID, XSL);
    }
}
</PRE>
</P>
<P>
The XSLT does the hard work of extracting data from the 
XML stream created from the bound model. An example XML document
produced is:
<PRE>
	&lt;?xml version="1.0"?>
	&lt;data ID="_ROOT">
	    &lt;name ID=".name">Steve&lt;/name>
	&lt;/data>
</PRE>
and FormView2's XSLT includes this:
<PRE>
	&lt;xsl:template match="/">
	&lt;HTML>
	
	&lt;HEAD>
	&lt;TITLE>Scope Servlet Examples: WebForm: FormView2&lt;/TITLE>
	&lt;/HEAD>
	
	&lt;BODY>
	&lt;H1>Scope Servlet Examples: WebForm: FormView2&lt;/H1>
	&lt;xsl:value-of select="/data/name" />
	&lt;/BODY>
	
	&lt;/HTML>
	&lt;/xsl:template>
</PRE>
Notice the extraction of the "name" property from the bound model.
</P>

<H2>Model</H2>
<P>
The model is a simple JavaBean: see the "helloworld" Swing samples
for further details.
</P>

</BODY>

</HTML>
</xsl:template>


</xsl:stylesheet>
