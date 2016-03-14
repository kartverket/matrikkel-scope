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
<TITLE>Scope Servlet Examples: Login: FormView1</TITLE>
</HEAD>

<BODY>

<H1>Scope Servlet Examples: Login: FormView1</H1>

<FORM ACTION="login?action=Enter&amp;view=formview1" METHOD="post">
<INPUT TYPE="text" NAME=".name" VALUE="{/data/name}" /><BR />
<INPUT TYPE="submit" />
</FORM>

</BODY>

</HTML>
</xsl:template>


</xsl:stylesheet>
