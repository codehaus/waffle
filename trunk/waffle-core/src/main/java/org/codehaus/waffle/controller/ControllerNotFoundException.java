/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.controller;

import org.codehaus.waffle.WaffleException;

/**
 * Thrown when controller is not found in registrar
 * 
 * @author Mauro Talevi
 */
@SuppressWarnings("serial")
public class ControllerNotFoundException extends WaffleException {
 
    public ControllerNotFoundException(String message) {
        super(message);
    }
 
}
