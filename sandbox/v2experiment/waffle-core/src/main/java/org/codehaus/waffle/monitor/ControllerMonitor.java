/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;


/**
 * A monitor for controller-related events
 * 
 * @author Mauro Talevi
 */
public interface ControllerMonitor extends Monitor {

    void controllerNameResolved(String name, String path);

    void controllerNotFound(String name);

    void methodDefinitionNotFound(String controllerName);

}
