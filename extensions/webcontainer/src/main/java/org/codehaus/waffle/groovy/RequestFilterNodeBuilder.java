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

import org.codehaus.waffle.context.WaffleRequestFilter;
import groovy.util.NodeBuilder;
import org.nanocontainer.webcontainer.PicoContextHandler;

import java.util.Map;

public class RequestFilterNodeBuilder extends NodeBuilder {

    public RequestFilterNodeBuilder(PicoContextHandler context, String pathMapping) {
        context.addFilterWithMapping(new WaffleRequestFilter(), pathMapping, 0);
    }

    protected Object createNode(Object name, Map attributes) {
        return null;
    }

}
