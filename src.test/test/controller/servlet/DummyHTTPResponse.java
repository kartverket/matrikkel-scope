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
 * $Id: DummyHTTPResponse.java,v 1.7 2002/09/19 18:09:34 ludovicc Exp $
 */
package test.controller.servlet;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.scopemvc.util.Debug;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * <P>
 *
 * An HTTPResponse used in testing the servlet controller/view impl. Collects
 * streamed output to be returned as a String from getContent(). </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 18 September 2002
 * @version $Revision: 1.7 $ $Date: 2002/09/19 18:09:34 $
 */
class DummyHTTPResponse implements HttpServletResponse {

    private String errorMessage;

    private int statusCode;

    private int contentLength;

    private String contentType;

    private ServletOutputStream stream = new DummyServletOutputStream();

    /**
     * Returns the name of the charset used for the MIME body sent in this
     * response. <p>
     *
     * If no charset has been assigned, it is implicitly set to <code>ISO-8859-1</code>
     * (<code>Latin-1</code>). <p>
     *
     * See RFC 2047 (http://ds.internic.net/rfc/rfc2045.txt) for more
     * information about character encoding and MIME.
     *
     * @return a <code>String</code> specifying the name of the charset, for
     *      example, <code>ISO-8859-1</code>
     */
    public String getCharacterEncoding() {
        return "ISO-8859-1";
    }

    /**
     * Returns a {@link ServletOutputStream} suitable for writing binary data in
     * the response. The servlet container does not encode the binary data.
     * Either this method or {@link #getWriter} may be called to write the body,
     * not both.
     *
     * @return a {@link ServletOutputStream} for writing binary data
     * @exception IOException if an input or output exception occurred
     * @see #getWriter
     */
    public ServletOutputStream getOutputStream() throws IOException {
        return stream;
    }

    /**
     * Returns a <code>PrintWriter</code> object that can send character text to
     * the client. The character encoding used is the one specified in the
     * <code>charset=</code> property of the {@link #setContentType} method,
     * which must be called <i>before</i> calling this method for the charset to
     * take effect. <p>
     *
     * If necessary, the MIME type of the response is modified to reflect the
     * character encoding used. <p>
     *
     * Either this method or {@link #getOutputStream} may be called to write the
     * body, not both.
     *
     * @return a <code>PrintWriter</code> object that can return character data
     *      to the client
     * @exception IOException if an input or output exception occurred
     * @see #getOutputStream
     * @see #setContentType
     */
    public PrintWriter getWriter() throws IOException {
        if (Debug.ON) {
            Debug.assertTrue(1 == 0, "getWriter not supported in this test class");
        }
        return null;
    }

    @Override
    public void setCharacterEncoding(String charset) {
    }

    /**
     * Returns the actual buffer size used for the response. If no buffering is
     * used, this method returns 0.
     *
     * @return the actual buffer size used
     * @see #setBufferSize
     * @see #flushBuffer
     * @see #isCommitted
     * @see #reset
     */
    public int getBufferSize() {
        return 0;
    }

    /**
     * Returns the locale assigned to the response.
     *
     * @return The locale value
     * @see #setLocale
     */
    public Locale getLocale() {
        return Locale.getDefault();
    }

    /**
     * Returns a boolean indicating if the response has been committed. A
     * commited response has already had its status code and headers written.
     *
     * @return a boolean indicating if the response has been committed
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #flushBuffer
     * @see #reset
     */
    public boolean isCommitted() {
        return true;
    }

    /**
     * Sets a response header with the given name and date-value. The date is
     * specified in terms of milliseconds since the epoch. If the header had
     * already been set, the new value overwrites the previous one. The <code>containsHeader</code>
     * method can be used to test for the presence of a header before setting
     * its value.
     *
     * @param name the name of the header to set
     * @param date The new dateHeader value
     * @see #containsHeader
     * @see #addDateHeader
     */
    public void setDateHeader(String name, long date) { }

    /**
     * Sets a response header with the given name and integer value. If the
     * header had already been set, the new value overwrites the previous one.
     * The <code>containsHeader</code> method can be used to test for the
     * presence of a header before setting its value.
     *
     * @param name the name of the header
     * @param value the assigned integer value
     * @see #containsHeader
     * @see #addIntHeader
     */
    public void setIntHeader(String name, int value) { }

    /**
     * Sets a response header with the given name and value. If the header had
     * already been set, the new value overwrites the previous one. The <code>containsHeader</code>
     * method can be used to test for the presence of a header before setting
     * its value.
     *
     * @param name the name of the header
     * @param value the header value
     * @see #containsHeader
     * @see #addHeader
     */
    public void setHeader(String name, String value) { }

    /**
     * @param sc the status code
     * @param sm the status message
     * @deprecated As of version 2.1, due to ambiguous meaning of the message
     *      parameter. To set a status code use <code>setStatus(int)</code>, to
     *      send an error with a description use <code>sendError(int, String)</code>
     *      . Sets the status code and message for this response.
     */
    public void setStatus(int sc, String sm) {
        statusCode = sc;
        errorMessage = sm;
    }

    /**
     * Sets the status code for this response. This method is used to set the
     * return status code when there is no error (for example, for the status
     * codes SC_OK or SC_MOVED_TEMPORARILY). If there is an error, the <code>sendError</code>
     * method should be used instead.
     *
     * @param sc the status code
     * @see #sendError
     */

    public void setStatus(int sc) {
        statusCode = sc;
    }

    /**
     * Sets the length of the content body in the response In HTTP servlets,
     * this method sets the HTTP Content-Length header.
     *
     * @param len an integer specifying the length of the content being returned
     *      to the client; sets the Content-Length header
     */
    public void setContentLength(int len) {
        contentLength = len;
    }

    /**
     * Sets the preferred buffer size for the body of the response. The servlet
     * container will use a buffer at least as large as the size requested. The
     * actual buffer size used can be found using <code>getBufferSize</code>.
     * <p>
     *
     * A larger buffer allows more content to be written before anything is
     * actually sent, thus providing the servlet with more time to set
     * appropriate status codes and headers. A smaller buffer decreases server
     * memory load and allows the client to start receiving data more quickly.
     * <p>
     *
     * This method must be called before any response body content is written;
     * if content has been written, this method throws an <code>IllegalStateException</code>
     * .
     *
     * @param size the preferred buffer size
     * @see #getBufferSize
     * @see #flushBuffer
     * @see #isCommitted
     * @see #reset
     */
    public void setBufferSize(int size) { }

    /**
     * Sets the content type of the response being sent to the client. The
     * content type may include the type of character encoding used, for
     * example, <code>text/html; charset=ISO-8859-4</code>. <p>
     *
     * If obtaining a <code>PrintWriter</code>, this method should be called
     * first.
     *
     * @param type a <code>String</code> specifying the MIME type of the content
     * @see #getOutputStream
     * @see #getWriter
     */
    public void setContentType(String type) {
        contentType = type;
    }

    /**
     * Sets the locale of the response, setting the headers (including the
     * Content-Type's charset) as appropriate. This method should be called
     * before a call to {@link #getWriter}. By default, the response locale is
     * the default locale for the server.
     *
     * @param loc the locale of the response
     * @see #getLocale
     */
    public void setLocale(Locale loc) { }

    /**
     * @param url the url to be encoded.
     * @return the encoded URL if encoding is needed; the unchanged URL
     *      otherwise.
     * @deprecated As of version 2.1, use encodeURL(String url) instead
     */
    public String encodeUrl(String url) {
        return url;
    }

    /**
     * Adds the specified cookie to the response. This method can be called
     * multiple times to set more than one cookie.
     *
     * @param cookie the Cookie to return to the client
     */
    public void addCookie(Cookie cookie) { }

    /**
     * Returns a boolean indicating whether the named response header has
     * already been set.
     *
     * @param name the header name
     * @return <code>true</code> if the named response header has already been
     *      set; <code>false</code> otherwise
     */
    public boolean containsHeader(String name) {
        return false;
    }

    /**
     * @param url the url to be encoded.
     * @return the encoded URL if encoding is needed; the unchanged URL
     *      otherwise.
     * @deprecated As of version 2.1, use encodeRedirectURL(String url) instead
     */
    public String encodeRedirectUrl(String url) {
        return url;
    }

    /**
     * Encodes the specified URL by including the session ID in it, or, if
     * encoding is not needed, returns the URL unchanged. The implementation of
     * this method includes the logic to determine whether the session ID needs
     * to be encoded in the URL. For example, if the browser supports cookies,
     * or session tracking is turned off, URL encoding is unnecessary. <p>
     *
     * For robust session tracking, all URLs emitted by a servlet should be run
     * through this method. Otherwise, URL rewriting cannot be used with
     * browsers which do not support cookies.
     *
     * @param url the url to be encoded.
     * @return the encoded URL if encoding is needed; the unchanged URL
     *      otherwise.
     */
    public String encodeURL(String url) {
        return url;
    }

    /**
     * Encodes the specified URL for use in the <code>sendRedirect</code> method
     * or, if encoding is not needed, returns the URL unchanged. The
     * implementation of this method includes the logic to determine whether the
     * session ID needs to be encoded in the URL. Because the rules for making
     * this determination can differ from those used to decide whether to encode
     * a normal link, this method is seperate from the <code>encodeURL</code>
     * method. <p>
     *
     * All URLs sent to the <code>HttpServletResponse.sendRedirect</code> method
     * should be run through this method. Otherwise, URL rewriting cannot be
     * used with browsers which do not support cookies.
     *
     * @param url the url to be encoded.
     * @return the encoded URL if encoding is needed; the unchanged URL
     *      otherwise.
     * @see #sendRedirect
     * @see #encodeUrl
     */
    public String encodeRedirectURL(String url) {
        return url;
    }

    /**
     * Sends a temporary redirect response to the client using the specified
     * redirect location URL. This method can accept relative URLs; the servlet
     * container will convert the relative URL to an absolute URL before sending
     * the response to the client. <p>
     *
     * If the response has already been committed, this method throws an
     * IllegalStateException. After using this method, the response should be
     * considered to be committed and should not be written to.
     *
     * @param location the redirect location URL
     * @exception IOException If an input or output exception occurs
     */
    public void sendRedirect(String location) throws IOException { }

    /**
     * Sends an error response to the client using the specified status code and
     * descriptive message. The server generally creates the response to look
     * like a normal server error page. <p>
     *
     * If the response has already been committed, this method throws an
     * IllegalStateException. After using this method, the response should be
     * considered to be committed and should not be written to.
     *
     * @param sc the error status code
     * @param msg the descriptive message
     * @exception IOException If an input or output exception occurs
     */
    public void sendError(int sc, String msg) throws IOException {
        statusCode = sc;
        errorMessage = msg;
    }

    /**
     * Sends an error response to the client using the specified status. The
     * server generally creates the response to look like a normal server error
     * page. <p>
     *
     * If the response has already been committed, this method throws an
     * IllegalStateException. After using this method, the response should be
     * considered to be committed and should not be written to.
     *
     * @param sc the error status code
     * @exception IOException If an input or output exception occurs
     */

    public void sendError(int sc) throws IOException {
        statusCode = sc;
    }

    /**
     * Adds a response header with the given name and value. This method allows
     * response headers to have multiple values.
     *
     * @param name the name of the header
     * @param value the additional header value
     * @see #setHeader
     */
    public void addHeader(String name, String value) { }

    /**
     * Adds a response header with the given name and date-value. The date is
     * specified in terms of milliseconds since the epoch. This method allows
     * response headers to have multiple values.
     *
     * @param name the name of the header to set
     * @param date The element to be added to the DateHeader attribute
     * @see #setDateHeader
     */
    public void addDateHeader(String name, long date) { }

    /**
     * Adds a response header with the given name and integer value. This method
     * allows response headers to have multiple values.
     *
     * @param name the name of the header
     * @param value the assigned integer value
     * @see #setIntHeader
     */
    public void addIntHeader(String name, int value) { }

    /**
     * Forces any content in the buffer to be written to the client. A call to
     * this method automatically commits the response, meaning the status code
     * and headers will be written.
     *
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #isCommitted
     * @see #reset
     * @throws IOException TODO: Describe the Exception
     */
    public void flushBuffer() throws IOException {
        stream.flush();
    }

    @Override
    public void resetBuffer() {
    }

    /**
     * Clears any data that exists in the buffer as well as the status code and
     * headers. If the response has been committed, this method throws an <code>IllegalStateException</code>
     * .
     *
     * @see #setBufferSize
     * @see #getBufferSize
     * @see #flushBuffer
     * @see #isCommitted
     */
    public void reset() { }

    /**
     * Gets the error message
     *
     * @return The errorMessage value
     */
    String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Gets the status code
     *
     * @return The statusCode value
     */
    int getStatusCode() {
        return statusCode;
    }

    /**
     * Gets the content length
     *
     * @return The contentLength value
     */
    int getContentLength() {
        return contentLength;
    }

    /**
     * Gets the content type
     *
     * @return The contentType value
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Gets the content
     *
     * @return The content value
     */
    String getContent() {
        return stream.toString();
    }
}

class DummyServletOutputStream extends ServletOutputStream {

    private static final Log LOG = LogFactory.getLog(DummyServletOutputStream.class);

    private ByteArrayOutputStream delegate = new ByteArrayOutputStream();

    /**
     * Writes the specified byte to this output stream. The general contract for
     * <code>write</code> is that one byte is written to the output stream. The
     * byte to be written is the eight low-order bits of the argument <code>b</code>
     * . The 24 high-order bits of <code>b</code> are ignored. <p>
     *
     * Subclasses of <code>OutputStream</code> must provide an implementation
     * for this method.
     *
     * @param b the <code>byte</code>.
     * @exception IOException if an I/O error occurs. In particular, an <code>IOException</code>
     *      may be thrown if the output stream has been closed.
     */
    public void write(int b) throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("write: " + b);
        }
        delegate.write(b);
    }

    /**
     * TODO: document the method
     *
     * @return TODO: Describe the Return Value
     */
    public String toString() {
        return delegate.toString();
    }
}
