package org.codehaus.waffle.example.simple;

import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.codehaus.waffle.action.annotation.DefaultActionMethod;
import org.codehaus.waffle.io.FileUploader;

public class UploadController {
   
    private FileUploader uploader;
    
    public UploadController(FileUploader uploader) {
        this.uploader = uploader;
    }

    @DefaultActionMethod
    public void upload(){
        List<FileItem> fileItems = uploader.getFileItems();
        for ( FileItem file : fileItems ){
            System.out.println(file.getName());         
            System.out.println(new String(file.get()));
        }
        if ( uploader.hasErrors() ){
            System.out.println(uploader.getErrors());        
        }
    }
    
}
