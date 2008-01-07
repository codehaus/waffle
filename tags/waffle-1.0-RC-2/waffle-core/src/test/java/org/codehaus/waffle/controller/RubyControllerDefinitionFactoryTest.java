package org.codehaus.waffle.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class RubyControllerDefinitionFactoryTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canHandleIRubyObject() {
        RubyControllerDefinitionFactory factory = new RubyControllerDefinitionFactory(null, null, null, new SilentMonitor());

        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                IRubyObject rubyObject = JavaUtil.convertJavaToRuby(Ruby.getDefaultInstance(), "Hello From Ruby");
                one(contextContainer).getComponentInstance("foobar");
                will(returnValue(rubyObject));
            }
        });
        RequestLevelContainer.set(contextContainer);

        Object controller = factory.findController("foobar", null);
        assertTrue(controller instanceof RubyController);
    }

    @Test
    public void canHandleNonRubyObjects() {
        RubyControllerDefinitionFactory factory = new RubyControllerDefinitionFactory(null, null, null, new SilentMonitor());

        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponentInstance("foobar");
                will(returnValue("Pojo"));
            }
        });
        RequestLevelContainer.set(contextContainer);

        Object controller = factory.findController("foobar", null);
        assertFalse(controller instanceof RubyController);
        assertEquals("Pojo", controller);
    }
}
