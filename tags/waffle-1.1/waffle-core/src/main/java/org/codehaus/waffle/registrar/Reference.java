/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.registrar;

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
