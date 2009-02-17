/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;

public class CustomRegistrar extends AbstractRegistrar {
    public CustomRegistrar(Registrar delegate) {
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
    }
}
