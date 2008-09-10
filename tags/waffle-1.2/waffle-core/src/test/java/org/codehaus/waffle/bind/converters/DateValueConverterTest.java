package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static org.codehaus.waffle.bind.converters.DateValueConverter.BIND_ERROR_DATE_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.BIND_ERROR_DATE_MISSING_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DATE_FORMAT_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DAY_FORMAT_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DAY_NAME_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DEFAULT_DATE_FORMAT;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DEFAULT_DATE_MESSAGE;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DEFAULT_DATE_MISSING_MESSAGE;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DEFAULT_DAY_FORMAT;
import static org.codehaus.waffle.bind.converters.DateValueConverter.DEFAULT_TIME_FORMAT;
import static org.codehaus.waffle.bind.converters.DateValueConverter.TIME_FORMAT_KEY;
import static org.codehaus.waffle.bind.converters.DateValueConverter.TIME_NAME_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class DateValueConverterTest extends AbstractValueConverterTest {

    @Test
    public void canAccept() {
        AbstractValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        assertTrue(converter.accept(Date.class));
        assertTrue(converter.accept(java.sql.Date.class));
        assertFalse(converter.accept(Object.class));
    }

    @Test
    public void canConvertWithDefaultDateFormats() {
        AbstractValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        assertDateFormattable("04/03/2008", DEFAULT_DATE_FORMAT, converter.convertValue("property-name", "04/03/2008",
                Date.class));
        assertDateFormattable("04/03/2008", DEFAULT_DAY_FORMAT, converter.convertValue("propertyDay", "04/03/2008",
                Date.class));
        assertDateFormattable("04/03/2008 11:11:11", DEFAULT_TIME_FORMAT, converter.convertValue("propertyTime",
                "04/03/2008 11:11:11", Date.class));
    }

    @Test
    public void canConvertWithDateFormatsConfiguredViaMessageResource() {
        MessageResources resources = new DefaultMessageResources(configuration);
        AbstractValueConverter converter = new DateValueConverter(resources);
        assertDateFormattable("04-03-2008", resources.getMessage(DATE_FORMAT_KEY), converter.convertValue(
                "property-name", "04-03-2008", Date.class));
        assertDateFormattable("04", resources.getMessage(DAY_FORMAT_KEY), converter.convertValue("day-property", "04",
                Date.class));
        assertDateFormattable("11:11:11", resources.getMessage(TIME_FORMAT_KEY), converter.convertValue(
                "time-property", "11:11:11", Date.class));
    }

    @Test
    public void canConvertWithDateFormatsConfiguredViaProperties() {
        DateValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        assertTrue(converter.getPatterns().isEmpty());
        Properties patterns = new Properties();
        patterns.setProperty(DATE_FORMAT_KEY, "dd-MM-yyyy");
        patterns.setProperty(DAY_FORMAT_KEY, "dd");
        patterns.setProperty(TIME_FORMAT_KEY, "HH:mm:ss");
        patterns.setProperty(DAY_NAME_KEY, "day.*");
        patterns.setProperty(TIME_NAME_KEY, "time.*");
        converter.changePatterns(patterns);
        assertDateFormattable("04-03-2008", "dd-MM-yyyy", converter.convertValue("property-name", "04-03-2008",
                Date.class));
        assertDateFormattable("04", "dd", converter.convertValue("day-property", "04", Date.class));
        assertDateFormattable("11:11:11", "HH:mm:ss", converter.convertValue("time-property", "11:11:11", Date.class));
    }

    private void assertDateFormattable(String value, String pattern, Object object) {
        assertEquals(value, new SimpleDateFormat(pattern).format(object));
    }

    @Test
    public void canHandleMissingValues() {
        MessageResources resources = new DefaultMessageResources(configuration);
        AbstractValueConverter converter = new DateValueConverter(resources);
        assertNull(converter.convertValue("property-name", null, Date.class));
        assertNull(converter.convertValue("property-name", "", Date.class));
        assertNull(converter.convertValue("property-name", " ", Date.class));
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        MessageResources resources = new DefaultMessageResources(configuration);
        AbstractValueConverter converter = new DateValueConverter(resources);
        try {
            converter.convertValue("property-name", "bad-value", Date.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(resources.getMessage(BIND_ERROR_DATE_KEY), "property-name", "bad-value", resources
                    .getMessage(DATE_FORMAT_KEY)), e.getMessage());
        }
        AbstractValueConverter strictConverter = new DateValueConverter(resources) {
            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            strictConverter.convertValue("property-name", null, Date.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(resources.getMessage(BIND_ERROR_DATE_MISSING_KEY), "property-name"), e.getMessage());
        }
    }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        AbstractValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        try {
            converter.convertValue("property-name", "bad-value", Date.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(DEFAULT_DATE_MESSAGE, "property-name", "bad-value", DEFAULT_DATE_FORMAT), e
                    .getMessage());
        }
        AbstractValueConverter strictConverter = new DateValueConverter(new DefaultMessageResources()) {
            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            strictConverter.convertValue("property-name", null, Date.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(DEFAULT_DATE_MISSING_MESSAGE, "property-name"), e.getMessage());
        }
    }

}
