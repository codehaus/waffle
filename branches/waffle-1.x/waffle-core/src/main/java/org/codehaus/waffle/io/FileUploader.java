/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.io;

import java.util.Collection;
import java.util.List;
import org.apache.commons.fileupload.FileItem;

/**
 * Allows to upload files from a multipart request
 * 
 * @author Gleb Mazursky
 * @author Mauro Talevi
 */
public interface FileUploader {

    /**
     * Returns the uploaded files
     * 
     * @return A List of FileItems, one for each uploaded file
     */
    List<FileItem> getFiles();
    
    /**
     * Returns the form fields
     * 
     * @return A List of FileItems, one for each form field
     */
    List<FileItem> getFormFields();

    /**
     * Returns errors generated when parsing the multipart request.
     * 
     * @return the error Collection.
     */
    Collection<String> getErrors();

    /**
     * Determines if any errors occured when parsing the multipart request
     * 
     * @return <tt>true</tt> if any errors occured 
     */
    boolean hasErrors();

}
