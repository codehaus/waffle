package org.codehaus.waffle.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.ruby.controller.RubyController;
import org.codehaus.waffle.ruby.controller.RubyControllerDefinitionFactory;
import org.codehaus.waffle.ComponentFinder;
import org.codehaus.waffle.pico.PicoComponentFinder;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;

/**
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class RubyControllerDefinitionFactoryTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canHandleIRubyObject() {
        ScriptedControllerDefinitionFactory factory = new RubyControllerDefinitionFactory(null, null, null,
                new SilentMonitor(), new DefaultMessageResources());

        final MutablePicoContainer contextContainer = mockery.mock(MutablePicoContainer.class);
        mockery.checking(new Expectations() {
            {
                IRubyObject rubyObject = JavaUtil.convertJavaToRuby(Ruby.newInstance(), "Hello From Ruby");
                one(contextContainer).getComponent("foobar");
                will(returnValue(new RubyController(rubyObject)));
            }
        });

        ComponentFinder componentFinder = new PicoComponentFinder(contextContainer);
        Object controller = factory.findController("foobar", null, componentFinder);
        assertTrue(controller instanceof RubyController);
    }

    @Test
    public void canHandleNonRubyObjects() {
        ScriptedControllerDefinitionFactory factory = new RubyControllerDefinitionFactory(null, null, null,
                new SilentMonitor(), new DefaultMessageResources());

        final MutablePicoContainer contextContainer = mockery.mock(MutablePicoContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponent("foobar");
                will(returnValue("Pojo"));
            }
        });
        ComponentFinder componentFinder = new PicoComponentFinder(contextContainer);

        Object controller = factory.findController("foobar", null, componentFinder);
        assertFalse(controller instanceof RubyController);
        assertEquals("Pojo", controller);
    }

}
