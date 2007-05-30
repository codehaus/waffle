package org.codehaus.waffle.context.pico;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jruby.Ruby;

import javax.servlet.ServletContext;
import java.util.HashSet;
import java.util.Set;

@RunWith(JMock.class)
public class RubyScriptLoaderTest {
    private final Mockery context = new JUnit4Mockery();

    @Test
    public void startShouldFindAllResourcesAndLoadScriptsIntoRubyRuntime() {
        final ServletContext servletContext = context.mock(ServletContext.class);

        context.checking(new Expectations() {{
            one (servletContext).getResourcePaths("/WEB-INF/classes/ruby");
            Set<String> paths = new HashSet<String>();
            paths.add("waffle.rb");
            will(returnValue(paths));

            one (servletContext).getResourceAsStream("waffle.rb");
            will(returnValue(this.getClass().getClassLoader().getResourceAsStream("waffle.rb"))); // actually get it
        }});


        Ruby runtime = Ruby.getDefaultInstance();

        RubyScriptLoader loader = new RubyScriptLoader(servletContext, runtime);
        loader.start();
    }
}
