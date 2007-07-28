package org.codehaus.waffle.controller;

import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

public class RubyControllerDefinitionFactoryTest extends MockObjectTestCase {

    public void testFindControllerHandlesIRubyObject() {
        RubyControllerDefinitionFactory factory = new RubyControllerDefinitionFactory(null, null, null);

        Mock mockContextContainer = mock(ContextContainer.class);
        IRubyObject rubyObject = JavaUtil.convertJavaToRuby(Ruby.getDefaultInstance(), "Hello From Ruby");
        mockContextContainer.expects(once()).method("getComponentInstance").with(eq("foobar")).will(returnValue(rubyObject));
        ContextContainer contextContainer = (ContextContainer) mockContextContainer.proxy();
        RequestLevelContainer.set(contextContainer);

        Object controller = factory.findController("foobar", null);
        assertTrue(controller instanceof RubyController);
    }

    public void testFindControllerHandlesNonRubyObjects() {
        RubyControllerDefinitionFactory factory = new RubyControllerDefinitionFactory(null, null, null);

        Mock mockContextContainer = mock(ContextContainer.class);
        mockContextContainer.expects(once()).method("getComponentInstance").with(eq("foobar")).will(returnValue("Pojo"));
        ContextContainer contextContainer = (ContextContainer) mockContextContainer.proxy();
        RequestLevelContainer.set(contextContainer);

        Object controller = factory.findController("foobar", null);
        assertFalse(controller instanceof RubyController);
        assertEquals("Pojo", controller);
    }
}
