package org.codehaus.waffle.registrar.pico;

import static org.junit.Assert.assertEquals;

import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.behaviors.Caching;

public class RubyScriptComponentAdapterTest {

    @Test
    public void testComponentKeyAndImplementationMethods() {
        ComponentAdapter<?> componentAdapter = new RubyScriptComponentAdapter("foobar", "'ruby script'");
        assertEquals("foobar", componentAdapter.getComponentKey());
        assertEquals(IRubyObject.class, componentAdapter.getComponentImplementation());
    }

    public void testGetComponentInstance() {
        Ruby runtime = Ruby.getCurrentInstance();
        runtime.evalScriptlet("$my_global = 'Waffle'\n");

        String script =
                "require 'erb'\n" +
                "module Waffle\n" +
                "  module Controller\n" +
                "   def __pico_container=(pico)\n" +
                "   end\n" +
                "  end\n" +
                "end\n" +
                "class FooBar\n" +
                "  def execute\n" +
                "    h(\"JRuby & #{$my_global}\")\n" + // Ensuring ERB::Util has been mixed in
                "  end\n" +
                "end";
        runtime.evalScriptlet(script);

        ComponentAdapter<?> componentAdapter = new RubyScriptComponentAdapter("foo_bar", "FooBar");
        MutablePicoContainer picoContainer = new DefaultPicoContainer(new Caching());
        picoContainer.addComponent(Ruby.class, runtime);

        IRubyObject instance = (IRubyObject) componentAdapter.getComponentInstance(picoContainer, IRubyObject.class);

        // call a method on the ruby instance ... enuring it was instantiated and that the runtime was set
        IRubyObject response = instance.callMethod(runtime.getCurrentContext(), "execute");
        assertEquals("JRuby &amp; Waffle", JavaUtil.convertRubyToJava(response));

    }
}
