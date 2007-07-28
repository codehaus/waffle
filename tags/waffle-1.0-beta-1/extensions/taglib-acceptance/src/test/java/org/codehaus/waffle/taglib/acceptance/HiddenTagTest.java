package org.codehaus.waffle.taglib.acceptance;

public class HiddenTagTest extends IntegrationTest {

	public void testChecksTheRenderedAttributeSetToFalse() {
	    try {
		open("hidden.jsp");
		selenium.getElementIndex("renderedFalse");
		fail();
	    } catch( Exception e ) {
		assertTrue(true);
	    }
	}

	public void testChecksTheDefaultAttribute() {
		open("hidden.jsp");
		assertEquals("54321", field("onlyName").value());
	}

	public void testChecksTheValueAttribute() {
		open("hidden.jsp");
		assertEquals("12345", field("nameAndValue").value());
	}
	
	public void testChecksTheRenderedAttributeSetToTrue() {
		open("hidden.jsp");
		assertEquals("54321", field("renderedTrue").value());
	}
}
