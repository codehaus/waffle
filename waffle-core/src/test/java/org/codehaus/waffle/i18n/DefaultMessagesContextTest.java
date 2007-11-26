package org.codehaus.waffle.i18n;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author Mauro Talevi
 */
public class DefaultMessagesContextTest  {

    @Test
    public void canAddAndGetMessages() {
        MessagesContext messages = new DefaultMessagesContext();
        messages.addMessage("success", "Waffle action was executed");
        assertEquals(1, messages.getMessagesCount());
        assertEquals("Waffle action was executed", messages.getMessage("success"));
        assertEquals("Waffle action was executed", messages.getMessages().iterator().next());                
    }

    @Test
    public void canAddAndClearMessages() {
        MessagesContext messages = new DefaultMessagesContext();
        messages.addMessage("success", "Waffle action was executed");
        assertEquals(1, messages.getMessagesCount());
        messages.clearMessages();
        assertEquals(0, messages.getMessagesCount());       
        // add again
        messages.addMessage("success", "Waffle action was executed");
        assertEquals(1, messages.getMessagesCount());
    }

}
