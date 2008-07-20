package org.codehaus.waffle.example.jruby;

import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyModule;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.builtin.InstanceVariables;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class RubyRegistrarTest {

//    Need to create an interface that sets the PicoContainer instance onto the Ruby Controller
//
//    WaffleRubyControllerProxy.__setWaffleContext(...PicoContainer...)
//
//    An associated module will be mixed in with the controller
//
//    module Waffle
//        module Controller
//           def __waffle_context=(waffle_context)
//                @@__waffle_context = waffle_context
//           end
//
//           def method_missing
//                check the waffle context ...
//           end
//
//           .. params, request, response
//        end
//
//    end
//
//    class FoobarController
//
//        def index
//
//        end
//
//    end


    // sandbox test for figuring out JRuby
    @Test
    public void canEvalRubyScript() {
        Ruby runtime = Ruby.newInstance();

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

        runtime.evalScriptlet(script);

        IRubyObject foo = runtime.evalScriptlet("foo = Foo.new()");
        foo.callMethod(runtime.getCurrentContext(), "bar");
        IRubyObject salmon = runtime.evalScriptlet("foo.salmon");

        RubyArray rubyArray = (RubyArray) runtime.evalScriptlet("foo.instance_variables");
        assertEquals("@salmon", rubyArray.get(0));

        InstanceVariables instanceVariables = foo.getInstanceVariables();
        assertEquals(JavaUtil.convertJavaToRuby(runtime, "fish"), instanceVariables.getInstanceVariable("@salmon"));

        assertEquals("HELLO fish!", runtime.evalScriptlet("foo.bar").toString());
        foo.callMethod(runtime.getCurrentContext(), "salmon=", JavaUtil.convertJavaToRuby(runtime, "shark"));
        assertEquals("HELLO shark!", runtime.evalScriptlet("foo.bar").toString());
        
        script =
                "class Foo\n" +
                "  def bar\n" +
                "    \"GOODBYE\"\n" +
                "  end\n"+
                "end";
        runtime.evalScriptlet(script);
        assertEquals("GOODBYE", runtime.evalScriptlet("foo.bar").toString());

        String module =
                "module Calculator\n" +
                "def one\n" +
                "    \"xxxx #{@salmon}\"\n" +
                "end\n" +
                "end\n" +
                "\n" +
                "Calculator # return the created Module";

        IRubyObject the_module = runtime.evalScriptlet(module);
        foo.callMethod(runtime.getCurrentContext(), "extend", the_module);
        runtime.evalScriptlet("p foo.one");

        Object javaX = JavaUtil.convertRubyToJava(rubyArray);
        Object javaObj = JavaUtil.convertRubyToJava(salmon);

        System.out.println("x = " + javaX);
        System.out.println("javaObj = " + javaObj);
    }

    @Test
    public void canExposeFieldsAsInstanceVariables() {
        Ruby runtime = Ruby.newInstance();

        String script =
                "require 'erb'\n" +
                "require 'java'\n" +
                "include_class java.util.ArrayList\n" +
                "f = ArrayList.new\n" +
                "f.instance_variable_get(:@java_object)";

        IRubyObject rubyObject = runtime.evalScriptlet(script);

        System.out.println("rubyObject = " + rubyObject);

        RubyModule erb = runtime.getClassFromPath("ERB");
        System.out.println("erb = " + erb);

        IRubyObject iRubyObject = (IRubyObject) JavaEmbedUtils.invokeMethod(runtime, erb, "new",
                new Object[] {JavaEmbedUtils.javaToRuby(runtime, "<html>foobar  <%= @java_object %></html>")}, IRubyObject.class);

        String o = (String)JavaEmbedUtils.invokeMethod(runtime, iRubyObject, "result", new Object[] {runtime.evalScriptlet("f.send :binding")}, String.class);
        System.out.println("o = " + o);
    }

    @Test
    public void testEvalRubyErb() {
       String script =
               "require \"erb\"\n" +
               "\n" +
               "  # Build template data class.\n" +
               "  class Product\n" +
               "    def initialize( code, name, desc, cost)\n" +
               "      @code = code\n" +
               "      @name = name\n" +
               "      @desc = desc\n" +
               "      @cost = cost\n" +
               "\n" +
               "      @features = [ ]\n" +
               "    end\n" +
               "\n" +
               "    def add_feature( feature )\n" +
               "      @features << feature\n" +
               "    end\n" +
               "\n" +
               "    # ...\n" +
               "  end\n" +
               "template = %{\n" +
               "    <html>\n" +
               "      <head><title>Ruby Toys -- <%= @name %></title></head>\n" +
               "      <body>\n" +
               "\n" +
               "        <h1><%= @name %> (<%= @code %>)</h1>\n" +
               "        <p><%= @desc %></p>\n" +
               "\n" +
               "        <ul>\n" +
               "          <% @features.each do |f| %>\n" +
               "            <li><b><%= f %></b></li>\n" +
               "          <% end %>\n" +
               "        </ul>\n" +
               "\n" +
               "        <p>\n" +
               "          <% if @cost < 10 %>\n" +
               "            <b>Only <%= @cost %>!!!</b>\n" +
               "          <% else %>\n" +
               "             Call for a price, today!\n" +
               "          <% end %>\n" +
               "        </p>\n" +
               "\n" +
               "      </body>\n" +
               "    </html>\n" +
               "  }.gsub(/^  /, '')\n" +
               "\n" +
               "rhtml = ERB.new(template)\n" +
               "# Set up template data.\n" +
               "  toy = Product.new( \"TZ-1002\",\n" +
               "                     \"Rubysapien\",\n" +
               "                     \"Geek's Best Friend!  Responds to Ruby commands...\",\n" +
               "                     999.95 )\n" +
               "  toy.add_feature(\"Listens for verbal commands in the Ruby language!\")\n" +
               "  toy.add_feature(\"Ignores Perl, Java, and all C variants.\")\n" +
               "  toy.add_feature(\"Karate-Chop Action!!!\")\n" +
               "  toy.add_feature(\"Matz signature on left leg.\")\n" +
               "  toy.add_feature(\"Gem studded eyes... Rubies, of course!\")\n" +
               "\n" +
               "  # Produce result.\n" +
               "rhtml.run(toy.send(:binding))";

        Ruby runtime = Ruby.newInstance();
        runtime.evalScriptlet(script);
    }
}
