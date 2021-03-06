package org.codehaus.waffle.taglib.form;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import java.io.IOException;
import java.io.Writer;

/**
 * A img element for html files.
 *
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class ImgTag extends FormElement implements DynamicAttributes {

    private String src;

    public ImgTag() {
        clear();
    }

    private void clear() {
        src = null;
    }

    @Override
    public void release() {
        super.release();
        src = null;
    }

    @Override
    public IterationResult start(Writer out) throws JspException, IOException {
        out.write("<img src=\"");
        out.write(getAbsoluteUrl(src));
        out.write("\"");
        attributes.outputTo(out);
        out.write("/>");
        clear();
        return IterationResult.BODY;
    }

    @Override
    protected String getDefaultLabel() {
        return "";
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
