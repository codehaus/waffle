package org.codehaus.waffle.taglib.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.el.ELException;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An calendar element for html files.
 * 
 * @author Nico Steppat
 * @author Guilherme Silveira
 */
public class CalendarTag extends FormElement {

    private static final String NEW_LINE = System.getProperty("line.separator");

    private static StringConverter converter = new DatePatternConverter();

    // *********************************************************************
    // Private constants

    private String name;

    private String pattern;

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
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    // Evaluates expressions as necessary
    private void evaluateExpressions() throws ELException {
        pattern = (String) pageContext.getExpressionEvaluator().evaluate(pattern, String.class, pageContext.getVariableResolver(), null);
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

        String id = this.attributes.get("id");

        if (id == null) {
            id = this.name + "_id";
        }

        out.write("<input type=\"text");
        out.write("\" name=\"");
        out.write(this.name);
        out.write("\" id=\"");
        out.write(id);
        out.write("\" ");

        out.write("value=\"");
        if (this.value != null) {
            out.write(this.getFormattedDate());
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
        out.write(NEW_LINE);

        // BUTTON
        String buttonId = this.name + "_button";

        out.write("<button ");
        out.write("id=\"" + buttonId + "\">...</button>");
        out.write(NEW_LINE);

        // JAVA SCRIPT
        out.write("<script type=\"text/javascript\">" + NEW_LINE);
        out.write("Calendar.setup({ " + NEW_LINE);

        out.write("inputField  : \"" + id + "\"," + NEW_LINE);
        out.write("ifFormat    : \"" + converter.convert(this.pattern) + "\"," + NEW_LINE);
        out.write("button      : \"" + buttonId + "\"," + NEW_LINE);
        out.write("singleClick : false," + NEW_LINE);
        out.write("showsTime   : true," + NEW_LINE);
        out.write("timeFormat  : \"24\"" + NEW_LINE);
        out.write("});" + NEW_LINE);
        out.write("</script>");
        out.write(NEW_LINE);

        clear();
        return IterationResult.PAGE;
    }

    private Date evaluateDate(String expression) throws JspException {
        try {
            return (Date) pageContext.getExpressionEvaluator().evaluate(expression, Date.class,
                    pageContext.getVariableResolver(), null);
        } catch (ELException e) {
            throw new JspException(e);
        }
    }

    private String getFormattedDate() throws JspException {
        return new SimpleDateFormat(this.pattern).format(this.value);
    }

    @Override
    protected String getDefaultLabel() {
        return this.name;
    }
}
