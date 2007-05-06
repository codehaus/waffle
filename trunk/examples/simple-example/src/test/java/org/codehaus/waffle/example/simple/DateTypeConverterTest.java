package org.codehaus.waffle.example.simple;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.example.simple.DateTypeConverter;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import junit.framework.TestCase;
import ognl.OgnlException;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeConverterTest extends TestCase {

    public void testAccept() {
        DateTypeConverter converter = new DateTypeConverter(new DefaultMessageResources());

        Assert.assertTrue(converter.accept(Date.class));
        Assert.assertTrue(converter.accept(java.sql.Date.class));
    }

    public void testConvert() throws OgnlException {
        DateTypeConverter converter = new DateTypeConverter(new DefaultMessageResources());
        Date date = (Date) converter.convert("property-name", "19-09-2004", Date.class);

        Assert.assertEquals("09-19-2004", new SimpleDateFormat("MM-dd-yyyy").format(date));
    }

    public void testConvertFails() {
        DateTypeConverter converter = new DateTypeConverter(new DefaultMessageResources());

        try {
            converter.convert("property-name", "bad-value", Date.class);
            Assert.fail("BindException expected");
        } catch (BindException expected) {
            assertEquals(
                    "The value \"bad-value\" entered in field <i>property-name</i> is not a valid date (dd-MM-yyyy).",
                    expected.getMessage());
        }
    }

    public void testDefaultFormatUsedWhenNoAlternativeProvided() {
        DateTypeConverter converter = new DateTypeConverter(new DefaultMessageResources());
        Date date = (Date) converter.convert("property-name", "00-21-1986", Date.class);

        Assert.assertNotNull(date);
    }
}
