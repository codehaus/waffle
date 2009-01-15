package org.codehaus.waffle.i18n;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Locale;

import org.junit.Test;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultMessageResourcesTest {

    @Test
    public void canGetDefaultLocaleIfNoLocaleSpecified() {
        MessageResources messageResources = new DefaultMessageResources();
        assertEquals(Locale.getDefault(), messageResources.getLocale());
    }

    @Test
    public void canGetMessageForDifferentURIsAndLocales() {
        MessageResources messageResources = new DefaultMessageResources(new DefaultMessageResourcesConfiguration("Bundle"));
        assertEquals("thoughtworks", messageResources.getMessage("company"));
        assertEquals("hello mars", messageResources.getMessage("foo.bar", "mars"));
        messageResources.useLocale(Locale.UK);
        assertEquals("thoughtworks", messageResources.getMessage("company"));
        assertEquals("cheerio mars", messageResources.getMessage("foo.bar", "mars"));
        messageResources.useLocale(Locale.ITALY);
        assertEquals("thoughtworks", messageResources.getMessage("company"));
        assertEquals("ciao mars", messageResources.getMessage("foo.bar", "mars"));
        messageResources = new DefaultMessageResources(new DefaultMessageResourcesConfiguration("AnotherBundle"));
        assertEquals("howdy mars", messageResources.getMessage("foo.bar", "mars"));

    }

    @Test
    public void canGetMessageUsingMultipleBundles() {
        assertMessagesUsingMultipleBundles(new DefaultMessageResources(new DefaultMessageResourcesConfiguration(
                "Bundle,AnotherBundle")));
        // can ignore trailing spaces in bundle names
        assertMessagesUsingMultipleBundles(new DefaultMessageResources(new DefaultMessageResourcesConfiguration(
                " Bundle, AnotherBundle ")));
        // can configure other separators for bundle names
        assertMessagesUsingMultipleBundles(new DefaultMessageResources(new DefaultMessageResourcesConfiguration(
                "Bundle;AnotherBundle")){
                    @Override
                    protected List<String> bundleNames(String uri) {
                        return asList(uri.split(";"));
                    }            
        });
    }

    private void assertMessagesUsingMultipleBundles(MessageResources messageResources) {
        assertEquals("thoughtworks", messageResources.getMessage("company"));
        assertEquals("howdy mars", messageResources.getMessage("foo.bar", "mars"));
        assertEquals("waffle", messageResources.getMessage("project.name", "mars"));
    }

    @Test
    public void canGetMessageWithDefault() {
        MessageResources messageResources = new DefaultMessageResources(new DefaultMessageResourcesConfiguration("Bundle"));
        assertEquals("thoughtworks", messageResources.getMessageWithDefault("company", "should-be-ignored"));
        assertEquals("hello mars", messageResources.getMessageWithDefault("foo.bar", "should-be-ignored", "mars"));
        assertEquals("defaultValue", messageResources.getMessageWithDefault("should.not.exist", "defaultValue"));
    }

}
