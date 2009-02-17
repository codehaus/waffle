package org.codehaus.waffle.taglib.acceptance;

public class TextareaTagTest extends IntegrationTest {

    public void testChecksTheTextareaCreation() {
        open("textarea.jsp");
        assertTrue(getSelenium().isElementPresent("itsName"));
    }

    public void testContainsItsBodyAsValue() {
        open("textarea.jsp");
        assertEquals("itsValue", field("itsName").value());
    }

}