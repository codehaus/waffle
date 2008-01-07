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
import org.picocontainer.PicoContainer;

import javax.servlet.http.HttpSession;

class SessionAttributeParameter extends AbstractWaffleParameter {

    protected SessionAttributeParameter(String key) {
        super(key);
    }

    public Object resolveInstance(PicoContainer picoContainer, ComponentAdapter componentAdapter, Class clazz) {
        HttpSession session = (HttpSession) picoContainer.getComponentInstanceOfType(HttpSession.class);
        return session.getAttribute(getKey());
    }
}
