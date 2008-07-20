package org.codehaus.waffle.example.jruby;

import org.jruby.Ruby;
import org.codehaus.waffle.Startable;

/**
 * This is a temporary way of allowing scripts to be reloaded ... helpful for development only!
 *
 * TODO move this up and allow it to be auto registered depending on servlet context init params
 */
public class RubyScriptReloader implements Startable {
    private final Ruby runtime;

    public RubyScriptReloader(Ruby runtime) {
        this.runtime = runtime;
    }

    public void start() {
        runtime.evalScriptlet("Waffle::ScriptLoader.load_from_file_system");
    }

    public void stop() {
    }
}
