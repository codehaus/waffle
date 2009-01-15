/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context.pico;

import static org.picocontainer.Characteristics.CACHE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.registrar.Registrar;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.registrar.pico.RubyScriptedRegistrar;
import org.jruby.Ruby;
import org.picocontainer.MutablePicoContainer;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
public class RubyPicoContextContainerFactory extends ContextContainerFactory {

    public RubyPicoContextContainerFactory(MessageResources messageResources, ContextMonitor contextMonitor,
            RegistrarMonitor registrarMonitor, ParameterResolver parameterResolver) {
        super(messageResources, contextMonitor, registrarMonitor, parameterResolver);
    }

    @Override
    public MutablePicoContainer buildApplicationContextContainer() {
        MutablePicoContainer contextContainer = super.buildApplicationContextContainer();
        registerScriptComponents(contextContainer);
        return contextContainer;
    }

    protected void registerScriptComponents(MutablePicoContainer contextContainer) {
        // Register Ruby Runtime at Application level
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

        contextContainer.addComponent(Ruby.class, runtime);
        contextContainer.as(CACHE).addComponent(RubyScriptLoader.class);
    }

    @Override
    public Registrar createRegistrar(MutablePicoContainer contextContainer) { // todo we need tests for this ... can this
                                                                             // be refactored cleaner?
        RegistrarMonitor registrarMonitor = getRegistrarMonitor();
        Registrar registrar = new RubyScriptedRegistrar(contextContainer, getParameterResolver(),
                getPicoLifecycleStrategy(), registrarMonitor, getPicoComponentMonitor(), getMessageResources());
        getContextMonitor().registrarCreated(registrar, registrarMonitor);
        return registrar;
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
