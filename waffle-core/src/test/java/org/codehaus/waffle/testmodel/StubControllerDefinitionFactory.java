package org.codehaus.waffle.testmodel;

import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * NOTE: This class cannot be an inner class because it is registered to Pico in a test and therefor
 * the test would would also have to be registered to pico.
 */
public class StubControllerDefinitionFactory implements ControllerDefinitionFactory {
    
    public ControllerDefinition getControllerDefinition(HttpServletRequest servletRequest, HttpServletResponse response) {
        return null;
    }
}