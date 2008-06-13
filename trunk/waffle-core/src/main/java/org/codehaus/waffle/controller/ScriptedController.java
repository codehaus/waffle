package org.codehaus.waffle.controller;

/**
 * 
 * @author Mauro Talevi
 */
public interface ScriptedController {

    /**
     * Invoke the method on the script object instance
     * 
     * @return the result from the method invocation.
     */
    Object execute();

    /**
     * Returns the underlying script object
     * 
     * @return The script Object
     */
    Object getScriptObject();

    /**
     * Sets the method name of the controller to be executed
     * 
     * @param methodName the method name
     */
    void setMethodName(String methodName);

}
