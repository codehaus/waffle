package org.codehaus.waffle.taglib;

import java.util.List;
import java.util.Map;

import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.FieldErrorMessage;

public class Functions {
	
	public static String retrieveAttributes(Map<String, String> att) {
		StringBuffer sb = new StringBuffer(" ");
		for(String key : att.keySet()) {
			sb.append(key);
			sb.append("=\"");
			sb.append(att.get(key));
			sb.append("\" ");
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

    public static List<FieldErrorMessage> findFieldErrors(ErrorsContext errorsContext, String fieldName) {
        return errorsContext.getFieldErrorMessages(fieldName);
    }

}