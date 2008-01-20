/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.codehaus.waffle.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.waffle.Startable;

/**
 * <p>
 * Request-based FileUploader. The files are uploaded when the object is started. If there are any errors they are
 * logged and retrievable via the {@link #getErrors()} method. An action handling a multipart form should first check
 * {@link #hasErrors()} before doing any other processing.
 * </p>
 * 
 * <p>
 * Note that it is assumed that, by default, resource cleanup should be handled by the FileUpload's cleanup
 * functionality, as detailed in the <a href="http://commons.apache.org/fileupload/using.html">user guide</a>. Custom
 * behaviour can be introduced by overriding the {@link #stop()} method.
 * </p>
 * 
 * @author Gleb Mazursky
 * @author Mauro Talevi
 */
public class RequestFileUploader implements Startable, FileUploader {
    private final HttpServletRequestWrapper request;
    private final FileItemFactory itemFactory;
    protected Collection<String> errors = new ArrayList<String>();
    protected List<FileItem> fileItems = new ArrayList<FileItem>();

    /**
     * Creates RequestFileUploader
     * 
     * @param request the HttpServletRequest
     * @param itemFactory the FileItemFactory
     */
    public RequestFileUploader(HttpServletRequest request, FileItemFactory itemFactory) {
        this.request = new HttpServletRequestWrapper(request);
        this.itemFactory = itemFactory;
    }

    public List<FileItem> getFiles() {
        List<FileItem> files = new ArrayList<FileItem>();
        // only return "true" files, ie non-form fields
        for (FileItem file : fileItems) {
            if ( !file.isFormField() ){
                files.add(file);
            }
        }
        return files;
    }

    public List<FileItem> getFormFields() {
        List<FileItem> fields = new ArrayList<FileItem>();
        // only return form fields
        for (FileItem file : fileItems) {
            if ( file.isFormField() ){
                fields.add(file);
            }
        }
        return fields;
    }
    
    public Collection<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Upload files on request start
     */
    public void start() {
        uploadFileItems(request, itemFactory);
    }

    /**
     * Performs any additional operation on request stop.
     */
    public void stop() {
        // does nothing by default
    }

    /**
     * Parses a multipart request to upload file items and records any errors.
     * Non-multipart requests are ignored.
     * 
     * @param request the HttpServletRequest
     * @param itemFactory the FileItemFactory
     */
    @SuppressWarnings("unchecked")
    protected void uploadFileItems(HttpServletRequest request, FileItemFactory itemFactory) {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                ServletFileUpload fileUpload = new ServletFileUpload(itemFactory);
                errors.clear();
                fileItems.clear();
                fileItems.addAll(fileUpload.parseRequest(request));
            } catch (FileUploadException e) {
                errors.add("Failed to upload file items: " + e.getMessage());
            }
        }
    }

}
