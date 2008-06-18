package org.codehaus.waffle.bind.ognl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.beans.IntrospectionException;
import java.util.List;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.bind.converters.ListValueConverter;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.testmodel.FakeControllerWithListMethods;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class DelegatingTypeConverterTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canConvertValueForEnum() {
        DelegatingTypeConverter converter = new DelegatingTypeConverter();
        assertEquals(ContextLevel.APPLICATION, converter.convertValue("foobar", "APPLICATION", ContextLevel.class));
    }

    @Test
    public void canConvertValueForIntegers() {
        DelegatingTypeConverter converter = new DelegatingTypeConverter();
        assertEquals(15, converter.convertValue("foobar", "15", Integer.class));
    }

    @Test
    public void canDelegateToListValueConverter() {
        final ValueConverter valueConverter = new ListValueConverter(new DefaultMessageResources());
        DelegatingTypeConverter converter = new DelegatingTypeConverter(new OgnlValueConverterFinder(valueConverter));
        assertEquals(asList("one", "two"), converter.convertValue("propertyName", "one,two", List.class));
        assertEquals(asList(), converter.convertValue("propertyName", "", List.class));
    }

    @Test
    public void canDelegateToCustomValueConverter() {
        // Mock ValueConverter
        final ValueConverter valueConverter = mockery.mock(ValueConverter.class);
        final FakeControllerWithListMethods controller = new FakeControllerWithListMethods();
        mockery.checking(new Expectations() {
            {
                one(valueConverter).accept(FakeControllerWithListMethods.class);
                will(returnValue(true));
                one(valueConverter).convertValue(with(same("propertyName")), with(same("foobar")),
                        with(same(FakeControllerWithListMethods.class)));
                will(returnValue(controller));
            }
        });
        DelegatingTypeConverter converter = new DelegatingTypeConverter(new OgnlValueConverterFinder(valueConverter));
        assertSame(controller, converter.convertValue("propertyName", "foobar", FakeControllerWithListMethods.class));
    }

    @Test
    public void canReturnValueIfNotConverterFoundForTypeThatIsNotAClass() throws IntrospectionException {
        DelegatingTypeConverter converter = new DelegatingTypeConverter(new OgnlValueConverterFinder());
        assertEquals("one,two", converter.convertValue("propertyName", "one,two", FakeControllerWithListMethods
                .methodParameterType("listOfStrings")));
    }

}
