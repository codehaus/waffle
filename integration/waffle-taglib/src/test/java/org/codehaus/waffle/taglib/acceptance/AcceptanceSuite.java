package org.codehaus.waffle.taglib.acceptance;

import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.framework.Test;
import junit.extensions.TestSetup;
import junit.runner.ClassPathTestCollector;
import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.DefaultSelenium;
import org.openqa.selenium.server.SeleniumServer;

import java.util.Enumeration;

/**
 * @Author Fabio Kung
 */
public class AcceptanceSuite {
    private static Selenium selenium;

    public static Selenium getSelenium() {
        return selenium;
    }

    public static Test suite() throws Exception {
        TestSuite suite = new TestSuite("Taglib acceptance tests");
        AcceptanceTestCollector testCollector = new AcceptanceTestCollector();
        Enumeration tests = testCollector.collectTests();
        while (tests.hasMoreElements()) {
            String testName = tests.nextElement().toString();
            suite.addTestSuite((Class<? extends TestCase>) Class.forName(testName));
        }

        suite.addTestSuite(CheckBoxTest.class);
        suite.addTestSuite(ErrorsTest.class);
        suite.addTestSuite(FormTest.class);

        TestSetup wrapper = new TestSetup(suite) {
            protected void setUp() throws Exception {
                String browserString = System.getProperty("seleniumBrowserString");
                int defaultPort = SeleniumServer.getDefaultPort();
                selenium = new DefaultSelenium("localhost", defaultPort, browserString, "http://localhost:8080");
                selenium.start();
            }

            protected void tearDown() throws Exception {
                selenium.stop();
            }
        };
        return wrapper;
    }

    private static class AcceptanceTestCollector extends ClassPathTestCollector {
        protected boolean isTestClass(String s) {
            return s.matches(".*/acceptance/.*") && s.endsWith("Test.class");
        }
    }
    
}
