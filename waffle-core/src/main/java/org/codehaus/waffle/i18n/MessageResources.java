/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.Locale;

/**
 * MessageResources represents messages for a given locale.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface MessageResources {

    Locale getLocale();

    void useLocale(Locale locale);

    String getMessage(String key, Object... arguments);

    String getMessageWithDefault(String key, String defaultValue, Object... arguments);

}
