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
package org.codehaus.waffle.context.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoVisitor;
import org.codehaus.waffle.context.CurrentHttpServletRequest;

import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings("serial")
public class HttpSessionComponentAdapter implements ComponentAdapter, Serializable {
    
    private final Class<?> componentImplementation = HttpSession.class;

    public Object getComponentKey() {
        return componentImplementation;
    }

    public Class<?> getComponentImplementation() {
        return componentImplementation;
    }

    public Object getComponentInstance(PicoContainer container) throws PicoInitializationException, PicoIntrospectionException {
        return Proxy.newProxyInstance(HttpSession.class.getClassLoader(),
                                      new Class[] {HttpSession.class},
                                      new HttpSessionProxy());
    }

    public void verify(PicoContainer container) throws PicoIntrospectionException {
        // do nothing
    }

    public void accept(PicoVisitor visitor) {
        // do nothing
    }

    private static class HttpSessionProxy implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            HttpSession session = CurrentHttpServletRequest.get().getSession();

            return method.invoke(session, args);
        }
    }
}
