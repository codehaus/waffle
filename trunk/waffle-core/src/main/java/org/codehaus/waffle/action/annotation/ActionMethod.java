/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
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

    /**
     * Used to define the parameter names that should be used to resolve an action methods
     * arguments (order is important).
     *
     * @return the parameter names to resolve with
     */
    String[] parameters() default {};

    /**
     * When set signal that the annotated method should be invoked when no other ActionMethod was requested.
     *
     * @return true if the annotated method should be <i>default</i>.
     */
    boolean asDefault() default false;
}
