package org.codehaus.waffle.context.pico;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.picocontainer.Startable;

import javax.servlet.ServletContext;
import java.util.Set;

public class RubyScriptLoader implements Startable {
    private final ServletContext servletContext;
    private final Ruby runtime;

    public RubyScriptLoader(ServletContext servletContext, Ruby runtime) {
        this.servletContext = servletContext;
        this.runtime = runtime;
    }

    public void start() {
        String path = "/WEB-INF/classes/ruby/";
        // noinspection unchecked
        Set<String> resourcePaths = servletContext.getResourcePaths(path); // todo should be able to override ruby location through a key in the web.xml

        runtime.getClassFromPath("Waffle::ScriptLoader")
                .callMethod(runtime.getCurrentContext(), "load_all",
                        new IRubyObject[]{
                                JavaEmbedUtils.javaToRuby(runtime, path),
                                JavaEmbedUtils.javaToRuby(runtime, resourcePaths)
                        });
    }

    public void stop() {
        // does nothing
    }

}
