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
 * A monitor for registrar-related events
 * 
 * @author Mauro Talevi
 */
public interface RegistrarMonitor extends Monitor {

    void componentRegistered(Object key, Class<?> clazz, Object[] parameters);

    void nonCachingComponentRegistered(Object key, Class<?> clazz, Object[] parameters);

    void instanceRegistered(Object key, Object instance);    

}