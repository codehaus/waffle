package org.waffle.taglib.acceptance;

public class SelectTest extends IntegrationTest {

	public void testAllowsTheUserToSpecifyTheSelectedValue() {
		open("select.jsp");
		assertEquals("SelectTest", selenium.getTitle());
		assertEquals(2, selenium.getSelectedId("produto.id.selected.2"));
	}

	public void testHonorsRenderAttribute() {
		open("select.jsp");
		verifyTrue(!selenium.isElementPresent("notRendered"));
		verifyTrue(selenium.isElementPresent("rendered"));
		checkForVerificationErrors();
	}

}
