package org.codehaus.waffle.taglib.acceptance;

/**
 * @author Fabio Kung
 */
public class ErrorsTest extends IntegrationTest {

    public void testGlobalErrorMessages() {
        open("products.waffle");

        int globalErrorCount = getSelenium().getXpathCount("//ul[@id='allErrors']/li").intValue();
        assertEquals(0, globalErrorCount);

        getSelenium().submit("productForm");
        getSelenium().waitForPageToLoad("2000");

        globalErrorCount = getSelenium().getXpathCount("//ul[@id='allErrors']/li").intValue();
        assertEquals(3, globalErrorCount);
    }

    public void testEmptySubmitGeneratesTwoFieldErrorMessages() {
        open("products.waffle");
        getSelenium().submit("productForm");
        getSelenium().waitForPageToLoad("2000");

        int nameErrorsCount = getSelenium().getXpathCount("//ul[@id='nameErrors']/li").intValue();
        assertEquals(1, nameErrorsCount);
        int priceErrorsCount = getSelenium().getXpathCount("//ul[@id='priceErrors']/li").intValue();
        assertEquals(1, priceErrorsCount);

    }

    public void testPriceEmptyGeneratesOneFieldErrorMessage() {
        open("products.waffle");
        getSelenium().type("nameField", "John Travolta");
        getSelenium().submit("productForm");
        getSelenium().waitForPageToLoad("2000");

        int nameErrorsCount = getSelenium().getXpathCount("//ul[@id='nameErrors']/li").intValue();
        assertEquals(0, nameErrorsCount);
        int priceErrorsCount = getSelenium().getXpathCount("//ul[@id='priceErrors']/li").intValue();
        assertEquals(1, priceErrorsCount);

    }

    public void testNameEmptyGeneratesOneFieldErrorMessages() {
        open("products.waffle");
        getSelenium().refresh();
        getSelenium().type("priceField", "10");
        getSelenium().submit("productForm");
        getSelenium().waitForPageToLoad("2000");

        int nameErrorsCount = getSelenium().getXpathCount("//ul[@id='nameErrors']/li").intValue();
        assertEquals(1, nameErrorsCount);
        int priceErrorsCount = getSelenium().getXpathCount("//ul[@id='priceErrors']/li").intValue();
        assertEquals(0, priceErrorsCount);

    }

    public void testFormFilledGeneratesNoErrorMessages() {
        open("products.waffle");
        getSelenium().type("nameField", "Luke");
        getSelenium().type("priceField", "10");
        getSelenium().submit("productForm");
        getSelenium().waitForPageToLoad("2000");

        int nameErrorsCount = getSelenium().getXpathCount("//ul[@id='nameErrors']/li").intValue();
        assertEquals(0, nameErrorsCount);
        int priceErrorsCount = getSelenium().getXpathCount("//ul[@id='priceErrors']/li").intValue();
        assertEquals(0, priceErrorsCount);
    }
}
