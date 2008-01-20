package org.codehaus.waffle.mock;

import org.codehaus.waffle.registrar.AbstractRegistrar;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.RequestParameterReference;

public class ParameterRegistrar extends AbstractRegistrar {

    public ParameterRegistrar(Registrar delegate) {
        super(delegate);
    }

    @Override
    public void request() {
        register("parameter_example", ParameterController.class, "Mike", 
                RequestParameterReference.requestParameter("age", 30));
    }

}
