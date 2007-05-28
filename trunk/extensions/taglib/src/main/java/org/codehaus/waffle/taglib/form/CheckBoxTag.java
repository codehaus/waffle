package org.codehaus.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;

/**
 * An text element for html files.
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 * @since upcoming
 */
public class CheckBoxTag extends FormElement {

	private String name, value;

	private Boolean checked;

	@Override
	public void release() {
		super.release();
		name = null;
		checked = null;
		value = "true";
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public IterationResult start(Writer out) throws JspException, IOException {

		if (!isRendered()) {
			return IterationResult.PAGE;
		}

		out.write("<input type=\"");
		out.write(getType());
		out.write("\" name=\"");
		out.write(name);
		out.write("\" ");
		out.write("value=\"");
		out.write(value);
		out.write("\"");

		if (this.checked != null && this.checked) {
			out.write(" checked=\"checked\"");
		}
		attributes.outputTo(out);
		out.write(" />");
		return IterationResult.BODY;
	}

	protected String getType() {
		return "checkbox";
	}

	@Override
	protected String getDefaultLabel() {
		return name;
	}
}
