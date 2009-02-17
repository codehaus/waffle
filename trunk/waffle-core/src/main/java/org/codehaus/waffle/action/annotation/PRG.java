/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.action.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Annotation to control the use of the PRG (Post/Redirect/Get) paradigm.
 * See <a href="http://en.wikipedia.org/wiki/Post/Redirect/Get">http://en.wikipedia.org/wiki/Post/Redirect/Get</a></p>
 * 
 * @author Mauro Talevi
 * @author Michael Ward
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PRG {
    boolean value() default true;
}
