/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.servlet;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.Constants;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.servlet.VelocityViewServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.handler.MessageContext;

/**
 * Allow waffle controllers to be used with velocity servlet.
 *
 * @author Michael Ward
 */
@SuppressWarnings("serial")
public class WaffleVelocityServlet extends VelocityViewServlet {
    private ControllerDefinitionFactory controllerDefinitionFactory;

    public WaffleVelocityServlet(ControllerDefinitionFactory controllerDefinitionFactory) {
        this.controllerDefinitionFactory = controllerDefinitionFactory;
    }

    public WaffleVelocityServlet() {
        ComponentRegistry componentRegistry = ServletContextHelper.getComponentRegistry(getServletContext());
        controllerDefinitionFactory = componentRegistry.getControllerDefinitionFactory();
    }

    @Override
    protected Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
            throws Exception {
        // Always add the controller to the context
        MessagesContext messageContext = RequestLevelContainer.get().getComponent(MessagesContext.class);
        ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(request, response, messageContext);
        context.put(Constants.CONTROLLER_KEY, controllerDefinition.getController());
        return super.handleRequest(request, response, context);
    }
}
