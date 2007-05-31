package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.i18n.MessageResources;
import org.jruby.Ruby;
import org.picocontainer.MutablePicoContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RubyAwarePicoContextContainerFactory extends PicoContextContainerFactory {

    public RubyAwarePicoContextContainerFactory(MessageResources messageResources) {
        super(messageResources);
    }

    public ContextContainer buildApplicationContextContainer() {
        ContextContainer contextContainer = super.buildApplicationContextContainer();

        Ruby runtime = Ruby.getDefaultInstance();

        loadRubyScriptFromClassLoader("string.rb", runtime);
        loadRubyScriptFromClassLoader("waffle.rb", runtime);

        // Register RubyRuntime at Application level
        MutablePicoContainer picoContainer = (MutablePicoContainer) contextContainer.getDelegate();
        picoContainer.registerComponentInstance(Ruby.class, runtime);
        picoContainer.registerComponentImplementation(RubyScriptLoader.class);

        return contextContainer;
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