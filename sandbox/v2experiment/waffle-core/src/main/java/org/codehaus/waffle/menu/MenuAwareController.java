/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.menu;

/**
 * Menu-aware controller that can be used as base class.
 * 
 * @author Mauro Talevi
 */
public class MenuAwareController implements MenuAware {

    private Menu menu;

    public MenuAwareController(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return menu;
    }

}
