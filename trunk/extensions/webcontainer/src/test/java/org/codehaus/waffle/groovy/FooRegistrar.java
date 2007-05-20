package org.codehaus.waffle.groovy;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;

public class FooRegistrar extends AbstractRegistrar {


    public FooRegistrar(Registrar delegate) {
        super(delegate);
    }

    public void application() {
        register(FooApplicationAction.class);
    }

    public void session() {
        register("fooAction", FooSessionAction.class);
    }

}
