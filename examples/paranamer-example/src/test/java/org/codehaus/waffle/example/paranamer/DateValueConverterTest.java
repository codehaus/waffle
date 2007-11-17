package org.codehaus.waffle.example.paranamer;

import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

import ognl.OgnlException;

import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.junit.Assert;
import org.junit.Test;


public class DateValueConverterTest {

    @Test
    public void canAccept() {
        DateValueConverter converter = new DateValueConverter(new DefaultMessageResources());

        Assert.assertTrue(converter.accept(Date.class));
        Assert.assertTrue(converter.accept(java.sql.Date.class));
    }

    @Test
    public void canConvert() throws OgnlException {
        DateValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        Date date = (Date) converter.convertValue("property-name", "19-09-2004", Date.class);

        Assert.assertEquals("09-19-2004", new SimpleDateFormat("MM-dd-yyyy").format(date));
    }

    @Test(expected=BindException.class)
    public void cannotConvertBadValue() {
        DateValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        converter.convertValue("property-name", "bad-value", Date.class);
     }

    @Test
    public void canUseDefaultFormatWhenNoAlternativeProvided() {
        DateValueConverter converter = new DateValueConverter(new DefaultMessageResources());
        Date date = (Date) converter.convertValue("property-name", "00-21-1986", Date.class);

        assertNotNull(date);
    }
}

