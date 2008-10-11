/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.io;

import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * XStream-based serializer.  Delegates to XStream the marshalling.
 * 
 * @author Mauro Talevi
 */
public class XStreamSerializer implements Serializer {
    
    private XStream xstream;
    
    /**
     * Creates a XStreamSerializer with default XStream instance with minimal dependencies
     */
    public XStreamSerializer(){
        this(new XStream(new DomDriver()));
    }

    /**
     * Creates a XStreamSerializer with a given XStream instance
     * @param xstream the XStream instance
     */
    public XStreamSerializer(XStream xstream) {
        this.xstream = xstream;
    }

    public void marshall(Object object, Writer writer) {
        xstream.toXML(object, writer);
    }

    public Object unmarshall(Reader reader) {
        return xstream.fromXML(reader);
    }

}
