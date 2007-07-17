package org.codehaus.waffle.taglib.form;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.io.Writer;

/**
 * A text element for html files.
 *
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class TextTag extends FormElement {
    private String name, value;

    @Override
    public void release() {
        super.release();
        name = null;
        value = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public IterationResult start(Writer out) throws JspException, IOException {

        out.write("<input type=\"");
        out.write(getType());
        out.write("\" name=\"");
        out.write(name);
        out.write("\" ");
        out.write("value=\"");
        if (value != null) {
            out.write(value);
        } else {
            out.write(evaluateEl(name));
        }
        out.write("\"");
        attributes.outputTo(out);
        out.write(" />");
        release();
        return IterationResult.BODY;
    }

    protected String getType() {
        return "text";
    }

    @Override
    protected String getDefaultLabel() {
        return name;
    }
}
