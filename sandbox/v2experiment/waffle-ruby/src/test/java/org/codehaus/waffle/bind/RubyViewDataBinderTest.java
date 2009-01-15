package org.codehaus.waffle.bind;

import org.codehaus.waffle.ruby.controller.RubyController;
import org.codehaus.waffle.ruby.bind.RubyViewDataBinder;
import org.codehaus.waffle.controller.ScriptedController;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.servlet.http.HttpServletRequest;

@RunWith(JMock.class)
public class RubyViewDataBinderTest {
    private final Mockery context = new Mockery();

    @Test
    public void rubyInstanceVariablesShouldBeBound() {
        String rubyScript =
                "class Foo\n" +
                "  def initialize\n" +
                "    @name = 'my_name'\n" +
                "    @number = 1985\n" +
                "  end\n" +
                "end\n" +
                "Foo.new";

        Ruby runtime = Ruby.newInstance();
        final IRubyObject scriptObject = runtime.evalScriptlet(rubyScript);
        ScriptedController controller = new RubyController(scriptObject);

        final HttpServletRequest request = context.mock(HttpServletRequest.class);

        context.checking(new Expectations() {{
            one (request).setAttribute("name", "my_name");
            one (request).setAttribute("number", 1985L);
        }});

        ScriptedViewDataBinder binder = new RubyViewDataBinder(new SilentMonitor());
        binder.bind(request, controller);
    }

}
