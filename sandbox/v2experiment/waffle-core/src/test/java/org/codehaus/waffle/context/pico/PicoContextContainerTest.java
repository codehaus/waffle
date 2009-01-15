package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.ContextContainer;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoException;

/**
 * @author Michael Ward
 */
@RunWith(JMock.class)
public class PicoContextContainerTest {
    private Mockery mockery = new Mockery();

    @SuppressWarnings("serial")
    @Test(expected = WaffleException.class)
    public void canWrapPicoContainerExceptionsAsWaffleExceptions() {
        final MutablePicoContainer delegate = mockery.mock(MutablePicoContainer.class);
        mockery.checking(new Expectations() {
            {
                one(delegate).getComponent("foo");
                will(throwException(new PicoException("mock") {
                }));
            }
        });

        ContextContainer container = new ContextContainer(delegate);
        container.getComponent("foo");
    }
}
