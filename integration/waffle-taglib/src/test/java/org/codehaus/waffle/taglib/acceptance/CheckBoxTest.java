package org.codehaus.waffle.taglib.acceptance;

public class CheckBoxTest extends IntegrationTest {

	public void testAllowsTheDefaultCheckedToBeCheckedOrNot() {
		open("checkbox.jsp");
		assertTrue(selenium.isChecked("checkedTrue"));
		assertFalse(selenium.isChecked("checkedFalse"));
	}

	public void testSetsTheDefaultCheckedToFalse() {
		open("checkbox.jsp");
		assertFalse(selenium.isChecked("noChecked"));
	}

}
