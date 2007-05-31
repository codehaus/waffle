package org.codehaus.waffle.view;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.Constants;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * A view that renders the controller as XML.
 * 
 * @author Paulo Silveira
 */
public class XMLView extends ResponderView {

    public static final String CONTENT_TYPE = "text/plain";

    @Override
    public void respond(ServletRequest request, HttpServletResponse response)
            throws IOException {
        XStream xs = new WaffleXStream();
        xs.registerConverter(new GetterXMLConverter(), -19);
        xs.registerConverter(new CollectionConverter(xs.getMapper()) {
            public boolean canConvert(Class c) {
                return Collection.class.isAssignableFrom(c);
            }
        }, -18);

        // TODO: should we stream.setMode(XStream.NO_REFERENCES); ?

        String data = xs.toXML(request.getAttribute(Constants.CONTROLLER_KEY));
        response.setContentType(CONTENT_TYPE);

        // TODO: char encoding?
        response.getOutputStream().print(data);
    }

}

// todo: public class? isolated unit test
/**
 * A XStream class already configured to output user friendly XML based on
 * getters.
 * 
 */
class WaffleXStream extends XStream {

    public WaffleXStream() {
        super(new DomDriver());
    }

    @Override
    protected MapperWrapper wrapMapper(MapperWrapper next) {
        return new MapperWrapper(next) {
            @Override
            public String serializedClass(Class type) {
                String value = super.serializedClass(type);
                if (type.getName().replace('$', '-').equals(value)) {
                    return type.getSimpleName();
                }
                return value;
            }
        };
    }

}