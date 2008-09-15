/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.Locale;

/**
 * Default MessageResourcesConfiguration which simply allows the injection of default locale and resource.
 * 
 * @author Mauro Talevi
 */
public class DefaultMessageResourcesConfiguration implements MessageResourcesConfiguration {

    private final String defaultURI;
    private final Locale defaultLocale;

    public DefaultMessageResourcesConfiguration() {
        this("ApplicationResources", Locale.getDefault());
    }

    public DefaultMessageResourcesConfiguration(String defaultURI, Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
        this.defaultURI = defaultURI;
    }

    /**
     * @deprecated Use DefaultMessageResourcesConfiguration(String defaultURI, Locale defaultLocale)
     */
    public DefaultMessageResourcesConfiguration(Locale defaultLocale, String defaultURI) {
        this(defaultURI, defaultLocale);
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public String getDefaultURI() {
        return defaultURI;
    }

}
