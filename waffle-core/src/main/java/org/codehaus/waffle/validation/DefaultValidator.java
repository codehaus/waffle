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
    private final ValidatorConfiguration validatorConfiguration;
    
    public DefaultValidator(ValidationMonitor validationMonitor){
        this(new DefaultValidatorConfiguration(), validationMonitor);        
    }
    
    public DefaultValidator(ValidatorConfiguration validatorConfiguration, ValidationMonitor validationMonitor){
        this.validatorConfiguration = validatorConfiguration;
        this.validationMonitor = validationMonitor;        
    }

    public void validate(ControllerDefinition controllerDefinition, ErrorsContext errorsContext) {
        ContextContainer container = RequestLevelContainer.get();
        Object controllerValidator = container.getComponentInstance(controllerDefinition.getName() + validatorConfiguration.getSuffix());

        if (controllerValidator == null) {
            validationMonitor.controllerValidatorNotFound();
            return; // doesn't exist ... go no further
        }

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
