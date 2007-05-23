package org.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;

/**
 * A form row.
 * 
 * @author Guilherme Silveira
 */
public class RowTag extends FormElement {

	@Override
	protected String getDefaultLabel() {
		return "";
	}
	
	public RowTag() {
		// does nothing
	}

	@Override
	protected IterationResult start(Writer out) throws JspException, IOException {
		return IterationResult.BODY;
	}

}
