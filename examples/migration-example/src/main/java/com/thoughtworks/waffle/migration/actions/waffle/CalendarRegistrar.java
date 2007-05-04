package com.thoughtworks.waffle.migration.actions.waffle;

import com.thoughtworks.waffle.registrar.AbstractRegistrar;
import com.thoughtworks.waffle.registrar.RegisterWithSession;
import com.thoughtworks.waffle.registrar.Registrar;

public class CalendarRegistrar extends AbstractRegistrar {

    public CalendarRegistrar(Registrar delegate) {
        super(delegate);
    }

    @RegisterWithSession
    public void session() {
        register("calendar", CalendarAction.class);
    }

}
