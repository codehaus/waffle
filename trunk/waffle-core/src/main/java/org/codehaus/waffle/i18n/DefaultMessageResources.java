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
 * Default ResourceBundle-based implementation of MessageResorces.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultMessageResources implements MessageResources {
    private final static ThreadLocal<Locale> userLocale = new ThreadLocal<Locale>();
    public final String bundleName;

    public DefaultMessageResources() {
        this(new DefaultMessageResourcesConfiguration());
    }

    public DefaultMessageResources(MessageResourcesConfiguration configuration) {
        bundleName = configuration.getResourceBundleName();
        userLocale.set(configuration.getDefaultLocale());
    }

    public Locale getLocale() {
        return userLocale.get();
    }

    public void useLocale(Locale locale) {
        userLocale.set(locale);
    }

    public String getMessage(String key, Object ... arguments) {
        ResourceBundle resourceBundle = getBundle(bundleName, userLocale.get());
        return format(resourceBundle.getString(key), arguments);
    }

    public String getMessageWithDefault(String key, String defaultValue, Object ... arguments) {
        try {
            return getMessage(key, arguments);
        } catch (MissingResourceException e) {
            return format(defaultValue, arguments);
        }
    }

}
