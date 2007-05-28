package org.codehaus.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * A text area.
 * 
 * @author Guilherme Silveira
 */
public class TextAreaTag extends FormElement {

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

	out.write("<textarea name=\"");
	out.write(name);
	out.write("\" ");
	attributes.outputTo(out);
	out.write(">");
	return IterationResult.BODY;
    }

    @Override
    protected String getDefaultLabel() {
	return name;
    }

    @Override
    protected IterationResult afterBody(JspWriter out) throws IOException {

	out.write("</textarea>");
	release();
	return IterationResult.PAGE;
    }
}
