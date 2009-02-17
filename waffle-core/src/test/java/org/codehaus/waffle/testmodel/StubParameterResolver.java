/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.registrar.ParameterResolver;
import org.picocontainer.Parameter;

public class StubParameterResolver extends ParameterResolver {
    public StubParameterResolver() {
        super(new StubStringTransmuter());    
    }

    public Parameter resolve(Object arg) {
        return null;
    }
}
