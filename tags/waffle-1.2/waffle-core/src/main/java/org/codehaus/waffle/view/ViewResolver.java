/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

/**
 * The view resolver determines the path the next view
 *
 * @author Michael Ward
 */
public interface ViewResolver {

    /**
     * Resolves the view by return the path to the next view
     *
     * @param view the View
     * @return The path to the next View.
     */
    String resolve(View view);
}
