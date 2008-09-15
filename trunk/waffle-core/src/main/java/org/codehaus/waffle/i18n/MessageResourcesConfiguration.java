/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.Locale;

/**
 * Allows to configure the URI and locale for message resources.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface MessageResourcesConfiguration {

    /**
     * @deprecated Use getURI()
     */
    String getDefaultResource();

    String getURI();

    /**
     * @deprecated Use getLocale()
     */
    Locale geDefaultLocale();

    Locale getLocale();
}
