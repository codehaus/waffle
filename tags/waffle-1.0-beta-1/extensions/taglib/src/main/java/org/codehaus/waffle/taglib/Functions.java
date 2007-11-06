package org.codehaus.waffle.taglib;

import org.codehaus.waffle.validation.ErrorsContext;

import java.util.List;
import java.util.Map;

public class Functions {

    public static String retrieveAttributes(Map<String, String> att) {
        StringBuilder sb = new StringBuilder(" ");
        for (String key : att.keySet()) {
            sb.append(key)
                    .append("=\"")
                    .append(att.get(key))
                    .append("\" ");
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

    public static List findFieldErrors(ErrorsContext errorsContext, String fieldName) {
        return errorsContext.getFieldErrorMessages(fieldName);
    }

}