/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.i18n;

import java.util.List;


/**
 * MessagesContext holds user business-level messages. Controllers can depend on it and use it to pass messages to the
 * view. It is not meant to display validation error messages, which are designed to be handled via the
 * {@link org.codehaus.waffle.validation.ErrorsContext}.
 * 
 * @author Mauro Talevi
 * @see org.codehaus.waffle.validation.ErrorsContext
 */
public interface MessagesContext {

    void addMessage(String key, String message);

    String getMessage(String key);

    List<String> getMessages();

    int getMessageCount();

    void clearMessages();
    
}

