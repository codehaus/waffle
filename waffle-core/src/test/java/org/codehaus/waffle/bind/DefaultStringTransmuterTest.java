package org.codehaus.waffle.bind;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class DefaultStringTransmuterTest {
    private final Mockery mockery = new Mockery();

    private ValueConverterFinder valueConverterFinder = mockery.mock(ValueConverterFinder.class);
    private ValueConverter valueConverter = mockery.mock(ValueConverter.class);

    @Test
    public void canFindCorrectValueConverter() {
        mockery.checking(new Expectations() {
            {
                one(valueConverterFinder).findConverter(Float.class);
                will(returnValue(valueConverter));
                one(valueConverter).convertValue(null, "foobar", Float.class);
                will(returnValue(99.9f));
            }
        });

        StringTransmuter stringTransmuter = new DefaultStringTransmuter(valueConverterFinder);
        assertEquals(Float.valueOf(99.9f), stringTransmuter.transmute("foobar", Float.class));
    }

    @Test
    public void canHandleEmptyStringValue() {
        mockery.checking(new Expectations() {
            {
                one(valueConverterFinder).findConverter(Integer.class);
                will(returnValue(valueConverter));
                one(valueConverter).convertValue(null, "", Integer.class);
                will(returnValue(54));
            }
        });

        StringTransmuter stringTransmuter = new DefaultStringTransmuter(valueConverterFinder);
        assertEquals(Integer.valueOf(54), stringTransmuter.transmute("", Integer.class));
    }
    
    @Test
    public void canHandleEmptyStringValueForPrimitiveType() {
        mockery.checking(new Expectations() {
            {
                one(valueConverterFinder).findConverter(float.class);
                will(returnValue(valueConverter));
                one(valueConverter).convertValue(null, null, float.class);
                will(returnValue(2.0f));
            }
        });

        StringTransmuter stringTransmuter = new DefaultStringTransmuter(valueConverterFinder);
        assertEquals(Float.valueOf(2.0f), stringTransmuter.transmute("", float.class));
    }
}
