package org.codehaus.waffle.bind.ognl;

import static org.codehaus.waffle.bind.ognl.OgnlBindErrorMessageResolver.DEFAULT_MESSAGE;
import static org.codehaus.waffle.bind.ognl.OgnlBindErrorMessageResolver.DEFAULT_NAME;
import static org.codehaus.waffle.bind.ognl.OgnlBindErrorMessageResolver.NUMBER_NAME;
import static org.codehaus.waffle.bind.ognl.OgnlBindErrorMessageResolver.keyFor;
import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;
import java.util.MissingResourceException;

import org.codehaus.waffle.bind.BindErrorMessageResolver;
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
public class OgnlBindErrorMessageResolverTest {

    private Mockery mockery = new Mockery();
    private MessageResources messageResources  = mockery.mock(MessageResources.class);

    @Test
    public void canResolveFirstCheckForCustomFieldMessage() {
        final String message = "My Error Message";
        mockery.checking(new Expectations() {
            {
                one(messageResources).getMessageWithDefault("decimal", "decimal");
                will(returnValue("my.field.name"));
                one(messageResources).getMessage(keyFor("my.field.name"), "bad-value");
                will(returnValue(message));
            }
        });

        BindErrorMessageResolver messageResolver = new OgnlBindErrorMessageResolver(messageResources);
        assertEquals(message, messageResolver.resolve(new FakeBean(), "decimal", "bad-value"));
    }

    @Test
    public void canResolveToTypeBasedBindErrorMessage() {
        final String message = "The error message";
        mockery.checking(new Expectations() {
            {
                one(messageResources).getMessageWithDefault("count", "count");
                will(returnValue("my.field.name"));
                one(messageResources).getMessage(keyFor("my.field.name"), "bad-value");
                will(throwException(new MissingResourceException("fake", "class", "key")));
                one(messageResources).getMessage(keyFor(NUMBER_NAME),
                        "my.field.name", "bad-value");
                will(returnValue(message));
            }
        });
        
        BindErrorMessageResolver messageResolver = new OgnlBindErrorMessageResolver(messageResources);
        assertEquals(message, messageResolver.resolve(new FakeBean(), "count", "bad-value"));
    }
    
    @Test
    public void canResolveToDefaultBindErrorMessage() {
        final String message = "The default error message";
        mockery.checking(new Expectations() {
            {
                one(messageResources).getMessageWithDefault("count", "count");
                will(returnValue("my.field.name"));
                one(messageResources).getMessage(keyFor("my.field.name"), "bad-value");
                will(throwException(new MissingResourceException("fake", "class", "key")));
                one(messageResources).getMessage(keyFor(NUMBER_NAME), "my.field.name", "bad-value");
                will(throwException(new MissingResourceException("fake", "class", "key")));
                one(messageResources)
                        .getMessageWithDefault(keyFor(DEFAULT_NAME), DEFAULT_MESSAGE, "my.field.name", "bad-value");
                will(returnValue(message));
            }
        });

        BindErrorMessageResolver messageResolver = new OgnlBindErrorMessageResolver(messageResources);
        assertEquals(message, messageResolver.resolve(new FakeBean(), "count", "bad-value"));
    }
    
    @Test
    public void canResolveToDefaultBindErrorMessageWhenKeyIsMissing() {
        final String defaultMessage = MessageFormat.format(DEFAULT_MESSAGE, "my.field.name", "bad-value");
        mockery.checking(new Expectations() {
            {
                one(messageResources).getMessageWithDefault("count", "count");
                will(returnValue("my.field.name"));
                one(messageResources).getMessage(keyFor("my.field.name"), "bad-value");
                will(throwException(new MissingResourceException("fake", "class", "key")));
                one(messageResources).getMessage(keyFor(NUMBER_NAME),
                        "my.field.name", "bad-value");
                will(throwException(new MissingResourceException("fake", "class", "key")));
                one(messageResources).getMessageWithDefault(keyFor(DEFAULT_NAME), DEFAULT_MESSAGE, "my.field.name",
                        "bad-value");
                will(returnValue(defaultMessage));
            }
        });
        BindErrorMessageResolver messageResolver = new OgnlBindErrorMessageResolver(messageResources);
        assertEquals(defaultMessage, messageResolver.resolve(new FakeBean(), "count", "bad-value"));
    }

    @Test
    public void canFindBindErrorMessageKey() {
        OgnlBindErrorMessageResolver messageResolver = new OgnlBindErrorMessageResolver(null);

        // Primitive numbers and their wrappers
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(byte.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(Byte.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(short.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(Short.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(int.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(Integer.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(long.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(Long.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(float.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(Float.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(double.class));
        assertEquals(keyFor(NUMBER_NAME), messageResolver.findBindErrorMessageKey(Double.class));

        // primitive boolean and its wrapper
        assertEquals(keyFor("boolean"), messageResolver.findBindErrorMessageKey(boolean.class));
        assertEquals(keyFor("boolean"), messageResolver.findBindErrorMessageKey(Boolean.class));
    }

}
