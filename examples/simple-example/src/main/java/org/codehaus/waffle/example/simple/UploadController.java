package org.codehaus.waffle.example.simple;

import java.util.Collection;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.codehaus.waffle.action.annotation.DefaultActionMethod;
import org.codehaus.waffle.io.FileUploader;

public class UploadController {
   
    private FileUploader uploader;
    private Collection<String> errors;
    private List<FileItem> files;
    
    public UploadController(FileUploader uploader) {
        this.uploader = uploader;
    }

    @DefaultActionMethod(prg=false) 
    public void upload(){ 
        // PRG needs to be disabled to allow request-scope content to be accessible in referring view
        files = uploader.getFileItems();
        errors = uploader.getErrors();        
    }

    public Collection<String> getErrors() {
        return errors;
    }

    public List<FileItem> getFiles() {
        return files;
    }
    
}
