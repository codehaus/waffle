package org.codehaus.waffle.example.jruby;

import junit.framework.TestCase;
import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.Map;

public class RubyRegistrarTest extends TestCase {

    /*

    Need to create an interface that sets the PicoContainer instance onto the Ruby Controller

    WaffleRubyControllerProxy.__setWaffleContext(...PicoContainer...)

    An associated module will be mixed in with the controller

    module Waffle
        module Controller
           def __waffle_context=(waffle_context)
                @@__waffle_context = waffle_context
           end

           def method_missing
                check the waffle context ...
           end

           .. params, request, response
        end

    end

    class FoobarController

        def index

        end

    end

     */

    // sandbox test for figuring out JRuby
    public void testJRuby() {
        Ruby runtime = Ruby.getDefaultInstance();

        String script =
                "class Foo\n" +
                "  attr_accessor :salmon\n" +
                "  def initialize\n" +
                "    @salmon = 'fish'\n" +
                "  end\n" +
                "  def bar\n" +
                "    return \"HELLO #{salmon}!\"\n" +
                "  end\n" +
                "end\n";

        runtime.evalScript(script);

        IRubyObject foo = runtime.evalScript("foo = Foo.new()");
        foo.callMethod(runtime.getCurrentContext(), "bar");
        IRubyObject salmon = runtime.evalScript("foo.salmon");

        RubyArray rubyArray = (RubyArray) runtime.evalScript("foo.instance_variables");
        assertEquals("@salmon", rubyArray.get(0));

        Map iVars = foo.getInstanceVariables();
        assertEquals(JavaUtil.convertJavaToRuby(runtime, "fish"), iVars.get("@salmon"));

        assertEquals("HELLO fish!", runtime.evalScript("foo.bar").toString());
        foo.callMethod(runtime.getCurrentContext(), "salmon=", JavaUtil.convertJavaToRuby(runtime, "shark"));
        assertEquals("HELLO shark!", runtime.evalScript("foo.bar").toString());
        
        script =
                "class Foo\n" +
                "  def bar\n" +
                "    \"GOODBYE\"\n" +
                "  end\n"+
                "end";
        runtime.evalScript(script);
        assertEquals("GOODBYE", runtime.evalScript("foo.bar").toString());

        String module =
                "module Calculator\n" +
                "def one\n" +
                "    \"xxxx #{@salmon}\"\n" +
                "end\n" +
                "end\n" +
                "\n" +
                "Calculator # return the created Module";

        IRubyObject the_module = runtime.evalScript(module);
        foo.callMethod(runtime.getCurrentContext(), "extend", the_module);
        runtime.evalScript("p foo.one");

        Object javaX = JavaUtil.convertRubyToJava(rubyArray);
        Object javaObj = JavaUtil.convertRubyToJava(salmon);

        System.out.println("x = " + javaX);
        System.out.println("javaObj = " + javaObj);
    }


}
