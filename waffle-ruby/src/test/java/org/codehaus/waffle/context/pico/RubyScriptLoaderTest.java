package org.codehaus.waffle.context.pico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.servlet.ServletContext;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class RubyScriptLoaderTest {
    private final Mockery mockery = new Mockery();

    @Test
    public void canLoadScriptsFromDefaultPathIntoRubyRuntime() {
        assertScriptPath(null, null, RubyScriptLoader.DEFAULT_RUBY_SCRIPT_PATH);
    }

    @Test
    public void canLoadScriptsFromScriptPathIntoRubyRuntime() {
        assertScriptPath("dir:/some/script/path", null, "dir:/some/script/path");
    }

    @Test
    public void canLoadScriptsFromScriptEnvIntoRubyRuntime() {
        String home = System.getenv("HOME");
        assertScriptPath(null, "HOME", "dir:"+home);
    }

    private void assertScriptPath(final String rubyScriptPath, final String rubyScriptEnv, String expectedPath) {

        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {{
            allowing (servletContext).getInitParameter(RubyScriptLoader.RUBY_SCRIPT_PATH_KEY);
            will(returnValue(rubyScriptPath));
            allowing (servletContext).getInitParameter(RubyScriptLoader.RUBY_SCRIPT_ENV_KEY);
            will(returnValue(rubyScriptEnv));
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
        assertEquals(expectedPath, JavaUtil.convertRubyToJava(runtime.evalScriptlet("$arg1")));
        assertNotNull(runtime.evalScriptlet("$arg2"));
    }

}
