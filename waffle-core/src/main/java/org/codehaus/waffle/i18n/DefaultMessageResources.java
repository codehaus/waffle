/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import static java.text.MessageFormat.format;
import static java.util.ResourceBundle.getBundle;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Default {@link ResourceBundle}-based implementation of MessageResorces.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultMessageResources implements MessageResources {
    private Locale userLocale;
    private String bundleName;

    public DefaultMessageResources() {
        this(new DefaultMessageResourcesConfiguration());
    }

    public DefaultMessageResources(MessageResourcesConfiguration configuration) {
        bundleName = configuration.getDefaultResource();
        userLocale = configuration.getDefaultLocale();
    }

    public String getResource() {
        return bundleName;
    }

    public void useResource(String resource) {
        this.bundleName = resource;
    }

    public Locale getLocale() {
        return userLocale;
    }

    public void useLocale(Locale locale) {
        userLocale = locale;
    }

    public String getMessage(String key, Object... arguments) {
        ResourceBundle resourceBundle = getBundle(bundleName, userLocale);
        return format(resourceBundle.getString(key), arguments);
    }

    public String getMessageWithDefault(String key, String defaultValue, Object... arguments) {
        try {
            return getMessage(key, arguments);
        } catch (MissingResourceException e) {
            return format(defaultValue, arguments);
        }
    }

}
