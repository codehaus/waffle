/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import java.util.List;


/**
 * MessagesContext holds user business-level messages. Controllers can depend on it and use it to pass messages to the
 * view. It is not meant to display validation error messages, which are designed to be handled via the
 * {@link org.codehaus.waffle.validation.ErrorsContext}.  The MessagesContext also exposes to the view the MessageResources.
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
    
    MessageResources getResources();
    
}

