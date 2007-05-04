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
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.RegisterWithApplication;
import org.codehaus.waffle.registrar.RegisterWithRequest;
import org.codehaus.waffle.registrar.RegisterWithSession;
import org.codehaus.waffle.registrar.Registrar;

public class CustomRegistrar extends AbstractRegistrar {
    public CustomRegistrar(Registrar delegate) {
        super(delegate);
    }

    @RegisterWithApplication
    public void application() {
        register("application", ApplicationLevelComponent.class);
    }

    @RegisterWithSession
    public void session() {
        register("session", SessionLevelComponent.class);
    }

    @RegisterWithRequest
    public void request() {
        register("request", RequestLevelComponent.class);
        register("FakeController", FakeController.class);
    }
}
