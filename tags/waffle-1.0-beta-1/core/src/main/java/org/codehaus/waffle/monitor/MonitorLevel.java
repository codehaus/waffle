/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
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
 * Enum that represent monitoring levels
 * 
 * @author Mauro Talevi
 */
public enum MonitorLevel {

    ERROR("Error"),
    INFO("Info"), 
    WARN("Warn"),
    DEBUG("Debug");

    private final String level;

    MonitorLevel(String level) {
        this.level = level;
    }

    public String toString(){
        return level;
    }
    
}
