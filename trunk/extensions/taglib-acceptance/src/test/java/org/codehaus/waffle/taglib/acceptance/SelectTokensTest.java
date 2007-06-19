package org.codehaus.waffle.taglib.acceptance;

public class SelectTokensTest extends IntegrationTest {

	public void testAllowsTheUserToSpecifyTheSelectedValue() {
		open("selectTokens.jsp");
        assertEquals("second", selenium.getSelectedValue("selected.2"));
	}

	public void testHonorsRenderAttribute() {
		open("selectTokens.jsp");
		verifyTrue(!selenium.isElementPresent("notRendered"));
		verifyTrue(selenium.isElementPresent("rendered"));
		checkForVerificationErrors();
	}

}