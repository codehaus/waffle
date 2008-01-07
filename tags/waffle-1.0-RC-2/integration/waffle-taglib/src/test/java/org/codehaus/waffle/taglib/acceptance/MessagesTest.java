package org.codehaus.waffle.taglib.acceptance;

/**
 * @author Fabio Kung
 */
public class MessagesTest extends IntegrationTest {

    public void testEmptyMessagesContextGivesNoMessage() {
        open("products.waffle");
        int messagesCount = getSelenium().getXpathCount("//ul[@id='messages']/li").intValue();
        assertEquals(0, messagesCount);
        assertTrue(getSelenium().getText("successMessage").trim().length() == 0);
        assertTrue(getSelenium().getText("failureMessage").trim().length() == 0);
    }

    public void testShowMessagesActionGivesTwoMessages() {
        open("products.waffle");
        getSelenium().click("showMessagesAction");
        getSelenium().waitForPageToLoad("2000");
        int messagesCount = getSelenium().getXpathCount("//ul[@id='messages']/li").intValue();
        assertEquals(2, messagesCount);
        assertFalse(getSelenium().getText("successMessage").trim().length() == 0);
        assertTrue(getSelenium().getText("failureMessage").trim().length() == 0);
    }
}
