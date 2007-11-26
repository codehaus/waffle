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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default Map-based implementation of MessagesContext.
 * 
 * @author Mauro Talevi
 */
public class DefaultMessagesContext implements MessagesContext {
    private final Map<String, String> messages = new HashMap<String, String>(); 
    
    public void addMessage(String key, String message) {
        messages.put(key, message);
    }

    public String getMessage(String key) {
        return messages.get(key);
    }

    public List<String> getMessages() {
        return new ArrayList<String>(messages.values());
    }

    public int getMessagesCount() {
        return messages.size();
    }

    public void clearMessages() {
        messages.clear();
    }

}
