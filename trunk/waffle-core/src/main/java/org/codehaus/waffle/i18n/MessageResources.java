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

import java.util.Locale;

/**
 * MessageResources represents messages for a given locale.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public interface MessageResources {

    Locale getLocale();

    void useLocale(Locale locale);

    String getMessage(String key, Object... arguments);

    String getMessageWithDefault(String key, String defaultValue, Object... arguments);

}
