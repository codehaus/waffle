/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.monitor;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.registrar.Registrar;

/**
 * A monitor for context-related events
 * 
 * @author Mauro Talevi
 */
public interface ContextMonitor extends Monitor {

    void registrarCreated(Registrar registrar, RegistrarMonitor registrarMonitor);

    void registrarNotFound(String registrarClassName);

    void contextInitialized();

    void applicationContextContainerStarted();

    void applicationContextContainerDestroyed();

    void sessionContextContainerCreated(ContextContainer applicationContextContainer);

    void requestContextContainerCreated(ContextContainer sessionContextContainer);

}
