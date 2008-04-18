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
package org.codehaus.waffle.bind.ognl;

import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.i18n.MessageResources;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import java.beans.IntrospectionException;
import java.lang.reflect.Method;
import java.util.MissingResourceException;

/**
 * Ognl-based BindErrorMessageResolver, which uses the following convention for the message resource bundles: all bind
 * error keys use prefix "bind.error." to which the type object it appended, eg
 * <ul>
 * <li>"bind.error.number": a bind error for any number (short, int, float, double, long)</li>
 * <li>"bind.error.boolean": a bind error for a boolean</li>
 * <li>"bind.error.[type.getName()]": a bind error for the type</li>
 * <li>"bind.error.default": any other bind error that does not satisfy any of the above</li>
 * </ul>
 * The default bind error also has a default message (cf OgnlBindErrorMessageResolver#DEFAULT_MESSAGE) if the default
 * key is not found.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class OgnlBindErrorMessageResolver implements BindErrorMessageResolver {
    static final String BIND_ERROR_KEY_PREFIX = "bind.error.";
    static final String NUMBER_NAME = "number";
    static final String DEFAULT_NAME = "default";
    static final String DEFAULT_MESSAGE = "Invalid value ''{1}'' for field ''{0}''";
    private final MessageResources messageResources;

    public OgnlBindErrorMessageResolver(MessageResources messageResources) {
        this.messageResources = messageResources;
    }

    public String resolve(Object model, String propertyName, String value) {
        String fieldName = messageResources.getMessageWithDefault(propertyName, propertyName);

        try {
            // Is a custom bind error message defined for this field?
            return messageResources.getMessage(keyFor(fieldName), value);
        } catch (MissingResourceException ignore) {
            // ignore ... common to not have custom
        }

        try {
            // Get the type for the "setter" and try to find a message for the type
            Method method = OgnlRuntime.getSetMethod(new OgnlContext(), model.getClass(), propertyName);

            if (method != null) {
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
        return messageResources.getMessageWithDefault(keyFor(DEFAULT_NAME), DEFAULT_MESSAGE, fieldName, value);
    }

    protected String findBindErrorMessageKey(Class<?> type) {
        if (byte.class.equals(type)
                || short.class.equals(type)
                || int.class.equals(type)
                || long.class.equals(type)
                || float.class.equals(type)
                || double.class.equals(type)
                || Number.class.isAssignableFrom(type)) {
            return keyFor(NUMBER_NAME);
        }

        if(Boolean.class.isAssignableFrom(type)) {
            return keyFor(boolean.class.getName());
        }

        return keyFor(type.getName());
    }
    
    static String keyFor(String name) {
        return BIND_ERROR_KEY_PREFIX + name;
    }


}
