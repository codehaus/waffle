package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.WaffleException;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.fail;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoException;

/**
 *
 * @author Michael Ward
 */
@RunWith(JMock.class)
public class PicoContextContainerTest {
    private Mockery mockery = new Mockery();

    @SuppressWarnings({"ThrowableInstanceNeverThrown", "serial"})
    @Test
    public void getComponentInstanceShouldWrapAnyPicoException() {
        final MutablePicoContainer mutablePicoContainer = mockery.mock(MutablePicoContainer.class);
        mockery.checking(new Expectations() {
            {
                one(mutablePicoContainer).getComponent("foo");
                will(throwException(new PicoException("fake from test") {}));
            }
        });

        PicoContextContainer picoContextContainer = new PicoContextContainer(mutablePicoContainer);

        try {
            picoContextContainer.getComponentInstance("foo");
            fail("WaffleException expected");
        } catch (WaffleException expected) {
            Assert.assertEquals("Unable to construct component 'foo'.", expected.getMessage());
        }
    }
}
