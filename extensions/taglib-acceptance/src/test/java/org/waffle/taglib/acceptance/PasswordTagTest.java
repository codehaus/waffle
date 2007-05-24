package org.waffle.taglib.acceptance;

public class PasswordTagTest extends IntegrationTest {

	public void testChecksTheRenderedAttributeSetToFalse() {
	    try {
		open("password.jsp");
		selenium.getElementIndex("renderedFalse");
		fail();
	    } catch( Exception e ) {
		assertTrue(true);
	    }
	}

	public void testChecksTheDefaultAttribute() {
		open("password.jsp");
		assertEquals("54321",selenium.getValue("onlyName"));
	}

	public void testChecksTheValueAttribute() {
		open("password.jsp");
		assertEquals("12345",selenium.getValue("nameAndValue"));
	}
	
	public void testChecksTheRenderedAttributeSetToTrue() {
		open("password.jsp");
		assertEquals("54321",selenium.getValue("renderedTrue"));
	}
}
