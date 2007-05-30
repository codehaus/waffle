package org.codehaus.waffle.registrar.pico;

import org.jmock.MockObjectTestCase;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;

public class RubyScriptComponentAdapterTest extends MockObjectTestCase {

    public void testComponentKeyAndImplementationMethods() {
        ComponentAdapter componentAdapter = new RubyScriptComponentAdapter("foobar", "'ruby script'");
        assertEquals("foobar", componentAdapter.getComponentKey());
        assertEquals(IRubyObject.class, componentAdapter.getComponentImplementation());
    }

    public void testGetComponentInstance() {
        Ruby runtime = Ruby.getDefaultInstance();
        runtime.evalScript("$my_global = 'Waffle'\n");

        String script =
                "module Waffle\n" +
                "  module Controller\n" +
                "   def __pico_container=(pico)\n" +
                "   end\n" +
                "  end\n" +
                "end\n" +
                "class FooBar\n" +
                "  def execute\n" +
                "    \"JRuby and #{$my_global}\"\n" +
                "  end\n" +
                "end";
        runtime.evalScript(script);

        ComponentAdapter componentAdapter = new RubyScriptComponentAdapter("foo_bar", "FooBar");
        MutablePicoContainer picoContainer = new DefaultPicoContainer();
        picoContainer.registerComponentInstance(Ruby.class, runtime);

        IRubyObject instance = (IRubyObject) componentAdapter.getComponentInstance(picoContainer);

        // call a method on the ruby instance ... enuring it was instantiated and that the runtime was set
        IRubyObject response = instance.callMethod(runtime.getCurrentContext(), "execute");
        assertEquals("JRuby and Waffle", JavaUtil.convertRubyToJava(response));
    }
}
