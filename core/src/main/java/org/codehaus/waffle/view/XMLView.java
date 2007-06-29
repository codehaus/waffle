package org.codehaus.waffle.view;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.serialisation.Serialiser;
import org.codehaus.waffle.serialisation.XStreamSerialiser;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * A view that renders the controller as XML.
 * 
 * @author Paulo Silveira
 * @author Mauro Talevi
 */
public class XMLView extends ResponderView {

    private static final String CHARACTER_ENCODING = "ISO-8859-1";
    public static final String CONTENT_TYPE = "text/xml";

    @Override
    public void respond(ServletRequest request, HttpServletResponse response) throws IOException {       
        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE); 
        response.getOutputStream().print(serialise(request.getAttribute(Constants.CONTROLLER_KEY)));
    }

    private String serialise(Object attribute) {
        Serialiser serialiser = createSerialiser();
        Writer writer = new StringWriter();
        serialiser.marshall(attribute, writer);
        return writer.toString();
    }

    //TODO: should the serialiser(s) be registered in WaffleComponentRegistry?
    private Serialiser createSerialiser() {
        // TODO: should we stream.setMode(XStream.NO_REFERENCES); ?
        XStream xstream = new XStream(new DomDriver()) {
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
        };
        xstream.registerConverter(new BeanPropertyConverter(), -19);
        xstream.registerConverter(new CollectionConverter(xstream.getMapper()) {
            @Override
            public boolean canConvert(Class clazz) {
                return Collection.class.isAssignableFrom(clazz);
            }
        }, -18);
        return new XStreamSerialiser(xstream);
    }

}
