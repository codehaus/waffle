/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

/**
 * Implementation of this interface can be used from within a {@link Registrar} to define how a components dependencies
 * can be resolved more explicitly.
 *
 * @author Michael Ward
 */
public interface Reference {

    /**
     * A key is used to indicate what is being referenced.
     *
     * @return the key pointing to the referenced item.
     */
    Object getKey();

}
