package org.codehaus.waffle.example.simple;

import org.apache.commons.fileupload.FileItem;
import org.codehaus.waffle.action.annotation.ActionMethod;
import org.codehaus.waffle.action.annotation.PRG;
import org.codehaus.waffle.io.FileUploader;

import java.util.Collection;
import java.util.List;

public class UploadController {
   
    private FileUploader uploader;
    private Collection<String> errors;
    private List<FileItem> files;
    private String comment;
    
    public UploadController(FileUploader uploader) {
        this.uploader = uploader;
    }

    @ActionMethod(asDefault=true)
    @PRG(false) // PRG needs to be disabled to allow request-scope content to be accessible in referring view
    public void upload(){ 
        files = uploader.getFiles();
        errors = uploader.getErrors();        
    }

    public Collection<String> getErrors() {
        return errors;
    }

    public List<FileItem> getFiles() {
        return files;
    }
    
    public String getComment(){
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
}
