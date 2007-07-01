package org.codehaus.waffle.taglib.form;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletRequest;

/**
 * A form style based on table rows.
 * 
 * @author Guilherme Silveira
 */
public class TableFormStyle implements FormStyle {

	private final Writer out;

	private final FormTag form;

	private final ServletRequest request;

	private boolean even;

	public TableFormStyle(FormTag form, ServletRequest request, Writer out) {
		this.out = out;
		this.form = form;
		this.request = request;
	}

	public void addLine(String label) throws IOException {
		even = !even;
		out.write("<tr class=\"");
		String clazz = form.getAttribute("class");
		if (clazz != null) {
			out.write(clazz);
		}
		if ("true".equals(form.getAttribute("showEvenOrOdd"))) {
			if (even) {
				out.write("Even");
			} else {
				out.write("Odd");
			}
		}
		out.write("\">\n<td>");
		out.write(label);
		out.write("</td>\n<td>");
	}

	public void beginForm() throws IOException {
		String clazz = form.getAttribute("class");
		if (clazz != null) {
			out.write("<table class=\"" + clazz + "\">\n");
		} else {
			out.write("<table>\n");
		}
		even = true;
	}

	public void finishForm() throws IOException {
		out.write("</table>\n");
	}

	public void finishLine() throws IOException {
		out.write("</td>\n</tr>");
	}

	public void addErrors(String label) throws IOException {
	}

}
