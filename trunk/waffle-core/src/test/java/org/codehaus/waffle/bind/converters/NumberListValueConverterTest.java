package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Type;
import java.util.List;
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
public class NumberListValueConverterTest {

    private static final List<Integer> INTEGERS = asList(-1,-2,-3);
    private static final List<Long> LONGS = asList(1000L,2000L,3000L);
    private static final List<Double> DOUBLES = asList(0.1d,0.2d,0.3d);
    private static final List<Float> FLOATS = asList(0.1f,0.2f,0.3f);
    private static final List<String> STRINGS = asList("one","two","three");

    private MessageResourcesConfiguration configuration = new MessageResourcesConfiguration(){

        public Locale getDefaultLocale() {
            return Locale.UK;
        }

        public String getResourceBundleName() {
            return "FakeResourceBundle";
        }
        
    };

    private Type listType(String methodName) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(CustomType.class);
        for (MethodDescriptor md : beanInfo.getMethodDescriptors()) {
            if (md.getMethod().getName().equals(methodName)) {
                return md.getMethod().getGenericParameterTypes()[0];
            }
        }
        return null;
    }

    private static interface CustomType {       
        void listOfStrings(List<String> list);
        void listOfIntegers(List<Integer> list);
        void listOfLongs(List<Integer> list);
        void listOfDoubles(List<Integer> list);
        void listOfFloats(List<Integer> list);
    };
    
    @Test
    public void canAccept() throws IntrospectionException {
        NumberListValueConverter converter = new NumberListValueConverter(new DefaultMessageResources());
        assertTrue(converter.accept(listType("listOfIntegers")));
        assertTrue(converter.accept(listType("listOfLongs")));
        assertTrue(converter.accept(listType("listOfDoubles")));
        assertTrue(converter.accept(listType("listOfFloats")));
        assertFalse(converter.accept(listType("listOfStrings")));
    }

    @Test
    public void canConvertListsOfNumbers() throws OgnlException, IntrospectionException {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        NumberListValueConverter converter = new NumberListValueConverter(resources);
        assertCanConvertValueToList(converter, INTEGERS, "-1,-2,-3", Long.class, "listOfIntegers");
        assertCanConvertValueToList(converter, LONGS, "1000,2000,3000", Long.class, "listOfLongs");
        assertCanConvertValueToList(converter, DOUBLES, "0.1,0.2,0.3", Double.class, "listOfDoubles");
        assertCanConvertValueToList(converter, FLOATS, "0.1,0.2,0.3", Double.class, "listOfFloats");
    }

    @Test //TODO decide if this behaviour is appropriate or if a bind exception should be thrown
    public void canReturnListOfStringsIfParsingFails() throws OgnlException, IntrospectionException {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        NumberListValueConverter converter = new NumberListValueConverter(resources);
        assertCanConvertValueToList(converter, STRINGS, "one,two,three", String.class, "listOfStrings");
    }

    @SuppressWarnings("unchecked")
    private void assertCanConvertValueToList(NumberListValueConverter converter, List<?> expected, String value, Class<?> expectedType, String methodName) throws IntrospectionException {
        List<?> actual = (List<?>) converter.convertValue("property-name", value, listType(methodName));
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
        NumberListValueConverter converter = new NumberListValueConverter(resources){

            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
               throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch ( BindException e) {
            assertEquals(format(resources.getMessage(NumberListValueConverter.BIND_ERROR_LIST_KEY), "property-name"), e.getMessage());
        }
     }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        NumberListValueConverter converter = new NumberListValueConverter(new DefaultMessageResources()){
            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
               throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch ( BindException e) {
            assertEquals(format(NumberListValueConverter.DEFAULT_LIST_MESSAGE, "property-name"), e.getMessage());
        }
     }

}

