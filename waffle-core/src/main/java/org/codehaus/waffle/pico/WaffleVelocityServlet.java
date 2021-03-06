/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.pico;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.ComponentFinder;
import org.codehaus.waffle.pico.PicoComponentFinder;
import org.codehaus.waffle.pico.WafflePicoServlet;
import org.codehaus.waffle.i18n.MessagesContext;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.servlet.VelocityViewServlet;
import org.picocontainer.MutablePicoContainer;

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

    public static class ServletFilter extends WafflePicoServlet.ServletFilter {

        protected void setAppContainer(MutablePicoContainer container) {
            currentAppContainer.set(container);
            super.setAppContainer(container);
        }

        protected void setRequestContainer(MutablePicoContainer container) {
            currentRequestContainer.set(container);
            super.setRequestContainer(container);
        }

        protected void setSessionContainer(MutablePicoContainer container) {
            currentSessionContainer.set(container);
            super.setSessionContainer(container);

        }
    }


    public WaffleVelocityServlet(ControllerDefinitionFactory controllerDefinitionFactory) {
        this.controllerDefinitionFactory = controllerDefinitionFactory;
    }

    public WaffleVelocityServlet() {
        controllerDefinitionFactory = currentAppContainer.get().getComponent(ControllerDefinitionFactory.class);
    }

    @Override
    protected Template handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
            throws Exception {
        // Always add the controller to the context
        MutablePicoContainer container = currentRequestContainer.get();
        MessagesContext messageContext = container.getComponent(MessagesContext.class);
        ComponentFinder componentFinder = new PicoComponentFinder(container);        
        ControllerDefinition controllerDefinition = controllerDefinitionFactory.getControllerDefinition(request, response, messageContext, componentFinder);
        context.put(Constants.CONTROLLER_KEY, controllerDefinition.getController());
        return super.handleRequest(request, response, context);
    }
}
