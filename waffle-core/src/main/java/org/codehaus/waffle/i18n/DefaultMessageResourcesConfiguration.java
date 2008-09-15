/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.Locale;

/**
 * Default MessageResourcesConfiguration which allows the injection of locale and resource.
 * 
 * @author Mauro Talevi
 */
public class DefaultMessageResourcesConfiguration implements MessageResourcesConfiguration {

    private static final String DEFAULT_URI = "ApplicationResources";
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private final String uri;
    private final Locale locale;

    public DefaultMessageResourcesConfiguration() {
        this(DEFAULT_URI, DEFAULT_LOCALE);
    }
    
    public DefaultMessageResourcesConfiguration(String defaultURI) {
        this(defaultURI, DEFAULT_LOCALE);
    }

    public DefaultMessageResourcesConfiguration(String uri, Locale locale) {
        this.uri = uri;
        this.locale = locale;
    }

    /**
     * @deprecated Use DefaultMessageResourcesConfiguration(String uri, Locale locale)
     */
    public DefaultMessageResourcesConfiguration(Locale locale, String uri) {
        this(uri, locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public String getURI() {
        return uri;
    }

}
