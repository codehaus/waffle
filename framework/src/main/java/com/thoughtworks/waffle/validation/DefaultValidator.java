/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package com.thoughtworks.waffle.validation;

import com.thoughtworks.waffle.WaffleException;
import com.thoughtworks.waffle.action.ControllerDefinition;
import com.thoughtworks.waffle.action.method.MethodDefinition;
import com.thoughtworks.waffle.context.ContextContainer;
import com.thoughtworks.waffle.context.RequestLevelContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DefaultValidator implements Validator {
    protected static final String VALIDATOR_SUFFIX = "Validator";

    public void validate(ControllerDefinition controllerDefinition, ErrorsContext errorsContext) {
        ContextContainer container = RequestLevelContainer.get();
        Object controllerValidator = container.getComponentInstance(controllerDefinition.getName() + VALIDATOR_SUFFIX);

        if (controllerValidator == null) {
            return; // doesn't exist ... go no further
        }

        MethodDefinition methodDefinition = controllerDefinition.getMethodDefinition();
        
        if(methodDefinition == null) {
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
            throw new WaffleException(e);
        } catch (InvocationTargetException e) {
            throw new WaffleException(e);
        }
    }
}
