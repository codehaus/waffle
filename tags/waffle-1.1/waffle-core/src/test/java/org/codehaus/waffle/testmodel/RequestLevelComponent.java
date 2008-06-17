/*****************************************************************************
 * Copyright (c) 2005-2008 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
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