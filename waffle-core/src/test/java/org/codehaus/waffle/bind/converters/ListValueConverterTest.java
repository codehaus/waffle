package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
public class ListValueConverterTest {

    private static final List<Integer> INTEGERS = asList(-1,-2,-3);
    private static final List<Long> LONGS = asList(1000L,2000L,3000L);
    private static final List<Double> DOUBLES = asList(0.1d,0.2d,0.3d);
    private static final List<Float> FLOATS = asList(0.1f,0.2f,0.3f);
    private static final List<String> STRINGS = asList("one","two","three");
    private static final List<String> MIXED_STRINGS = asList("0#.A", "1#.B");
    
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
        ListValueConverter converter = new ListValueConverter(new DefaultMessageResources());
        assertTrue(converter.accept(List.class));
    }

    @Test
    public void canConvert() throws OgnlException {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        ListValueConverter converter = new ListValueConverter(resources);
        assertCanConvertValueToList(converter, INTEGERS, "-1,-2,-3", Integer.class);
        assertCanConvertValueToList(converter, LONGS, "1000,2000,3000", Long.class);
        assertCanConvertValueToList(converter, DOUBLES, "0.1,0.2,0.3", Double.class);
        assertCanConvertValueToList(converter, FLOATS, "0.1,0.2,0.3", Float.class);
        assertCanConvertValueToList(converter, STRINGS, "one,two,three", String.class);
        assertCanConvertValueToList(converter, STRINGS, ",one,two,three", String.class);
        assertCanConvertValueToList(converter, STRINGS, "one,,two,three", String.class);
        assertCanConvertValueToList(converter, MIXED_STRINGS, "0#.A,1#.B", String.class);
    }

    private void assertCanConvertValueToList(ListValueConverter converter, List<?> list, String value, Class<?> type) {
        assertEquals(list.toString(), converter.convertValue("property-name", value, List.class).toString());
        assertTrue(list.get(0).getClass().isAssignableFrom(type));
    }

    @Test
    public void canHandleMissingValues() {
        ListValueConverter converter = new ListValueConverter(new DefaultMessageResources());
        assertEmptyList(converter, null);
        assertEmptyList(converter, "");
        assertEmptyList(converter, " ");
     }

    private void assertEmptyList(ListValueConverter converter, String value) {
        List<?> list = converter.convertValue("property-name", value, List.class);
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        ListValueConverter converter = new ListValueConverter(resources){

            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
               throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch ( BindException e) {
            assertEquals(format(resources.getMessage(ListValueConverter.BIND_ERROR_LIST_KEY), "property-name"), e.getMessage());
        }
     }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        ListValueConverter converter = new ListValueConverter(new DefaultMessageResources()){
            @Override
            protected Object convertMissingValue(String key, String defaultMessage, Object... parameters) {
               throw newBindException(key, defaultMessage, parameters);
            }
        };
        try {
            converter.convertValue("property-name", null, List.class);
            fail("Expected BindException");
        } catch ( BindException e) {
            assertEquals(format(ListValueConverter.DEFAULT_LIST_MESSAGE, "property-name"), e.getMessage());
        }
     }

}

