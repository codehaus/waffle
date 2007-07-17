package org.codehaus.waffle.taglib.form;

import org.codehaus.waffle.taglib.internal.BasicTag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;
import java.io.IOException;

/**
 * An form element. This class has been created in order to be extended.
 *
 * @author Guilherme Silveira
 */
public abstract class FormElement extends BasicTag implements DynamicAttributes {

    protected Attributes attributes;

    /**
     * The element label.
     */
    private String label;

    public void setLabel(String label) {
        this.label = label;
    }

    public int doEndTag() throws JspException {
        int value = super.doEndTag();
        if (!isRendered()) {
            return IterationResult.PAGE.getTagResult();
        }
        try {
            FormTag form = getForm();
            if (form != null) {
                FormStyle style = form.getStyle();
                if (label != null && !label.equals("")) {
                    style.addErrors(label);
                }
                style.finishLine();
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
        release();
        return value;
    }

    /**
     * Returns its form.
     *
     * @return the form
     */
    protected FormTag getForm() {
        return findAncestor(FormTag.class);
    }

    public int doStartTag() throws JspException {

        if (!isRendered()) {
            return IterationResult.SKIP_BODY.getTagResult();
        }

        try {
            FormTag form = getForm();
            if (form != null) {
                FormStyle style = form.getStyle();
                if (label == null) {
                    style.addLine(getI18NMessage(getDefaultLabel()));
                } else {
                    style.addLine(getI18NMessage(label));
                }
            }
            return start(pageContext.getOut()).getTagResult();

        } catch (IOException e) {
            throw new JspException(e);
        }
    }

    /**
     * Method to be implemented on child classes. Returns the default label if
     * none is specified.
     *
     * @return the default label
     */
    protected abstract String getDefaultLabel();

    public void setDynamicAttribute(String uri, String localName, Object value)
            throws JspException {
        attributes.put(localName, value);
    }

    @Override
    public void release() {
        super.release();
        label = null;
        attributes = new Attributes();
    }

}
