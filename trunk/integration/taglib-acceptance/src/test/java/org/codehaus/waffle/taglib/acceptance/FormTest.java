package org.codehaus.waffle.taglib.acceptance;

public class FormTest extends IntegrationTest {

	public void testHonorsRenderAttribute() {
		open("form.jsp");
		assertFalse(selenium.isElementPresent("notRendered"));
		assertTrue(selenium.isElementPresent("rendered"));
	}

}
