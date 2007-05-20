/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package org.codehaus.waffle.webcontainer.groovy;

import org.nanocontainer.webcontainer.PicoContext;
import org.picocontainer.PicoContainer;

import java.util.Map;

public class RegisterActionsNodeBuilder extends Object {
    private final PicoContainer parentContainer;
    private final PicoContext context;

    public RegisterActionsNodeBuilder(PicoContainer parentContainer, PicoContext context) {
        this.parentContainer = parentContainer;
        this.context = context;
    }

    protected Object createNode(Object name, Map attributes) {
        if (name.equals("application")) {
            return new ApplicationActionsNodeBuilder(parentContainer, context);
        } else if (name.equals("session")) {
            return new SessionActionsNodeBuilder(context);
        } else if (name.equals("request")) {
            return new RequestActionsNodeBuilder(context);
        }
        return null;
    }

}
