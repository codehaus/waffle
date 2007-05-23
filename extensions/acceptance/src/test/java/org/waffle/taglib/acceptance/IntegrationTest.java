package org.waffle.taglib.acceptance;

import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * A basic integration test class that runs all tests in firefox.
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 * @since 1.0
 */
public abstract class IntegrationTest extends SeleneseTestCase {

	public void setUp() throws Exception {
		setUp("http://localhost:8080", "*firefox /usr/lib/firefox/firefox-bin");
		// setUp("http://localhost:8080", "*konqueror /usr/bin/konqueror");
	}

	protected void open(String url) {
		selenium.open("/waffle-acceptance-1.0-SNAPSHOT/" + url);
		selenium.waitForPageToLoad("2000");
	}

}
