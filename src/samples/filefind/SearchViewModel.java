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
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.io.Perl5FilenameFilter;
import org.scopemvc.core.Selector;
import org.scopemvc.model.basic.BasicModel;
import org.scopemvc.model.collection.ListModel;

/**
 * ***** Should fire a change event intermittently during a long search
 *
 * @author <A HREF="mailto:daniel.michalik@autel.cz">Daniel Michalik</A>
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @version $Revision: 1.7 $ $Date: 2002/09/12 18:26:02 $
 * @created 12 September 2002
 */
public final class SearchViewModel extends BasicModel {

    /**
     * TODO: describe of the Field
     */
    public static final Selector DATE_CRITERIA_ENABLED =
            Selector.fromString("dateCriteriaEnabled");
    /**
     * TODO: describe of the Field
     */
    public static final Selector FILE_NAME_PATTERN =
            Selector.fromString("fileNamePattern");
    /**
     * TODO: describe of the Field
     */
    public static final Selector DATE_CRITERIA =
            Selector.fromString("dateCriteria");

    private static final Log LOG = LogFactory.getLog(SearchViewModel.class);

    private static final int SEARCH_THREAD_PRIORITY = Thread.MIN_PRIORITY;

    private boolean dateCriteriaEnabled = true;
    private String fileNamePattern = ".*";
    private DateCriteriaModel dateCriteria;
    private FSRootsModel fsRootsModel = new FSRootsModel();


    /**
     * Constructor for the SearchViewModel object
     */
    public SearchViewModel() {
        dateCriteria = new DateCriteriaModel();
        dateCriteria.addModelChangeListener(this);
        // must listen to submodels for event propagation
    }


    /**
     * Gets the fs roots
     *
     * @return The fsRoots value
     */
    public FSRootsModel getFsRoots() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getFSRootsModel: " + fsRootsModel);
        }
        return fsRootsModel;
    }


    /**
     * Gets the date criteria enabled
     *
     * @return The dateCriteriaEnabled value
     */
    public boolean isDateCriteriaEnabled() {
        return dateCriteriaEnabled;
    }


    /**
     * Gets the file name pattern
     *
     * @return The fileNamePattern value
     */
    public String getFileNamePattern() {
        return fileNamePattern;
    }


    /**
     * Gets the date criteria
     *
     * @return The dateCriteria value
     */
    public DateCriteriaModel getDateCriteria() {
        return dateCriteria;
    }


    /**
     * Sets the date criteria enabled
     *
     * @param dateCriteriaEnabled The new dateCriteriaEnabled value
     */
    public void setDateCriteriaEnabled(boolean dateCriteriaEnabled) {
        this.dateCriteriaEnabled = dateCriteriaEnabled;
        fireModelChange(VALUE_CHANGED, DATE_CRITERIA_ENABLED);
    }


    /**
     * Sets the file name pattern
     *
     * @param fileNamePattern The new fileNamePattern value
     */
    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
        fireModelChange(VALUE_CHANGED, FILE_NAME_PATTERN);
    }


    /**
     * Creates background thread and search files.
     *
     * @return TODO: Describe the Return Value
     */
    public List search() {
        List files = new ListModel(new ArrayList());
        FilenameFilter nameFilter = new Perl5FilenameFilter(getFileNamePattern());
        ComplexFileFilter filter = new ComplexFileFilter();
        filter.nameFilter = nameFilter;
        if (isDateCriteriaEnabled()) {
            dateCriteria.prepareFilter(filter);
        }
        new SearchThread(files, roots(), filter).start();
        return files;
    }


    private List roots() {
        return fsRootsModel.getSelectedRoots();
    }


    private void search0(List files, List roots, FileFilter filter) {
        for (Iterator i = roots.iterator(); i.hasNext(); ) {
            File root = (File) i.next();
            search(root, filter, files);
        }
    }


    private void search(File dir, FileFilter filter, List result) {
        // disable event firing completely while we do this
        ((BasicModel) result).makeActive(false);
        makeActive(false);

        try {
            File files[];
            try {
                files = dir.listFiles(filter);
            } catch (SecurityException ex) {
                LOG.error("Cannot open directory " + dir.getAbsolutePath(), ex);
                return;
            }

            if (files == null) {
                return;
            }

            List dirs = new ArrayList();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    dirs.add(files[i]);
                } else {
                    result.add(new FileProperties(files[i]));
                }
            }

            for (Iterator i = dirs.iterator(); i.hasNext(); ) {
                search((File) i.next(), filter, result);
            }

        } finally {
            // reenable event firing and tell the world about the change
            ((BasicModel) result).makeActive(true);
            makeActive(true);
            ((BasicModel) result).fireModelChange(VALUE_CHANGED, null);
        }
    }


    static class ComplexFileFilter implements FileFilter {
        FilenameFilter nameFilter;

        Date dateFrom;
        // if null, dates are ignored
        Date dateTo;

        /**
         * TODO: document the method
         *
         * @param file TODO: Describe the Parameter
         * @return TODO: Describe the Return Value
         */
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }
            if (!nameFilter.accept(file.getParentFile(), file.getName())) {
                return false;
            }
            if (dateFrom != null) {
                if (dateFrom.getTime() > file.lastModified()) {
                    return false;
                }
            }
            if (dateTo != null) {
                if (dateTo.getTime() < file.lastModified()) {
                    return false;
                }
            }
            return true;
        }
    }


    class SearchThread extends Thread {
        FileFilter fileFilter;
        List roots;
        List files;

        /**
         * Constructor for the SearchThread object
         *
         * @param f TODO: Describe the Parameter
         * @param r TODO: Describe the Parameter
         * @param filter TODO: Describe the Parameter
         */
        SearchThread(List f, List r, FileFilter filter) {
            roots = r;
            fileFilter = filter;
            files = f;
        }

        /**
         * Main processing method for the SearchThread object
         */
        public void run() {
            setPriority(SEARCH_THREAD_PRIORITY);
            search0(files, roots, fileFilter);
            files = null;
            roots = null;
            fileFilter = null;
        }
    }
}
