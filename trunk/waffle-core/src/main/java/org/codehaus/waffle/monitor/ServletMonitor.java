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

import java.util.List;
import java.util.Map;


/**
 * A monitor for servlet-related events
 * 
 * @author Mauro Talevi
 */
public interface ServletMonitor extends Monitor {

    void servletServiceFailed(Exception cause);
    
    void servletServiceRequested(Map<String, List<String>> parameters);

}
