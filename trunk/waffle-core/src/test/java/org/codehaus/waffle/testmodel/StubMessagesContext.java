package org.codehaus.waffle.testmodel;

import java.util.List;

import org.codehaus.waffle.i18n.MessagesContext;

public class StubMessagesContext implements MessagesContext {

    public void addMessage(String key, String message) {
    }

    public void clearMessages() {
    }

    public String getMessage(String key) {
        return null;
    }

    public List<String> getMessages() {
        return null;
    }

    public int getMessagesCount() {
        return 0;
    }

}
