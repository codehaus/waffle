package org.codehaus.waffle.resources.ftl;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * @author Mauro Talevi
 */
public class FormController {

    private List<String> values = asList("one", "two");
    private List<Nameable> nameables = asList(new Nameable("one", "One"), new Nameable("two", "Two"));
    private List<Displayable> displayables = asList(new Displayable("one", "One"), new Displayable("two", "Two"));
    private List<String> selectedValues;

    public List<String> getValues() {
        return values;
    }

    public List<String> getSelectedValues() {
        return selectedValues;
    }

    public void setSelectedValues(List<String> selectedValues) {
        this.selectedValues = selectedValues;
    }

    public List<Nameable> getNameables() {
        return nameables;
    }

    public List<Displayable> getDisplayables() {
        return displayables;
    }

    public static class Nameable {
        private final String value;
        private final String name;

        public Nameable(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

    }

    public static class Displayable {
        private final String id;
        private final String display;

        public Displayable(String id, String display) {
            this.id = id;
            this.display = display;
        }

        public String getId() {
            return id;
        }

        public String getDisplay() {
            return display;
        }

    }

}
