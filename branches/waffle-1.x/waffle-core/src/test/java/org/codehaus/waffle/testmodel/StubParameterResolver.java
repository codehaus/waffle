/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.picocontainer.Parameter;

public class StubParameterResolver implements ParameterResolver {
    public Parameter resolve(Object arg) {
        return null;
    }
}
