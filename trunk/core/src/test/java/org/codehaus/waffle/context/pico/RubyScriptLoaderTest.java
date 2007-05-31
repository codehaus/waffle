package org.codehaus.waffle.context.pico;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

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
            one (servletContext).getResourcePaths("/WEB-INF/classes/ruby/");
            Set<String> paths = new HashSet<String>();
            paths.add("/WEB-INF/classes/ruby/fake_script.rb");
            will(returnValue(paths));
        }});

        String script =
                "module Waffle\n" +
                "  class ScriptLoader\n" +
                "    def ScriptLoader.load_all(*args)\n" +
                "      $arg1 = args[0]\n" +
                "      $arg2 = args[1]\n" +
                "    end\n" +
                "  end\n" +
                "end\n";

        Ruby runtime = Ruby.getDefaultInstance();
        runtime.evalScript(script);

        RubyScriptLoader loader = new RubyScriptLoader(servletContext, runtime);
        loader.start();

        // Ensure Waffle::ScriptLoader.load_all was called
        Assert.assertEquals("/WEB-INF/classes/ruby/", JavaUtil.convertRubyToJava(runtime.evalScript("$arg1")));
        Set<String> expected = new HashSet<String>();
        expected.add("/WEB-INF/classes/ruby/fake_script.rb");
        Assert.assertEquals(expected, JavaUtil.convertRubyToJava(runtime.evalScript("$arg2")));
    }
}
