package org.codehaus.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;

/**
 * A file element for html files.
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class FileTag extends FormElement {

	private String name;


	@Override
	public void release() {
	    super.release();
	    this.name = null;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

	public IterationResult start(Writer out) throws JspException, IOException {

		out.write("<input type=\"file\" name=\"");
		out.write(name);
		out.write("\"");
		attributes.outputTo(out);
		out.write("/>");
		return IterationResult.BODY;
	}

	@Override
	protected String getDefaultLabel() {
		return name;
	}
}
