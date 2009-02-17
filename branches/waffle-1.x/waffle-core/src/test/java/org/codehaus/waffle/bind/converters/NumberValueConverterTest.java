package org.codehaus.waffle.bind.converters;

import ognl.OgnlException;
import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import static org.codehaus.waffle.testmodel.FakeControllerWithNumberMethods.methodParameterType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.beans.IntrospectionException;

/**
 * @author Mauro Talevi
 */
public class NumberValueConverterTest extends AbstractValueConverterTest {

    @Test
    public void canAccept() throws IntrospectionException {
        NumberValueConverter converter = new NumberValueConverter(new DefaultMessageResources());
        assertTrue(converter.accept(Number.class));
        assertTrue(converter.accept(Double.class));
        assertTrue(converter.accept(methodParameterType("primitiveDouble")));
        assertTrue(converter.accept(Float.class));
        assertTrue(converter.accept(methodParameterType("primitiveFloat")));
        assertTrue(converter.accept(Long.class));
        assertTrue(converter.accept(methodParameterType("primitiveLong")));
        assertTrue(converter.accept(Integer.class));
        assertTrue(converter.accept(methodParameterType("primitiveInteger")));
        assertFalse(converter.accept(Object.class));
    }
    
    @Test
    public void canConvertNumbers() throws OgnlException, IntrospectionException {
        NumberValueConverter converter = new NumberValueConverter(new DefaultMessageResources());
        assertCanConvertValueToNumber(converter, -1, "-1", Long.class);
        assertCanConvertValueToNumber(converter, -1E3, "-1,000", Long.class);
        assertCanConvertValueToNumber(converter, 1E3, "1000", Long.class);
        assertCanConvertValueToNumber(converter, 1E3, "1,000", Long.class);
        assertCanConvertValueToNumber(converter, 1E6, "1,000,000", Long.class);
        assertCanConvertValueToNumber(converter, 0.1, "0.1", Double.class);
        assertCanConvertValueToNumber(converter, 0.1E-3, ".1E-3", Double.class);
    }

    @Test(expected=BindException.class)
    public void cannotParseInvalidNumber() throws OgnlException, IntrospectionException {
        NumberValueConverter converter = new NumberValueConverter(new DefaultMessageResources());
        converter.convertValue("property-name", "invalid-number", Number.class); 
    }

    private void assertCanConvertValueToNumber(NumberValueConverter converter, Number expected, String value,
            Class<?> expectedType) throws IntrospectionException {
        Number actual = (Number)converter.convertValue("property-name", value, expectedType);
        assertEquals(expected.doubleValue(), actual.doubleValue(),1E-6);
    }


}
