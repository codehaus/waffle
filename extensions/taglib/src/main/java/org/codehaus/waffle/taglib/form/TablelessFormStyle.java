package org.codehaus.waffle.taglib.form;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * A form style implementation using div and spans.
 *
 * @author Guilherme Silveira
 */
public class TablelessFormStyle implements FormStyle {
    private final JspWriter out;
    private final FormTag form;
    private final ServletRequest request;

    public TablelessFormStyle(FormTag form, ServletRequest request,
                              JspWriter out) {
        this.request = request;
        this.out = out;
        this.form = form;
    }

    public void addLine(String label) throws IOException {
        out.print("<p>\n<span>");
        out.print(label);
        out.print("</span>\n<span>");
    }

    public void beginForm() throws IOException {
        out.print("<div>\n");
    }

    public void finishForm() throws IOException {
        out.print("</div>\n");
    }

    public void finishLine() throws IOException {
        out.print("</span>\n</p>\n");
    }

    public void addErrors(String label) throws IOException {
    }

}
