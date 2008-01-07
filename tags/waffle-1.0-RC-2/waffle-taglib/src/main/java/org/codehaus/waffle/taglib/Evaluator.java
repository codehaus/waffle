package org.codehaus.waffle.taglib;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import java.io.IOException;
import java.util.Map;

/**
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class Evaluator {

    private static final String EMPTY = "";

    public static Object evaluate(String key, PageContext jspContext) throws ELException, IOException {
        ExpressionEvaluator evaluator = jspContext.getExpressionEvaluator();
        String expression = "${" + jspContext.getAttribute("name") + "}";
        Object obj = evaluator.evaluate(expression, Object.class, jspContext.getVariableResolver(), null);
        return obj == null ? EMPTY : obj;
    }

    public static String getAttributes(Map<String,String> attributes) {
        StringBuilder builder = new StringBuilder();
        for ( String key : attributes.keySet() ){
            builder.append(' ')
            .append(key)
            .append("=\"")
            .append(attributes.get(key))
            .append('\"');
        }
        return builder.toString();
    }

}
