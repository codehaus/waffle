package org.codehaus.waffle.testing.registrar;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.testing.ListController;

/**
 * @author Mauro Talevi
 */
public class MyRegistrar extends AbstractRegistrar {

    public MyRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void application() {
        register("list", ListController.class);
    }

}
