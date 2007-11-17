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

import org.codehaus.waffle.validation.BindErrorMessage;

/**
 * A monitor for bind-related events
 * 
 * @author Mauro Talevi
 */
public interface BindMonitor extends Monitor {

    void bindFailedForModel(Object bindModel, BindErrorMessage errorMessage);

    void bindFailedForController(Object controller, Throwable cause);
    
}
