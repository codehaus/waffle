package org.codehaus.waffle.taglib.form;

/**
 * A password element for html files.
 * 
 * @author Guilherme Silveira
 */
public class PasswordTag extends TextTag {

	protected String getType() {
		return "password";
	}
}
