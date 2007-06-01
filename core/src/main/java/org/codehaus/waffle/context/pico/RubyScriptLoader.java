package org.codehaus.waffle.context.pico;

import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.javasupport.JavaEmbedUtils;
import org.picocontainer.Startable;

import javax.servlet.ServletContext;

public class RubyScriptLoader implements Startable {
    public static final String DEFAULT_RUBY_SCRIPT_PATH = "/WEB-INF/classes/ruby/";

    private final ServletContext servletContext;
    private final Ruby runtime;

    public RubyScriptLoader(ServletContext servletContext, Ruby runtime) {
        this.servletContext = servletContext;
        this.runtime = runtime;
    }

    public void start() {
        RubyModule rubyModule = runtime.getClassFromPath("Waffle::ScriptLoader");
        rubyModule.callMethod(runtime.getCurrentContext(), "servlet_context=", JavaEmbedUtils.javaToRuby(runtime, servletContext));
        rubyModule.callMethod(runtime.getCurrentContext(), "load_all");
    }

    public void stop() {
        // does nothing
    }

}
