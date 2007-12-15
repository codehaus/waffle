package org.codehaus.waffle.i18n;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.codehaus.waffle.Constants;
import org.jmock.Expectations;
import org.jmock.Mockery;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Mauro Talevi
 */
public class DefaultMessagesContextTest  {

    @Test
    public void canAddAndGetMessages() {
        MessagesContext messages = new DefaultMessagesContext(null);
        messages.addMessage("success", "Waffle action was executed");
        assertEquals(1, messages.getMessageCount());
        assertEquals("Waffle action was executed", messages.getMessage("success"));
        assertEquals("Waffle action was executed", messages.getMessages().iterator().next());                
    }

    @Test
    public void canAddAndClearMessages() {
        MessagesContext messages = new DefaultMessagesContext(null);
        messages.addMessage("success", "Waffle action was executed");
        assertEquals(1, messages.getMessageCount());
        messages.clearMessages();
        assertEquals(0, messages.getMessageCount());       
        // add again
        messages.addMessage("success", "Waffle action was executed");
        assertEquals(1, messages.getMessageCount());
    }

    @Test
    public void shouldSelfRegisterOnStart() {
        Mockery mockery = new Mockery();
        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        final DefaultMessagesContext context = new DefaultMessagesContext(request);

        mockery.checking(new Expectations() {
            {
                one(request).setAttribute(Constants.MESSAGES_KEY, context);
            }
        });

        context.start();
    }

}
