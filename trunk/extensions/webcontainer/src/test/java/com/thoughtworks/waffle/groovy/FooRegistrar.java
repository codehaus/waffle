package com.thoughtworks.waffle.groovy;

import com.thoughtworks.waffle.registrar.AbstractRegistrar;
import com.thoughtworks.waffle.registrar.Registrar;
import com.thoughtworks.waffle.registrar.RegisterWithApplication;
import com.thoughtworks.waffle.registrar.RegisterWithSession;

public class FooRegistrar extends AbstractRegistrar {


    public FooRegistrar(Registrar delegate) {
        super(delegate);
    }

    @RegisterWithApplication
    public void application() {
        register(FooApplicationAction.class);
    }

    @RegisterWithSession
    public void session() {
        register("fooAction", FooSessionAction.class);
    }


    // ---------------

    public static class FooApplicationAction {
        public String toString() {
            return "(ABC)";
        }
    }

    public static class FooSessionAction {
        private final FooApplicationAction fooApplicationAction;

        public FooSessionAction(FooApplicationAction fooApplicationAction) {
            this.fooApplicationAction = fooApplicationAction;
        }

        public String getMessage() {
            return fooApplicationAction.toString() + "(DEF)";
        }
    }

}
