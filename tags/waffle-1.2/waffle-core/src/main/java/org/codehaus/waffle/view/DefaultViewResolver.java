/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

/**
 * The default ViewResolver simply returns the path of the View being resolved.
 *
 * @author Michael Ward
 */
public class DefaultViewResolver implements ViewResolver {

    public String resolve(View view) {
        return view.getPath();
    }
    
}
