/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.testmodel;

public class RequestLevelComponent extends AbstractStartable {
    private ApplicationLevelComponent applicationLevelComponent;
    private SessionLevelComponent sessionLevelComponent;

    public RequestLevelComponent(ApplicationLevelComponent applicationLevelComponent,
                                 SessionLevelComponent sessionLevelComponent) {
        this.applicationLevelComponent = applicationLevelComponent;
        this.sessionLevelComponent = sessionLevelComponent;
    }

    public ApplicationLevelComponent getApplicationLevelComponent() {
        return applicationLevelComponent;
    }

    public SessionLevelComponent getSessionLevelComponent() {
        return sessionLevelComponent;
    }
}
