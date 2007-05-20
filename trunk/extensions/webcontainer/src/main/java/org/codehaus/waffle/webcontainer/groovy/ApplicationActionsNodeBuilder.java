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

import java.util.Map;

import org.nanocontainer.webcontainer.PicoContext;
import org.picocontainer.PicoContainer;

public class ApplicationActionsNodeBuilder extends ActionsNodeBuilder {

    public ApplicationActionsNodeBuilder(PicoContainer parentContainer, PicoContext context) {
        super(parentContainer, context);
    }

    protected Object createNode(Object name, Map attributes) {
        if (name.equals("register")) {
            registerAction(attributes);
            return null; // err something really
        } else {
            return null;
        }
    }

}
