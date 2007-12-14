/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.codehaus.waffle.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.waffle.Startable;

/**
 * Request-based FileUploader. The files are uploaded when the object is started. If there are any errors they are
 * logged and retrievable via the {@link #getErrors()} method. An action handling a multipart form should first check
 * {@link #hasErrors()} before doing any other processing.
 * 
 * @author Gleb Mazursky
 * @author Mauro Talevi
 */
public class RequestFileUploader implements Startable, FileUploader {
    private final HttpServletRequest request;
    private final FileItemFactory itemFactory;
    private Collection<String> errors = new ArrayList<String>();
    private List<FileItem> fileItems = new ArrayList<FileItem>();

    /**
     * Creates RequestFileUploader
     * 
     * @param request the HttpServletRequest
     * @param itemFactory the FileItemFactory
     */
    public RequestFileUploader(HttpServletRequest request, FileItemFactory itemFactory) {
        this.request = request;
        this.itemFactory = itemFactory;
    }

    public List<FileItem> getFileItems() {
        return fileItems;
    }

    public Collection<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Upload files on request init
     */
    public void start() {
        uploadFiles(request, itemFactory);
    }

    /**
     * Performs files cleanup on request destroy
     */
    public void stop() {
        for (FileItem file : getFileItems()) {
            if (file instanceof DiskFileItem) {
                File diskFile = ((DiskFileItem) file).getStoreLocation();
                if (diskFile.exists()) {
                    diskFile.delete();
                }
            }
        }
    }

    /**
     *  Processes request to retrieve file uploads and records any errors.
     * 
     * @param request the HttpServletRequest
     * @param itemFactory the FileItemFactory
     */
    @SuppressWarnings("unchecked")
    private void uploadFiles(HttpServletRequest request, FileItemFactory itemFactory) {
        if (ServletFileUpload.isMultipartContent(request)) {
            try {
                ServletFileUpload fileUpload = new ServletFileUpload(itemFactory);
                errors.clear();
                fileItems.clear();
                fileItems.addAll(fileUpload.parseRequest(request));
            } catch (FileUploadException e) {
                errors.add("Failed to upload files: " + e.getMessage());
            }
        }
    }


}