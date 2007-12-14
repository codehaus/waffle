package org.codehaus.waffle.taglib.acceptance;

/**
 * @Author Fabio Kung
 */
public class ErrorsTest extends IntegrationTest {

    public void testGlobalErrorMessages() {
        open("products.waffle");

        int globalErrorCount = selenium.getXpathCount("//ul[@id='allErrors']/li").intValue();
        assertEquals(0, globalErrorCount);

        selenium.submit("productForm");
        selenium.waitForPageToLoad("2000");

        globalErrorCount = selenium.getXpathCount("//ul[@id='allErrors']/li").intValue();
        assertEquals(3, globalErrorCount);
    }

    public void testEmptySubmitGeneratesTwoFieldErrorMessages() {
        open("products.waffle");
        selenium.submit("productForm");
        selenium.waitForPageToLoad("2000");

        int nameErrorsCount = selenium.getXpathCount("//ul[@id='nameErrors']/li").intValue();
        assertEquals(1, nameErrorsCount);
        int priceErrorsCount = selenium.getXpathCount("//ul[@id='priceErrors']/li").intValue();
        assertEquals(1, priceErrorsCount);

    }

    public void testPriceEmptyGeneratesOneFieldErrorMessage() {
        open("products.waffle");
        selenium.type("nameField", "John Travolta");
        selenium.submit("productForm");
        selenium.waitForPageToLoad("2000");

        int nameErrorsCount = selenium.getXpathCount("//ul[@id='nameErrors']/li").intValue();
        assertEquals(0, nameErrorsCount);
        int priceErrorsCount = selenium.getXpathCount("//ul[@id='priceErrors']/li").intValue();
        assertEquals(1, priceErrorsCount);

    }

    public void testNameEmptyGeneratesOneFieldErrorMessages() {
        open("products.waffle");
        selenium.refresh();
        selenium.type("priceField", "10");
        selenium.submit("productForm");
        selenium.waitForPageToLoad("2000");

        int nameErrorsCount = selenium.getXpathCount("//ul[@id='nameErrors']/li").intValue();
        assertEquals(1, nameErrorsCount);
        int priceErrorsCount = selenium.getXpathCount("//ul[@id='priceErrors']/li").intValue();
        assertEquals(0, priceErrorsCount);

    }

    public void testFormFilledGeneratesNoErrorMessages() {
        open("products.waffle");
        selenium.type("nameField", "Luke");
        selenium.type("priceField", "10");
        selenium.submit("productForm");
        selenium.waitForPageToLoad("2000");

        int nameErrorsCount = selenium.getXpathCount("//ul[@id='nameErrors']/li").intValue();
        assertEquals(0, nameErrorsCount);
        int priceErrorsCount = selenium.getXpathCount("//ul[@id='priceErrors']/li").intValue();
        assertEquals(0, priceErrorsCount);
    }
}
