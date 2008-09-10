package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.codehaus.waffle.bind.converters.StringListMapValueConverter.KEY_SEPARATOR_KEY;
import static org.codehaus.waffle.bind.converters.StringListMapValueConverter.LIST_SEPARATOR_KEY;
import static org.codehaus.waffle.bind.converters.StringListMapValueConverter.NEWLINE_SEPARATOR_KEY;
import static org.codehaus.waffle.testmodel.FakeControllerWithListMethods.methodParameterType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import ognl.OgnlException;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
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
        assertFalse(converter.accept(methodParameterType("map")));
        assertFalse(converter.accept(methodParameterType("mapOfStringLists")));
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
        assertCanConvertValueToMap(converter, map, "a=1");
        assertCanConvertValueToMap(converter, map, "a=1\n");
        map.clear();
        map.put("a", asList(1));
        map.put("b", asList(2));
        assertCanConvertValueToMap(converter, map, "a=1\n b=2\n");
        assertCanConvertValueToMap(converter, map, "a=1\n   b=2  \n");
        map.clear();
        map.put("a", asList(1));
        map.put("b", asList(1, 2));
        map.put("c", asList(1, 2, 3));
        assertCanConvertValueToMap(converter, map, "a=1\n b=1,2\n c=1,2,3");
  
    }

    @Test
    public void canConvertMapsOfStringNumberListsWithCustomSeparators() throws OgnlException {
        Properties patterns = new Properties();
        patterns.setProperty(NEWLINE_SEPARATOR_KEY, ";");
        patterns.setProperty(KEY_SEPARATOR_KEY, ":");
        patterns.setProperty(LIST_SEPARATOR_KEY, "-");
        StringNumberListMapValueConverter converter = new StringNumberListMapValueConverter(new DefaultMessageResources(), patterns);
        Map<String, List<? extends Number>> map = new HashMap<String, List<? extends Number>>();
        map.put("a", asList(1));
        assertCanConvertValueToMap(converter, map, "a:1");
        map.clear();
        map.put("a", asList(1));
        map.put("b", asList(2));
        assertCanConvertValueToMap(converter, map, "a:1; b:2");
        map.clear();
        map.put("a", asList(1));
        map.put("b", asList(1, 2));
        map.put("c", asList(1, 2, 3));
        assertCanConvertValueToMap(converter, map, "a:1;b:1-2;c:1-2-3");
    }

    @SuppressWarnings("unchecked")
    private void assertCanConvertValueToMap(StringNumberListMapValueConverter converter,
            Map<String, List<? extends Number>> expected, String value) {
        Map<String, List<? extends Number>> actual = (Map<String, List<? extends Number>>) converter.convertValue(
                "property-name", value, Map.class);
        // Note: no conversion is done from String to Numbers and the assertion is done on the string representation
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void canHandleMissingValues() {
        StringNumberListMapValueConverter converter = new StringNumberListMapValueConverter(
                new DefaultMessageResources());
        assertEmptyMap(converter, null);
        assertEmptyMap(converter, "");
        assertEmptyMap(converter, " ");
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        MessageResources resources = new DefaultMessageResources(configuration);
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
