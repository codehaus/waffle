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
package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.Startable;

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
