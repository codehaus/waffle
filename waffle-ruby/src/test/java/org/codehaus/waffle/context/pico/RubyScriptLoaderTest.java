package org.codehaus.waffle.context.pico;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.ServletContext;

@RunWith(JMock.class)
public class RubyScriptLoaderTest {
    private final Mockery context = new Mockery();

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

        Ruby runtime = Ruby.newInstance();
        runtime.evalScriptlet(script);

        RubyScriptLoader loader = new RubyScriptLoader(servletContext, runtime);
        loader.start();

        // Ensure Waffle::ScriptLoader.load_all was called
        assertEquals("/WEB-INF/classes/ruby/", JavaUtil.convertRubyToJava(runtime.evalScriptlet("$arg1")));
        assertNotNull(runtime.evalScriptlet("$arg2"));
    }
}
