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
    private static final ResourceBundle EMPTY_BUNDLE = new EmptyResourceBundle();
    private String uri;
    private Locale locale;
    private ResourceBundle bundle;

    public DefaultMessageResources() {
        this(new DefaultMessageResourcesConfiguration());
    }

    public DefaultMessageResources(MessageResourcesConfiguration configuration) {
        this.uri = configuration.getDefaultURI();
        this.locale = configuration.getDefaultLocale();
        lookupBundle();
    }

    private void lookupBundle() {        
        bundle = lookupBunde(uri);
    }

    private ResourceBundle lookupBunde(String bundleName) {
        try {
            return getBundle(bundleName.trim(), locale);
        } catch (MissingResourceException e) {
            return EMPTY_BUNDLE;
        }
    }
    public String getURI() {
        return uri;
    }

    public void useURI(String uri) {
        this.uri = uri;
        lookupBundle();
    }

    public Locale getLocale() {
        return locale;
    }

    public void useLocale(Locale locale) {
        this.locale = locale;
        lookupBundle();
    }

    public String getMessage(String key, Object... arguments) {        
        return format(bundle.getString(key), arguments);
    }

    public String getMessageWithDefault(String key, String defaultValue, Object... arguments) {
        try {
            return getMessage(key, arguments);
        } catch (MissingResourceException e) {
            return format(defaultValue, arguments);
        }
    }

}
