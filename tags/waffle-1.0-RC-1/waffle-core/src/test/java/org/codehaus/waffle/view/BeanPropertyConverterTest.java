package org.codehaus.waffle.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.codehaus.waffle.testmodel.FakeBean;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class BeanPropertyConverterTest {

    @Test(expected = UnsupportedOperationException.class)
    public void cannotUnmarshalNullObjects() {
        BeanPropertyConverter beanPropertyConverter = new BeanPropertyConverter();
        beanPropertyConverter.unmarshal(null, null);
    }

    @Test
    public void convertShouldAlwaysBeTrue() {
        BeanPropertyConverter beanPropertyConverter = new BeanPropertyConverter();
        assertTrue(beanPropertyConverter.canConvert(this.getClass()));
    }

    @Test
    public void testMarshall() {
        XStream xstream = new XStream();
        xstream.registerConverter(new BeanPropertyConverter(), -19);

        FakeBean fakeBean = new FakeBean();
        String xml = xstream.toXML(fakeBean);

        String expected = "<org.codehaus.waffle.testmodel.FakeBean>\n" 
                + "  <count>0</count>\n" 
                + "  <list/>\n"
                + "</org.codehaus.waffle.testmodel.FakeBean>";

        assertEquals(expected, xml);
    }

}
