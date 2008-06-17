/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.NameBinding;

import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;

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
    public <T> T resolveInstance(PicoContainer picoContainer,
                                 ComponentAdapter componentAdapter,
                                 Class<T> clazz,
                                 NameBinding nameBinding,
                                 boolean b,
                                 Annotation annotation) {
        HttpSession session = picoContainer.getComponent(HttpSession.class);
        return (T) session.getAttribute(getKey());
    }
}
