package org.codehaus.waffle.testmodel;

import java.util.List;

public class FakeBean {
    private int count;
    private List<?> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
