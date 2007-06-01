/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.monitor;

import org.codehaus.waffle.action.MethodDefinition;

import java.util.Set;

/**
 * Implementation of Monitor that writes to console
 * 
 * @author Mauro Talevi
 */
public class ConsoleMonitor implements Monitor {

    protected void write(String message) {
        System.out.println(message);
    }

    public void defaultActionMethodFound(MethodDefinition methodDefinition) {
        write("Default ActionMethod found: "+methodDefinition);
    }

    public void defaultActionMethodCached(Class controllerType, MethodDefinition methodDefinition) {
        write("Default ActionMethod cached for controller "+ controllerType.getName()+": "+methodDefinition);        
    }

    public void pragmaticActionMethodFound(MethodDefinition methodDefinition) {
        write("Pragmatic ActionMethod found: "+methodDefinition);
    }

    public void actionMethodFound(MethodDefinition methodDefinition) {        
        write("ActionMethod found:  "+methodDefinition);
    }

    public void methodNameResolved(String methodName, String methodKey, Set<String> keys) {
        write("Method name '"+methodName+"' found for key '"+methodKey+"' among keys "+keys);
    }
    
}
