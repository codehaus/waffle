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
package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.context.AbstractContextContainerFactory;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.LifecycleStrategy;
import org.picocontainer.monitors.NullComponentMonitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class PicoContextContainerFactory extends AbstractContextContainerFactory {
    private final ComponentMonitor picoComponentMonitor = new NullComponentMonitor();
    private final LifecycleStrategy picoLifecycleStrategy = new PicoLifecycleStrategy(picoComponentMonitor);
    private final RegistrarMonitor registrarMonitor;

    public PicoContextContainerFactory(MessageResources messageResources,
                                       ContextMonitor contextMonitor,
                                       RegistrarMonitor registrarMonitor) {
        super(messageResources, contextMonitor);
        this.registrarMonitor = registrarMonitor;
    }

    protected ContextContainer buildApplicationContextContainer() {
        return new PicoContextContainer(buildMutablePicoContainer(null));
    }

    public ContextContainer buildSessionLevelContainer() {
        MutablePicoContainer parentContainer = (MutablePicoContainer) applicationContextContainer.getDelegate();
        MutablePicoContainer delegate = buildMutablePicoContainer(parentContainer);

        PicoContextContainer sessionContextContainer = new PicoContextContainer(delegate);
        registrarAssistant.executeDelegatingRegistrar(createRegistrar(sessionContextContainer), ContextLevel.SESSION);
        getContextMonitor().sessionContextContainerCreated(applicationContextContainer);
        return sessionContextContainer;
    }

    public ContextContainer buildRequestLevelContainer(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            PicoContextContainer sessionContextContainer = (PicoContextContainer) session.getAttribute(Constants.SESSION_CONTAINER_KEY);
            if (sessionContextContainer == null) {
                sessionContextContainer = (PicoContextContainer) buildSessionLevelContainer();
                sessionContextContainer.registerComponentInstance(session);
                session.setAttribute(Constants.SESSION_CONTAINER_KEY, sessionContextContainer);
                sessionContextContainer.start();
            }
            MutablePicoContainer delegate = sessionContextContainer.getDelegate();

            ContextContainer requestContextContainer = new PicoContextContainer(buildMutablePicoContainer(delegate));
            registrarAssistant.executeDelegatingRegistrar(createRegistrar(requestContextContainer), ContextLevel.REQUEST);
            getContextMonitor().requestContextContainerCreated(sessionContextContainer);
            return requestContextContainer;
        } finally {
            messageResources.useLocale(request.getLocale());
        }
    }

    protected Registrar createRegistrar(ContextContainer contextContainer) {
        Registrar registrar = new PicoRegistrar((MutablePicoContainer) contextContainer.getDelegate(), registrarMonitor);
        getContextMonitor().registrarCreated(registrar, registrarMonitor);
        return registrar;
    }

    private MutablePicoContainer buildMutablePicoContainer(PicoContainer parent) {
        return new DefaultPicoContainer(picoComponentMonitor, picoLifecycleStrategy, parent);
    }
}
