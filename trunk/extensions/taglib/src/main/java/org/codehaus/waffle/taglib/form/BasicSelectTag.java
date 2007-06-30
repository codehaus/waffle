package org.codehaus.waffle.taglib.form;

import org.codehaus.waffle.taglib.writer.TypeWriter;
import org.codehaus.waffle.taglib.writer.ComboBoxTypeWriter;
import org.codehaus.waffle.taglib.writer.RadioButtonTypeWriter;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import java.util.Map;
import java.io.Writer;
import java.io.IOException;

/**
 * A basic select tag.
 *
 * @author Guilherme Silveira
 */
abstract class BasicSelectTag extends FormElement {

    private static final String COMBOBOX_TYPE = "combobox";

    private static final String RADIO_TYPE = "radio";

    private String var, name;

    private Object selected;

    private ItemsIterator iterator;

    private TypeWriter typeWriter;

    private boolean addEmpty;

    private boolean first;

    private void addItem(Writer out, String key, boolean selected) throws IOException {
        out.write(this.typeWriter.getOpeningItem(key, selected));
    }

    private String getIterationVarKey() {
        return var == null ? "item" : var;
    }

    @Override
    public void release() {
        super.release();
        var = "item";
        iterator = null;
        selected = 0;
        typeWriter = new ComboBoxTypeWriter();
        first = true;
        addEmpty = false;
        name = null;
    }

    public void setType(String type) {
        if (COMBOBOX_TYPE.equalsIgnoreCase(type)) {
            typeWriter = new ComboBoxTypeWriter();
        } else if (RADIO_TYPE.equalsIgnoreCase(type)) {
            typeWriter = new RadioButtonTypeWriter();
        }
    }

    public void setSelected(Object selected) {
        this.selected = selected;
    }

    public void setVar(String var) {
        this.var = var;
    }

    @Override
    protected IterationResult afterBody(JspWriter out) throws IOException {
        return iterator.hasNext() ? IterationResult.BODY_AGAIN : IterationResult.PAGE;
    }

    @Override
    protected void end(Writer out) throws JspException, IOException {
        out.write(this.typeWriter.getClosingTag(first));
        pageContext.removeAttribute(getIterationVarKey());
        release();
        super.end(out);
    }

    @Override
    protected void beforeBody(Writer out) throws JspException, IOException {
        if (!first) {
            out.write(this.typeWriter.getClosingItem());
        }
        first = false;
        Map.Entry entry = iterator.next();
        Object itemKey = entry.getKey();
        Object value = entry.getValue();
        boolean isSelected;
        if (!(itemKey instanceof Number)) {
            isSelected = itemKey.equals(selected);
        } else {
            isSelected = ((Number) itemKey).longValue() == ((Number)selected).longValue();
        }
        addItem(out, itemKey.toString(), isSelected);
        pageContext.setAttribute(getIterationVarKey(), value);
    }


    public void setAddEmpty(boolean addEmpty) {
        this.addEmpty = addEmpty;
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
        iterator = getItemsIterator();
        if (iterator == null) {
            return IterationResult.PAGE;
        }
        return iterator.hasNext() ? IterationResult.BODY : IterationResult.PAGE;
    }

    /**
     * Returns an iterator of items to populate this select tag.
     * @return  the items. null or empty iterator if there are no items.
     */
    protected abstract ItemsIterator getItemsIterator() ;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected String getDefaultLabel() {
        return name;
    }

    /**
     * An item iterator.
     */
    // gs: we do not use Iterator<Map.Entry> as you can not cache SelectTag values and we do not want to write the remove method
    interface ItemsIterator {
        boolean hasNext();
        Map.Entry next();
    }
}
