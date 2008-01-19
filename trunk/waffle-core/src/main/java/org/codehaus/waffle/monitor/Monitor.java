/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
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
