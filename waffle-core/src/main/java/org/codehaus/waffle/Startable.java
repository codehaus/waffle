/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
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
