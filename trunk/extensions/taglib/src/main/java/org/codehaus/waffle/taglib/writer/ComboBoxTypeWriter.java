package org.codehaus.waffle.taglib.writer;

import org.codehaus.waffle.taglib.form.Attributes;

public class ComboBoxTypeWriter implements TypeWriter{

    public String getOpeningItem(String key, boolean selected) {
	String output = "<option value=\"" + key + "\"";
	if (selected) {
	    output += " selected";
	}
	output += ">";
	return output;
    }

    public String getEmptyTag() {
	return "";
    }

    public String getOpeningTag(Attributes attributes) {
	return "<select " + attributes.asString() + ">";
    }

    public String getClosingTag(boolean first) {
	String output = "";
	if (!first) {
	    output += "</option>";
	}
	output += "</select>";
	
	return output;
    }

    public String getClosingItem() {
	return "</option>";
    }
    
    

}
