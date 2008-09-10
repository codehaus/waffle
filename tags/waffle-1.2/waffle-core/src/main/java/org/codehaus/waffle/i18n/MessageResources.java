/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.Locale;

/**
 * Represents message resources for a given locale.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface MessageResources {

    String getResource();

    void useResource(String resource);

    Locale getLocale();

    void useLocale(Locale locale);

    String getMessage(String key, Object... arguments);

    String getMessageWithDefault(String key, String defaultValue, Object... arguments);

}
