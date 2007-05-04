package com.thoughtworks.waffle.testmodel;

import com.thoughtworks.waffle.i18n.MessageResources;

import java.util.Locale;

public class StubMessageResources implements MessageResources {
    public Locale getLocale() {
        return null;
    }

    public void setLocale(Locale locale) {

    }

    public String getMessage(String key, Object ... arguments) {
        return null;
    }

    public String getMessageWithDefault(String key, String defaultValue, Object ... arguments) {
        return null;
    }
}
