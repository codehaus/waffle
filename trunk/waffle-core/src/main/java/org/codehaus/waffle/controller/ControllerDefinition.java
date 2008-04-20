/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.controller;

import org.codehaus.waffle.action.MethodDefinition;

/**
 * <p>In Waffle a Controller can be any Pojo.  Controllers are registered per application
 * with a custom <code>Registrar</code>.  This class is, a wrapper to merge both
 * the name the controller was registered under and the actual controller instance.
 * <br/>
 * <b>NOTE:</b>  This is required so that Waffle can properly direct to a
 * view when no 'controller method' request parameter was found, which typically occurs
 * when a user first enters a web application.
 * </p>
 *
 * @author Michael Ward
 */
public class ControllerDefinition {
    private final String name;
    private final Object controller;
    private final MethodDefinition methodDefinition;

    public ControllerDefinition(String name, Object controller, MethodDefinition methodDefinition) {
        this.name = name;
        this.controller = controller;
        this.methodDefinition = methodDefinition;
    }

    /**
     * The name the controller is registered under in Waffle.
     */
    public String getName() {
        return name;
    }

    /**
     * The controller (Pojo) this definition is wrapping
     */
    public Object getController() {
        return controller;
    }

    /**
     * The method definition which defines tha method and argument values to be invoked
     * on the controller
     */
    public MethodDefinition getMethodDefinition() {
        return methodDefinition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[ControllerDefinition name=").append(name).append(", controller=")
                .append(controller).append(", methodDefinition=").append(methodDefinition).append("]");
        return sb.toString();
    }
}
