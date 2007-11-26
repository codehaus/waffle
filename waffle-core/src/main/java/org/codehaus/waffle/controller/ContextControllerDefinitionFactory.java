/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.controller;

import static org.codehaus.waffle.Constants.CONTROLLER_KEY;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MissingActionMethodException;
import org.codehaus.waffle.context.ContextContainer;
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
            String error = "No context container found at request level. "
                    + "Please ensure that a WaffleRequestFilter is registered in the web.xml";
            throw new WaffleException(error);
        }

        Object controller = requestLevelContainer.getComponentInstance(name);
        if (controller == null) {
            controllerMonitor.controllerNotFound(name);
            String error = "No controller '" + name + "' configured for the specified path: '"
                    + request.getRequestURI() + ". Please ensure that controller '" + name + "' is registered in the Registrar.";
            throw new WaffleException(error);
        }

        return controller;
    }

    protected MethodDefinition findMethodDefinition(Object controller, HttpServletRequest request, HttpServletResponse response) {
        return methodDefinitionFinder.find(controller, request, response);
    }

    
}
