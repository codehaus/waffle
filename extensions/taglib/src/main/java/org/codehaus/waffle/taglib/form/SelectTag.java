package org.codehaus.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.codehaus.waffle.taglib.writer.ComboBoxTypeWriter;
import org.codehaus.waffle.taglib.writer.RadioButtonTypeWriter;
import org.codehaus.waffle.taglib.writer.TypeWriter;
import org.codehaus.waffle.taglib.Functions;

/**
 * A combo selection or check box tag.
 *
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class SelectTag extends FormElement {

    private static final long serialVersionUID = -2367444646817282900L;

    private static final String COMBOBOX_TYPE = "combobox";

    private static final String RADIO_TYPE = "radio";

    private Collection<Object> items;

    private String name, value;

    private String var;

    private boolean addEmpty, first;

    private long selected;

    private Iterator<Object> iterator;

    private TypeWriter typeWriter;

    private void addItem(Writer out, String key, boolean selected) throws IOException {
        out.write(this.typeWriter.getOpeningItem(key, selected));
    }

    private String getKey() {
        return var == null ? "item" : var;
    }

    @Override
    public void release() {
        super.release();
        items = null;
        addEmpty = false;
        var = "item";
        name = null;
        iterator = null;
        selected = 0;
        value = null;
        first = true;
        typeWriter = new ComboBoxTypeWriter();
    }

    public void setAddEmpty(boolean addEmpty) {
        this.addEmpty = addEmpty;
    }

    public void setType(String type) {
        if (COMBOBOX_TYPE.equalsIgnoreCase(type)) {
            typeWriter = new ComboBoxTypeWriter();
        } else if (RADIO_TYPE.equalsIgnoreCase(type)) {
            typeWriter = new RadioButtonTypeWriter();
        }
    }

    public void setItems(Collection<Object> collection) {
        this.items = collection;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(long selected) {
        this.selected = selected;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    protected String getDefaultLabel() {
        return name;
    }

    @Override
    protected IterationResult start(Writer out) throws JspException, IOException {

        first = true;
        attributes.put("name", name);
        out.write(typeWriter.getOpeningTag(attributes));
        if (addEmpty) {
            addItem(out, "", false);
            first = false;
        }
        if (items == null) {
            return IterationResult.PAGE;
        }
        iterator = items.iterator();
        return iterator.hasNext() ? IterationResult.BODY : IterationResult.PAGE;
    }

    @Override
    protected IterationResult afterBody(JspWriter out) throws IOException {
        return iterator.hasNext() ? IterationResult.BODY_AGAIN : IterationResult.PAGE;
    }

    @Override
    protected void end(Writer out) throws JspException, IOException {

        out.write(this.typeWriter.getClosingTag(first));
        pageContext.removeAttribute(getKey());
        release();
        super.end(out);
    }

    @Override
    protected void beforeBody(Writer out) throws JspException, IOException {
        if (!first) {
            out.write(this.typeWriter.getClosingItem());
        }
        first = false;
        Object current = iterator.next();
        Object key = Functions.getProperty(current, value);
        boolean isSelected;
        if (!(key instanceof Number)) {
            isSelected = key.equals(selected);
        } else {
            isSelected = ((Number) key).longValue() == selected;
        }
        addItem(out, key.toString(), isSelected);
        pageContext.setAttribute(getKey(), current);
    }

}
