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

import org.codehaus.waffle.validation.BindErrorMessage;

/**
 * A monitor for bind-related events
 * 
 * @author Mauro Talevi
 */
public interface BindMonitor extends Monitor {

    void viewBindFailed(Object controller, Exception cause);

    void viewValueBound(String name, Object value, Object controller);

    void controllerBindFailed(Object controller, BindErrorMessage errorMessage, Exception cause);

    void controllerValueBound(String name, Object value, Object controller);
    
}
