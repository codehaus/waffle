package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static org.codehaus.waffle.bind.converters.DateValueConverter.BIND_ERROR_DATE_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.BIND_ERROR_DATE_MISSING_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DATE_FORMAT_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DEFAULT_DATE_FORMAT;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DEFAULT_DATE_MESSAGE;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DEFAULT_DATE_MISSING_MESSAGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ognl.OgnlException;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResourcesConfiguration;
import org.junit.Test;

/**
 * 
 * @author Mauro Talevi
 */
public class DateValueConverterTest {

    private MessageResourcesConfiguration configuration = new MessageResourcesConfiguration(){

        public Locale getDefaultLocale() {
            return Locale.UK;
        }

        public String getResourceBundleName() {
            return "FakeResourceBundle";
        }
        
    };
    @Test
    public void canAccept() {
        DateValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        assertTrue(converter.accept(Date.class));
        assertTrue(converter.accept(java.sql.Date.class));
    }

    @Test
    public void canConvertWithCustomDateFormat() throws OgnlException {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        DateValueConverter converter = new DateValueConverter(resources);
        Date date = converter.convertValue("property-name", "04-03-2008", Date.class);

        assertEquals("04-03-2008", new SimpleDateFormat(resources.getMessage(DATE_FORMAT_KEY)).format(date));
    }

    @Test
    public void canConvertWithDefaultDateFormat() {
        DateValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        Date date = converter.convertValue("property-name", "04/03/2008", Date.class);

        assertEquals("04/03/2008", new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date));
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        DateValueConverter converter = new DateValueConverter(resources);
        try {
            converter.convertValue("property-name", "bad-value", Date.class);
        } catch ( BindException e) {
            assertEquals(format(resources.getMessage(BIND_ERROR_DATE_KEY), "property-name", "bad-value", resources.getMessage(DATE_FORMAT_KEY)), e.getMessage());
        }
        try {
            converter.convertValue("property-name", null, Date.class);
        } catch ( BindException e) {
            assertEquals(format(resources.getMessage(BIND_ERROR_DATE_MISSING_KEY), "property-name"), e.getMessage());
        }
     }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        DateValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        try {
            converter.convertValue("property-name", "bad-value", Date.class);
        } catch ( BindException e) {
            assertEquals(format(DEFAULT_DATE_MESSAGE, "property-name", "bad-value", DEFAULT_DATE_FORMAT), e.getMessage());
        }
        try {
            converter.convertValue("property-name", null, Date.class);
        } catch ( BindException e) {
            assertEquals(format(DEFAULT_DATE_MISSING_MESSAGE, "property-name"), e.getMessage());
        }
     }

}

