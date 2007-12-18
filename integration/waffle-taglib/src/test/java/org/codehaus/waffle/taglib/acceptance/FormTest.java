package org.codehaus.waffle.taglib.acceptance;

public class FormTest extends IntegrationTest {

	public void testHonorsRenderAttribute() {
		open("form.jsp");
		assertFalse(getSelenium().isElementPresent("notRendered"));
		assertTrue(getSelenium().isElementPresent("rendered"));
	}

}
