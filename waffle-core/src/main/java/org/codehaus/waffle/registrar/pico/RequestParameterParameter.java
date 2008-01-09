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
import org.codehaus.waffle.bind.StringTransmuter;

import javax.servlet.http.HttpServletRequest;

/**
 * This class is dependent on <code>StringTransmuter</code> so that a parameter value can
 * be converted into the correct type before satisfying the dependency.  This is useful
 * because request parameters are required to be String.
 *
 * @see StringTransmuter
 */
class RequestParameterParameter extends AbstractWaffleParameter {
    private final StringTransmuter stringTransmuter;

    protected RequestParameterParameter(String key, StringTransmuter stringTransmuter) {
        super(key);
        this.stringTransmuter = stringTransmuter;
    }

    @SuppressWarnings({"unchecked"})
    public Object resolveInstance(PicoContainer picoContainer, ComponentAdapter adapter, Class expectedType) {
        HttpServletRequest request = (HttpServletRequest) picoContainer
                .getComponentInstanceOfType(HttpServletRequest.class);
        String value = request.getParameter(getKey());
        return stringTransmuter.transmute(value, expectedType);
    }
}