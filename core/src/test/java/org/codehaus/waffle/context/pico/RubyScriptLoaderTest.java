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

@RunWith(JMock.class)
public class RubyScriptLoaderTest {
    private final Mockery context = new JUnit4Mockery();

    @Test
    public void startShouldFindAllResourcesAndLoadScriptsIntoRubyRuntime() {
        final ServletContext servletContext = context.mock(ServletContext.class);

        context.checking(new Expectations() {{
            one (servletContext).getInitParameter(RubyScriptLoader.RUBY_SCRIPT_PATH_KEY);
            will(returnValue(null));
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
        Assert.assertEquals(servletContext, JavaUtil.convertRubyToJava(runtime.evalScript("$arg2")));
    }
}
