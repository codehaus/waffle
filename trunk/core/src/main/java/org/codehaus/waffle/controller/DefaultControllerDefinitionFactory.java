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
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Default implementation of the controller definition factory which uses the context container to look up the
 * controller objected registered.
 *
 * @author Michael Ward
 * @author Mauro Talevi
 * @todo (mt) Rename to ContextContainerControllerDefinitionFactory?
 */
public class DefaultControllerDefinitionFactory implements ControllerDefinitionFactory {
    private final MethodDefinitionFinder methodDefinitionFinder;
    private final ControllerNameResolver controllerNameResolver;

    public DefaultControllerDefinitionFactory(MethodDefinitionFinder methodDefinitionFinder, ControllerNameResolver controllerNameResolver) {
        this.methodDefinitionFinder = methodDefinitionFinder;
        this.controllerNameResolver = controllerNameResolver;
    }

    protected Object findController(String name, HttpServletRequest request) {
        ContextContainer requestLevelContainer = RequestLevelContainer.get();

        if (requestLevelContainer == null) {
            String error = "No context container found at request level."
                    + "Please ensure that a WaffleRequestFilter was registered in the web.xml";
            throw new WaffleException(error);
        }

        Object controller = requestLevelContainer.getComponentInstance(name);
        if (controller == null) {
            String error = "No controller configured for the specified path: '"
                    + request.getRequestURI() + "' (controller name='" + name + "') "
                    + "Please ensure that controller '" + name + "' was registered in the Registrar.";
            throw new WaffleException(error);
        }

        return controller;
    }

    protected MethodDefinition findMethodDefinition(Object controller, HttpServletRequest request, HttpServletResponse response) {
        return methodDefinitionFinder.find(controller, request, response);
    }

    /**
     * Retrieves the controller definition from the context container via the WaffleRequestFilter
     *
     * @see org.codehaus.waffle.context.WaffleRequestFilter
     */
    public ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
        String name = controllerNameResolver.findControllerName(request);

        Object controller = findController(name, request);
        MethodDefinition methodDefinition = findMethodDefinition(controller, request, response);

        // set the controller to the request so it can be accessed from the view
        request.setAttribute(CONTROLLER_KEY, controller);
        return new ControllerDefinition(name, controller, methodDefinition);
    }

}
