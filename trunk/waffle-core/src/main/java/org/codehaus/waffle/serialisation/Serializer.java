/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.serialisation;

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
