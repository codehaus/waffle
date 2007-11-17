package org.codehaus.waffle.taglib.form;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.io.Writer;

/**
 * A button element for html files. Accepts a value which will be i18n.
 *
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class ButtonTag extends FormElement {

    private String value;

    public ButtonTag() {
        clear();
    }

    private void clear() {
        value = null;
    }

    @Override
    public IterationResult start(Writer out) throws JspException, IOException {

        out.write("<input type=\"");
        out.write(getButtonType());
        out.write("\"");
        attributes.outputTo(out);
        out.write(" value=\"");
        out.write(getI18NMessage(value));
        out.write("\"/>");
        clear();
        return IterationResult.BODY;
    }

    /**
     * Returns this button type.
     *
     * @return the button type
     */
    protected String getButtonType() {
        return "button";
    }

    @Override
    protected String getDefaultLabel() {
        return "";
    }

    public void setValue(String value) {
        this.value = value;
    }

}
