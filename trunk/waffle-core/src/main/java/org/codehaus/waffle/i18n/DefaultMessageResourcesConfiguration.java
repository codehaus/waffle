package org.codehaus.waffle.i18n;

import java.util.Locale;

/**
 * Default MessageResourcesConfiguration
 * 
 * @author Mauro Talevi
 */
public class DefaultMessageResourcesConfiguration implements MessageResourcesConfiguration {

    private final Locale defaultLocale;
    private final String bundleName;

    public DefaultMessageResourcesConfiguration() {
        this(Locale.getDefault(), "ApplicationResources");
    }

    public DefaultMessageResourcesConfiguration(Locale defaultLocale, String bundleName) {
        this.defaultLocale = defaultLocale;
        this.bundleName = bundleName;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    public String getResourceBundleName() {
        return bundleName;
    }

}
