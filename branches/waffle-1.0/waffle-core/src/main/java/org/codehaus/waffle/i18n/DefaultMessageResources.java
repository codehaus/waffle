/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author Michael Ward
 */
public class DefaultMessageResources implements MessageResources {
    private final static ThreadLocal<Locale> userLocale = new ThreadLocal<Locale>();
    public final String bundleName;

    public DefaultMessageResources() {
        bundleName = "ApplicationResources";
        userLocale.set(Locale.getDefault());
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
        ResourceBundle resourceBundle = ResourceBundle
                .getBundle(bundleName, userLocale.get());
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
