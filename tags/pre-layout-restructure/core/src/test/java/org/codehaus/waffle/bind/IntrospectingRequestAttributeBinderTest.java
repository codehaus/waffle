package org.codehaus.waffle.bind;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.controller.RubyController;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jruby.Ruby;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class IntrospectingRequestAttributeBinderTest {
    private final Mockery context = new Mockery();

    @Test
    public void allControllerPropertiesShouldBeBoundAsRequestAttributes() {
        final HttpServletRequest request = context.mock(HttpServletRequest.class);

        context.checking(new Expectations() {{
            one (request).setAttribute("name", "my controller");
            one (request).setAttribute("null", null);
        }});

        IntrospectingRequestAttributeBinder binder = new IntrospectingRequestAttributeBinder(new SilentMonitor());
        binder.bind(request, new SimpleController());
    }

    class SimpleController {
        private String name = "my controller";

        public String getName() {
            return name;
        }

        public Object getNull() {
            return null;
        }
    }

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

        Ruby runtime = Ruby.getDefaultInstance();
        IRubyObject iRubyObject = runtime.evalScript(rubyScript);
        RubyController controller = new RubyController(iRubyObject);

        final HttpServletRequest request = context.mock(HttpServletRequest.class);

        context.checking(new Expectations() {{
            one (request).setAttribute("name", "my_name");
            one (request).setAttribute("number", 1985L);
        }});

        IntrospectingRequestAttributeBinder binder = new IntrospectingRequestAttributeBinder(new SilentMonitor());
        binder.bind(request, controller);
    }

}
