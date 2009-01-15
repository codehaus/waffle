/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.registrar.Registrar;
import org.picocontainer.MutablePicoContainer;

/**
 * A monitor for context-related events
 * 
 * @author Mauro Talevi
 */
public interface ContextMonitor extends Monitor {

    void registrarCreated(Registrar registrar, RegistrarMonitor registrarMonitor);

    void registrarNotFound(String registrarClassName);

    void contextInitialized();

    void contextInitializationFailed(WaffleException cause);

    void applicationContextContainerStarted();

    void applicationContextContainerDestroyed();

    void sessionContextContainerCreated(MutablePicoContainer applicationContextContainer);

    void requestContextContainerCreated(MutablePicoContainer sessionContextContainer);

}
