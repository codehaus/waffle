package org.codehaus.waffle.bind.ognl;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

import org.codehaus.waffle.bind.ValueConverter;
import org.codehaus.waffle.context.ContextLevel;
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
    public void canDelegateToValueConverter() {
        // Mock ValueConverter 
        final ValueConverter valueConverter = mockery.mock(ValueConverter.class);
        mockery.checking(new Expectations() {
            {
                one(valueConverter).accept(Vector.class);
                will(returnValue(true));
                one(valueConverter).convertValue(with(same("propertyName")), with(same("foobar")), with(same(Vector.class)));
                will(returnValue(new Vector<Object>()));
            }
        });
        DelegatingTypeConverter converter = new DelegatingTypeConverter(new OgnlValueConverterFinder(valueConverter));

        converter.convertValue("propertyName", "foobar", Vector.class);
    }
}
