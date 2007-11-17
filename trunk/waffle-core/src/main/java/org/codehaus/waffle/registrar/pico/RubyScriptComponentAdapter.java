package org.codehaus.waffle.registrar.pico;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoIntrospectionException;
import org.picocontainer.PicoVisitor;

public class RubyScriptComponentAdapter implements ComponentAdapter {
    private final Object componentKey;
    private final String rubyClassName;

    public RubyScriptComponentAdapter(Object componentKey, String rubyClassName) {
        this.componentKey = componentKey;
        this.rubyClassName = rubyClassName;
    }

    public Object getComponentKey() {
        return componentKey;
    }

    public Class getComponentImplementation() {
        return IRubyObject.class;
    }

    public Object getComponentInstance(PicoContainer picoContainer) throws PicoInitializationException, PicoIntrospectionException {
        Ruby runtime = (Ruby) picoContainer.getComponentInstance(Ruby.class);

        String script =
                "controller = " + rubyClassName + ".new\n" + // instantiate controller
                "controller.extend(Waffle::Controller)\n" + // mixin Waffle module
                "controller.extend(ERB::Util)"; // mixin ERB::Util
        IRubyObject controller = runtime.evalScript(script);

        // inject pico container
        controller.callMethod(runtime.getCurrentContext(), "__pico_container=", JavaEmbedUtils.javaToRuby(runtime, picoContainer));

        return controller;
    }

    public void verify(PicoContainer picoContainer) throws PicoIntrospectionException {
        // do nothing!
    }

    public void accept(PicoVisitor picoVisitor) {
        // do nothing!
    }
}
