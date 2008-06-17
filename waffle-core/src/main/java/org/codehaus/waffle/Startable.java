/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle;

/**
 * <p>Implementors of this interface will be notified of lifecycle events (Start and Stop)
 * as it relates to the context they have been registered with in the application's Registrar.
 * </p>
 *
 * @author Michael Ward
 * @see org.codehaus.waffle.registrar.Registrar#application()
 * @see org.codehaus.waffle.registrar.Registrar#session()
 * @see org.codehaus.waffle.registrar.Registrar#request() 
 */
public interface Startable {

    /**
     * Start this component.  This is executed when the context (Application, Session or Request) is initialized. 
     */
    public void start();

    /**
     * Stop this component.  This is executed when the context (Application, Session or Request) is destroyed.
     */
    public void stop();
    
}
