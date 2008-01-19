package org.codehaus.waffle.taglib.acceptance;

public class NumberTest extends IntegrationTest {

	public void testUsesTheCurrentLocaleIfNoPatternIsSpecified() {
		open("number.jsp?locale=pt_BR");
        assertEquals("1.234,5", field("localizedNumber").value());
	}

    public void testUsesTheSpecifiedPattern() {
        open("number.jsp?locale=pt_BR");
        assertEquals("01.234,50", field("patternNumber").value());
    }

}