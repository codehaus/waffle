package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.codehaus.waffle.testmodel.FakeControllerWithListMethods.methodParameterType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.OgnlException;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class StringNumberListMapValueConverterTest extends AbstractValueConverterTest {

    @Test
    public void canAccept() throws IntrospectionException {
        StringNumberListMapValueConverter converter = new StringNumberListMapValueConverter(
                new DefaultMessageResources());
        assertTrue(converter.accept(methodParameterType("mapOfStringIntegerLists")));
        assertFalse(converter.accept(List.class));
        assertFalse(converter.accept(Object.class));
        assertFalse(converter.accept(methodParameterType("object")));
    }

    @Test
    public void canConvertMapsOfStringNumberLists() throws OgnlException {
        StringNumberListMapValueConverter converter = new StringNumberListMapValueConverter(
                new DefaultMessageResources());
        Map<String, List<? extends Number>> map = new HashMap<String, List<? extends Number>>();
        map.put("a", asList(1));
        map.put("b", asList(1, 2));
        map.put("c", asList(1, 2, 3));
        // Note: no conversion is done from String to Numbers and the assertion is done on the string representation
        assertCanConvertValueToList(converter, map, "a=1\n b=1,2\n c=1,2,3");
    }

    @SuppressWarnings("unchecked")
    private void assertCanConvertValueToList(StringNumberListMapValueConverter converter,
            Map<String, List<? extends Number>> expected, String value) {
        Map<String, List<? extends Number>> actual = (Map<String, List<? extends Number>>) converter.convertValue(
                "property-name", value, Map.class);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void canHandleMissingValues() {
        StringNumberListMapValueConverter converter = new StringNumberListMapValueConverter(
                new DefaultMessageResources());
        assertEmptyList(converter, null);
        assertEmptyList(converter, "");
        assertEmptyList(converter, " ");
    }

    private void assertEmptyList(StringNumberListMapValueConverter converter, String value) {
        List<?> list = (List<?>) converter.convertValue("property-name", value, List.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        StringNumberListMapValueConverter converter = new StringNumberListMapValueConverter(resources) {

            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(resources.getMessage(StringNumberListMapValueConverter.BIND_ERROR_MAP_KEY),
                    "property-name"), e.getMessage());
        }
    }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        StringNumberListMapValueConverter converter = new StringNumberListMapValueConverter(
                new DefaultMessageResources()) {
            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(StringNumberListMapValueConverter.DEFAULT_MAP_MESSAGE, "property-name"), e.getMessage());
        }
    }

}
