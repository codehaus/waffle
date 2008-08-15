package org.codehaus.waffle.testing;

import static java.util.Arrays.asList;

import java.util.List;

/**
 * @author Mauro Talevi
 */
public class ListController implements Controller {
    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public void process() {
        names = asList("one", "two");
    }

}
