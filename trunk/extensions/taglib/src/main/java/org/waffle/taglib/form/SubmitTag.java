package org.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;

/**
 * A basic submit button. Accepts an action as to which logic should be executed
 * if this button is clicked.
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class SubmitTag extends FormElement {

	private String value;

	private String action;


	@Override
	public void release() {
	    super.release();
	    this.value = null;
	    this.action = null;
	}
	
	@Override
	public IterationResult start(Writer out) throws JspException, IOException {

		out.write("<input type=\"submit\" ");
		if (action != null && !action.equals("") && getForm() != null) {
			FormTag form = getForm();
			String id = form.getAttribute("id");
			out.write("onClick=\"var form = document.getElementById('" + id
					+ "'); form.action='" + getAbsoluteUrl(action) + "';\" ");
		}
		if (value != null) {
			out.write("value=\"");
			out.write(getI18NMessage(value));
			out.write("\" ");
		}
		attributes.outputTo(out);
		out.write("/>");
		release();
		return IterationResult.BODY;
	}

	@Override
	protected String getDefaultLabel() {
		return "";
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
