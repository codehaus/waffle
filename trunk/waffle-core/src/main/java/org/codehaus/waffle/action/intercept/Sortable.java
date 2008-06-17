/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action.intercept;

/**
 * <p>Identifies a {@link MethodInterceptor} so that it will be evaluated in the correct order</p>
 */
public interface Sortable {

    /**
     * Defines the order the implementing MethodInterceptor should be evaluated (in comparison to other MethodInterceptors).
     *
     * @return the value representing its place in line
     */
    Integer getIndex();
}
