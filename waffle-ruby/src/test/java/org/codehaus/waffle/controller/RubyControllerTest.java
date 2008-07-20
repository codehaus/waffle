package org.codehaus.waffle.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.jruby.Ruby;
import org.junit.Test;

/**
 * 
 * @author Michael Ward
 */
public class RubyControllerTest  {

    @Test
    public void canExecute() {
        String script =
                "class Foo\n" +
                "  def my_method\n" +
                "    'Hello World'\n" +
                "  end\n" +
                "end\n";

        Ruby runtime = Ruby.newInstance();
        runtime.evalScriptlet(script);

        ScriptedController rubyController = new RubyController(runtime.evalScriptlet("Foo.new"));
        rubyController.setMethodName("my_method");
        assertEquals("Hello World", rubyController.execute());
    }

    //@Test
    public void onlyPublicRubyMethodsShouldBeExecutable() {
        String script =
                "class Foo\n" +
                "  protected\n" +
                "  def one\n" +
                "    'ONE'\n" +
                "  end\n" +
                "  private\n" +
                "  def two\n" +
                "    'TWO'\n" +
                "  end\n" +
                "end\n";

        Ruby runtime = Ruby.newInstance();
        runtime.evalScriptlet(script);

        ScriptedController rubyController = new RubyController(runtime.evalScriptlet("Foo.new"));

        try {
            rubyController.setMethodName("one");
            rubyController.execute();
            fail("calling a protected method should throw MethodInvocationException");
        } catch (ActionMethodInvocationException expected) {
            // expected
        }

        try {
            rubyController.setMethodName("two");
            rubyController.execute();
            fail("calling a private method should throw MethodInvocationException");
        } catch (ActionMethodInvocationException expected) {
            // expected
        }

    }
}
