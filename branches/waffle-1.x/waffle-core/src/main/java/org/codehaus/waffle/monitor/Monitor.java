/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

/**
 * Marker Monitor defining the levels and any methods common to all monitors.
 * 
 * A monitor defines events that need to be monitored, eg for debugging purposes.
 * 
 * Each implementing class can opt to disregard some of these events or handle them with 
 * different priorities.
 * 
 * @author Mauro Talevi
 */
public interface Monitor {
    enum Level {
        ERROR, INFO, WARN, DEBUG
    }
}
