/*****************************************************************************
 * Copyright (C) 2005,2006 Michael Ward                                      *
 * All rights reserved.                                                      *
 * ------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the BSD      *
 * style license a copy of which has been included with this distribution in *
 * the LICENSE.txt file.                                                     *
 *                                                                           *
 * Original code by: Michael Ward                                            *
 *****************************************************************************/
package com.thoughtworks.waffle.testmodel;

public class SessionLevelComponent extends AbstractStartable {
    private ApplicationLevelComponent applicationLevelComponent;

    public SessionLevelComponent(ApplicationLevelComponent applicationLevelComponent) {
        this.applicationLevelComponent = applicationLevelComponent;
    }

    public ApplicationLevelComponent getApplicationLevelComponent() {
        return applicationLevelComponent;
    }
}
