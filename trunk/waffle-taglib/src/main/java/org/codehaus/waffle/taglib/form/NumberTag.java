package org.codehaus.waffle.taglib.form;

import org.codehaus.waffle.taglib.LocaleSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * A formatted number element for html files.
 *
 * @author Fabio Kung
 */
// Decide whether to use locale always or not
public class NumberTag extends FormElement {

    private final LocaleSupport localeSupport = new LocaleSupport();
    private String name, pattern;
    private Number value;

    @Override
    public void release() {
        super.release();
        clear();
    }

    private void clear() {
        name = null;
        pattern = null;
        value = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    // Evaluates expressions as necessary
    private void evaluateExpressions() throws ELException {

        ExpressionEvaluator evaluator = pageContext.getExpressionEvaluator();
        // 'name' attribute
        if (name != null) {
            name = (String) evaluator.evaluate(name, String.class, pageContext.getVariableResolver(), null);
        }
        // 'pattern' attribute
        if (pattern != null) {
            pattern = (String) evaluator.evaluate(pattern, String.class, pageContext.getVariableResolver(), null);
        }
    }

    // evaluates expression and chains to parent
    @Override
    public int doStartTag() throws JspException {

        // evaluate any expressions we were passed, once per invocation
        try {
            evaluateExpressions();
        } catch (ELException e) {
            throw new JspException(e);
        }

        // chain to the parent implementation
        return super.doStartTag();
    }

    @Override
    public IterationResult start(Writer out) throws JspException, IOException {

        out.write("<input type=\"");
        out.write(getType());
        out.write("\" name=\"");
        out.write(name);
        out.write("\" ");
        out.write("value=\"");
        if (this.value != null) {
            out.write(getFormattedNumber());
        } else {
            Number number = this.evaluateNumber("${" + this.name + "}");
            this.setValue(number);
            if (number != null) {
                out.write(getFormattedNumber());
            }
        }
        out.write("\"");
        attributes.outputTo(out);
        out.write("/>");
        clear();
        return IterationResult.BODY;
    }

    private Number evaluateNumber(String expression) throws JspException {
        try {
            return (Number) pageContext.getExpressionEvaluator().evaluate(
                    expression, Number.class, pageContext.getVariableResolver(),
                    null);
        } catch (ELException e) {
            throw new JspException(e);
        }
    }

    private String getFormattedNumber() throws JspException {
        NumberFormat formatter = getFormatter();
        return formatter.format(value);
    }

    private NumberFormat getFormatter() {
        NumberFormat formatter;
        Locale locale = this.localeSupport.getFormattingLocale(pageContext);
        if(pattern != null) {
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
            formatter = decimalFormat;
        } else {
            formatter = NumberFormat.getNumberInstance(locale);
        }
        return formatter;
    }

    protected String getType() {
        return "text";
    }

    @Override
    protected String getDefaultLabel() {
        return name;
    }
}