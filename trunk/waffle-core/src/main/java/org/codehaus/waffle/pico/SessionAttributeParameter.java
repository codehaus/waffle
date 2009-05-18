/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.NameBinding;

import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Will resolve value from the {@code HttpSession} attribute.
 *
 * @author Michael Ward
 */
class SessionAttributeParameter extends AbstractWaffleParameter {

    protected SessionAttributeParameter(String key) {
        super(key);
    }

    @SuppressWarnings({"unchecked"})
    public Resolver resolve(PicoContainer picoContainer, ComponentAdapter<?> forAdapter,
            ComponentAdapter<?> injecteeAdapter, Type expectedType, NameBinding expectedNameBinding,
            boolean useNames, Annotation binding) {
        HttpSession session = picoContainer.getComponent(HttpSession.class);
        Object value = session.getAttribute(getKey());
        return new ValueResolver(value != null, value, forAdapter);
    }
}
