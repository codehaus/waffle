package org.codehaus.waffle.taglib.form;

import org.apache.taglibs.standard.tag.common.fmt.LocaleSupport;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * An date element for html files.
 *
 * @author Nico Steppat
 * @author Guilherme Silveira
 */
public class DateTag extends FormElement {

    // *********************************************************************
    // Private constants
    private static final String SHORT = "short";
    private static final String MEDIUM = "medium";
    private static final String LONG = "long";
    private static final String FULL = "full";
    private static final String DEFAULT = "default";
    private final LocaleSupport localeSupport = new LocaleSupport();
    private String name, pattern, dateStyle;
    private Date value;

    @Override
    public void release() {
        super.release();
        clear();
    }

    private void clear() {
        name = null;
        pattern = null;
        value = null;
        dateStyle = DEFAULT;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setDateStyle(String style) {
        this.dateStyle = style;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    // Evaluates expressions as necessary
    private void evaluateExpressions() throws ELException {

        ExpressionEvaluator evaluator = pageContext.getExpressionEvaluator();
        // 'dateStyle' attribute
        if (dateStyle != null) {
            dateStyle = (String) evaluator.evaluate(dateStyle, String.class, pageContext.getVariableResolver(), null);
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
            out.write(getFormattedDate());
        } else {
            Date data = this.evaluateDate("${" + this.name + "}");
            this.setValue(data);
            if (data != null) {
                out.write(getFormattedDate());
            }
        }
        out.write("\"");
        attributes.outputTo(out);
        out.write("/>");
        clear();
        return IterationResult.BODY;
    }

    private Date evaluateDate(String expression) throws JspException {
        try {
            return (Date) pageContext.getExpressionEvaluator().evaluate(
                    expression, Date.class, pageContext.getVariableResolver(),
                    null);
        } catch (ELException e) {
            throw new JspException(e);
        }
    }

    private String getFormattedDate() throws JspException {

        String formatted = null;

        Locale locale = this.localeSupport.getFormattingLocale(pageContext);

        if (locale != null) {
            // Create formatter
            DateFormat formatter = createFormatter(locale);

            // Apply pattern, if present
            if (pattern != null) {
                try {
                    ((SimpleDateFormat) formatter).applyPattern(pattern);
                } catch (ClassCastException cce) {
                    formatter = new SimpleDateFormat(pattern, locale);
                }
            }

            formatted = formatter.format(value);
        } else {
            // no formatting locale available, use Date.toString()
            formatted = value.toString();
        }

        return formatted;
    }

    private DateFormat createFormatter(Locale locale) throws JspException {
        return DateFormat.getDateInstance(getStyle(dateStyle), locale);
    }

    /*
      * Converts the given string description of a formatting style for dates and
      * times to the corresponding java.util.DateFormat constant.
      *
      * @param style String description of formatting style for dates and times
      * @param errCode Error code to throw if given style is invalid
      *
      * @return java.util.DateFormat constant corresponding to given style
      *
      * @throws JspException if the given style is invalid
      */
    private int getStyle(String style) throws JspException {
        int ret = DateFormat.DEFAULT;

        if (style != null) {
            if (DEFAULT.equalsIgnoreCase(style)) {
                ret = DateFormat.DEFAULT;
            } else if (SHORT.equalsIgnoreCase(style)) {
                ret = DateFormat.SHORT;
            } else if (MEDIUM.equalsIgnoreCase(style)) {
                ret = DateFormat.MEDIUM;
            } else if (LONG.equalsIgnoreCase(style)) {
                ret = DateFormat.LONG;
            } else if (FULL.equalsIgnoreCase(style)) {
                ret = DateFormat.FULL;
            } else {
                throw new JspException("Invalid date style");
            }
        }

        return ret;
    }

    protected String getType() {
        return "text";
    }

    @Override
    protected String getDefaultLabel() {
        return name;
    }
}
