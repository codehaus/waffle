package org.codehaus.waffle.example.freemarker.controller;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DateProvider implements Serializable {

    private String dayPattern;
    private String timePattern;
    private String defaultPattern;

    public DateProvider(String dayPattern, String timePattern, String defaultPattern) {
        this.dayPattern = dayPattern;
        this.timePattern = timePattern;
        this.defaultPattern = defaultPattern;
    }

    public String getDayPattern() {
        return dayPattern;
    }

    public String getTimePattern() {
        return timePattern;
    }

    public String getDefaultPattern() {
        return defaultPattern;
    }

}
