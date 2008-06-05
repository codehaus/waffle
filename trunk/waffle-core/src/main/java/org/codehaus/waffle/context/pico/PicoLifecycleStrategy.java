/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
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
