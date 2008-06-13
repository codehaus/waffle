/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
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
        ServletContext servletContext = picoContainer
                .getComponent(ServletContext.class);
        return (T) servletContext.getAttribute(getKey());
    }
}
