package org.codehaus.waffle.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.Constants;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.thoughtworks.xstream.converters.javabean.BeanProperty;
import com.thoughtworks.xstream.converters.javabean.PropertyDictionary;

/**
 * @author Paulo Silveira
 */
@RunWith(JMock.class)
public class XMLViewTest {
    private Mockery mockery = new Mockery();
    private HttpServletRequest mockRequest = mockRequest();
    private HttpServletResponse mockResponse = mockResponse();
    private MockServletOutputStream mockOutput = new MockServletOutputStream();

    @Test
    public void testStripsPackageNameForNonAliasedType() throws IOException {
        final MyObject object = new MyObject();

        buildExpectationsFor(object);

        new XMLView().respond(mockRequest, mockResponse);
        assertEquals("<MyObject/>", mockOutput.getContent());
    }

    @Test
    public void testObjectsWithRelationships() throws IOException {
        final MyOtherObject object = new MyOtherObject();

        buildExpectationsFor(object);

        new XMLView().respond(mockRequest, mockResponse);
        assertEquals("<MyOtherObject>\n  <myGetter/>\n</MyOtherObject>",
                mockOutput.getContent());
    }

    @Test
    public void testObjectsWithRelationshipsAndNullAttributes()
            throws IOException {
        final MyOtherObject object = new MyOtherObject();
        object.my = null;

        buildExpectationsFor(object);

        new XMLView().respond(mockRequest, mockResponse);
        assertEquals("<MyOtherObject>\n  <myGetter/>\n</MyOtherObject>",
                mockOutput.getContent());
    }

    @Test
    public void testObjectsCollections() throws IOException {
        final BeanWithCollection object = new BeanWithCollection();
        buildExpectationsFor(object);

        new XMLView().respond(mockRequest, mockResponse);
        assertEquals(
                "<BeanWithCollection>\n  <collection>\n    <MyObject/>\n    <MyObject/>\n  </collection>\n</BeanWithCollection>",
                mockOutput.getContent());
    }

    @Test
    public void testDoesntStripPackageNameForWrapperTypes() throws IOException {
        final Number[] numbers = new Number[]{(byte) 0, (short) 0, 0, 0L, 0f, 0d};
        String expected = "<number-array>\n" + "  <byte>0</byte>\n"
                + "  <short>0</short>\n" + "  <int>0</int>\n"
                + "  <long>0</long>\n" + "  <float>0.0</float>\n"
                + "  <double>0.0</double>\n" + "</number-array>";

        buildExpectationsFor(numbers);

        new XMLView().respond(mockRequest, mockResponse);
        assertEquals(expected, mockOutput.getContent());
    }

    private void buildExpectationsFor(final Object object) throws IOException {
        Expectations expectations = new Expectations() {
            {
                one(mockRequest).getAttribute(Constants.CONTROLLER_KEY);
                will(returnValue(object));
                one(mockResponse).setCharacterEncoding(with(any(String.class)));
                one(mockResponse).setContentType(with(any(String.class)));
                one(mockResponse).getOutputStream();
                will(returnValue(mockOutput));
            }
        };
        mockery.checking(expectations);
    }

    private HttpServletResponse mockResponse() {
        return mockery.mock(HttpServletResponse.class);
    }

    private HttpServletRequest mockRequest() {
        return mockery.mock(HttpServletRequest.class);
    }

    // JMock 2 is not able to mock concrete classes yet
    class MockServletOutputStream extends ServletOutputStream {
        private String content;

        public void write(int b) {

        }

        @Override
        public void print(String s) {
            this.content = s;
        }

        public String getContent() {
            return content;
        }
    }

    class SomeClass {
        private String a;

        private String URL;

        private String c;

        private String d;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String url) {
            this.URL = url;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getD() {
            return d;
        }

        public void setE(String e) {
        }
    }

    @Test
    public void testListsFieldsInClassInDefinitionOrder() {
        Iterator<?> properties = new PropertyDictionary()
                .serializablePropertiesFor(SomeClass.class);
        assertEquals("URL", ((BeanProperty) properties.next()).getName());
        assertEquals("a", ((BeanProperty) properties.next()).getName());
        assertEquals("c", ((BeanProperty) properties.next()).getName());
        assertFalse("No more fields should be present", properties.hasNext());
    }

}

class MyOtherObject {
    MyObject my = new MyObject();

    public MyObject getMyGetter() {
        return my;
    }
}

class MyObject {

}

class BeanWithCollection {

    private Map<Object, Object> map = new HashMap<Object, Object>();

    {
        map.put("1", new MyObject());
        map.put("2", new MyObject());
    }

    public Collection<Object> getCollection() {
        return map.values();
    }

}