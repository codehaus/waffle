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
import org.codehaus.waffle.context.WaffleWebappComposer;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;

public class SimpleExampleWebappComposer extends WaffleWebappComposer {

    @Override
    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        super.composeApplication(picoContainer, servletContext);

        picoContainer.addComponent("DateConverter", StrictDateValueConverter.class);

        picoContainer.addComponent(SimplePersonDAO.class);
        picoContainer.addComponent("helloworld", HelloWorldController.class);
        picoContainer.addComponent("ajaxexample", AjaxExample.class);
    }

    @Override
    public void composeSession(MutablePicoContainer picoContainer) {
        super.composeSession(picoContainer);
        picoContainer.addComponent("calculator", CalculatorController.class);
        picoContainer.addComponent("automobile", AutomobileController.class);
        //validation for automobile controller done in the controller itself
        //uncomment registration of validator if you prefer to override it
        //register("automobileValidator", AutomobileControllerValidator.class);
        picoContainer.addComponent("person", PersonController.class);
    }

    @Override
    public void composeRequest(MutablePicoContainer picoContainer) {
        super.composeRequest(picoContainer);
        picoContainer.addComponent("fileItemFactory", DiskFileItemFactory.class);
        picoContainer.addComponent("uploader", RequestFileUploader.class);
        picoContainer.addComponent("upload", UploadController.class);
        picoContainer.addComponent("parameter_example", ParameterExampleController.class,
                picoParameters("Mike", requestParameter("age", 30)));
    }

}
