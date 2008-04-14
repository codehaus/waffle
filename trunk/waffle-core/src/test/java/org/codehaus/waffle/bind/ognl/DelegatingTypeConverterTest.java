package org.codehaus.waffle.bind.ognl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.converters.ListValueConverter;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class DelegatingTypeConverterTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canConvertValueForEnum() {
        DelegatingTypeConverter converter = new DelegatingTypeConverter();
        Object result = converter.convertValue("foobar", "APPLICATION", ContextLevel.class);

        assertEquals(ContextLevel.APPLICATION, result);
    }

    @Test
    public void canConvertValueForIntegers() {
        DelegatingTypeConverter converter = new DelegatingTypeConverter();
        int value = (Integer) converter.convertValue("foobar", "15", Integer.class);

        assertEquals(15, value);
    }
    
    @Test
    public void canDelegateToListValueConverter() {
        final ValueConverter valueConverter = new ListValueConverter(new DefaultMessageResources());
        final List<String> list = asList("one","two");
        DelegatingTypeConverter converter = new DelegatingTypeConverter(new OgnlValueConverterFinder(valueConverter));

        Object convertedValue = converter.convertValue("propertyName", "one,two", List.class);
        assertEquals(convertedValue, list);
    }
    
    @Test(expected=BindException.class)
    public void cannotDelegateToListValueConverterNullValue() {
        final ValueConverter valueConverter = new ListValueConverter(new DefaultMessageResources());
        DelegatingTypeConverter converter = new DelegatingTypeConverter(new OgnlValueConverterFinder(valueConverter));
        
        converter.convertValue("propertyName", null, List.class);
    }
    
    @Test
    public void canDelegateToCustomValueConverter() {
        // Mock ValueConverter 
        final ValueConverter valueConverter = mockery.mock(ValueConverter.class);
        final CustomType type = new CustomType(){};
        mockery.checking(new Expectations() {
            {
                one(valueConverter).accept(CustomType.class);
                will(returnValue(true));
                one(valueConverter).convertValue(with(same("propertyName")), with(same("foobar")), with(same(CustomType.class)));
                will(returnValue(type));
            }
        });
        DelegatingTypeConverter converter = new DelegatingTypeConverter(new OgnlValueConverterFinder(valueConverter));

        Object convertedValue = converter.convertValue("propertyName", "foobar", CustomType.class);
        assertSame(convertedValue, type);
    }
    
    private static interface CustomType {};
}
