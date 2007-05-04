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
package com.thoughtworks.waffle.registrar.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;

import javax.servlet.http.HttpServletRequest;

public class RequestAttributeParameter extends AbstractWaffleParameter {

    public RequestAttributeParameter(String key) {
        super(key);
    }

    public Object resolveInstance(PicoContainer picoContainer, ComponentAdapter componentAdapter, Class type) {
        HttpServletRequest request = (HttpServletRequest) picoContainer
                .getComponentInstanceOfType(HttpServletRequest.class);
        return request.getAttribute(getKey());
    }
}
