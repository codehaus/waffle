/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.validation;

public class DefaultValidatorConfiguration implements ValidatorConfiguration {
    static final String DEFAULT_SUFFIX = "Validator";
    
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
