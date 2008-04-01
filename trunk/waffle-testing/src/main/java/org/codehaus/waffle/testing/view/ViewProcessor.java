package org.codehaus.waffle.testing.view;

/**
 * ViewProcessor abstracts the view processing implemented by different template engines, eg Freemarker, Velocity etc.
 * 
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

}
