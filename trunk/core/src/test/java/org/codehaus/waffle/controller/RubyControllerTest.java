package org.codehaus.waffle.controller;

import org.codehaus.waffle.action.ActionMethodInvocationException;
import org.jmock.MockObjectTestCase;
import org.jruby.Ruby;

public class RubyControllerTest extends MockObjectTestCase {

    public void testExecute() {
        String script =
                "class Foo\n" +
                "  def my_method\n" +
                "    'Hello World'\n" +
                "  end\n" +
                "end\n";

        Ruby runtime = Ruby.getDefaultInstance();
        runtime.evalScript(script);

        RubyController rubyController = new RubyController(runtime.evalScript("Foo.new"));
        rubyController.setMethodName("my_method");
        assertEquals("Hello World", rubyController.execute());
    }

    /*public void testOnlyPublicRubyMethodsShouldBeExecutable() {
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

        Ruby runtime = Ruby.getDefaultInstance();
        runtime.evalScript(script);

        RubyController rubyController = new RubyController(runtime.evalScript("Foo.new"));

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

    }*/
}
