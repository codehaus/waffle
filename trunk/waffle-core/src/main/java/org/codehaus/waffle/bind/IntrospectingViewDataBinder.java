/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.monitor.BindMonitor;

/**
 * ViewDataBinder implementation which uses Java beans introspector to bind {@link PropertyDescriptor#getReadMethod()
 * read methods}.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class IntrospectingViewDataBinder implements ViewDataBinder {

    private final BindMonitor bindMonitor;

    public IntrospectingViewDataBinder(BindMonitor bindMonitor) {
        this.bindMonitor = bindMonitor;
    }

    public void bind(HttpServletRequest request, Object controller) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(controller.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                Method readMethod = propertyDescriptor.getReadMethod();

                if (isBindable(readMethod)) {
                    String name = propertyDescriptor.getName();
                    request.setAttribute(name, readMethod.invoke(controller));
                    Object value = request.getAttribute(name);
                    bindMonitor.viewValueBound(name, value, controller);
                }
            }
        } catch (IntrospectionException e) {
            bindMonitor.viewBindFailed(controller, e);
            throw new WaffleException(e);
        } catch (IllegalAccessException e) {
            bindMonitor.viewBindFailed(controller, e);
            throw new WaffleException(e);
        } catch (InvocationTargetException e) {
            bindMonitor.viewBindFailed(controller, e);
            throw new WaffleException(e);
        }
    }

    private boolean isBindable(Method method) {
        return method != null && method.getParameterTypes().length == 0 && !method.getName().equals("getClass");
    }

}
