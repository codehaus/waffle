/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.monitor.ValidationMonitor;

/**
 * Default implementation of Validator
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultValidator implements Validator {

    private final ValidationMonitor validationMonitor;

    public DefaultValidator(ValidationMonitor validationMonitor){
        this.validationMonitor = validationMonitor;
    }

    public void validate(ControllerDefinition controllerDefinition, ErrorsContext errorsContext, Object controllerValidator) {

        MethodDefinition methodDefinition = controllerDefinition.getMethodDefinition();
        if (methodDefinition == null) {
            validationMonitor.methodDefinitionNotFound(controllerDefinition);
            return; // no method ... go no further
        }

        Method method = methodDefinition.getMethod();
        Class<?>[] parameterTypes = new Class[method.getParameterTypes().length + 1];
        parameterTypes[0] = ErrorsContext.class;

        for (int i = 0; i < method.getParameterTypes().length; i++) {
            parameterTypes[i + 1] = method.getParameterTypes()[i];
        }

        try {
            Method validationMethod = controllerValidator.getClass().getMethod(method.getName(), parameterTypes);

            List<Object> methodArguments = new ArrayList<Object>();
            for (Object argument : methodDefinition.getMethodArguments()) {
                methodArguments.add(argument);
            }
            methodArguments.add(0, errorsContext);

            validationMethod.invoke(controllerValidator, methodArguments.toArray());
        } catch (NoSuchMethodException ignore) {
            // ignore
        } catch (IllegalAccessException e) {
            validationMonitor.validationFailed(e);
            throw new WaffleException(e);
        } catch (InvocationTargetException e) {
            validationMonitor.validationFailed(e);
            throw new WaffleException(e);
        }
    }

}
