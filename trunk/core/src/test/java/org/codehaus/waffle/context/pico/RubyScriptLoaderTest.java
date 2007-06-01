package org.codehaus.waffle.context.pico;

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

        String script =
                "module Waffle\n" +
                "  class ScriptLoader\n" +
                "    def ScriptLoader.servlet_context=(servlet_context)\n" +
                "      $sc = servlet_context\n" +
                "    end\n" +
                "    def ScriptLoader.load_all(*args)\n" +
                "      $arg1 = 'called'\n" +
                "    end\n" +
                "  end\n" +
                "end\n";

        Ruby runtime = Ruby.getDefaultInstance();
        runtime.evalScript(script);

        RubyScriptLoader loader = new RubyScriptLoader(servletContext, runtime);
        loader.start();

        // Ensure Waffle::ScriptLoader.load_all was called
        Assert.assertEquals("called", JavaUtil.convertRubyToJava(runtime.evalScript("$arg1")));
        Assert.assertEquals(servletContext, JavaUtil.convertRubyToJava(runtime.evalScript("$sc")));
    }
}
