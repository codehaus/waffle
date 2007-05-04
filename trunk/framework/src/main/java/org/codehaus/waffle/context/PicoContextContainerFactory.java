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
package org.codehaus.waffle.context;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.registrar.pico.PicoRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.monitors.NullComponentMonitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class PicoContextContainerFactory extends AbstractContextContainerFactory {
    private final NullComponentMonitor nullComponentMonitor = new NullComponentMonitor();
    private final PicoLifecycleStrategy picoLifecycleStrategy = new PicoLifecycleStrategy(nullComponentMonitor);

    public PicoContextContainerFactory(MessageResources messageResources) {
        super(messageResources);
    }

    public ContextContainer buildApplicationContextContainer() {
        return new PicoContextContainer(buildMutablePicoContainer(null));
    }

    public ContextContainer buildSessionLevelContainer() {
        MutablePicoContainer parentContainer = (MutablePicoContainer) applicationContextContainer.getDelegate();
        MutablePicoContainer delegate = buildMutablePicoContainer(parentContainer);

        PicoContextContainer sessionContextContainer = new PicoContextContainer(delegate);
        registrarAssistant.executeDelegatingRegistrar(createRegistrar(sessionContextContainer), ContextLevel.SESSION);
        return sessionContextContainer;
    }

    public ContextContainer buildRequestLevelContainer(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();
            PicoContextContainer sessionContextContainer = (PicoContextContainer) session.getAttribute(Constants.SESSION_CONTAINER_KEY);
            if (sessionContextContainer == null) {
                throw new RuntimeException("Waffle: Possible Tomcat deployment OR Configuration error - read http://waffle.sourceforge.net/tomcat_deployment_issue.html");
            }
            MutablePicoContainer delegate = sessionContextContainer.getDelegate();

            ContextContainer requestContextContainer = new PicoContextContainer(buildMutablePicoContainer(delegate));
            registrarAssistant.executeDelegatingRegistrar(createRegistrar(requestContextContainer), ContextLevel.REQUEST);
            return requestContextContainer;
        } finally {
            messageResources.setLocale(request.getLocale());
        }
    }

    public Registrar createRegistrar(ContextContainer contextContainer) {
        return new PicoRegistrar((MutablePicoContainer) contextContainer.getDelegate());
    }

    private MutablePicoContainer buildMutablePicoContainer(PicoContainer parent) {
        return new DefaultPicoContainer(nullComponentMonitor, picoLifecycleStrategy, parent);
    }
}
