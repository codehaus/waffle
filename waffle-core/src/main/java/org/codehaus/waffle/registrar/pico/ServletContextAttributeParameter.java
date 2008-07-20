/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.NameBinding;

import javax.servlet.ServletContext;
import java.lang.annotation.Annotation;

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
    public <T> T resolveInstance(PicoContainer picoContainer,
                                 ComponentAdapter componentAdapter,
                                 Class<T> clazz,
                                 NameBinding nameBinding,
                                 boolean b,
                                 Annotation annotation) {
        ServletContext servletContext = picoContainer.getComponent(ServletContext.class);
        return (T) servletContext.getAttribute(getKey());
    }
}
