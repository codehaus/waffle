package org.codehaus.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.codehaus.waffle.taglib.internal.BasicTag;

/**
 * A basic form element.
 * 
 * @author Guilherme Silveira
 * @since 1.0
 */
public class FormTag extends BasicTag implements DynamicAttributes {

	private Attributes attributes;

	private String action;

	private FormStyle style;

	@Override
	public void release() {
		super.release();
		attributes = new Attributes();
		action = null;
		style = new NoFormStyle();
	}

	public IterationResult start(Writer out) throws JspException, IOException {
		style.beginForm();
		out.write("<form action=\"");
		out.write(getAbsoluteUrl(action));
		out.write("\" ");
		attributes.outputTo(out);
		out.write(">");
		return IterationResult.BODY;
	}

	public void end(Writer out) throws JspException, IOException {
		out.write("</form>");
		style.finishForm();
	}

	public void setAction(String name) {
		this.action = name;
	}

	public void setDynamicAttribute(String uri, String localName, Object value)
			throws JspException {
		attributes.put(localName, value);
	}

	public void setType(String type) {
		if (type == null || type.equals("none")) {
			this.style = new NoFormStyle();
		} else if (type.equals("table")) {
			this.style = new TableFormStyle(this, pageContext.getRequest(), pageContext.getOut());
		} else if (type.equals("tableless")) {
			this.style = new TablelessFormStyle(this, pageContext.getRequest(),pageContext.getOut());
		}
	}

	public FormStyle getStyle() {
		return style;
	}

	public String getAttribute(String key) {
		return attributes.get(key);
	}

}
