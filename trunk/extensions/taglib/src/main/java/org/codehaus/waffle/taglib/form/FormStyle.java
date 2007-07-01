package org.codehaus.waffle.taglib.form;

import java.io.IOException;

/**
 * Form styles.
 * 
 * @author Guilherme Silveira
 */
public interface FormStyle {

	void addLine(String label) throws IOException;

	void beginForm() throws IOException;

	void finishForm() throws IOException;

	void finishLine() throws IOException;

	void addErrors(String label) throws IOException;

}
