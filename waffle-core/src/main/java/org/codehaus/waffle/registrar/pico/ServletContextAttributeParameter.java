/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

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
    public Object resolveInstance(PicoContainer picoContainer,
                                 ComponentAdapter componentAdapter,
                                 Type type,
                                 NameBinding nameBinding,
                                 boolean b,
                                 Annotation annotation) {
        ServletContext servletContext = picoContainer.getComponent(ServletContext.class);
        return servletContext.getAttribute(getKey());
    }
}
