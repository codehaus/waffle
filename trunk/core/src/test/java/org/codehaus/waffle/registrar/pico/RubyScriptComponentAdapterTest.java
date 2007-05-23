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
        ComponentAdapter componentAdapter = new RubyScriptComponentAdapter("Foobar", "'ruby script'");
        assertEquals("Foobar", componentAdapter.getComponentKey());
        assertEquals(IRubyObject.class, componentAdapter.getComponentImplementation());
    }

    public void testGetComponentInstance() {
        Ruby runtime = Ruby.getDefaultInstance();
        runtime.evalScript("$my_global = 'Waffle'\n");

        String script =
                "class Foobar\n" +
                "  def execute\n" +
                "    \"JRuby and #{$my_global}\"\n" +
                "  end\n" +
                "end";

        ComponentAdapter componentAdapter = new RubyScriptComponentAdapter("Foobar", script);
        MutablePicoContainer picoContainer = new DefaultPicoContainer();
        picoContainer.registerComponentInstance(Ruby.class, runtime);

        IRubyObject instance = (IRubyObject) componentAdapter.getComponentInstance(picoContainer);

        // call a method on the ruby instance ... enuring it was instantiated and that the runtime was set
        IRubyObject response = instance.callMethod(runtime.getCurrentContext(), "execute");
        assertEquals("JRuby and Waffle", JavaUtil.convertRubyToJava(response));
    }
}
