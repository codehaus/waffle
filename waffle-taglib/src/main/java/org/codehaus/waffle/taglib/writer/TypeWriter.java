package org.codehaus.waffle.taglib.writer;

import org.codehaus.waffle.taglib.form.Attributes;


/**
 * Interface which hide the implementation of the presentation of the final html element.
 * E.g. Used for collection which can be presented as combobox or checkboxes.
 *
 * @author Nico Stepapt
 */
public interface TypeWriter {

    String getOpeningItem(String key, boolean selected);

    String getEmptyTag();

    String getOpeningTag(Attributes attributes);

    String getClosingTag(boolean first);

    String getClosingItem();

}
