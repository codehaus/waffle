package org.codehaus.waffle.context.pico;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.picocontainer.Startable;

import javax.servlet.ServletContext;

public class RubyScriptLoader implements Startable {
    public static final String RUBY_SCRIPT_PATH_KEY = "org.codehaus.waffle.ruby.path";
    public static final String DEFAULT_RUBY_SCRIPT_PATH = "/WEB-INF/classes/ruby/";

    private final ServletContext servletContext;
    private final Ruby runtime;
    private final String rubyScriptPath;

    public RubyScriptLoader(ServletContext servletContext, Ruby runtime) {
        this.servletContext = servletContext;
        this.runtime = runtime;

        String path = servletContext.getInitParameter(RUBY_SCRIPT_PATH_KEY);
        
        if(path == null) {
            rubyScriptPath = DEFAULT_RUBY_SCRIPT_PATH;
        } else {
            rubyScriptPath = path;
        }
    }

    public void start() {
        runtime.getClassFromPath("Waffle::ScriptLoader")
                .callMethod(runtime.getCurrentContext(), "load_all",
                        new IRubyObject[]{
                                JavaEmbedUtils.javaToRuby(runtime, rubyScriptPath),
                                JavaEmbedUtils.javaToRuby(runtime, servletContext)
                        });
    }

    public void stop() {
        // does nothing
    }

}