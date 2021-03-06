/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.monitor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.validation.BindErrorMessage;

/**
 * A monitor for bind-related events
 * 
 * @author Mauro Talevi
 */
public interface BindMonitor extends Monitor {

    void viewBindFailed(Object controller, Exception cause);

    void viewValueBound(String name, Object value, Object controller);

    void controllerBindFailed(Object controller, BindErrorMessage errorMessage, Exception cause);

    void controllerValueBound(String name, Object value, Object controller);

    void genericParameterTypeFound(Type type, Method method);

    void genericParameterTypeNotFound(Method method);

    void valueConverterFound(Type type, ValueConverter converter);

    void valueConverterNotFound(Type type);
    
}
