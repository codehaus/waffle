package org.codehaus.waffle.taglib.acceptance;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;

/**
 * @Author Fabio Kung
 */
public class TaglibAcceptanceRegistrar extends AbstractRegistrar {

    public TaglibAcceptanceRegistrar(Registrar delegate) {
        super(delegate);
    }

    public void request() {
        register("products", ProductsController.class);
        register("productsValidator", ProductsControllerValidator.class);
    }
}
