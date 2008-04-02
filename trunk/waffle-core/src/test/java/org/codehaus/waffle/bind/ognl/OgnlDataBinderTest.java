package org.codehaus.waffle.bind.ognl;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.BindException;
import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.bind.converters.ListValueConverter;
import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.testmodel.FakeBean;
import org.codehaus.waffle.testmodel.FakeController;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorMessage;
import org.codehaus.waffle.validation.ErrorsContext;
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
public class OgnlDataBinderTest {

    private Mockery mockery = new Mockery();

    @Test
    public void canBindSingleValue() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("name");
        parameters.add("contextLevel");
        final Enumeration<String> enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getParameterValues("name");
                will(returnValue(new String[]{"foobar"}));
                one(request).getParameterValues("contextLevel");
                will(returnValue(new String[]{"APPLICATION"}));
            }
        });

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor());
        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        binder.bind(request, null, errorsContext, fakeController);

        assertEquals("foobar", fakeController.getName());
        assertEquals(ContextLevel.APPLICATION, fakeController.getContextLevel());
        assertFalse(errorsContext.hasErrorMessages());
    }
    
    @Test
    public void canBindListValues() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("list");
        final Enumeration<String> enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        final String[] values = new String[]{"foo", "bar"};
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getParameterValues("list");
                will(returnValue(values));
            }
        });

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new OgnlValueConverterFinder(new ListValueConverter()), null, new SilentMonitor());
        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        binder.bind(request, null, errorsContext, fakeController);

        assertEquals(asList(values), fakeController.getList());
        assertFalse(errorsContext.hasErrorMessages());
    }
       
    @Test
    public void canBindEmptyValueForEnum() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("contextLevel");
        final Enumeration<String> enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getParameterValues("contextLevel");
                will(returnValue(new String[]{""}));
            }
        });

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor());
        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        binder.bind(request, null, errorsContext, fakeController);

        assertNull(fakeController.getContextLevel());
        assertFalse(errorsContext.hasErrorMessages());
    }

    @Test
    public void canIgnoreNoSuchPropertyException() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("method");
        final Enumeration<String> enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getParameterValues("method");
                will(returnValue(new String[]{"this should cause a NoSuchPropertyException!"}));
            }
        });

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor());

        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        binder.bind(request, null, errorsContext, fakeController);
        assertFalse(errorsContext.hasErrorMessages());
    }

    /**
     * This tests that parameter names with strange characters (i.e. '-') don't cause errors. <p/> This was discovered
     * when using displaytags.sf.net project with waffle
     */
    @Test
    public void canIgnoreInappropriateExpression() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("x-01234567-s");
        final Enumeration<String> enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getParameterValues("x-01234567-s");
                will(returnValue(new String[]{"blah"}));
            }
        });

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor());

        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        binder.bind(request, null, errorsContext, fakeController);
        assertFalse(errorsContext.hasErrorMessages());
    }

    @Test
    public void canHandleFieldValueBindError() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("count");
        final Enumeration<String> enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getParameterValues("count");
                will(returnValue(new String[]{"bad value"}));
            }
        });

        // Mock BindErrorMessageResolver
        final BindErrorMessageResolver resolver = mockery.mock(BindErrorMessageResolver.class);
        mockery.checking(new Expectations() {
            {
                one(resolver).resolve(with(an(FakeBean.class)), with(equal("count")), with(equal("bad value")));
            }
        });

        DataBinder binder = new OgnlDataBinder(new OgnlValueConverterFinder(), resolver, new SilentMonitor());

        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        binder.bind(request, null, errorsContext, new FakeBean());
        assertTrue(errorsContext.hasErrorMessages());

        List<? extends ErrorMessage> messages = errorsContext.getErrorMessagesForField(ErrorMessage.Type.BIND, "count");
        assertEquals(1, messages.size());

        BindErrorMessage bindValidationMessage = (BindErrorMessage) messages.get(0);
        assertEquals("count", bindValidationMessage.getName());
        assertEquals("bad value", bindValidationMessage.getValue());
    }

    @Test
    public void canHandleBindException() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("count");
        final Enumeration<String> enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        final HttpServletRequest request = mockery.mock(HttpServletRequest.class);
        mockery.checking(new Expectations() {
            {
                one(request).getParameterNames();
                will(returnValue(enumeration));
                one(request).getParameterValues("count");
                will(returnValue(new String[]{"bad value"}));
            }
        });

        DataBinder binder = new OgnlDataBinder(new OgnlValueConverterFinder(), null, new SilentMonitor()) {
            protected void handleConvert(String parameterName, String parameterValue, Object model) {
                throw new BindException("fake from test");
            }
        };

        ErrorsContext errorsContext = new DefaultErrorsContext(null);
        binder.bind(request, null, errorsContext, new FakeBean());
        assertTrue(errorsContext.hasErrorMessages());
    }

}
