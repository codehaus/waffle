/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

/**
 * A monitor for registrar-related events
 * 
 * @author Mauro Talevi
 */
public interface RegistrarMonitor extends Monitor {

    void componentRegistered(Object key, Class<?> type, Object[] parameters);

    void nonCachingComponentRegistered(Object key, Class<?> type, Object[] parameters);

    void instanceRegistered(Object key, Object instance);    

}
