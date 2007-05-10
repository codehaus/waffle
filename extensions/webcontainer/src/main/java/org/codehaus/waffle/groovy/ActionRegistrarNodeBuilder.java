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

import org.codehaus.waffle.registrar.RegistrarAssistant;
import org.codehaus.waffle.registrar.Registrar;
import org.picocontainer.PicoContainer;
import org.nanocontainer.webcontainer.PicoContextHandler;

public class ActionRegistrarNodeBuilder extends NodeBuilder {

    private final PicoContainer parentContainer;
    Object registrarClass;
    private final PicoContextHandler context;


    public ActionRegistrarNodeBuilder(PicoContainer parentContainer, Object registrarClass, PicoContextHandler context) {
        this.parentContainer = parentContainer;
        this.registrarClass = registrarClass;
        this.context = context;
    }

    protected Object createNode(Object current, Map attributes) {

        if (current.equals("registrar")) {
            context.addInitParam(Registrar.class.getName(), registrarClass instanceof Class ? ((Class) registrarClass).getName() : (String) registrarClass);
        }
        

        return "";
    }
}
