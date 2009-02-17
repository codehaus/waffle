/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

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
    public Object resolveInstance(PicoContainer picoContainer,
                                 ComponentAdapter componentAdapter,
                                 Type type,
                                 NameBinding nameBinding,
                                 boolean b,
                                 Annotation annotation) {
        HttpSession session = picoContainer.getComponent(HttpSession.class);
        return session.getAttribute(getKey());
    }
}
