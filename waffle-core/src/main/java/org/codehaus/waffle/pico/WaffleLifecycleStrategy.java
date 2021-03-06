/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.pico;

import org.codehaus.waffle.Startable;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.lifecycle.StartableLifecycleStrategy;

@SuppressWarnings("serial")
public class WaffleLifecycleStrategy extends StartableLifecycleStrategy {
    public WaffleLifecycleStrategy(ComponentMonitor componentMonitor) {
        super(componentMonitor);
    }

    protected String getStopMethodName() {
        return "stop";
    }

    protected String getStartMethodName() {
        return "start";
    }

    protected Class<?> getStartableInterface() {
        return Startable.class;
    }
}
