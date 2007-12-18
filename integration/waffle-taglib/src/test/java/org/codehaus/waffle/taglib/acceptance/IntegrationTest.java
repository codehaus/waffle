package org.codehaus.waffle.taglib.acceptance;

import com.thoughtworks.selenium.SeleneseTestCase;
import com.thoughtworks.selenium.Selenium;
import junit.framework.TestCase;

/**
 * A basic integration test class that runs all tests in browser
 * 
 * @author Guilherme Silveira
 * @author Nico Steppat
 */
public abstract class IntegrationTest extends TestCase {

    protected void open(String url) {
		String contextPath = System.getProperty("seleniumContextPath");
        getSelenium().open(contextPath + url);
        getSelenium().waitForPageToLoad("2000");
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
            return getSelenium().getValue(localizer);
        }
    }

    public Selenium getSelenium() {
        return AcceptanceSuite.getSelenium();
    }
    
}
