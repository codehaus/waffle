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
package com.thoughtworks.waffle.bind;

import com.thoughtworks.waffle.i18n.MessageResources;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.MissingResourceException;

public class DefaultBindErrorMessageResolver implements BindErrorMessageResolver {
    private final static String BIND_ERROR_SUFFIX = ".bind.error";
    private final static String DEFAULT_BIND_ERROR = "default" + BIND_ERROR_SUFFIX;
    private final MessageResources messageResources;

    public DefaultBindErrorMessageResolver(MessageResources messageResources) {
        this.messageResources = messageResources;
    }

    public String resolve(Object model, String propertyName, String value) {
        String fieldName = messageResources.getMessageWithDefault(propertyName, propertyName);

        try {
            // Is a custom bind error message defined for this field?
            return messageResources.getMessage(fieldName + BIND_ERROR_SUFFIX, value);
        } catch (MissingResourceException ignore) {
            // ignore ... common to not have custom
        }

        try {
            // Get the type for the "setter" and try to find a message for the type
            Method method = OgnlRuntime.getSetMethod(new OgnlContext(), model.getClass(), propertyName);

            if(method != null) {
                String key = findBindErrorMessageKey(method.getParameterTypes()[0]);
                return messageResources.getMessage(key, fieldName, value);
            }
        } catch (IntrospectionException ignore) {
            // ignore
        } catch (OgnlException ignore) {
            // ignore
        } catch(MissingResourceException ignore) {
            // ignore
        }

        // Get the default bind error message
        return messageResources.getMessage(DEFAULT_BIND_ERROR, fieldName, value);
    }

    protected String findBindErrorMessageKey(Class type) {
        if (byte.class.equals(type)
                || short.class.equals(type)
                || int.class.equals(type)
                || long.class.equals(type)
                || float.class.equals(type)
                || double.class.equals(type)
                || Number.class.isAssignableFrom(type)) {
            return "number" + BIND_ERROR_SUFFIX;
        }

        if(Boolean.class.isAssignableFrom(type)) {
            return boolean.class + BIND_ERROR_SUFFIX;
        }

        return type.getName() + BIND_ERROR_SUFFIX;
    }
}
