/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
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
import org.codehaus.waffle.registrar.Registrar;

public class CustomRegistrarWithContexts extends AbstractRegistrar {
    public CustomRegistrarWithContexts(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register("application", ApplicationLevelComponent.class);
    }

    @Override
    public void session() {
        register("session", SessionLevelComponent.class);
    }

    @Override
    public void request() {
        register("request", RequestLevelComponent.class);
        register("FakeController", FakeController.class);
        register("errors", CustomErrorsContext.class);
        register("messages", CustomMessagesContext.class);        
    }
}