package org.codehaus.waffle.testmodel;

public class ConstructorInjectionComponent {
    private final FakeBean fakeBean;

    public ConstructorInjectionComponent(FakeBean fakeBean) {
        this.fakeBean = fakeBean;
    }

    public FakeBean getFakeBean() {
        return fakeBean;
    }
}
