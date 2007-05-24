package org.waffle.taglib.acceptance;

public class CheckBoxTest extends IntegrationTest {

	public void testAllowsTheDefaultCheckedToBeCheckedOrNot() {
		open("checkbox.jsp");
		assertTrue(selenium.isChecked("checkedTrue"));
		assertFalse(selenium.isChecked("checkedFalse"));
	}

	public void testSetsTheDefaultCheckedToFalse() {
		open("check.jsp");
		assertFalse(selenium.isChecked("noChecked"));
	}

}
