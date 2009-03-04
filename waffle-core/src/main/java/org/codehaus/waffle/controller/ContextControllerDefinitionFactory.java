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
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.codehaus.waffle.ComponentFinder;

/**
 * Implementation of the controller definition factory which uses the context container to look up the controller
 * objected registered.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class ContextControllerDefinitionFactory implements ControllerDefinitionFactory {
    private final MethodDefinitionFinder methodDefinitionFinder;
    private final ControllerNameResolver controllerNameResolver;
    private final ControllerMonitor controllerMonitor;
    protected final MessageResources messageResources;

    public ContextControllerDefinitionFactory(MethodDefinitionFinder methodDefinitionFinder,
            ControllerNameResolver controllerNameResolver, ControllerMonitor controllerMonitor,
            MessageResources messageResources) {
        this.methodDefinitionFinder = methodDefinitionFinder;
        this.controllerNameResolver = controllerNameResolver;
        this.controllerMonitor = controllerMonitor;
        this.messageResources = messageResources;
    }

    /**
     * Retrieves the controller definition from the context container via the WaffleRequestFilter
     * 
     * @see org.codehaus.waffle.context.WaffleRequestFilter
     */
    public ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response, MessagesContext messageContext, 
                                                        ComponentFinder componentFinder) {
        String name = controllerNameResolver.findControllerName(request);

        Object controller = findController(name, request, componentFinder);
        MethodDefinition methodDefinition = null;
        try {            
            methodDefinition = findMethodDefinition(controller, request, response, messageContext);
        } catch (MissingActionMethodException e) {
            controllerMonitor.methodDefinitionNotFound(name);
            // default to null
            // TODO introduce a NullMethodDefinition?
        }
        // set the controller to the request so it can be accessed from the view
        request.setAttribute(CONTROLLER_KEY, controller);
        return new ControllerDefinition(name, controller, methodDefinition);
    }

    protected Object findController(String name, HttpServletRequest request, ComponentFinder componentFinder) {

        Object controller = componentFinder.getComponent(Object.class, name);
        if (controller == null) {
            controllerMonitor.controllerNotFound(name);
            String message = messageResources.getMessageWithDefault("controllerNotFound",
                    "Controller ''{0}'' not found in the Registrar for the request path ''{1}''", name, request
                            .getRequestURI());
            throw new ControllerNotFoundException(message);
        }

        return controller;
    }

    protected MethodDefinition findMethodDefinition(Object controller, HttpServletRequest request,
                                                    HttpServletResponse response, MessagesContext messageContext) {
        return methodDefinitionFinder.find(controller, request, response, messageContext);
    }

}
