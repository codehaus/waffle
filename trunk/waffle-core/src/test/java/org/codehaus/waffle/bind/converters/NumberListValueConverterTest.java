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
public class NumberListValueConverterTest extends AbstractValueConverterTest {

    @Test
    public void canAccept() throws IntrospectionException {
        NumberListValueConverter converter = new NumberListValueConverter(new DefaultMessageResources());
        assertTrue(converter.accept(methodParameterType("listOfIntegers")));
        assertTrue(converter.accept(methodParameterType("listOfLongs")));
        assertTrue(converter.accept(methodParameterType("listOfDoubles")));
        assertTrue(converter.accept(methodParameterType("listOfFloats")));
        assertFalse(converter.accept(List.class));
        assertFalse(converter.accept(Object.class));
        assertFalse(converter.accept(methodParameterType("list")));
        assertFalse(converter.accept(methodParameterType("listOfStrings")));
        assertFalse(converter.accept(methodParameterType("object")));            }

    @Test
    public void canConvertListsOfNumbers() throws OgnlException, IntrospectionException {
        NumberListValueConverter converter = new NumberListValueConverter(new DefaultMessageResources());
        assertCanConvertValueToList(converter, INTEGERS, "-1,-2,-3", Long.class, "listOfIntegers");
        assertCanConvertValueToList(converter, LONGS, "1000,2000,3000", Long.class, "listOfLongs");
        assertCanConvertValueToList(converter, DOUBLES, "0.10,0.200,0.300", Double.class, "listOfDoubles");
        assertCanConvertValueToList(converter, FLOATS, "0.10,0.200,0.300", Double.class, "listOfFloats");
    }

    @Test
    public void canConvertNonParameterizedList() throws OgnlException, IntrospectionException {
        NumberListValueConverter converter = new NumberListValueConverter(new DefaultMessageResources());
        assertCanConvertValueToList(converter, LONGS, "1000,2000,3000", Long.class, "list"); 
    }

    @Test
    // TODO decide if this behaviour is appropriate or if a bind exception should be thrown
    public void canReturnListOfStringsIfParsingFails() throws OgnlException, IntrospectionException {
        NumberListValueConverter converter = new NumberListValueConverter(new DefaultMessageResources());
        assertCanConvertValueToList(converter, STRINGS, "one,two,three", String.class, "listOfStrings");
    }

    @SuppressWarnings("unchecked")
    private void assertCanConvertValueToList(NumberListValueConverter converter, List<?> expected, String value,
            Class<?> expectedType, String methodName) throws IntrospectionException {
        List<?> actual = (List<?>) converter.convertValue("property-name", value, methodParameterType(methodName));
        assertEquals(expected.toString(), actual.toString());
        assertTrue(expectedType.isAssignableFrom(actual.get(0).getClass()));
    }

    @Test
    public void canHandleMissingValues() {
        NumberListValueConverter converter = new NumberListValueConverter(new DefaultMessageResources());
        assertEmptyList(converter, null);
        assertEmptyList(converter, "");
        assertEmptyList(converter, " ");
    }

    private void assertEmptyList(NumberListValueConverter converter, String value) {
        List<?> list = (List<?>) converter.convertValue("property-name", value, List.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        NumberListValueConverter converter = new NumberListValueConverter(resources) {

            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(resources.getMessage(NumberListValueConverter.BIND_ERROR_LIST_KEY), "property-name"), e
                    .getMessage());
        }
    }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        NumberListValueConverter converter = new NumberListValueConverter(new DefaultMessageResources()) {
            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
                throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch (BindException e) {
            assertEquals(format(NumberListValueConverter.DEFAULT_LIST_MESSAGE, "property-name"), e.getMessage());
        }
    }

}
