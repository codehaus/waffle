package org.codehaus.waffle.view;

import com.thoughtworks.xstream.XStream;
import org.codehaus.waffle.testmodel.FakeBean;
import org.junit.Assert;
import org.junit.Test;

public class GetterXMLConverterTest {

    @Test
    public void unmarshalShouldThrowUnsupportedOperationException() {
        GetterXMLConverter getterXMLConverter = new GetterXMLConverter();

        try {
            getterXMLConverter.unmarshal(null, null);
            Assert.fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException expected) {
            // expected    
        }
    }

    @Test
    public void convertShouldAlwaysBeTrue() {
        GetterXMLConverter getterXMLConverter = new GetterXMLConverter();
        Assert.assertTrue(getterXMLConverter.canConvert(this.getClass()));
    }

    @Test
    public void testMarshall() {
        XStream xStream = new XStream();
        xStream.registerConverter(new GetterXMLConverter(), -19);

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
