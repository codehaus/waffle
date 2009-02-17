/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.menu;

/**
 * Implemented by any controller that requires a menu
 * 
 * @author Mauro Talevi
 */
public interface MenuAware {

    Menu getMenu();

}
