package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.codehaus.waffle.testmodel.FakeControllerWithListMethods.methodParameterType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class StringListMapValueConverterTest extends AbstractValueConverterTest {

    @Test
    public void canAccept() throws IntrospectionException {
        StringListMapValueConverter converter = new StringListMapValueConverter(
                new DefaultMessageResources());
        assertTrue(converter.accept(methodParameterType("mapOfStringLists")));
        assertFalse(converter.accept(List.class));
        assertFalse(converter.accept(Object.class));
        assertFalse(converter.accept(methodParameterType("object")));
    }

    @Test
    public void canConvertMapsOfStringNumberLists() throws OgnlException {
        StringListMapValueConverter converter = new StringListMapValueConverter(
                new DefaultMessageResources());
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("a", asList("x"));
        map.put("b", asList("x", "y"));
        map.put("c", asList("x", "y", "z"));
        assertCanConvertValueToList(converter, map, "a=x\n b=x,y\n c=x,y,z");
    }

    @SuppressWarnings("unchecked")
    private void assertCanConvertValueToList(StringListMapValueConverter converter,
            Map<String, List<String>> expected, String value) {
        Map<String, List<String>> actual = (Map<String, List<String>>) converter.convertValue(
                "property-name", value, Map.class);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void canHandleMissingValues() {
        StringListMapValueConverter converter = new StringListMapValueConverter(
                new DefaultMessageResources());
        assertEmptyMap(converter, null);
        assertEmptyMap(converter, "");
        assertEmptyMap(converter, " ");
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        StringListMapValueConverter converter = new StringListMapValueConverter(resources) {

            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(resources.getMessage(StringListMapValueConverter.BIND_ERROR_MAP_KEY),
                    "property-name"), e.getMessage());
        }
    }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        StringListMapValueConverter converter = new StringListMapValueConverter(
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
            assertEquals(format(StringListMapValueConverter.DEFAULT_MAP_MESSAGE, "property-name"), e.getMessage());
        }
    }

}
