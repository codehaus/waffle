/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Default {@link ResourceBundle}-based implementation of MessageResorces. Supports a URI defining multiple resource
 * bundle names as CSV-list. The decoding of the bundle names from the URI (including the separator string) is
 * overrideable by subclassing the method {@see DefaultMessageResources#bundleNames(String)}.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultMessageResources implements MessageResources {
    private static final ResourceBundle EMPTY_BUNDLE = new EmptyResourceBundle();
    private final ResourceBundleMerger merger = new ResourceBundleMerger();
    private String uri;
    private Locale locale;
    private ResourceBundle bundle;

    public DefaultMessageResources() {
        this(new DefaultMessageResourcesConfiguration());
    }

    public DefaultMessageResources(MessageResourcesConfiguration configuration) {
        this.uri = configuration.getURI();
        this.locale = configuration.getLocale();
        lookupBundle();
    }

    private void lookupBundle() {
        List<String> bundleNames = bundleNames(uri);
        List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
        for (String bundleName : bundleNames) {
            bundles.add(lookupBunde(bundleName));
        }
        bundle = merger.merge(bundles);
    }

    private ResourceBundle lookupBunde(String bundleName) {
        try {
            return getBundle(bundleName.trim(), locale);
        } catch (MissingResourceException e) {
            return EMPTY_BUNDLE;
        }
    }

    protected List<String> bundleNames(String uri) {
        return asList(uri.split(","));
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
