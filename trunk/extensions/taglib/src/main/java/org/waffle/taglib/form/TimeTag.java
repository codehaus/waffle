package org.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.el.ELException;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.apache.taglibs.standard.resources.Resources;
import org.apache.taglibs.standard.tag.common.fmt.DateSupport;

/**
 * An text element for html files.
 * 
 * @author Nico Steppat
 * @since 0.9.4
 */
public class TimeTag extends FormElement {

    // *********************************************************************
    // Private constants

    private static final String SHORT = "short";
    private static final String MEDIUM = "medium";
    private static final String LONG = "long";
    private static final String FULL = "full";
    private static final String DEFAULT = "default";
    private String name, pattern, timeStyle;

    private Date value;


    @Override
    public void release() {
	super.release();
	name = pattern = timeStyle = null;
	value = null;

    }

    public void setName(String name) {
	this.name = name;
    }

    public void setPattern(String pattern) {
	this.pattern = pattern;
    }

    public void setTimeStyle(String style) {
	this.timeStyle = style;
    }

    public void setValue(Date value) {
	this.value = value;
    }

    // Evaluates expressions as necessary
    private void evaluateExpressions() throws JspException {
	/*
         * Note: we don't check for type mismatches here; we assume the
         * expression evaluator will return the expected type (by virtue of
         * knowledge we give it about what that type is). A ClassCastException
         * here is truly unexpected, so we let it propagate up.
         */

	// 'value' attribute
	// TODO check how the ExpressionEvaluatorManager evaluates the date ()
	/*
         * if (value != null) { value= (Date)
         * ExpressionEvaluatorManager.evaluate("value", value, Date.class, this,
         * pageContext); }
         */

	// 'dateStyle' attribute
	if (timeStyle != null) {
	    timeStyle = (String) ExpressionEvaluatorManager.evaluate("dateStyle", timeStyle, String.class, this, pageContext);
	}

	// 'pattern' attribute
	if (pattern != null) {
	    pattern = (String) ExpressionEvaluatorManager.evaluate("pattern", pattern, String.class, this, pageContext);
	}
    }

    // evaluates expression and chains to parent
    @Override
    public int doStartTag() throws JspException {

	// evaluate any expressions we were passed, once per invocation
	evaluateExpressions();

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
	} else if (pageContext.getAttribute(this.name) != null) {

	    Date data = this.evaluateDate("${" + this.name + "}");
	    this.setValue(data);
	    out.write(getFormattedDate());
	}
	out.write("\"");
	attributes.outputTo(out);
	out.write("/>");
	release();
	return IterationResult.BODY;
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

	String formatted = null;

	Locale locale = DateSupport.getFormattingLocale(pageContext, this, true, DateFormat.getAvailableLocales());
	// Locale locale = new Locale("pt");

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
	return DateFormat.getTimeInstance(getStyle(timeStyle, "FORMAT_DATE_INVALID_DATE_STYLE"), locale);
    }

    /*
         * Converts the given string description of a formatting style for dates
         * and times to the corresponding java.util.DateFormat constant.
         * 
         * @param style String description of formatting style for dates and
         * times @param errCode Error code to throw if given style is invalid
         * 
         * @return java.util.DateFormat constant corresponding to given style
         * 
         * @throws JspException if the given style is invalid
         */
    private int getStyle(String style, String errCode) throws JspException {
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
		throw new JspException(Resources.getMessage(errCode, style));
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
