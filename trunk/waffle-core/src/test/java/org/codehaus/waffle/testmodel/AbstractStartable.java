/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.Startable;

public class AbstractStartable implements Startable {
    private boolean started = false;
    private boolean stopped = false;

    public void start() {
        started = true;
    }

    public void stop() {
        stopped = true;
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isStopped() {
        return stopped;
    }
}
