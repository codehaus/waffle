/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 *****************************************************************************/
package com.thoughtworks.waffle.groovy;

import groovy.util.NodeBuilder;
import org.nanocontainer.webcontainer.PicoContextHandler;

import java.util.Map;

public class SessionActionsNodeBuilder extends NodeBuilder {
    private final PicoContextHandler context;

    public SessionActionsNodeBuilder(PicoContextHandler context) {
        this.context = context;
    }


    protected Object createNode(Object name, Map attributes) {
        if (name.equals("register")) {
            Object clazz = attributes.remove("class"); // could be string or Class
            // register application
            return null; // err something really
        } else {
            return null;
        }
    }
}
