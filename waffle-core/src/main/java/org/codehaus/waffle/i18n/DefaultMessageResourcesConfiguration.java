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

    private final Locale defaultLocale;
    private final String defaultResource;

    public DefaultMessageResourcesConfiguration() {
        this(Locale.getDefault(), "ApplicationResources");
    }

    public DefaultMessageResourcesConfiguration(Locale defaultLocale, String defaultResource) {
        this.defaultLocale = defaultLocale;
        this.defaultResource = defaultResource;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public String getDefaultResource() {
        return defaultResource;
    }

}
