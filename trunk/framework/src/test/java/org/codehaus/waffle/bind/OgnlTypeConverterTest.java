package org.codehaus.waffle.bind;

import org.codehaus.waffle.context.ContextLevel;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.util.Vector;

public class OgnlTypeConverterTest extends MockObjectTestCase {

    public void testConvertValueForEnum() {
        OgnlTypeConverter converter = new OgnlTypeConverter();
        Object result = converter.convertValue("foobar", "APPLICATION", ContextLevel.class);

        assertEquals(ContextLevel.APPLICATION, result);
    }

    public void testConstructorHandlesNull() {
        OgnlTypeConverter converter = new OgnlTypeConverter();
        Integer value = (Integer) converter.convertValue("foobar", "15", Integer.class);

        assertEquals(15, value.intValue());
    }

    public void testConvertDelegatesToWaffleTypeConverter() {
        // Mock TypeConverter
        Mock mockWaffleTypeConverter = mock(WaffleTypeConverter.class);
        mockWaffleTypeConverter.expects(once())
                .method("accept")
                .with(eq(Vector.class))
                .will(returnValue(true));
        mockWaffleTypeConverter.expects(once())
                .method("convert")
                .with(eq("propertyName"), eq("foobar"), eq(Vector.class))
                .will(returnValue(new Vector()));
        WaffleTypeConverter waffleTypeConverter = (WaffleTypeConverter) mockWaffleTypeConverter.proxy();

        WaffleTypeConverter[] waffleTypeConverters = {waffleTypeConverter};
        OgnlTypeConverter converter = new OgnlTypeConverter(waffleTypeConverters);

        converter.convertValue("propertyName", "foobar", Vector.class);
    }
}
