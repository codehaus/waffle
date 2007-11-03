package org.codehaus.waffle.taglib.acceptance;

import com.thoughtworks.selenium.SeleneseTestCase;

/**
 * A basic integration test class that runs all tests in browser
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
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

    protected Field field(String localizer) {
        return new Field(localizer);
    }
    
    protected class Field {
        private final String localizer;

        public Field(String localizer) {
            this.localizer = localizer;
        }

        public String value() {
            return selenium.getValue(localizer);
        }
    }
    
}
