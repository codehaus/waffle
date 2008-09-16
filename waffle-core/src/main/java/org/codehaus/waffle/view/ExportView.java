/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

/**
 * Indicates that the view has content which should be exported to a filename using the given content type.
 * 
 * @author Mauro Talevi
 */
public class ExportView extends View {
    private final String contentType;
    private final byte[] content;
    private final String filename;

    /*
     * @deprecated Use {@link #ExportView(String,byte[],String)} instead
     */
    public ExportView(Object controller, String contentType, byte[] content, String filename) {
        this(contentType, content, filename);
    }

    public ExportView(String contentType, byte[] content, String filename) {
        super((String)null);
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
