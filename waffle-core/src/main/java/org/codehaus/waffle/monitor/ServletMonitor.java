/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;


/**
 * A monitor for servlet-related events
 * 
 * @author Mauro Talevi
 */
public interface ServletMonitor extends Monitor {

    void actionMethodInvocationFailed(Exception cause);
    
    void servletInitialized(Servlet servlet);

    void servletServiceRequested(Map<String, List<String>> parameters);

    void servletServiceFailed(Exception cause);

}
