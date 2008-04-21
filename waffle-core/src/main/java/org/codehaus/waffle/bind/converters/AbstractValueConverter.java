/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.bind.converters;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.i18n.MessageResources;

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
    
    protected BindException newBindException(String key, String defaultMessage, Object... parameters) {
        String message = messageResources.getMessageWithDefault(key, defaultMessage, parameters);
        return new BindException(message);
    }

    protected String messageFor(String key, String defaultMessage, Object... parameters) {
        return messageResources.getMessageWithDefault(key, defaultMessage, parameters);
    }
}
