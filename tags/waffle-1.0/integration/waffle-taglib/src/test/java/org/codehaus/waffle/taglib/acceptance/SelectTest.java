package org.codehaus.waffle.taglib.acceptance;

public class SelectTest extends IntegrationTest {

	public void testAllowsTheUserToSpecifyTheSelectedValue() {
		open("select.jsp");
		assertEquals("SelectTest", getSelenium().getTitle());
		assertEquals(2, getSelenium().getSelectedId("produto.id.selected.2"));
	}

	public void testHonorsRenderAttribute() {
		open("select.jsp");
		assertTrue(!getSelenium().isElementPresent("notRendered"));
		assertTrue(getSelenium().isElementPresent("rendered"));
	}

    public void testAcceptsNullAsSelectedValue() {
        open("select.jsp");
        assertEquals("", getSelenium().getSelectedId("produto.id.selected.null"));
    }

    public void testAcceptsNullAsContent() {
        open("select.jsp");
        String[] options = getSelenium().getSelectOptions("nulledList");
        assertTrue(options.length <= 1);
        if (options.length != 0) {
            assertEquals("", options[0]);
        }
    }

}
