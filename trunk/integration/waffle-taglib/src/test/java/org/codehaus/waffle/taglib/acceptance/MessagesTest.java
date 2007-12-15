package org.codehaus.waffle.taglib.acceptance;

/**
 * @author Fabio Kung
 */
public class MessagesTest extends IntegrationTest {

    public void testEmptyMessagesContextGivesNoMessage() {
        open("products.waffle");
        int messagesCount = selenium.getXpathCount("//ul[@id='messages']/li").intValue();
        assertEquals(0, messagesCount);
        assertTrue(selenium.getText("successMessage").trim().length() == 0);
        assertTrue(selenium.getText("failureMessage").trim().length() == 0);
    }

    public void testShowMessagesActionGivesTwoMessages() {
        open("products.waffle");
        selenium.click("showMessagesAction");
        selenium.waitForPageToLoad("2000");
        int messagesCount = selenium.getXpathCount("//ul[@id='messages']/li").intValue();
        assertEquals(2, messagesCount);
        assertFalse(selenium.getText("successMessage").trim().length() == 0);
        assertTrue(selenium.getText("failureMessage").trim().length() == 0);
    }
}
