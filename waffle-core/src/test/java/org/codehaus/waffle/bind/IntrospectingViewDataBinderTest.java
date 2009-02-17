package org.codehaus.waffle.bind;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.monitor.SilentMonitor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class IntrospectingViewDataBinderTest {
    private final Mockery context = new Mockery();

    @Test
    public void allControllerPropertiesShouldBeBoundAsRequestAttributes() {
        final HttpServletRequest request = context.mock(HttpServletRequest.class);

        context.checking(new Expectations() {{
            one (request).setAttribute("name", "my controller");
            one (request).getAttribute("name");
            will(returnValue("my controller"));
            one (request).setAttribute("null", null);
            one (request).getAttribute("null");
            will(returnValue(null));
        }});

        IntrospectingViewDataBinder binder = new IntrospectingViewDataBinder(new SilentMonitor());
        binder.bind(request, new SimpleController());
    }

    @Test
    public void shouldNotThrowNullPointerExceptionWhenControllerMissingReadMethodForProperty() {
        final ControllerWithMissingReadMethod controller = new ControllerWithMissingReadMethod();

        IntrospectingViewDataBinder binder = new IntrospectingViewDataBinder(new SilentMonitor());
        binder.bind(null, controller);
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

    class ControllerWithMissingReadMethod {
        public void setName(String name) {
            // do nothing
        }
    }

}
