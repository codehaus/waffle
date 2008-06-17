/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.Locale;

public interface MessageResourcesConfiguration {

    String getResourceBundleName();

    Locale getDefaultLocale();
}
