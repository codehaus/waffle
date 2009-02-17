package org.codehaus.waffle.taglib.acceptance;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.runner.ClassPathTestCollector;
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
        // FIXME auto-load all test cases
//        loadAcceptanceTests(suite);
        suite.addTestSuite(CheckBoxTest.class);
        suite.addTestSuite(ErrorsTest.class);
        suite.addTestSuite(FormTest.class);
        suite.addTestSuite(HiddenTagTest.class);
        suite.addTestSuite(MessagesTest.class);
        suite.addTestSuite(NumberTest.class);
        suite.addTestSuite(PasswordTagTest.class);
        suite.addTestSuite(SelectTest.class);
        suite.addTestSuite(SelectTokensTest.class);
        suite.addTestSuite(TextareaTagTest.class);

        TestSetup wrapper = new TestSetup(suite) {
            protected void setUp() throws Exception {
                String browserString = System.getProperty("seleniumBrowserString");
                if(browserString == null) {
                    browserString = "*firefox";
                }
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

    private static void loadAcceptanceTests(TestSuite suite) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        AcceptanceTestCollector testCollector = new AcceptanceTestCollector();
        Enumeration tests = testCollector.collectTests();
        while (tests.hasMoreElements()) {
            String testName = tests.nextElement().toString();
            Class<? extends TestCase> testClass = (Class<? extends TestCase>) Class.forName(testName);
            testClass.newInstance();
            suite.addTestSuite(testClass);
        }
    }

    private static class AcceptanceTestCollector extends ClassPathTestCollector {
        protected boolean isTestClass(String s) {
            return s.matches(".*/acceptance/.*") && s.endsWith("Test.class") && !s.endsWith("IntegrationTest.class");
        }
    }

}
