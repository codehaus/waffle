/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.Startable;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.lifecycle.StartableLifecycleStrategy;

public class PicoLifecycleStrategy extends StartableLifecycleStrategy {
    public PicoLifecycleStrategy(ComponentMonitor componentMonitor) {
        super(componentMonitor);
    }

    protected String getStopMethodName() {
        return "stop";
    }

    protected String getStartMethodName() {
        return "start";
    }

    protected Class getStartableInterface() {
        return Startable.class;    
    }
}
