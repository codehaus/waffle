package org.codehaus.waffle.bind.converters;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

    private static final List<Integer> INTEGERS = asList(1,2,3);
    private static final List<Long> LONGS = asList(1L,2L,3L);
    private static final List<Float> FLOATS = asList(0.1f,0.2f,0.3f);
    private static final List<Double> DOUBLES = asList(0.1d,0.2d,0.3d);
    
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
        assertCanConvertValueToList(converter, INTEGERS, "1,2,3");
        assertCanConvertValueToList(converter, LONGS, "1,2,3");
        assertCanConvertValueToList(converter, FLOATS, "0.1,0.2,0.3");
        assertCanConvertValueToList(converter, DOUBLES, "0.1,0.2,0.3");
    }

    private void assertCanConvertValueToList(ListValueConverter converter, List<?> list, String value) {
        assertEquals(list.toString(), converter.convertValue("property-name", value, List.class).toString());
    }

    @Test
    public void canFailConversionWithCustomErrorMessages() {
        DefaultMessageResources resources = new DefaultMessageResources(configuration);
        ListValueConverter converter = new ListValueConverter(resources);
        try {
            converter.convertValue("property-name", null, List.class);
        } catch ( BindException e) {
            assertEquals(format(resources.getMessage(ListValueConverter.BIND_ERROR_LIST_KEY), "property-name"), e.getMessage());
        }
     }

    @Test
    public void canFailConversionWithDefaultErrorMessages() {
        ListValueConverter converter = new ListValueConverter(new DefaultMessageResources());
        try {
            converter.convertValue("property-name", null, List.class);
        } catch ( BindException e) {
            assertEquals(format(ListValueConverter.DEFAULT_LIST_MESSAGE, "property-name"), e.getMessage());
        }
     }

}
