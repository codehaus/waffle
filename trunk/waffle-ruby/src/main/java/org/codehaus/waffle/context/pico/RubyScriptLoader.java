/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import javax.servlet.ServletContext;

import org.codehaus.waffle.Startable;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

public class RubyScriptLoader implements Startable {
    public static final String RUBY_SCRIPT_PATH_KEY = "org.codehaus.waffle.ruby.path";
    public static final String DEFAULT_RUBY_SCRIPT_PATH = "/WEB-INF/classes/ruby/";

    private final ServletContext servletContext;
    private final Ruby runtime;
    private final String rubyScriptPath;

    public RubyScriptLoader(ServletContext servletContext, Ruby runtime) {
        this.servletContext = servletContext;
        this.runtime = runtime;
        this.rubyScriptPath = getScriptPath(servletContext);
    }

    private String getScriptPath(ServletContext servletContext) {
        String path = servletContext.getInitParameter(RUBY_SCRIPT_PATH_KEY);
        if (path == null) {
            return DEFAULT_RUBY_SCRIPT_PATH;
        }
        return path;
    }

    public void start() {
        runtime.getClassFromPath("Waffle::ScriptLoader").callMethod(
                runtime.getCurrentContext(),
                "load_all",
                new IRubyObject[] { JavaEmbedUtils.javaToRuby(runtime, rubyScriptPath),
                        JavaEmbedUtils.javaToRuby(runtime, servletContext) });
    }

    public void stop() {
        // does nothing
    }

}
