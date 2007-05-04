package com.thoughtworks.waffle.migration.model;

public class Month {
    private String language;
    private String displayName;
    private String year;

    public Month(String language, String displayName) {
        this.language = language;
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLanguage() {
        return language;
    }

    public String getYear() {
        return year;
    }

    public void changeYear(String year) {
        this.year = year;
    }
}
