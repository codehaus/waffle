/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import java.lang.reflect.Type;

/**
 * Finder interface for {@code ValueConverters} registered per application.
 *
 * @author Mauro Talevi
 */
public interface ValueConverterFinder {

    /**
     * Should return the {@code ValueConverter} that is responsible for handling the type passed in.
     *
     * @param type the Type identifying the ValueConverter needed
     * @return the associated ValueConverter is returned or {@code null} if none was found. 
     */
    ValueConverter findConverter(Type type);

}
