package com.thoughtworks.waffle.example.simple;

import com.thoughtworks.waffle.bind.BindException;
import com.thoughtworks.waffle.bind.WaffleTypeConverter;
import com.thoughtworks.waffle.i18n.MessageResources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Michael Ward
 */
public class DateTypeConverter implements WaffleTypeConverter {
    private final MessageResources messageResources;

    public DateTypeConverter(MessageResources messageResources) {
        this.messageResources = messageResources;
    }

    /**
     * Will accept any class stemming from <code>java.util.Date</code>
     *
     * @param type represent the type of the field a value is to be bound to
     * @return true if isA Date
     */
    public boolean accept(Class type) {
        return Date.class.isAssignableFrom(type);
    }

    public Object convert(String propertyName, String value, Class toType) throws BindException {
        if (value == null || value.equals("")) {
            return null;
        }

        String datePattern = messageResources.getMessageWithDefault("date.format", "dd-MM-yyyy");

        try {
            return new SimpleDateFormat(datePattern).parse(value);
        } catch (ParseException e) {
            String fieldName = messageResources.getMessageWithDefault(propertyName, propertyName);
            String message = messageResources.getMessage("date.bind.error", fieldName, value, datePattern);
            throw new BindException(message);
        }
    }

}
