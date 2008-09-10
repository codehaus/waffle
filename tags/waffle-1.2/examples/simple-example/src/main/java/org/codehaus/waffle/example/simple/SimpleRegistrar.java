package org.codehaus.waffle.example.simple;

import static org.codehaus.waffle.registrar.RequestParameterReference.requestParameter;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.codehaus.waffle.example.simple.controllers.AutomobileController;
import org.codehaus.waffle.example.simple.controllers.CalculatorController;
import org.codehaus.waffle.example.simple.controllers.HelloWorldController;
import org.codehaus.waffle.example.simple.controllers.ParameterExampleController;
import org.codehaus.waffle.example.simple.controllers.PersonController;
import org.codehaus.waffle.example.simple.controllers.UploadController;
import org.codehaus.waffle.example.simple.dao.SimplePersonDAO;
import org.codehaus.waffle.io.RequestFileUploader;
import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;

public class SimpleRegistrar extends AbstractRegistrar {

    public SimpleRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register(SimplePersonDAO.class);
        register("helloworld", HelloWorldController.class);
        register("ajaxexample", AjaxExample.class);
    }

    @Override
    public void session() {
        register("calculator", CalculatorController.class);
        register("automobile", AutomobileController.class);
        //validation for automobile controller done in the controller itself
        //uncomment registration of validator if you prefer to override it
        //register("automobileValidator", AutomobileControllerValidator.class);
        register("person", PersonController.class);
    }

    @Override
    public void request() {
        register("fileItemFactory", DiskFileItemFactory.class);
        register("uploader", RequestFileUploader.class);
        register("upload", UploadController.class);
        register("parameter_example", ParameterExampleController.class, "Mike", requestParameter("age", 30));
    }
}
