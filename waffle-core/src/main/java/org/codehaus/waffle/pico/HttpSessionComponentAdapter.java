/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.pico;

import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;
import org.picocontainer.PicoCompositionException;
import org.codehaus.waffle.context.CurrentHttpServletRequest;

import javax.servlet.http.HttpSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

@SuppressWarnings({ "serial", "unchecked" })
public class HttpSessionComponentAdapter implements ComponentAdapter, Serializable {
    
    private final Class<?> componentImplementation = HttpSession.class;

    public ComponentAdapter getDelegate() {
        return null;
    }

    public Object getComponentKey() {
        return componentImplementation;
    }

    public Class<?> getComponentImplementation() {
        return componentImplementation;
    }

    public Object getComponentInstance(PicoContainer picoContainer, Type type) throws PicoCompositionException {
        return Proxy.newProxyInstance(HttpSession.class.getClassLoader(),
                                      new Class[] {HttpSession.class},
                                      new HttpSessionProxy());
    }

    public Object getComponentInstance(PicoContainer container)  {
        return this.getComponentInstance(container, null);
    }

    public ComponentAdapter findAdapterOfType(Class aClass) {
        return null;
    }

    public void verify(PicoContainer container) {
        // do nothing
    }

    public void accept(PicoVisitor visitor) {
        // do nothing
    }


    public String getDescriptor() {
        return "HttpSessionComponentAdapter";
    }

    private static class HttpSessionProxy implements InvocationHandler {

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            HttpSession session = CurrentHttpServletRequest.get().getSession();

            return method.invoke(session, args);
        }
    }
}
