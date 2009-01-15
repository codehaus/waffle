/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.bind.StringTransmuter;
import org.picocontainer.Parameter;

public class StubParameterResolver extends ParameterResolver {
    public StubParameterResolver() {
        super(new StubStringTransmuter());    
    }

    public Parameter resolve(Object arg) {
        return null;
    }
}
