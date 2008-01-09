/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package org.codehaus.waffle.action.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation identifies a method as being an Action Method.
 * See {@link org.codehaus.waffle.action.intercept.SecurityMethodInterceptor} to protect your application
 * from potential security risk (e.g. hackers).</p>
 * 
 * @author Mauro Talevi
 * @author Michael Ward
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionMethod {
    String[] parameters() default {};

    /**
     * When set signal that the annotated method should be invoked when no other ActionMethod was requested.
     *
     * @return true if the annotated method should be <i>default</i>.
     */
    boolean asDefault() default false;
}
