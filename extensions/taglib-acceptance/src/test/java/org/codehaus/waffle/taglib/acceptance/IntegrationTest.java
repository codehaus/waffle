package org.codehaus.waffle.taglib.acceptance;

import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * A basic integration test class that runs all tests in browser
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 * @since 1.0
 */
public abstract class IntegrationTest extends SeleneseTestCase {

	public void setUp() throws Exception {        
		String browserString = System.getProperty("seleniumBrowserString");
        setUp("http://localhost:8080", browserString);
	}

	protected void open(String url) {
		String contextPath = System.getProperty("seleniumContextPath");
        selenium.open(contextPath + url);
		selenium.waitForPageToLoad("2000");
	}

}
