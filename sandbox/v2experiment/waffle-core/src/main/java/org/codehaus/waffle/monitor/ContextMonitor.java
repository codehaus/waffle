/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

import org.codehaus.waffle.WaffleException;
import org.picocontainer.MutablePicoContainer;

/**
 * A monitor for context-related events
 * 
 * @author Mauro Talevi
 */
public interface ContextMonitor extends Monitor {

    void contextInitialized();

    void contextInitializationFailed(WaffleException cause);

}
