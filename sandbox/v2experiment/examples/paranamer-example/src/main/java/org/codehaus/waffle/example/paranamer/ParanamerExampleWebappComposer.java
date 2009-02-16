package org.codehaus.waffle.example.paranamer;

import javax.servlet.ServletContext;

import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.ParanamerMethodDefinitionFinder;
import org.codehaus.waffle.bind.converters.DateValueConverter;
import org.codehaus.waffle.context.pico.WaffleWebappComposer;
import org.codehaus.waffle.example.paranamer.action.CalculatorController;
import org.codehaus.waffle.example.paranamer.action.HelloWorldController;
import org.codehaus.waffle.example.paranamer.action.PersonController;
import org.codehaus.waffle.example.paranamer.dao.SimplePersonDAO;
import org.picocontainer.MutablePicoContainer;

public class ParanamerExampleWebappComposer extends WaffleWebappComposer {

    @Override
    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        picoContainer.addComponent(DateValueConverter.class);
        picoContainer.addComponent(SimplePersonDAO.class);
        super.composeApplication(picoContainer, servletContext);
        picoContainer.addComponent("helloworld", HelloWorldController.class);
        picoContainer.addComponent("ajaxexample", AjaxExample.class);
        picoContainer.addComponent("people/person", PersonController.class);
    }

    protected Class<? extends MethodDefinitionFinder> methodDefinitionFinder() {
        return ParanamerMethodDefinitionFinder.class;
    }

    @Override
    public void composeSession(MutablePicoContainer picoContainer) {
        super.composeSession(picoContainer);
        picoContainer.addComponent("calculator", CalculatorController.class);
    }
}
