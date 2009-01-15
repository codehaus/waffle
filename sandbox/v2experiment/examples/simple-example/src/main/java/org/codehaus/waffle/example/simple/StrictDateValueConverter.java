package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.bind.converters.DateValueConverter;
import org.codehaus.waffle.i18n.MessageResources;

/**
 * Extends DateValueConverter to enforce no missing values are allowed.
 * 
 * @author Mauro Talevi
 */
public class StrictDateValueConverter extends DateValueConverter {

    public StrictDateValueConverter(MessageResources messageResources) {
        super(messageResources);
    }

    @Override
    protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
        throw newBindException(key, defaultMessage, parameters);
    }
    
}
