package org.codehaus.waffle.taglib.acceptance;

public class CheckBoxTest extends IntegrationTest {

	public void testAllowsTheDefaultCheckedToBeCheckedOrNot() {
		open("checkbox.jsp");
		assertTrue(getSelenium().isChecked("checkedTrue"));
		assertFalse(getSelenium().isChecked("checkedFalse"));
	}

	public void testSetsTheDefaultCheckedToFalse() {
		open("checkbox.jsp");
		assertFalse(getSelenium().isChecked("noChecked"));
	}

}
