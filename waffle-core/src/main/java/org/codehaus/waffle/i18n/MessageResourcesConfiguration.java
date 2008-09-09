/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.Locale;

/**
 * Allows to retrieve the default resource and locale for message resources.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface MessageResourcesConfiguration {

    String getDefaultResource();

    Locale getDefaultLocale();
}
