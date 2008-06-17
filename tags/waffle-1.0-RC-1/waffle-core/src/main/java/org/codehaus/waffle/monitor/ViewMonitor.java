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

import org.codehaus.waffle.view.RedirectView;
import org.codehaus.waffle.view.ResponderView;

/**
 * A monitor for view-related events
 * 
 * @author Mauro Talevi
 */
public interface ViewMonitor extends Monitor {

    void viewForwarded(String path);

    void viewResponded(ResponderView responderView);

    void viewRedirected(RedirectView redirectView);

}