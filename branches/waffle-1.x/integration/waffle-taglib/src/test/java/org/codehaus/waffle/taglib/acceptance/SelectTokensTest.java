package org.codehaus.waffle.taglib.acceptance;

public class SelectTokensTest extends IntegrationTest {

	public void testAllowsTheUserToSpecifyTheSelectedValue() {
		open("selectTokens.jsp");
        assertEquals("second", getSelenium().getSelectedValue("selected.2"));
	}

	public void testHonorsRenderAttribute() {
		open("selectTokens.jsp");
		assertTrue(!getSelenium().isElementPresent("notRendered"));
		assertTrue(getSelenium().isElementPresent("rendered"));
	}

}