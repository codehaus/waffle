package org.codehaus.waffle.taglib.form;


/**
 * A basic reset button.
 *
 * @author Guilherme Silveira
 */
public class ResetTag extends ButtonTag {

    public ResetTag() {
    }

    /**
     * Returns this button type.
     *
     * @return the button type
     */
    protected String getButtonType() {
        return "reset";
    }

}
