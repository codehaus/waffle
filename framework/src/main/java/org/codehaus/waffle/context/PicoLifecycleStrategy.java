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

import org.codehaus.waffle.Startable;
import org.picocontainer.ComponentMonitor;
import org.picocontainer.defaults.DefaultLifecycleStrategy;

import java.lang.reflect.Method;

public class PicoLifecycleStrategy extends DefaultLifecycleStrategy {
    private static Method start, stop;

    {
        try {
            start = Startable.class.getMethod("start", (Class[]) null);
            stop = Startable.class.getMethod("stop", (Class[]) null);
        } catch (NoSuchMethodException e) {
            // ignore
        }
    }

    public PicoLifecycleStrategy(ComponentMonitor monitor) {
        super(monitor);
    }

    public void start(Object component) {
        if (component != null && component instanceof Startable) {
            long str = System.currentTimeMillis();
            currentMonitor().invoking(start, component);
            try {
                ((Startable) component).start();
                currentMonitor().invoked(start, component, System.currentTimeMillis() - str);
            } catch (RuntimeException cause) {
                currentMonitor().lifecycleInvocationFailed(start, component, cause); // may re-throw
            }
        } else {
            super.start(component);
        }
    }

    public void stop(Object component) {
        if (component != null && component instanceof Startable) {
            long str = System.currentTimeMillis();
            currentMonitor().invoking(stop, component);
            try {
                ((Startable) component).stop();
                currentMonitor().invoked(stop, component, System.currentTimeMillis() - str);
            } catch (RuntimeException cause) {
                currentMonitor().lifecycleInvocationFailed(stop, component, cause); // may re-throw
            }
        } else {
            super.stop(component);
        }
    }

    public boolean hasLifecycle(Class type) {
        return Startable.class.isAssignableFrom(type) || super.hasLifecycle(type);
    }

}
