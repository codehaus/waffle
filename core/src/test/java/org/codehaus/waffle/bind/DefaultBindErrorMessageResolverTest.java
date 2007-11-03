package org.codehaus.waffle.bind;

import static org.junit.Assert.assertEquals;

import java.util.MissingResourceException;

import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.testmodel.FakeBean;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class DefaultBindErrorMessageResolverTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canResolveFirstCheckForCustomFieldMessage() {
        // Mock MessageResources
        final MessageResources messageResources = mockery.mock(MessageResources.class);
        mockery.checking(new Expectations() {
            {
                one(messageResources).getMessageWithDefault("decimal", "decimal", new Object[] {});
                will(returnValue("my.field.name"));
                one(messageResources).getMessage("my.field.name.bind.error", new Object[] { "bad-value" });
                will(returnValue("My Error Message"));
            }
        });

        BindErrorMessageResolver messageResolver = new DefaultBindErrorMessageResolver(messageResources);
        String message = messageResolver.resolve(new FakeBean(), "decimal", "bad-value");

        assertEquals("My Error Message", message);
    }

    @Test
    public void canResolveToTypeBasedBindErrorMessage() {
        // Mock MessageResources
        final MessageResources messageResources = mockery.mock(MessageResources.class);
        mockery.checking(new Expectations() {
            {
                one(messageResources).getMessageWithDefault("count", "count", new Object[] {});
                will(returnValue("my.field.name"));
                one(messageResources).getMessage("my.field.name.bind.error", new Object[] { "bad-value" });
                will(throwException(new MissingResourceException("fake", "class", "key")));
                one(messageResources).getMessage("number.bind.error", 
                        new Object[] { "my.field.name", "bad-value" });
                will(returnValue("The error message"));
            }
        });
        
        BindErrorMessageResolver messageResolver = new DefaultBindErrorMessageResolver(messageResources);
        String message = messageResolver.resolve(new FakeBean(), "count", "bad-value");
        assertEquals("The error message", message);
    }
    
    @Test
    public void canResolveToDefaultBindErrorMessage() {
     // Mock MessageResources
        final MessageResources messageResources = mockery.mock(MessageResources.class);
        mockery.checking(new Expectations() {
            {
                one(messageResources).getMessageWithDefault("count", "count", new Object[] {});
                will(returnValue("my.field.name"));
                one(messageResources).getMessage("my.field.name.bind.error", new Object[] { "bad-value" });
                will(throwException(new MissingResourceException("fake", "class", "key")));
                one(messageResources).getMessage("number.bind.error", 
                        new Object[] { "my.field.name", "bad-value" });
                will(throwException(new MissingResourceException("fake", "class", "key")));
                one(messageResources).getMessage("default.bind.error",
                        new Object[] { "my.field.name", "bad-value" });
                will(returnValue("The default error message"));
            }
        });

        BindErrorMessageResolver messageResolver = new DefaultBindErrorMessageResolver(messageResources);
        String message = messageResolver.resolve(new FakeBean(), "count", "bad-value");
        assertEquals("The default error message", message);
    }

    @Test
    public void canFindBindErrorMessageKey() {
        DefaultBindErrorMessageResolver messageResolver = new DefaultBindErrorMessageResolver(null);

        // Primitive numbers and their wrappers
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(byte.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(Byte.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(short.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(Short.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(int.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(Integer.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(long.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(Long.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(float.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(Float.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(double.class));
        assertEquals("number.bind.error", messageResolver.findBindErrorMessageKey(Double.class));

        // primitive boolean and its wrapper
        assertEquals("boolean.bind.error", messageResolver.findBindErrorMessageKey(boolean.class));
        assertEquals("boolean.bind.error", messageResolver.findBindErrorMessageKey(Boolean.class));
    }

}
