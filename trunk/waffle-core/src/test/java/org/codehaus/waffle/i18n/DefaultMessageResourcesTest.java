package org.codehaus.waffle.i18n;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class DefaultMessageResourcesTest  {

    private MessageResourcesConfiguration configuration = new MessageResourcesConfiguration() {
        public String getDefaultResource() {
            return "FakeResourceBundle";
        }

        public Locale getDefaultLocale() {
            return Locale.US;
        }
    };

    @Test
    public void canGetDefaultLocaleIfNoLocaleSpecified() {
        MessageResources messageResources = new DefaultMessageResources();
        assertEquals(Locale.getDefault(), messageResources.getLocale());
    }

    @Test
    public void canGetMessage() {
        MessageResources messageResources = new DefaultMessageResources(configuration);
        assertEquals("thoughtworks", messageResources.getMessage("company"));
        assertEquals("hello mars", messageResources.getMessage("foo.bar", "mars"));

        messageResources.useLocale(Locale.UK);
        assertEquals("thoughtworks", messageResources.getMessage("company"));
        assertEquals("cheerio mars", messageResources.getMessage("foo.bar", "mars"));
    }

    @Test
    public void canGetMessageWithDefault() {
        MessageResources messageResources = new DefaultMessageResources(configuration);

        assertEquals("thoughtworks", messageResources.getMessageWithDefault("company", "BEARS"));
        assertEquals("hello mars", messageResources.getMessageWithDefault("foo.bar", "should-be-ignored", "mars"));
        assertEquals("defaultValue", messageResources.getMessageWithDefault("should.not.exist", "defaultValue"));
    }

}
