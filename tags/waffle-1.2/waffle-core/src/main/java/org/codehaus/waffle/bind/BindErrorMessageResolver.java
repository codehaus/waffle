/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.bind;

public interface BindErrorMessageResolver {

    String resolve(Object model, String propertyName, String value);
}
