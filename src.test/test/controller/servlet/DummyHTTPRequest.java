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
 * $Id: DummyHTTPRequest.java,v 1.3 2002/09/05 15:41:48 ludovicc Exp $
 * Changes:
 *  - Added Servlet 2.5/3.2 compatibility
 */
package test.controller.servlet;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <P>
 *
 * An HTTPRequest used in testing the servlet controller/view impl. </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.3 $ $Date: 2002/09/05 15:41:48 $
 */
class DummyHTTPRequest implements HttpServletRequest {

    private Hashtable parameters;

    /**
     * Constructor for the DummyHTTPRequest object
     *
     * @param inParameters TODO: Describe the Parameter
     */
    DummyHTTPRequest(String[][] inParameters) {
        parameters = new Hashtable();
        for (int i = 0; i < inParameters.length; i++) {
            parameters.put(inParameters[i][0], inParameters[i][1]);
        }
    }

    /**
     * Returns an array containing all of the <code>Cookie</code> objects the
     * client sent with this request. This method returns <code>null</code> if
     * no cookies were sent.
     *
     * @return an array of all the <code>Cookies</code> included with this
     *      request, or <code>null</code> if the request has no cookies
     */
    public Cookie[] getCookies() {
        return null;
    }

    /**
     * Returns the name of the authentication scheme used to protect the
     * servlet, for example, "BASIC" or "SSL," or <code>null</code> if the
     * servlet was not protected. <p>
     *
     * Same as the value of the CGI variable AUTH_TYPE.
     *
     * @return a <code>String</code> specifying the name of the authentication
     *      scheme, or <code>null</code> if the request was not authenticated
     */
    public String getAuthType() {
        return null;
    }

    /**
     * Returns any extra path information after the servlet name but before the
     * query string, and translates it to a real path. Same as the value of the
     * CGI variable PATH_TRANSLATED. <p>
     *
     * If the URL does not have any extra path information, this method returns
     * <code>null</code>.
     *
     * @return a <code>String</code> specifying the real path, or <code>null</code>
     *      if the URL does not have any extra path information
     */
    public String getPathTranslated() {
        return null;
    }

    /**
     * Returns an enumeration of all the header names this request contains. If
     * the request has no headers, this method returns an empty enumeration. <p>
     *
     * Some servlet containers do not allow do not allow servlets to access
     * headers using this method, in which case this method returns <code>null</code>
     *
     * @return an enumeration of all the header names sent with this request; if
     *      the request has no headers, an empty enumeration; if the servlet
     *      container does not allow servlets to use this method, <code>null</code>
     */
    public Enumeration getHeaderNames() {
        return null;
    }

    /**
     * Returns the value of the specified request header as a <code>long</code>
     * value that represents a <code>Date</code> object. Use this method with
     * headers that contain dates, such as <code>If-Modified-Since</code>. <p>
     *
     * The date is returned as the number of milliseconds since January 1, 1970
     * GMT. The header name is case insensitive. <p>
     *
     * If the request did not have a header of the specified name, this method
     * returns -1. If the header can't be converted to a date, the method throws
     * an <code>IllegalArgumentException</code>.
     *
     * @param name a <code>String</code> specifying the name of the header
     * @return a <code>long</code> value representing the date specified in the
     *      header expressed as the number of milliseconds since January 1, 1970
     *      GMT, or -1 if the named header was not included with the reqest
     */
    public long getDateHeader(String name) {
        return -1;
    }

    /**
     * Returns the value of the specified request header as a <code>String</code>
     * . If the request did not include a header of the specified name, this
     * method returns <code>null</code>. The header name is case insensitive.
     * You can use this method with any request header.
     *
     * @param name a <code>String</code> specifying the header name
     * @return a <code>String</code> containing the value of the requested
     *      header, or <code>null</code> if the request does not have a header
     *      of that name
     */
    public String getHeader(String name) {
        return null;
    }

    /**
     * Returns all the values of the specified request header as an <code>Enumeration</code>
     * of <code>String</code> objects. <p>
     *
     * Some headers, such as <code>Accept-Language</code> can be sent by clients
     * as several headers each with a different value rather than sending the
     * header as a comma separated list. <p>
     *
     * If the request did not include any headers of the specified name, this
     * method returns an empty <code>Enumeration</code>. The header name is case
     * insensitive. You can use this method with any request header.
     *
     * @param name a <code>String</code> specifying the header name
     * @return a <code>Enumeration</code> containing the values of the requested
     *      header, or <code>null</code> if the request does not have any
     *      headers of that name
     */
    public Enumeration getHeaders(String name) {
        return null;
    }

    /**
     * Returns a boolean indicating whether the authenticated user is included
     * in the specified logical "role". Roles and role membership can be defined
     * using deployment descriptors. If the user has not been authenticated, the
     * method returns <code>false</code>.
     *
     * @param role a <code>String</code> specifying the name of the role
     * @return a <code>boolean</code> indicating whether the user making this
     *      request belongs to a given role; <code>false</code> if the user has
     *      not been authenticated
     */
    public boolean isUserInRole(String role) {
        return false;
    }

    /**
     * Returns the value of the specified request header as an <code>int</code>.
     * If the request does not have a header of the specified name, this method
     * returns -1. If the header cannot be converted to an integer, this method
     * throws a <code>NumberFormatException</code>. <p>
     *
     * The header name is case insensitive.
     *
     * @param name a <code>String</code> specifying the name of a request header
     * @return an integer expressing the value of the request header or -1 if
     *      the request doesn't have a header of this name
     */
    public int getIntHeader(String name) {
        return -1;
    }

    /**
     * Returns the name of the HTTP method with which this request was made, for
     * example, GET, POST, or PUT. Same as the value of the CGI variable
     * REQUEST_METHOD.
     *
     * @return a <code>String</code> specifying the name of the method with
     *      which this request was made
     */
    public String getMethod() {
        return "POST";
    }

    /**
     * Returns any extra path information associated with the URL the client
     * sent when it made this request. The extra path information follows the
     * servlet path but precedes the query string. This method returns <code>null</code>
     * if there was no extra path information. <p>
     *
     * Same as the value of the CGI variable PATH_INFO.
     *
     * @return a <code>String</code> specifying extra path information that
     *      comes after the servlet path but before the query string in the
     *      request URL; or <code>null</code> if the URL does not have any extra
     *      path information
     */
    public String getPathInfo() {
        return null;
    }

    /**
     * Returns the part of this request's URL that calls the servlet. This
     * includes either the servlet name or a path to the servlet, but does not
     * include any extra path information or a query string. Same as the value
     * of the CGI variable SCRIPT_NAME.
     *
     * @return a <code>String</code> containing the name or path of the servlet
     *      being called, as specified in the request URL
     */
    public String getServletPath() {
        return null;
    }

    /**
     * Returns the portion of the request URI that indicates the context of the
     * request. The context path always comes first in a request URI. The path
     * starts with a "/" character but does not end with a "/" character. For
     * servlets in the default (root) context, this method returns "".
     *
     * @return a <code>String</code> specifying the portion of the request URI
     *      that indicates the context of the request
     */
    public String getContextPath() {
        return null;
    }

    /**
     * Returns the query string that is contained in the request URL after the
     * path. This method returns <code>null</code> if the URL does not have a
     * query string. Same as the value of the CGI variable QUERY_STRING.
     *
     * @return a <code>String</code> containing the query string or <code>null</code>
     *      if the URL contains no query string
     */
    public String getQueryString() {
        return null;
    }

    /**
     * Returns the login of the user making this request, if the user has been
     * authenticated, or <code>null</code> if the user has not been
     * authenticated. Whether the user name is sent with each subsequent request
     * depends on the browser and type of authentication. Same as the value of
     * the CGI variable REMOTE_USER.
     *
     * @return a <code>String</code> specifying the login of the user making
     *      this request, or <code>null</code if the user login is not known
     */
    public String getRemoteUser() {
        return null;
    }

    /**
     * Checks whether the requested session ID came in as a cookie.
     *
     * @return <code>true</code> if the session ID came in as a cookie;
     *      otherwise, <code>false</code>
     * @see #getSession
     */
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    /**
     * Returns a <code>java.security.Principal</code> object containing the name
     * of the current authenticated user. If the user has not been
     * authenticated, the method returns <code>null</code>.
     *
     * @return a <code>java.security.Principal</code> containing the name of the
     *      user making this request; <code>null</code> if the user has not been
     *      authenticated
     */
    public java.security.Principal getUserPrincipal() {
        return null;
    }

    /**
     * Returns the session ID specified by the client. This may not be the same
     * as the ID of the actual session in use. For example, if the request
     * specified an old (expired) session ID and the server has started a new
     * session, this method gets a new session with a new ID. If the request did
     * not specify a session ID, this method returns <code>null</code>.
     *
     * @return a <code>String</code> specifying the session ID, or <code>null</code>
     *      if the request did not specify a session ID
     * @see #isRequestedSessionIdValid
     */
    public String getRequestedSessionId() {
        return null;
    }

    /**
     * Returns the part of this request's URL from the protocol name up to the
     * query string in the first line of the HTTP request. For example:
     * <blockquote>
     * <table>
     *
     *   <tralign=left>
     *
     *     <th>
     *       First line of HTTP request
     *       <th>
     *
     *         <th>
     *           Returned Value
     *           <tr>
     *
     *             <td>
     *               POST /some/path.html HTTP/1.1
     *               <td>
     *
     *                 <td>
     *                   /some/path.html
     *                   <tr>
     *
     *                     <td>
     *                       GET http://foo.bar/a.html HTTP/1.0
     *                       <td>
     *
     *                         <td>
     *                           http://foo.bar/a.html
     *                           <tr>
     *
     *                             <td>
     *                               HEAD /xyz?a=b HTTP/1.1
     *                               <td>
     *
     *                                 <td>
     *                                   /xyz
     *                                 </table>
     *                                 </blockquote> <p>
     *
     *                                 To reconstruct an URL with a scheme and
     *                                 host, use {@link
     *                                 HttpUtils#getRequestURL}.
     *
     * @return a <code>String</code> containing the part of the URL from the
     *      protocol name up to the query string
     * @see HttpUtils#getRequestURL
     */
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    /**
     * Returns the current <code>HttpSession</code> associated with this request
     * or, if if there is no current session and <code>create</code> is true,
     * returns a new session. <p>
     *
     * If <code>create</code> is <code>false</code> and the request has no valid
     * <code>HttpSession</code>, this method returns <code>null</code>. <p>
     *
     * To make sure the session is properly maintained, you must call this
     * method before the response is committed.
     *
     * @param create TODO: Describe the Parameter
     * @return the <code>HttpSession</code> associated with this request or
     *      <code>null</code> if <code>create</code> is <code>false</code> and
     *      the request has no valid session
     * @see #getSession()
     */
    public HttpSession getSession(boolean create) {
        return null;
    }

    /**
     * Returns the current session associated with this request, or if the
     * request does not have a session, creates one.
     *
     * @return the <code>HttpSession</code> associated with this request
     * @see #getSession(boolean)
     */
    public HttpSession getSession() {
        return null;
    }

    /**
     * Checks whether the requested session ID is still valid.
     *
     * @return <code>true</code> if this request has an id for a valid session
     *      in the current session context; <code>false</code> otherwise
     * @see #getRequestedSessionId
     * @see #getSession
     * @see HttpSessionContext
     */
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    /**
     * Checks whether the requested session ID came in as part of the request
     * URL.
     *
     * @return <code>true</code> if the session ID came in as part of a URL;
     *      otherwise, <code>false</code>
     * @see #getSession
     */
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    /**
     * @return The requestedSessionIdFromUrl value
     * @deprecated As of Version 2.1 of the Java Servlet API, use {@link
     *      #isRequestedSessionIdFromURL} instead.
     */
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    /**
     * Returns a {@link RequestDispatcher} object that acts as a wrapper for the
     * resource located at the given path. A <code>RequestDispatcher</code>
     * object can be used to forward a request to the resource or to include the
     * resource in a response. The resource can be dynamic or static. <p>
     *
     * The pathname specified may be relative, although it cannot extend outside
     * the current servlet context. If the path begins with a "/" it is
     * interpreted as relative to the current context root. This method returns
     * <code>null</code> if the servlet container cannot return a <code>RequestDispatcher</code>
     * . <p>
     *
     * The difference between this method and {@link
     * ServletContext#getRequestDispatcher} is that this method can take a
     * relative path.
     *
     * @param path a <code>String</code> specifying the pathname to the resource
     * @return a <code>RequestDispatcher</code> object that acts as a wrapper
     *      for the resource at the specified path
     * @see RequestDispatcher
     * @see ServletContext#getRequestDispatcher
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    /**
     * @param path TODO: Describe the Parameter
     * @return The realPath value
     * @deprecated As of Version 2.1 of the Java Servlet API, use {@link
     *      ServletContext#getRealPath} instead.
     */
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    /**
     * Returns the value of the named attribute as an <code>Object</code>, or
     * <code>null</code> if no attribute of the given name exists. <p>
     *
     * Attributes can be set two ways. The servlet container may set attributes
     * to make available custom information about a request. For example, for
     * requests made using HTTPS, the attribute <code>javax.servlet.request.X509Certificate</code>
     * can be used to retrieve information on the certificate of the client.
     * Attributes can also be set programatically using {@link
     * ServletRequest#setAttribute}. This allows information to be embedded into
     * a request before a {@link RequestDispatcher} call. <p>
     *
     * Attribute names should follow the same conventions as package names. This
     * specification reserves names matching <code>java.*</code>, <code>javax.*</code>
     * , and <code>sun.*</code>.
     *
     * @param name a <code>String</code> specifying the name of the attribute
     * @return an <code>Object</code> containing the value of the attribute, or
     *      <code>null</code> if the attribute does not exist
     */
    public Object getAttribute(String name) {
        return null;
    }

    /**
     * Retrieves the body of the request as binary data using a {@link
     * ServletInputStream}. Either this method or {@link #getReader} may be
     * called to read the body, not both.
     *
     * @return a {@link ServletInputStream} object containing the body of the
     *      request
     * @exception IOException if an input or output exception occurred
     */
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    /**
     * Returns an <code>Enumeration</code> containing the names of the
     * attributes available to this request. This method returns an empty <code>Enumeration</code>
     * if the request has no attributes available to it.
     *
     * @return an <code>Enumeration</code> of strings containing the names of
     *      the request's attributes
     */
    public Enumeration getAttributeNames() {
        return null;
    }

    /**
     * Returns the value of a request parameter as a <code>String</code>, or
     * <code>null</code> if the parameter does not exist. Request parameters are
     * extra information sent with the request. For HTTP servlets, parameters
     * are contained in the query string or posted form data. <p>
     *
     * You should only use this method when you are sure the parameter has only
     * one value. If the parameter might have more than one value, use {@link
     * #getParameterValues}. <p>
     *
     * If you use this method with a multivalued parameter, the value returned
     * is equal to the first value in the array returned by <code>getParameterValues</code>
     * . <p>
     *
     * If the parameter data was sent in the request body, such as occurs with
     * an HTTP POST request, then reading the body directly via {@link
     * #getInputStream} or {@link #getReader} can interfere with the execution
     * of this method.
     *
     * @param name a <code>String</code> specifying the name of the parameter
     * @return a <code>String</code> representing the single value of the
     *      parameter
     * @see #getParameterValues
     */
    public String getParameter(String name) {
        return (String) parameters.get(name);
    }

    /**
     * Returns the name of the character encoding used in the body of this
     * request. This method returns <code>null</code> if the request does not
     * specify a character encoding
     *
     * @return a <code>String</code> containing the name of the chararacter
     *      encoding, or <code>null</code> if the request does not specify a
     *      character encoding
     */
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
    }

    /**
     * Returns the length, in bytes, of the request body and made available by
     * the input stream, or -1 if the length is not known. For HTTP servlets,
     * same as the value of the CGI variable CONTENT_LENGTH.
     *
     * @return an integer containing the length of the request body or -1 if the
     *      length is not known
     */
    public int getContentLength() {
        return -1;
    }

    /**
     * Returns the MIME type of the body of the request, or <code>null</code> if
     * the type is not known. For HTTP servlets, same as the value of the CGI
     * variable CONTENT_TYPE.
     *
     * @return a <code>String</code> containing the name of the MIME type of the
     *      request, or -1 if the type is not known
     */
    public String getContentType() {
        return null;
    }

    /**
     * Returns the name and version of the protocol the request uses in the form
     * <i>protocol/majorVersion.minorVersion</i> , for example, HTTP/1.1. For
     * HTTP servlets, the value returned is the same as the value of the CGI
     * variable <code>SERVER_PROTOCOL</code>.
     *
     * @return a <code>String</code> containing the protocol name and version
     *      number
     */
    public String getProtocol() {
        return "HTTP/1.0";
    }

    /**
     * Returns the name of the scheme used to make this request, for example,
     * <code>http</code>, <code>https</code>, or <code>ftp</code>. Different
     * schemes have different rules for constructing URLs, as noted in RFC 1738.
     *
     * @return a <code>String</code> containing the name of the scheme used to
     *      make this request
     */
    public String getScheme() {
        return "http";
    }

    /**
     * Returns an <code>Enumeration</code> of <code>String</code> objects
     * containing the names of the parameters contained in this request. If the
     * request has no parameters, the method returns an empty <code>Enumeration</code>
     * .
     *
     * @return an <code>Enumeration</code> of <code>String</code> objects, each
     *      <code>String</code> containing the name of a request parameter; or
     *      an empty <code>Enumeration</code> if the request has no parameters
     */
    public Enumeration getParameterNames() {
        return parameters.keys();
    }

    /**
     * Returns an array of <code>String</code> objects containing all of the
     * values the given request parameter has, or <code>null</code> if the
     * parameter does not exist. <p>
     *
     * If the parameter has a single value, the array has a length of 1.
     *
     * @param name a <code>String</code> containing the name of the parameter
     *      whose value is requested
     * @return an array of <code>String</code> objects containing the
     *      parameter's values
     * @see #getParameter
     */
    public String[] getParameterValues(String name) {
        return new String[]{(String) parameters.get(name)};
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    /**
     * Retrieves the body of the request as character data using a <code>BufferedReader</code>
     * . The reader translates the character data according to the character
     * encoding used on the body. Either this method or {@link #getReader} may
     * be called to read the body, not both.
     *
     * @return a <code>BufferedReader</code> containing the body of the request
     * @exception IOException if an input or output exception occurred
     * @see #getInputStream
     */
    public BufferedReader getReader() throws IOException {
        return null;
    }

    /**
     * Returns the Internet Protocol (IP) address of the client that sent the
     * request. For HTTP servlets, same as the value of the CGI variable <code>REMOTE_ADDR</code>
     * .
     *
     * @return a <code>String</code> containing the IP address of the client
     *      that sent the request
     */
    public String getRemoteAddr() {
        return "127.0.0.1";
    }

    /**
     * Returns the host name of the server that received the request. For HTTP
     * servlets, same as the value of the CGI variable <code>SERVER_NAME</code>.
     *
     * @return a <code>String</code> containing the name of the server to which
     *      the request was sent
     */
    public String getServerName() {
        return "localhost";
    }

    /**
     * Returns the port number on which this request was received. For HTTP
     * servlets, same as the value of the CGI variable <code>SERVER_PORT</code>.
     *
     * @return an integer specifying the port number
     */
    public int getServerPort() {
        return 80;
    }

    /**
     * Returns the preferred <code>Locale</code> that the client will accept
     * content in, based on the Accept-Language header. If the client request
     * doesn't provide an Accept-Language header, this method returns the
     * default locale for the server.
     *
     * @return the preferred <code>Locale</code> for the client
     */
    public Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Returns the fully qualified name of the client that sent the request, or
     * the IP address of the client if the name cannot be determined. For HTTP
     * servlets, same as the value of the CGI variable <code>REMOTE_HOST</code>.
     *
     * @return a <code>String</code> containing the fully qualified name of the
     *      client
     */
    public String getRemoteHost() {
        return "localhost";
    }

    /**
     * Returns an <code>Enumeration</code> of <code>Locale</code> objects
     * indicating, in decreasing order starting with the preferred locale, the
     * locales that are acceptable to the client based on the Accept-Language
     * header. If the client request doesn't provide an Accept-Language header,
     * this method returns an <code>Enumeration</code> containing one <code>Locale</code>
     * , the default locale for the server.
     *
     * @return an <code>Enumeration</code> of preferred <code>Locale</code>
     *      objects for the client
     */
    public Enumeration getLocales() {
        return null;
    }

    /**
     * Returns a boolean indicating whether this request was made using a secure
     * channel, such as HTTPS.
     *
     * @return a boolean indicating if the request was made using a secure
     *      channel
     */
    public boolean isSecure() {
        return false;
    }

    /**
     * Stores an attribute in this request. Attributes are reset between
     * requests. This method is most often used in conjunction with {@link
     * RequestDispatcher}. <p>
     *
     * Attribute names should follow the same conventions as package names.
     * Names beginning with <code>java.*</code>, <code>javax.*</code>, and
     * <code>com.sun.*</code>, are reserved for use by Sun Microsystems.
     *
     * @param name a <code>String</code> specifying the name of the attribute
     * @param o the <code>Object</code> to be stored
     */
    public void setAttribute(String name, Object o) { }

    /**
     * Removes an attribute from this request. This method is not generally
     * needed as attributes only persist as long as the request is being
     * handled. <p>
     *
     * Attribute names should follow the same conventions as package names.
     * Names beginning with <code>java.*</code>, <code>javax.*</code>, and
     * <code>com.sun.*</code>, are reserved for use by Sun Microsystems.
     *
     * @param name a <code>String</code> specifying the name of the attribute to
     *      remove
     */
    public void removeAttribute(String name) { }
}

