/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.servlet;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.Constants;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.servlet.VelocityViewServlet;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.web.PicoServletContainerFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allow waffle controllers to be used with velocity servlet.
 *
 * @author Michael Ward
 */
@SuppressWarnings("serial")
public class WaffleVelocityServlet extends VelocityViewServlet {

    private ControllerDefinitionFactory controllerDefinitionFactory;

    private static ThreadLocal<MutablePicoContainer> currentRequestContainer = new ThreadLocal<MutablePicoContainer>();
    private static ThreadLocal<MutablePicoContainer> currentSessionContainer = new ThreadLocal<MutablePicoContainer>();
    private static ThreadLocal<MutablePicoContainer> currentAppContainer = new ThreadLocal<MutablePicoContainer>();

    public static class ServletFilter extends WaffleServlet.ServletFilter {

        protected void setAppContainer(MutablePicoContainer container) {
            currentAppContainer.set(container);
        }

        protected void setRequestContainer(MutablePicoContainer container) {
            currentRequestContainer.set(container);
        }

        protected void setSessionContainer(MutablePicoContainer container) {
            currentSessionContainer.set(container);
        }
    }


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
        ContextContainer container = (ContextContainer) currentRequestContainer.get();
        MessagesContext messageContext = container.getComponent(MessagesContext.class);
        ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(request, response, messageContext, container);
        context.put(Constants.CONTROLLER_KEY, controllerDefinition.getController());
        return super.handleRequest(request, response, context);
    }
}
