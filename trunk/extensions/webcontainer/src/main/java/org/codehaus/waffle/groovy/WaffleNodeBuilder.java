/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/

package org.codehaus.waffle.groovy;

import groovy.util.NodeBuilder;

import java.util.Map;

import org.nanocontainer.script.NanoContainerMarkupException;
import org.nanocontainer.webcontainer.PicoContextHandler;
import org.picocontainer.PicoContainer;

public class WaffleNodeBuilder extends NodeBuilder {
    private final PicoContainer parentContainer;
    private final PicoContextHandler context;

    public WaffleNodeBuilder(PicoContainer parentContainer, PicoContextHandler context) {
        this.parentContainer = parentContainer;
        this.context = context;
    }

    public Object createNode(Object name, Map attributes) throws NanoContainerMarkupException {
        if (name.equals("actionRegistrar")) {
            return new ActionRegistrarNodeBuilder(parentContainer, (Class) attributes.remove("class"));
        } else if (name.equals("requestFilter")) {
            return new RequestFilterNodeBuilder(context, (String) attributes.remove("filter"));
        } else if (name.equals("viewSuffix")) {
            return new ViewSuffixNodeBuilder(context, (String) attributes.remove("suffix"));
        } else if (name.equals("registerActions")) {
            return new RegisterActionsNodeBuilder(parentContainer, context);
        }
        return null;
    }
}
