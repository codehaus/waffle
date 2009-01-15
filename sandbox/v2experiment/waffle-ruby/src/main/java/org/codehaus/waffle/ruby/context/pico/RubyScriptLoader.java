/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.ruby.context.pico;

import javax.servlet.ServletContext;

import org.codehaus.waffle.Startable;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * Loads ruby scripts at startup. The script path can be defined either explicitly, via the
 * {@link #RUBY_SCRIPT_PATH_KEY} context param key, or via an env variable using the {@link #RUBY_SCRIPT_ENV_KEY}
 * context param key.  The {@link #RUBY_SCRIPT_PATH_KEY} takes precendence over the {@link #RUBY_SCRIPT_ENV_KEY}.
 * 
 * @author Micheal Ward
 * @author Mauro Talevi
 */
public class RubyScriptLoader implements Startable {
    public static final String DEFAULT_RUBY_SCRIPT_PATH = "/WEB-INF/classes/";
    public static final String RUBY_SCRIPT_PATH_KEY = "org.codehaus.waffle.ruby.path";
    public static final String RUBY_SCRIPT_ENV_KEY = "org.codehaus.waffle.ruby.env";

    private final ServletContext servletContext;
    private final Ruby runtime;
    private final String rubyScriptPath;

    public RubyScriptLoader(ServletContext servletContext, Ruby runtime) {
        this.servletContext = servletContext;
        this.runtime = runtime;
        this.rubyScriptPath = getScriptPath(servletContext);
    }

    String getScriptPath(ServletContext servletContext) {
        String path = servletContext.getInitParameter(RUBY_SCRIPT_PATH_KEY);
        if (path == null) {
            // path not defined, look for env
            String env = getScriptEnv(servletContext);
            if (env == null) {
                // env not defined, return default
                return DEFAULT_RUBY_SCRIPT_PATH;
            }
            path = env;
        }
        return path;
    }

    private String getScriptEnv(ServletContext servletContext) {
        String name = servletContext.getInitParameter(RUBY_SCRIPT_ENV_KEY);
        if ( name == null ){
            return null;
        }
        return System.getenv(name);
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
