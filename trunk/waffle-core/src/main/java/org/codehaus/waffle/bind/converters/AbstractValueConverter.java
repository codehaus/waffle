/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind.converters;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.i18n.MessageResources;

import java.util.Properties;

/**
 * Abstract <code>ValueConverter</code> that holds utility functionality common to all value converters.
 * 
 * @author Mauro Talevi
 */
public abstract class AbstractValueConverter implements ValueConverter {

    protected final MessageResources messageResources;

    protected AbstractValueConverter(MessageResources messageResources) {
        this.messageResources = messageResources;
    }

    /**
     * Determines if the value is missing.  
     * 
     * @param value the String value
     * @return A boolean, <code>true</code> if value is <code>null</code> or trimmed length is 0.
     */
    protected boolean missingValue(String value) {
        return value == null || value.trim().length() == 0;
    }

    /**
     * Handles the case of a missing value.  By default it return a <code>null</code> converted value,
     * but can be overridden to throw a BindException
     * 
     * @param key the error message key
     * @param defaultMessage the default message if key is not found
     * @param parameters the message formatting parameters
     * @return A converted object when value is missing, <code>null</code> by default.
     */
    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        return null;
    }
    
    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    protected BindException newBindException(String key, String defaultMessage, Object... parameters) {
        String message = messageResources.getMessageWithDefault(key, defaultMessage, parameters);
        return new BindException(message);
    }

    protected String messageFor(String key, String defaultMessage, Object... parameters) {
        return messageResources.getMessageWithDefault(key, defaultMessage, parameters);
    }

    protected boolean matches(String value, String regex) {
        return value != null && value.matches(regex);
    }

    protected String patternFor(Properties patterns, String key, String defaultPattern) {
        if ( patterns.containsKey(key)) {
            return patterns.getProperty(key);
        }
        return messageFor(key, defaultPattern);
    }
}
