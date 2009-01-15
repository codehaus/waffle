/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

/**
 * SilentMonitor is a writing monitor that writes nothing.
 * 
 * @author Mauro Talevi
 */
public class SilentMonitor extends AbstractWritingMonitor {

    protected void write(Level level, String message) {
        // write nothing
    }

    protected void trace(Throwable exception) {
        // write nothing
    }
}
