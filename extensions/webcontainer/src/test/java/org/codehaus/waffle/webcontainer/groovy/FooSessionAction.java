/**
 * 
 */
package org.codehaus.waffle.webcontainer.groovy;

public class FooSessionAction {
    private final FooApplicationAction applicationAction;

    public FooSessionAction(FooApplicationAction applicationAction) {
        this.applicationAction = applicationAction;
    }

    public String getMessage() {
        return applicationAction.toString() + "(DEF)";
    }
}