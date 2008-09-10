/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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
