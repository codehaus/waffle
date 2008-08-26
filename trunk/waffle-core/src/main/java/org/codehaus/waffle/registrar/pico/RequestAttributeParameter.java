/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

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

    @SuppressWarnings({"unchecked"})
    public Object resolveInstance(PicoContainer picoContainer,
                                 ComponentAdapter<?> componentAdapter,
                                 Type type,
                                 NameBinding nameBinding,
                                 boolean b,
                                 Annotation annotation) {
        HttpServletRequest request = picoContainer.getComponent(HttpServletRequest.class);
        return request.getAttribute(getKey());
    }

}
