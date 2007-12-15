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
