/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package com.thoughtworks.waffle.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DefaultMessageResources implements MessageResources {
    private final static ThreadLocal<Locale> userLocale = new ThreadLocal<Locale>();
    public final String baseName;

    public DefaultMessageResources() {
        baseName = "ApplicationResources";
        userLocale.set(Locale.getDefault());
    }

    public DefaultMessageResources(DefaultMessageResourcesConfiguration configuration) {
        baseName = configuration.getResourceBundleBaseName();
        userLocale.set(configuration.getDefaultLocale());
    }

    public Locale getLocale() {
        return userLocale.get();
    }

    public void setLocale(Locale locale) {
        userLocale.set(locale);
    }

    public String getMessage(String key, Object ... arguments) {
        ResourceBundle resourceBundle = ResourceBundle
                .getBundle(baseName, userLocale.get());
        String message = resourceBundle.getString(key);
        return MessageFormat.format(message, arguments);
    }

    public String getMessageWithDefault(String key, String defaultValue, Object ... arguments) {
        try {
            return this.getMessage(key, arguments);
        } catch (MissingResourceException e) {
            return defaultValue;
        }
    }

}
