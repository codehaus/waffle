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
package org.codehaus.waffle.servlet;

import org.codehaus.waffle.WaffleComponentRegistry;
import org.codehaus.waffle.Constants;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.servlet.VelocityViewServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allow waffle actions to be used with velocity servlet.
 *
 * @author Michael Ward
 */
public class WaffleVelocityServlet extends VelocityViewServlet {
    private ControllerDefinitionFactory controllerDefinitionFactory;

    public WaffleVelocityServlet(ControllerDefinitionFactory controllerDefinitionFactory) {
        this.controllerDefinitionFactory = controllerDefinitionFactory;
    }

    public WaffleVelocityServlet() {
        WaffleComponentRegistry componentRegistry = ServletContextHelper.getWaffleComponentRegistry(getServletContext());
        controllerDefinitionFactory = componentRegistry.getControllerDefinitionFactory();
    }

    @Override
    protected Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
            throws Exception {
        // Always add the action to the context
        ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(request, response);
        context.put(Constants.CONTROLLER_KEY, controllerDefinition.getController());
        return super.handleRequest(request, response, context);
    }
}
