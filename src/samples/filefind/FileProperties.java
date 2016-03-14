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
package samples.filefind;


import java.io.File;
import java.util.Date;

/**
 * Not a BasicModel because doesn't need to implement ModelChangeEventSource
 * since immutable.
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @version $Revision: 1.4 $ $Date: 2002/09/05 15:41:47 $
 * @created 05 September 2002
 */
public class FileProperties implements Comparable {

    private String name;
    private long length;
    private Date lastModified;
    private String suffix;
    private String path;


    /**
     * Constructor for the FileProperties object
     *
     * @param f TODO: Describe the Parameter
     */
    public FileProperties(File f) {
        name = f.getName();
        length = f.length();
        lastModified = new Date(f.lastModified());
        int lastDot = name.lastIndexOf('.');
        if ((lastDot > -1) && (lastDot + 1 < name.length())) {
            suffix = name.substring(lastDot + 1);
        } else {
            suffix = "";
        }
        path = f.getParent();
    }


    /**
     * Gets the name
     *
     * @return The name value
     */
    public String getName() {
        return name;
    }


    /**
     * Gets the length
     *
     * @return The length value
     */
    public long getLength() {
        return length;
    }


    /**
     * Gets the suffix
     *
     * @return The suffix value
     */
    public String getSuffix() {
        return suffix;
    }


    /**
     * Gets the last modified
     *
     * @return The lastModified value
     */
    public Date getLastModified() {
        return lastModified;
    }


    /**
     * Gets the path
     *
     * @return The path value
     */
    public String getPath() {
        return path;
    }


    /**
     * TODO: document the method
     *
     * @param o TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public int compareTo(Object o) {
        return getName().compareTo(((FileProperties) o).getName());
    }
}
