/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.ruby;

import org.codehaus.waffle.context.WaffleComposer;
import org.codehaus.waffle.bind.ControllerDataBinder;
import org.codehaus.waffle.ruby.bind.ognl.RubyControllerDataBinder;
import org.codehaus.waffle.ruby.controller.RubyControllerDefinitionFactory;
import org.codehaus.waffle.ruby.context.pico.RubyScriptLoader;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.WaffleException;
import org.picocontainer.MutablePicoContainer;
import static org.picocontainer.Characteristics.CACHE;
import org.jruby.Ruby;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class RubyWaffleComposer extends WaffleComposer {

    protected Class<? extends ControllerDataBinder> controllerDataBinder() {
        return RubyControllerDataBinder.class;    
    }

    protected Class<? extends ControllerDefinitionFactory> controllerDefinitionFactory() {
        return RubyControllerDefinitionFactory.class;    
    }

    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        super.composeApplication(picoContainer, servletContext);

        Ruby runtime = Ruby.newInstance();
        runtime.getLoadService().init(new ArrayList<Object>()); // this must be called, else we won't be able to load
                                                                // scripts!!
        loadRubyScriptFromClassLoader("org/codehaus/waffle/waffle_erb.rb", runtime);
        loadRubyScriptFromClassLoader("org/codehaus/waffle/waffle.rb", runtime);

        // I'd prefer to do the following:
        // runtime.evalScript("require 'string'\nrequire 'waffle'"); // load Waffle custom scripts
        //
        // but JRuby fails when web app is reloaded...
        // <script>:1:in `require': JAR entry string.rb not found in ~/jruby-example/exploded/WEB-INF/lib/core.jar
        // (IOError)

        picoContainer.addComponent(Ruby.class, runtime);
        picoContainer.as(CACHE).addComponent(RubyScriptLoader.class);

    }

    private void loadRubyScriptFromClassLoader(String fileName, Ruby runtime) {
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;

        try {
            inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder script = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                script.append(line).append("\n");
                line = bufferedReader.readLine();
            }

            runtime.executeScript(script.toString(), fileName);
        } catch (IOException e) {
            throw new WaffleException(e);
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ignore) {
                // ignore
            }
        }
    }


}
