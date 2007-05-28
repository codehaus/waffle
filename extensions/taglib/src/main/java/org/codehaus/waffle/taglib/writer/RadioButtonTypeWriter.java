package org.codehaus.waffle.taglib.writer;

import org.codehaus.waffle.taglib.form.Attributes;

public class RadioButtonTypeWriter implements TypeWriter{

    private Attributes attributes;

    public String getClosingItem() {
	return "<br/>";
    }

    public String getClosingTag(boolean first) {
	return "";
    }

    public String getEmptyTag() {
	return "";
    }

    public String getOpeningItem(String key, boolean selected) {
	StringBuilder builder = new StringBuilder();
	
	builder.append("<input type=\"radio\" value=\"");
	builder.append(key);
	builder.append("\" ");
	
	builder.append(attributes.asString());
	
	if (selected) {
	    builder.append(" checked=\"checked\"");
	}
	
	builder.append("/>");
	return builder.toString();	
    }

    public String getOpeningTag(Attributes attributes) {
	this.attributes = attributes;
	return "";
    }


}
