package org.codehaus.waffle.bind;

import org.codehaus.waffle.context.ContextLevel;
import org.codehaus.waffle.testmodel.FakeController;
import org.codehaus.waffle.testmodel.FakeBean;
import org.codehaus.waffle.validation.BindErrorMessage;
import org.codehaus.waffle.validation.DefaultErrorsContext;
import org.codehaus.waffle.validation.ErrorsContext;
import ognl.DefaultTypeConverter;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class OgnlDataBinderTest extends MockObjectTestCase {

    public void testBind() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("name");
        parameters.add("contextLevel");
        Enumeration enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameterNames")
                .will(returnValue(enumeration));
        mockRequest.expects(once()).method("getParameter")
                .with(eq("name"))
                .will(returnValue("foobar"));
        mockRequest.expects(once()).method("getParameter")
                .with(eq("contextLevel"))
                .will(returnValue("APPLICATION"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new OgnlTypeConverter(), null);
        ErrorsContext errorsContext = new DefaultErrorsContext();
        binder.bind(request, errorsContext, fakeController);

        assertEquals("foobar", fakeController.getName());
        assertEquals(ContextLevel.APPLICATION, fakeController.getContextLevel());
        assertFalse(errorsContext.hasErrorMessages());
    }

    public void testBindEmptyValueForEnum() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("contextLevel");
        Enumeration enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameterNames").will(returnValue(enumeration));
        mockRequest.expects(once()).method("getParameter").with(eq("contextLevel")).will(returnValue(""));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new DefaultTypeConverter(), null);
        ErrorsContext errorsContext = new DefaultErrorsContext();
        binder.bind(request, errorsContext, fakeController);

        assertNull(fakeController.getContextLevel());
        assertFalse(errorsContext.hasErrorMessages());
    }

    public void testBindNoSuchPropertyExceptionIgnored() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("method");
        Enumeration enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameterNames")
                .will(returnValue(enumeration));
        mockRequest.expects(once()).method("getParameter")
                .with(eq("method"))
                .will(returnValue("this should cause a NoSuchPropertyException!"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new DefaultTypeConverter(), null);

        ErrorsContext errorsContext = new DefaultErrorsContext();
        binder.bind(request, errorsContext, fakeController);
        assertFalse(errorsContext.hasErrorMessages());
    }

    /**
     * This tests that parameter names with strange characters (i.e. '-') don't cause errors.
     * <p/>
     * This was discovered when using displaytags.sf.net project with waffle
     */
    public void testBindInappropriateExpressionExceptionIgnored() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("x-01234567-s");
        Enumeration enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameterNames")
                .will(returnValue(enumeration));
        mockRequest.expects(once()).method("getParameter")
                .with(eq("x-01234567-s"))
                .will(returnValue("blah"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        FakeController fakeController = new FakeController();
        DataBinder binder = new OgnlDataBinder(new DefaultTypeConverter(), null);

        ErrorsContext errorsContext = new DefaultErrorsContext();
        binder.bind(request, errorsContext, fakeController);
        assertFalse(errorsContext.hasErrorMessages());
    }

    public void testFieldValueBindError() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("count");
        Enumeration enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameterNames")
                .will(returnValue(enumeration));
        mockRequest.expects(once()).method("getParameter")
                .with(eq("count"))
                .will(returnValue("bad value"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        // Mock BindErrorMessageResolver
        Mock mockBindErrorMessageResolver = mock(BindErrorMessageResolver.class);
        mockBindErrorMessageResolver.expects(once())
                .method("resolve")
                .with(isA(FakeBean.class), eq("count"), eq("bad value"));
        BindErrorMessageResolver resolver = (BindErrorMessageResolver) mockBindErrorMessageResolver.proxy();

        DataBinder binder = new OgnlDataBinder(new DefaultTypeConverter(), resolver);

        ErrorsContext errorsContext = new DefaultErrorsContext();
        binder.bind(request, errorsContext, new FakeBean());
        assertTrue(errorsContext.hasErrorMessages());

        List<BindErrorMessage> messages = errorsContext.getBindErrorMessages("count");
        assertEquals(1, messages.size());

        BindErrorMessage bindValidationMessage = messages.get(0);
        assertEquals("count", bindValidationMessage.getName());
        assertEquals("bad value", bindValidationMessage.getValue());
    }

    public void testBindHandlesBindException() {
        List<String> parameters = new ArrayList<String>();
        parameters.add("count");
        Enumeration enumeration = Collections.enumeration(parameters);

        // Mock HttpServletRequest
        Mock mockRequest = mock(HttpServletRequest.class);
        mockRequest.expects(once()).method("getParameterNames")
                .will(returnValue(enumeration));
        mockRequest.expects(once()).method("getParameter")
                .with(eq("count"))
                .will(returnValue("bad value"));
        HttpServletRequest request = (HttpServletRequest) mockRequest.proxy();

        DataBinder binder = new OgnlDataBinder(new DefaultTypeConverter(), null) {
            protected void handleConvert(String parameterName, String parameterValue, Object model) {
                throw new BindException("fake from test");
            }
        };

        ErrorsContext errorsContext = new DefaultErrorsContext();
        binder.bind(request, errorsContext, new FakeBean());
        assertTrue(errorsContext.hasErrorMessages());
    }

}
