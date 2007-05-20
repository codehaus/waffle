package org.codehaus.waffle.bind;

import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.testmodel.FakeBean;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.util.MissingResourceException;

public class DefaultBindErrorMessageResolverTest extends MockObjectTestCase {

    public void testResolveFirstCheckForCustomFieldMessage() {
        Mock mockMessageResources = mock(MessageResources.class);
        mockMessageResources.expects(once())
                .method("getMessageWithDefault")
                .with(eq("decimal"), eq("decimal"), ANYTHING)
                .will(returnValue("my.field.name"));
        mockMessageResources.expects(once())
                .method("getMessage")
                .with(eq("my.field.name.bind.error"), eq(new Object[] {"bad-value"}))
                .will(returnValue("My Error Message"));
        MessageResources messageResources = (MessageResources) mockMessageResources.proxy();

        BindErrorMessageResolver messageResolver = new DefaultBindErrorMessageResolver(messageResources);
        String message = messageResolver.resolve(new FakeBean(), "decimal", "bad-value");

        assertEquals("My Error Message", message);
    }

    public void testResolveRevertsToTypeBasedBindErrorMessage() {
        Mock mockMessageResources = mock(MessageResources.class);
        mockMessageResources.expects(once())
                .method("getMessageWithDefault")
                .with(eq("count"), eq("count"), ANYTHING)
                .will(returnValue("my.field.name"));
        mockMessageResources.expects(once())
                .method("getMessage")
                .with(eq("my.field.name.bind.error"), eq(new Object[] {"bad-value"}))
                .will(throwException(new MissingResourceException("fake", "class", "key")));
        mockMessageResources.expects(once())
                .method("getMessage")
                .with(eq("number.bind.error"), eq(new Object[] {"my.field.name", "bad-value"}))
                .will(returnValue("The error message"));
        MessageResources messageResources = (MessageResources) mockMessageResources.proxy();

        BindErrorMessageResolver messageResolver = new DefaultBindErrorMessageResolver(messageResources);
        String message = messageResolver.resolve(new FakeBean(), "count", "bad-value");
        assertEquals("The error message", message);
    }

    public void testResolveRevertsToDefaultBindErrorMessage() {
        Mock mockMessageResources = mock(MessageResources.class);
        mockMessageResources.expects(once())
                .method("getMessageWithDefault")
                .with(eq("count"), eq("count"), ANYTHING)
                .will(returnValue("my.field.name"));
        mockMessageResources.expects(once())
                .method("getMessage")
                .with(eq("my.field.name.bind.error"), eq(new Object[] {"bad-value"}))
                .will(throwException(new MissingResourceException("fake", "class", "key")));
        mockMessageResources.expects(once())
                .method("getMessage")
                .with(eq("number.bind.error"), eq(new Object[] {"my.field.name", "bad-value"}))
                .will(throwException(new MissingResourceException("fake", "class", "key")));
        mockMessageResources.expects(once())
                .method("getMessage")
                .with(eq("default.bind.error"), eq(new Object[] {"my.field.name", "bad-value"}))
                .will(returnValue("The default error message"));
        MessageResources messageResources = (MessageResources) mockMessageResources.proxy();

        BindErrorMessageResolver messageResolver = new DefaultBindErrorMessageResolver(messageResources);
        String message = messageResolver.resolve(new FakeBean(), "count", "bad-value");
        assertEquals("The default error message", message);
    }

    public void testFindBindErrorMessageKey() {
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
