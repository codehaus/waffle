package org.codehaus.waffle.registrar.pico;

import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoVisitor;

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

        String script =
                "controller = eval(\"#{String.camelize('" + componentKey + "')}.new\")\n" + // instantiate controller
                "controller.extend(Waffle::Controller)"; // mixin Waffle module
        return runtime.evalScript(script);
    }

    public void verify(PicoContainer picoContainer) throws PicoIntrospectionException {
        // do nothing!
    }

    public void accept(PicoVisitor picoVisitor) {
        // do nothing!
    }
}
