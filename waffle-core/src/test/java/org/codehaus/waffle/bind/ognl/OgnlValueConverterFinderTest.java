package org.codehaus.waffle.bind.ognl;

import static org.codehaus.waffle.testmodel.FakeControllerWithListMethods.methodParameterType;
import static org.junit.Assert.assertEquals;

import java.beans.IntrospectionException;
import java.lang.reflect.Type;
import java.util.List;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.bind.converters.StringListValueConverter;
import org.codehaus.waffle.bind.converters.NumberListValueConverter;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.junit.Test;

/**
 * @author Mauro Talevi
 */
public class OgnlValueConverterFinderTest {

    private static MessageResources RESOURCES = new DefaultMessageResources();

    @Test
    public void canFindDifferentListConverters() throws IntrospectionException {
        ValueConverterFinder finder = new OgnlValueConverterFinder(new StringListValueConverter(RESOURCES), new NumberListValueConverter(RESOURCES));
        assertConverterType(finder, List.class, OgnlValueConverter.class); // List.class is not parameterized and matches default converter
        assertConverterType(finder, methodParameterType("listOfStrings"), StringListValueConverter.class);
        assertConverterType(finder, methodParameterType("listOfIntegers"), NumberListValueConverter.class);
    }

    private void assertConverterType(ValueConverterFinder finder, Type type, Class<? extends ValueConverter> expectedConverterType) {
        assertEquals(expectedConverterType, finder.findConverter(type).getClass());
    }

}
