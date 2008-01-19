/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Mauro Talevi                                            *
 *****************************************************************************/
package org.codehaus.waffle.validation;

/**
 * Holds validator configuration
 * 
 * @author Mauro Talevi
 */
public interface ValidatorConfiguration {

    /**
     * Returns the suffix of the validator class name
     * 
     * @return The suffix
     */
    String getSuffix();

}
