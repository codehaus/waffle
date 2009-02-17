package org.codehaus.waffle.testmodel;

import java.util.Properties;

import org.codehaus.waffle.view.View;
import org.codehaus.waffle.view.ViewResolver;

public class StubViewResolver implements ViewResolver {
    public String resolve(View view) {
        return null;
    }

    public void configureViews(Properties viewProperties) {
    }

    public void configureView(String key, String value) {
        
    }
}
