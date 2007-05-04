package com.thoughtworks.waffle.i18n;

import junit.framework.TestCase;

import java.util.Locale;

public class DefaultMessageResourcesTest extends TestCase {

    private DefaultMessageResourcesConfiguration configuration = new DefaultMessageResourcesConfiguration() {
        public String getResourceBundleBaseName() {
            return "FakeResourceBundle";
        }

        public Locale getDefaultLocale() {
            return Locale.US;
        }
    };

    public void testGetLocaleReturnsDefaultIfNull() {
        MessageResources messageResources = new DefaultMessageResources();
        assertEquals(Locale.getDefault(), messageResources.getLocale());
    }

    public void testGetMessage() {
        MessageResources messageResources = new DefaultMessageResources(configuration);
        assertEquals("thoughtworks", messageResources.getMessage("company"));
        assertEquals("hello mars", messageResources.getMessage("foo.bar", "mars"));

        messageResources.setLocale(Locale.UK);
        assertEquals("thoughtworks", messageResources.getMessage("company"));
        assertEquals("cheerio mars", messageResources.getMessage("foo.bar", "mars"));
    }

    public void testGetMessageWithDefault() {
        MessageResources messageResources = new DefaultMessageResources(configuration);

        assertEquals("thoughtworks", messageResources.getMessageWithDefault("company", "BEARS"));
        assertEquals("hello mars", messageResources.getMessageWithDefault("foo.bar", "should-be-ignored", "mars"));
        assertEquals("defaultValue", messageResources.getMessageWithDefault("should.not.exist", "defaultValue"));
    }

}
