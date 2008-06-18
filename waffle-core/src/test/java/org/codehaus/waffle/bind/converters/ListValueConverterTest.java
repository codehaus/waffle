package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import ognl.OgnlException;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class ListValueConverterTest extends AbstractValueConverterTest {

    @Test
    public void canAccept() {
        ListValueConverter converter = new ListValueConverter(new DefaultMessageResources());
        assertTrue(converter.accept(List.class));
    }

    @Test
    public void canConvertLists() throws OgnlException {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        ListValueConverter converter = new ListValueConverter(resources);
        // Note: no conversion is done from String to Numbers and the assertion is done on the string representation
        assertCanConvertValueToList(converter, INTEGERS, "-1,-2,-3");
        assertCanConvertValueToList(converter, LONGS, "1000,2000,3000");
        assertCanConvertValueToList(converter, DOUBLES, "0.1,0.2,0.3");
        assertCanConvertValueToList(converter, FLOATS, "0.1,0.2,0.3");
        assertCanConvertValueToList(converter, STRINGS, "one,two,three");
        assertCanConvertValueToList(converter, STRINGS, ",one,two,three");
        assertCanConvertValueToList(converter, STRINGS, "one,,two,three");
        assertCanConvertValueToList(converter, MIXED_STRINGS, "0#.A,1#.B");
    }

    @SuppressWarnings("unchecked")
    private void assertCanConvertValueToList(ListValueConverter converter, List<?> expected, String value) {
        List<String> actual = (List<String>) converter.convertValue("property-name", value, List.class);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void canHandleMissingValues() {
        ListValueConverter converter = new ListValueConverter(new DefaultMessageResources());
        assertEmptyList(converter, null);
        assertEmptyList(converter, "");
        assertEmptyList(converter, " ");
    }

    private void assertEmptyList(ListValueConverter converter, String value) {
        List<?> list = (List<?>) converter.convertValue("property-name", value, List.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        ListValueConverter converter = new ListValueConverter(resources) {

            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(resources.getMessage(ListValueConverter.BIND_ERROR_LIST_KEY), "property-name"), e
                    .getMessage());
        }
    }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        ListValueConverter converter = new ListValueConverter(new DefaultMessageResources()) {
            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(ListValueConverter.DEFAULT_LIST_MESSAGE, "property-name"), e.getMessage());
        }
    }

}
