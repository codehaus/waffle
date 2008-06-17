/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.registrar.pico;

import org.picocontainer.Parameter;

/**
 * Implementations of this interface will find the correct PicoContainer Parameter needed based on the
 * argument being resolved
 *
 * @author Michael Ward
 */
public interface ParameterResolver {

    /**
     * Find the correct Parameter
     *
     * @param arg the argument to be resolved
     * @return the correct Parameter.
     */
    Parameter resolve(Object arg);
}
