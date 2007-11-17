package org.codehaus.waffle.bind;

import static org.junit.Assert.assertEquals;

import java.util.Vector;

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
public class OgnlTypeConverterTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canConvertValueForEnum() {
        OgnlTypeConverter converter = new OgnlTypeConverter();
        Object result = converter.convertValue("foobar", "APPLICATION", ContextLevel.class);

        assertEquals(ContextLevel.APPLICATION, result);
    }

    @Test
    public void canHandleNulls() {
        OgnlTypeConverter converter = new OgnlTypeConverter();
        int value = (Integer) converter.convertValue("foobar", "15", Integer.class);

        assertEquals(15, value);
    }

    @Test
    public void canConvertDelegatesToWaffleTypeConverter() {
        // Mock TypeConverter 
        final ValueConverter waffleTypeConverter = mockery.mock(ValueConverter.class);
        mockery.checking(new Expectations() {
            {
                one(waffleTypeConverter).accept(Vector.class);
                will(returnValue(true));
                one(waffleTypeConverter).convertValue(with(same("propertyName")), with(same("foobar")), with(same(Vector.class)));
                will(returnValue(new Vector<Object>()));
            }
        });
        ValueConverter[] waffleTypeConverters = {waffleTypeConverter};
        OgnlTypeConverter converter = new OgnlTypeConverter(waffleTypeConverters);

        converter.convertValue("propertyName", "foobar", Vector.class);
    }
}
