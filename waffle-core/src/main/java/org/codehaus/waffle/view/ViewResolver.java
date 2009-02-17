/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

import java.util.Properties;

/**
 * The view resolver determines the path the next view. 
 * The view resolver allows the configuration of view properties.  
 * Every implementation should hold default values for the 
 * properties configurable via init-params in the web.xml:
 * <ul>
 * <li>"view.prefix"</li>
 * <li>"view.suffix"</li>
 * <li>"errors.view"</li>
 * </ul>
 * as well as any optional mapping between controller names and view names.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 * @see DefaultViewResolver
 */
public interface ViewResolver {

    /**
     * Resolves the view by return the path to the next view
     * 
     * @param view the View
     * @return The path to the next View.
     */
    String resolve(View view);

    /**
     * Configures the resolution of a single view
     * 
     * @param key the view key
     * @param value the view value
     */
    void configureView(String key, String value);
    
    /**
     * Configures the resolution of multiple views
     * 
     * @param viewProperties the view Properties
     */
    void configureViews(Properties viewProperties);

}
