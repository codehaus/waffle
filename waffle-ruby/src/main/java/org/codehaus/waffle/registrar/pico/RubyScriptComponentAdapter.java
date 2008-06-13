package org.codehaus.waffle.registrar.pico;

import java.lang.reflect.Type;

import org.codehaus.waffle.controller.RubyController;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoCompositionException;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;

/**
 * This ComponentAdapter implementation is needed to correctly instantiate a Ruby script for use in Waffle.
 *
 * @author Michael Ward
 */
public class RubyScriptComponentAdapter implements ComponentAdapter {
    private final Object componentKey;
    private final String rubyClassName;

    public RubyScriptComponentAdapter(Object componentKey, String rubyClassName) {
        this.componentKey = componentKey;
        this.rubyClassName = rubyClassName;
    }

    public ComponentAdapter getDelegate() {
        return null;
    }

    public Object getComponentKey() {
        return componentKey;
    }

    public Class getComponentImplementation() {
        return IRubyObject.class;
    }

    public Object getComponentInstance(PicoContainer picoContainer, Type type) throws PicoCompositionException {
        Ruby runtime = picoContainer.getComponent(Ruby.class);

        String script =
                "controller = " + rubyClassName + ".new\n" + // instantiate controller
                "controller.extend(Waffle::Controller)\n" + // mixin Waffle module
                "controller.extend(ERB::Util)"; // mixin ERB::Util
        IRubyObject controller = runtime.evalScript(script);

        // inject pico container
        controller.callMethod(runtime.getCurrentContext(), "__pico_container=", JavaEmbedUtils.javaToRuby(runtime, picoContainer));

        return new RubyController(controller);
    }

    public Object getComponentInstance(PicoContainer picoContainer) throws PicoCompositionException {
        return this.getComponentInstance(picoContainer, null);
    }

    public ComponentAdapter findAdapterOfType(Class type) {
        return null;
    }

    public void verify(PicoContainer picoContainer) {
        // do nothing!
    }

    public void accept(PicoVisitor picoVisitor) {
        // do nothing!
    }

    public String getDescriptor() {
        return "RubyScriptComponentAdapter";
    }
}
