/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

public class SessionLevelComponent extends AbstractStartable {
    private ApplicationLevelComponent applicationLevelComponent;

    public SessionLevelComponent(ApplicationLevelComponent applicationLevelComponent) {
        this.applicationLevelComponent = applicationLevelComponent;
    }

    public ApplicationLevelComponent getApplicationLevelComponent() {
        return applicationLevelComponent;
    }
}
