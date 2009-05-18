/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.NameBinding;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Will resolve value from the {@code HttpServletRequest} attribute.
 * 
 * @author Michael Ward
 */
class RequestAttributeParameter extends AbstractWaffleParameter {

    protected RequestAttributeParameter(String key) {
        super(key);
    }

    public Resolver resolve(PicoContainer picoContainer, ComponentAdapter<?> forAdapter,
            ComponentAdapter<?> injecteeAdapter, Type expectedType, NameBinding expectedNameBinding,
            boolean useNames, Annotation binding) {
        HttpServletRequest request = picoContainer.getComponent(HttpServletRequest.class);
        Object value = request.getAttribute(getKey());
        return new ValueResolver(value != null, value, forAdapter);
    }

}
