/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
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
import org.picocontainer.Parameter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;

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

    public boolean isResolvable(PicoContainer picoContainer, ComponentAdapter componentAdapter, Class clazz) {
        return resolveInstance(picoContainer, componentAdapter, clazz) != null;
    }

    public void verify(PicoContainer picoContainer, ComponentAdapter componentAdapter, Class clazz) {
        // do nothing
    }

    public void accept(PicoVisitor picoVisitor) {
        picoVisitor.visitParameter(this);
    }

}
