/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.i18n;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.Startable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default Map-based implementation of MessagesContext.
 * 
 * @author Mauro Talevi
 */
public class DefaultMessagesContext implements MessagesContext, Startable {
    private final HttpServletRequest request;
    private final MessageResources resources;
    private final Map<String, String> messages = new HashMap<String, String>();

    public DefaultMessagesContext(HttpServletRequest request) {
        this(request, new DefaultMessageResources());
    }

    public DefaultMessagesContext(HttpServletRequest request, MessageResources resources) {
        this.request = request;
        this.resources = resources;
    }

    public void addMessage(String key, String message) {
        messages.put(key, message);
    }

    public String getMessage(String key) {
        return messages.get(key);
    }

    public List<String> getMessages() {
        return new ArrayList<String>(messages.values());
    }

    public int getMessageCount() {
        return messages.size();
    }

    public void clearMessages() {
        messages.clear();
    }

    public MessageResources getResources() {
        return resources;
    }

    public void start() {
        request.setAttribute(Constants.MESSAGES_KEY, this);
    }

    public void stop() {
        // do nothing
    }

}
