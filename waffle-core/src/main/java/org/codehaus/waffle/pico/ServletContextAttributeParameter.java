/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.NameBinding;

import javax.servlet.ServletContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Will resolve value from the {@code ServletContext} attribute.
 *
 * @author Michael Ward
 */
class ServletContextAttributeParameter extends AbstractWaffleParameter {

    protected ServletContextAttributeParameter(String key) {
        super(key);
    }

    @SuppressWarnings({"unchecked"})
    public Resolver resolve(PicoContainer picoContainer, ComponentAdapter<?> forAdapter,
            ComponentAdapter<?> injecteeAdapter, Type expectedType, NameBinding expectedNameBinding,
            boolean useNames, Annotation binding) {
        ServletContext servletContext = picoContainer.getComponent(ServletContext.class);
        Object value = servletContext.getAttribute(getKey());
        return new ValueResolver(value != null, value, forAdapter);
    }
}
