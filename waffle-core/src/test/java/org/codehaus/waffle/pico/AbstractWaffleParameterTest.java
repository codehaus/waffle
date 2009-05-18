package org.codehaus.waffle.pico;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoVisitor;
import org.picocontainer.NameBinding;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class AbstractWaffleParameterTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canAcceptParameter() {
        final AbstractWaffleParameter parameter = new AbstractWaffleParameter("foobar") {

            @SuppressWarnings("unchecked")
            public Resolver resolve(PicoContainer picoContainer, ComponentAdapter<?> forAdapter,
                    ComponentAdapter<?> injecteeAdapter, Type expectedType, NameBinding expectedNameBinding,
                    boolean useNames, Annotation binding) {
                throw new UnsupportedOperationException("don't call");
            }
        };

        // Mock PicoVisitor
        final PicoVisitor visitor = mockery.mock(PicoVisitor.class);
        mockery.checking(new Expectations() {
            {
                one(visitor).visitParameter(with(same(parameter)));
            }
        });
        parameter.accept(visitor);
    }
}
