package org.codehaus.waffle.context.pico;

import org.picocontainer.Startable;
import org.jruby.Ruby;
import org.codehaus.waffle.WaffleException;

import javax.servlet.ServletContext;
import java.util.Set;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class RubyScriptLoader implements Startable {
    private final ServletContext servletContext;
    private final Ruby runtime;

    public RubyScriptLoader(ServletContext servletContext, Ruby runtime) {
        this.servletContext = servletContext;
        this.runtime = runtime;
    }

    public void start() {
        // noinspection unchecked
        Set<String> resourcePaths = servletContext.getResourcePaths("/WEB-INF/classes/ruby"); // todo should be able to override ruby location through a key in the web.xml

        for (String path : resourcePaths) {
            // todo cache path and file create time
            loadRubyScript(path);
        }
    }

    // todo implement a reload which checks files to ensure they are upto date in the runtime

    public void stop() {
        // does nothing
    }

    private void loadRubyScript(String path) {
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;

        try {
            inputStream = servletContext.getResourceAsStream(path);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder script = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                script.append(line).append("\n");
                line = bufferedReader.readLine();
            }

            runtime.evalScript(script.toString());
        } catch (IOException e) {
            throw new WaffleException(e);
        } finally {
            try {
                if(inputStream != null) inputStream.close();
                if(bufferedReader != null) bufferedReader.close();
            } catch (IOException ignore) {
                // ignore
            }
        }

    }
}
