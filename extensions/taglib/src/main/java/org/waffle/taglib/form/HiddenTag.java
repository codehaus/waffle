package org.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.waffle.taglib.internal.BasicTag;

/**
 * A basic hidden tag.
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public class HiddenTag extends BasicTag implements DynamicAttributes {

    private static final long serialVersionUID = -2367444646817282900L;

    private String name, value;

    private Attributes attributes;

    public void setName(String name) {
	this.name = name;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public int doEndTag() throws JspException {
	return EVAL_PAGE;
    }

    public IterationResult start(Writer out) throws JspException, IOException {
	try {
	    out.write("<input type=\"hidden\" name=\"");
	    
	    out.write(name);

	    
	    out.write("\" value=\"");
	    if (value != null) {
		out.write(value);
	    } else {
		out.write(evaluateEl(name));
	    }
	    
	    out.write("\"");
	    
	    attributes.outputTo(out);
	    
	    out.write("/>");
	} finally {
	    release();
	}
	return IterationResult.BODY;
    }
    
    @Override
    public void release() {
    	super.release();
    	name = null;
    	value = null;
    	this.attributes = new Attributes();
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
	attributes.put(localName, value);	
    }
    

}
