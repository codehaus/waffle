package org.codehaus.waffle.registrar.pico;

import org.picocontainer.PicoVisitor;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoInitializationException;
import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;

public class RubyScriptComponentAdapter implements ComponentAdapter {
    private Object componentKey;
    private final String rubyScript;

    public RubyScriptComponentAdapter(Object componentKey, String rubyScript) {
        this.componentKey = componentKey;
        this.rubyScript = rubyScript;
    }

    public Object getComponentKey() {
        return componentKey;
    }

    public Class getComponentImplementation() {
        return IRubyObject.class;
    }

    public Object getComponentInstance(PicoContainer picoContainer) throws PicoInitializationException, PicoIntrospectionException {
        Ruby runtime = (Ruby) picoContainer.getComponentInstance(Ruby.class);
        runtime.evalScript(rubyScript);

        // TODO RUBY: mixin custom module!

        return runtime.evalScript(componentKey + ".new"); // TODO RUBY: convert name to Camelize
    }

    public void verify(PicoContainer picoContainer) throws PicoIntrospectionException {
        // do nothing!
    }

    public void accept(PicoVisitor picoVisitor) {
        // do nothing!
    }
}
