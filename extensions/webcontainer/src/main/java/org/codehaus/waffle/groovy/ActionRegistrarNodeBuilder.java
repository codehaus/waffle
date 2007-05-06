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
import org.picocontainer.PicoContainer;

public class ActionRegistrarNodeBuilder extends NodeBuilder {

    private final PicoContainer parentContainer;
    Class registrarClass;


    public ActionRegistrarNodeBuilder(PicoContainer parentContainer, Class registrarClass) {
        this.parentContainer = parentContainer;
        this.registrarClass = registrarClass;

        //TODO What to do here Mike?

        //PicoContextContainerFactory contextContainerFactory = new PicoContextContainerFactory(null);
        //contextContainerFactory.buildApplicationContextContainer();


        RegistrarAssistant registrarAssistant = new RegistrarAssistant(registrarClass);

    }

    protected Object createNode(Object current, Map attributes) {
        return "";
    }
}
