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
