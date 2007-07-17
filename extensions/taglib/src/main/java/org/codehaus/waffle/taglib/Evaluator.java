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

    public static Object evaluate(String key, PageContext jspContext) throws ELException, IOException {
        ExpressionEvaluator evaluator = jspContext.getExpressionEvaluator();
        String expression = "${" + jspContext.getAttribute("name") + "}";
        Object obj = evaluator.evaluate(expression, Object.class, jspContext.getVariableResolver(), null);
        return obj == null ? "" : obj;
    }

    public static String getAttributes(Map attributes) {
        StringBuilder builder = new StringBuilder();
        // note: dont know why "Map.Entry entry : " does not work here (guilherme) 
        for (Object obj : attributes.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            builder.append(' ')
                    .append(entry.getKey())
                    .append("=\"")
                    .append(entry.getValue())
                    .append('\"');
        }
        return builder.toString();
    }

}
