/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

import java.lang.reflect.Type;

/**
 * Finder interface for {@link org.codehaus.waffle.bind.ValueConverter ValueConverter} instances registered per application.
 * 
 * @author Mauro Talevi
 */
public interface ValueConverterFinder {

    /**
     * Returns the converter that is responsible for handling the type passed in.
     * 
     * @param type the Type identifying the ValueConverter needed
     * @return the associated ValueConverter is returned or {@code null} if none was found.
     */
    ValueConverter findConverter(Type type);

    /**
     * Registers the given converter
     * 
     * @param converter the ValueConverter to register
     */
    void registerConverter(ValueConverter converter);
}
