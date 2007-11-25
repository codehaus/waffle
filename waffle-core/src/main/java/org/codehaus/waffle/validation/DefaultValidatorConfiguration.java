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
package org.codehaus.waffle.validation;

public class DefaultValidatorConfiguration implements ValidatorConfiguration {
    private static final String DEFAULT_SUFFIX = "Validator";
    
    private String suffix;
    
    public DefaultValidatorConfiguration(){
        this(DEFAULT_SUFFIX);
    }
    
    public DefaultValidatorConfiguration(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

}
