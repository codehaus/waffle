package org.codehaus.waffle.testmodel;

public class SetterInjectionComponent {
    private FakeBean fakeBean;

    public FakeBean getFakeBean() {
        return fakeBean;
    }

    public void setFakeBean(FakeBean fakeBean) {
        this.fakeBean = fakeBean;
    }
}
