package org.codehaus.waffle.taglib.form;

/**
 * A basic form style which does nothing.
 *
 * @author Guilherme Silveira
 */
public class NoFormStyle implements FormStyle {

    public void addLine(String label) {
    }

    public void beginForm() {
    }

    public void finishForm() {
    }

    public void finishLine() {
    }

    public void addErrors(String label) {
    }

}
