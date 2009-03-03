/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.NameBinding;
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * A base for Waffle's implementation of PicoContainer Parameter.
 *
 * @author Michael Ward
 */
abstract class AbstractWaffleParameter implements Parameter {
    private final String key;

    public AbstractWaffleParameter(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public boolean isResolvable(PicoContainer picoContainer, ComponentAdapter<?> componentAdapter, Type type, NameBinding nameBinding, boolean b, Annotation annotation) {
        return resolveInstance(picoContainer, componentAdapter, type, nameBinding, b, annotation) != null;
    }

    public void verify(PicoContainer picoContainer, ComponentAdapter<?> componentAdapter, Type type, NameBinding nameBinding, boolean b, Annotation annotation) {
    }

    public void accept(PicoVisitor picoVisitor) {
        picoVisitor.visitParameter(this);
    }

}
