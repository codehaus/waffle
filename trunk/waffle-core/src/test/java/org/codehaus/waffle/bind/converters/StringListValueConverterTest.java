package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static org.codehaus.waffle.testmodel.FakeControllerWithListMethods.methodParameterType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import java.util.List;

import ognl.OgnlException;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class StringListValueConverterTest extends AbstractValueConverterTest {

    @Test
    public void canAccept() throws IntrospectionException {
        StringListValueConverter converter = new StringListValueConverter(new DefaultMessageResources());
        assertTrue(converter.accept(methodParameterType("listOfStrings")));
        assertFalse(converter.accept(List.class));
        assertFalse(converter.accept(Object.class));
        assertFalse(converter.accept(methodParameterType("object")));
    }

    @Test
    public void canConvertListsOfStrings() throws OgnlException {
        StringListValueConverter converter = new StringListValueConverter(new DefaultMessageResources());
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
    private void assertCanConvertValueToList(StringListValueConverter converter, List<?> expected, String value) {
        List<String> actual = (List<String>) converter.convertValue("property-name", value, List.class);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void canHandleMissingValues() {
        StringListValueConverter converter = new StringListValueConverter(new DefaultMessageResources());
        assertEmptyList(converter, null);
        assertEmptyList(converter, "");
        assertEmptyList(converter, " ");
    }

    private void assertEmptyList(StringListValueConverter converter, String value) {
        List<?> list = (List<?>) converter.convertValue("property-name", value, List.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        StringListValueConverter converter = new StringListValueConverter(resources) {

            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(resources.getMessage(StringListValueConverter.BIND_ERROR_LIST_KEY), "property-name"), e
                    .getMessage());
        }
    }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        StringListValueConverter converter = new StringListValueConverter(new DefaultMessageResources()) {
            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(StringListValueConverter.DEFAULT_LIST_MESSAGE, "property-name"), e.getMessage());
        }
    }

}
