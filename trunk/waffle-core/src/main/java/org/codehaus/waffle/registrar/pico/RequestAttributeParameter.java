/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.NameBinding;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * Will resolve value from the {@code HttpServletRequest} attribute.
 *
 * @author Michael Ward
 */
class RequestAttributeParameter extends AbstractWaffleParameter {

    protected RequestAttributeParameter(String key) {
        super(key);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T resolveInstance(PicoContainer picoContainer,
                                 ComponentAdapter componentAdapter,
                                 Class<T> clazz,
                                 NameBinding nameBinding,
                                 boolean b,
                                 Annotation annotation) {
        HttpServletRequest request = picoContainer
                .getComponent(HttpServletRequest.class);
        return (T) request.getAttribute(getKey());
    }
}
