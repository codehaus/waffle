package org.codehaus.waffle.example.paranamer;

import org.codehaus.waffle.example.paranamer.action.CalculatorController;
import org.codehaus.waffle.example.paranamer.action.HelloWorldController;
import org.codehaus.waffle.example.paranamer.action.PersonController;
import org.codehaus.waffle.example.paranamer.dao.SimplePersonDAO;
import org.codehaus.waffle.context.WaffleWebappComposer;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;

public class ParanamerExampleWebappComposer extends WaffleWebappComposer {

    @Override
    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        super.composeApplication(picoContainer, servletContext);
        picoContainer.addComponent(SimplePersonDAO.class);
        picoContainer.addComponent("helloworld", HelloWorldController.class);
        picoContainer.addComponent("ajaxexample", AjaxExample.class);
        picoContainer.addComponent("people/person", PersonController.class);
    }

    @Override
    public void composeSession(MutablePicoContainer picoContainer) {
        super.composeSession(picoContainer);
        picoContainer.addComponent("calculator", CalculatorController.class);
    }
}
