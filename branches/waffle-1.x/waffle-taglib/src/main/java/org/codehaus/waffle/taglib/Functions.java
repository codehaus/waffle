package org.codehaus.waffle.taglib;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.waffle.validation.ErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.ErrorMessage.Type;
import org.codehaus.waffle.i18n.MessagesContext;

/**
 * A collection of utility functions accessible in taglibs
 * 
 * @author Guilherme Silveira
 * @author Mauro Talevi
 */
public class Functions {

    public static String retrieveAttributes(Map<String, String> attributes) {
        StringBuilder sb = new StringBuilder(' ');
        for (String key : attributes.keySet()) {
            sb.append(key).append("=\"").append(attributes.get(key)).append("\" ");
        }
        return sb.toString();
    }

    public static Object getProperty(Object o, String field) {
        field = Character.toUpperCase(field.charAt(0))
                + field.substring(1, field.length());
        try {
            return o.getClass().getMethod("get" + field).invoke(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static List<? extends ErrorMessage> findAllErrors(ErrorsContext errorsContext) {
        return errorsContext.getAllErrorMessages();
    }

    public static List<? extends ErrorMessage> findErrors(ErrorsContext errorsContext, String typeName) {
        Type type = Enum.valueOf(ErrorMessage.Type.class, typeName);
        return errorsContext.getErrorMessagesOfType(type);
    }

    public static List<? extends ErrorMessage> findErrorsForField(ErrorsContext errorsContext, String typeName, String fieldName) {
        Type type = Enum.valueOf(ErrorMessage.Type.class, typeName);
        switch ( type ){
            case BIND: case FIELD:
                return errorsContext.getErrorMessagesForField(type, fieldName);
            case GLOBAL:
                return errorsContext.getErrorMessagesOfType(type);
            default:
                return Collections.emptyList();           
        }
    }

    public static String findMessageForKey(MessagesContext messagesContext, String key) {
        return messagesContext.getMessage(key);
    }
}
