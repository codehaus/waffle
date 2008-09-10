/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.controller;

import static org.codehaus.waffle.Constants.CONTROLLER_KEY;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MissingActionMethodException;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextContainerNotFoundException;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.monitor.ControllerMonitor;

/**
 * Implementation of the controller definition factory which uses the context container to look up the
 * controller objected registered.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class ContextControllerDefinitionFactory implements ControllerDefinitionFactory {
    private final MethodDefinitionFinder methodDefinitionFinder;
    private final ControllerNameResolver controllerNameResolver;
    private final ControllerMonitor controllerMonitor;

    public ContextControllerDefinitionFactory(MethodDefinitionFinder methodDefinitionFinder,
            ControllerNameResolver controllerNameResolver, ControllerMonitor controllerMonitor) {
        this.methodDefinitionFinder = methodDefinitionFinder;
        this.controllerNameResolver = controllerNameResolver;
        this.controllerMonitor = controllerMonitor;
    }

    /**
     * Retrieves the controller definition from the context container via the WaffleRequestFilter
     *
     * @see org.codehaus.waffle.context.WaffleRequestFilter
     */
    public ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
        String name = controllerNameResolver.findControllerName(request);

        Object controller = findController(name, request);
        MethodDefinition methodDefinition = null;        
        try {
            methodDefinition = findMethodDefinition(controller, request, response);
        } catch ( MissingActionMethodException e) {
            controllerMonitor.methodDefinitionNotFound(name);
            // default to null 
            // TODO introduce a NullMethodDefinition?
        }        
        // set the controller to the request so it can be accessed from the view
        request.setAttribute(CONTROLLER_KEY, controller);
        return new ControllerDefinition(name, controller, methodDefinition);
    }

    protected Object findController(String name, HttpServletRequest request) {
        ContextContainer requestLevelContainer = RequestLevelContainer.get();

        if (requestLevelContainer == null) {
            controllerMonitor.requestContextContainerNotFound();
            throw new ContextContainerNotFoundException("No Waffle context container found at request level.  WaffleRequestFilter must be registered in the web.xml");
        }

        Object controller = requestLevelContainer.getComponentInstance(name);
        if (controller == null) {
            controllerMonitor.controllerNotFound(name);
            throw new ControllerNotFoundException(("No controller '" + name + "' is configured in the Registrar for the request path '"+request.getRequestURI()+"'"));
        }

        return controller;
    }

    protected MethodDefinition findMethodDefinition(Object controller, HttpServletRequest request, HttpServletResponse response) {
        return methodDefinitionFinder.find(controller, request, response);
    }

    
}
