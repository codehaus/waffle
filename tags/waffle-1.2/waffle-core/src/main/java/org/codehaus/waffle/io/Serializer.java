/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.io;

import java.io.Reader;
import java.io.Writer;

/**
 * Serializer is responsible for marshalling and unmarshalling objects.
 * 
 * @author Mauro Talevi
 */
public interface Serializer {

    /**
     * Marshalls object to a writer
     * 
     * @param object the Object to marshall
     * @param writer the writer to which the object is marshalled
     */
    void marshall(Object object, Writer writer);

    /**
     * Unmarshalls object from reader input
     * 
     * @param reader the input resource
     * @return An unmarshalled Object
     */
    Object unmarshall(Reader reader);

}
