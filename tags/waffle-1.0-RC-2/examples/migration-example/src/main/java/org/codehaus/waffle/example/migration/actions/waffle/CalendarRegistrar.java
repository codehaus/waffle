package org.codehaus.waffle.example.migration.actions.waffle;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;

public class CalendarRegistrar extends AbstractRegistrar {

    public CalendarRegistrar(Registrar delegate) {
        super(delegate);
    }

    public void session() {
        register("calendar", CalendarAction.class);
    }

}
