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
package com.thoughtworks.waffle.action;

import com.thoughtworks.waffle.WaffleException;
import com.thoughtworks.waffle.Constants;
import com.thoughtworks.waffle.context.ContextContainer;
import com.thoughtworks.waffle.context.RequestLevelContainer;
import com.thoughtworks.waffle.action.method.MethodDefinition;
import com.thoughtworks.waffle.action.method.MethodDefinitionFinder;

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

    /**
     * Retrieves the controller definition from the context container via the WaffleRequestFilter
     * @see com.thoughtworks.waffle.context.WaffleRequestFilter
     */
    public ControllerDefinition getControllerDefinition(HttpServletRequest request, HttpServletResponse response) {
        ContextContainer requestLevelContainer = RequestLevelContainer.get();

        if (requestLevelContainer == null) {
            String error = "No context container found at request level."
                    + "Please ensure that a WaffleRequestFilter was registered in the web.xml";
            throw new WaffleException(error);
        }

        String name = controllerNameResolver.findControllerName(request);
        Object controller = requestLevelContainer.getComponentInstance(name);
        if ( controller == null ){
            String error = "No controller configured for the specified path: '"
                    + request.getRequestURI() + "' (controller name='" + name + "') "
                + "Please ensure that controller '" + name + "' was registered in the Registrar.";
            throw new WaffleException(error);            
        }
        MethodDefinition methodDefinition = methodDefinitionFinder.find(controller, request, response);

        // set the controller to the request so it can be accessed from the view
        request.setAttribute(Constants.CONTROLLER_KEY, controller);
        return new ControllerDefinition(name, controller, methodDefinition);
    }

}
