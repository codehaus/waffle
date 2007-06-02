package org.codehaus.waffle.view;

import com.thoughtworks.xstream.XStream;
import org.codehaus.waffle.testmodel.FakeBean;
import org.junit.Assert;
import org.junit.Test;

public class BeanPropertyConverterTest {

    @Test
    public void unmarshalShouldThrowUnsupportedOperationException() {
        BeanPropertyConverter beanPropertyConverter = new BeanPropertyConverter();

        try {
            beanPropertyConverter.unmarshal(null, null);
            Assert.fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
            // expected    
        }
    }

    @Test
    public void convertShouldAlwaysBeTrue() {
        BeanPropertyConverter beanPropertyConverter = new BeanPropertyConverter();
        Assert.assertTrue(beanPropertyConverter.canConvert(this.getClass()));
    }

    @Test
    public void testMarshall() {
        XStream xStream = new XStream();
        xStream.registerConverter(new BeanPropertyConverter(), -19);

        FakeBean fakeBean = new FakeBean();
        String xml = xStream.toXML(fakeBean);

        String expected =
                "<org.codehaus.waffle.testmodel.FakeBean>\n" +
                "  <count>0</count>\n" +
                "  <list/>\n" +
                "</org.codehaus.waffle.testmodel.FakeBean>";

        Assert.assertEquals(expected, xml);
    }

}
