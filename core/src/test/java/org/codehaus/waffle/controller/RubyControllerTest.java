package org.codehaus.waffle.controller;

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
}
