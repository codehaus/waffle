/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.view;

import org.codehaus.waffle.controller.ControllerDefinition;

/**
 * Represents the view that the resolver will dispatch. View holds:
 * <ol>
 * <li>the path of the view</li>
 * <li>the controller definition</li>
 * </ol>
 * which allows for more granular decisions on how to handle a View.
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class View {
    private final String path;
    private final ControllerDefinition controllerDefinition;

    /**
     * Creates a View
     * 
     * @param path represents the path of the View to be resolved
     */
    public View(String path) {
        this.path = path;
        this.controllerDefinition = null;
    }
    
    /**
     * Creates a View
     * 
     * @param controllerDefinition the ControllerDefinition where the view originated from
     */
    public View(ControllerDefinition controllerDefinition) {
       this.path = null;
       this.controllerDefinition = controllerDefinition;
    }
   
    /**
     * Returns the view path
     * 
     * @return The View path
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the controller associated to this view
     * 
     * @return The Controller instance
     */
    public Object getController() {
        if ( controllerDefinition != null ){
            return controllerDefinition.getController();            
        }
        return null;
    }
    
    /**
     * Returns the controller definition associated to this view
     * 
     * @return The ControllerDefinition
     */
    public ControllerDefinition getControllerDefinition() {
        return controllerDefinition;
    }

}
