/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.NameBinding;
import org.codehaus.waffle.bind.StringTransmuter;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * This class is dependent on <code>StringTransmuter</code> so that a parameter value can
 * be converted into the correct type before satisfying the dependency.  This is useful
 * because request parameters are required to be String.
 *
 * @see StringTransmuter
 */
class RequestParameterParameter extends AbstractWaffleParameter {
    private final StringTransmuter stringTransmuter;
    private final Object defaultValue;

    protected RequestParameterParameter(String key, StringTransmuter stringTransmuter, Object defaultValue) {
        super(key);
        this.stringTransmuter = stringTransmuter;
        this.defaultValue = defaultValue;
    }


    @SuppressWarnings({"unchecked"})
    public Object resolveInstance(PicoContainer picoContainer, ComponentAdapter componentAdapter, Type expectedType, NameBinding nameBinding, boolean b, Annotation annotation) {
        HttpServletRequest request = picoContainer
                .getComponent(HttpServletRequest.class);
        String value = request.getParameter(getKey());
        Object result = stringTransmuter.transmute(value, expectedType);

        if(result == null) {
            return defaultValue;
        }

        return result;
    }
}
