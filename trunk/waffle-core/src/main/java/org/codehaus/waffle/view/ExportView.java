/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.view;

/**
 * Indicates that the view should contain export data.
 *
 * @author Mauro Talevi
 */
public class ExportView extends View {
    private final String contentType;
    private final byte[] content;
    private final String filename;

    public ExportView(Object fromController, String contentType, byte[] content, String filename) {
        super(null, fromController);
        this.contentType = contentType;
        this.content = content;
        this.filename = filename;        
    }
    
    public String getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public String getFilename() {
        return filename;
    }

}
