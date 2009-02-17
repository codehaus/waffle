package org.codehaus.waffle.taglib.acceptance;

public class PasswordTagTest extends IntegrationTest {

	public void testChecksTheRenderedAttributeSetToFalse() {
	    try {
		open("password.jsp");
		getSelenium().getElementIndex("renderedFalse");
		fail();
	    } catch( Exception e ) {
		assertTrue(true);
	    }
	}

	public void testChecksTheDefaultAttribute() {
		open("password.jsp");
		assertEquals("54321",getSelenium().getValue("onlyName"));
	}

	public void testChecksTheValueAttribute() {
		open("password.jsp");
		assertEquals("12345",getSelenium().getValue("nameAndValue"));
	}
	
	public void testChecksTheRenderedAttributeSetToTrue() {
		open("password.jsp");
		assertEquals("54321",getSelenium().getValue("renderedTrue"));
	}
}
