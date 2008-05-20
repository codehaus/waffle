package org.codehaus.waffle.testing.view;

import java.util.Map;

/**
 * ViewProcessor abstracts the view processing implemented by different template engines, eg Freemarker, Velocity etc.
 * The view expects a single controller instance under key "controller".
 * 
 * @author Mauro Talevi
 */
public interface ViewProcessor {

    /**
     * Processes view content with given controller
     * 
     * @param resource the template resource
     * @param controller the controller instance
     * @return The processed content
     */
    String process(String resource, Object controller);

    /**
     * Processes template content with given data model
     * 
     * @param resource the template resource
     * @param dataModel the Map<String, Object> holding the data model
     * @return The processed content
     */
    String process(String resource, Map<String, Object> dataModel);

    /**
     * Creates an data model for the given controller. The data model contains:
     * <ul>
     * <li>"base": ""</li>
     * <li>"controller": controller instance</li>
     * <li>"errors": the default errors context</li>
     * <li>"messages": the default messages context</li>
     * </ul>
     * 
     * @param controller the controller instance
     * @return A Map<String, Object>
     */
    Map<String, Object> createDataModel(Object controller);

}
