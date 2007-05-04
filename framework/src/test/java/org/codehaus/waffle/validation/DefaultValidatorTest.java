package org.codehaus.waffle.validation;

import org.codehaus.waffle.action.ControllerDefinition;
import org.codehaus.waffle.action.method.MethodDefinition;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.testmodel.FakeController;
import org.codehaus.waffle.testmodel.FakeControllerValidator;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import java.lang.reflect.Method;

public class DefaultValidatorTest extends MockObjectTestCase {

    protected void tearDown() throws Exception {
        RequestLevelContainer.set(null);
    }

    public void testValidate() throws Exception {
        FakeControllerValidator fakeControllerValidator = new FakeControllerValidator();

        // Mock ContextContainer
        Mock mockContextContainer = mock(ContextContainer.class);
        mockContextContainer.expects(once())
                .method("getComponentInstance")
                .with(eq("theControllerValidator"))
                .will(returnValue(fakeControllerValidator));
        RequestLevelContainer.set((ContextContainer) mockContextContainer.proxy());

        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("foobar");

        FakeController fakeController = new FakeController();
        ControllerDefinition controllerDefinition = new ControllerDefinition("theController", fakeController, methodDefinition);

        ErrorsContext errorsContext = new DefaultErrorsContext();
        Validator validator = new DefaultValidator();
        validator.validate(controllerDefinition, errorsContext);

        assertSame(errorsContext, fakeControllerValidator.errorsContext);
        assertEquals("foobar", fakeControllerValidator.value);
    }

    public void testValidateWhenControllerDefinitionHasANullMethodDefinition() {
        FakeControllerValidator fakeControllerValidator = new FakeControllerValidator();

        // Mock ContextContainer
        Mock mockContextContainer = mock(ContextContainer.class);
        mockContextContainer.expects(once())
                .method("getComponentInstance")
                .with(eq("theControllerValidator"))
                .will(returnValue(fakeControllerValidator));
        RequestLevelContainer.set((ContextContainer) mockContextContainer.proxy());

        FakeController fakeController = new FakeController();
        ControllerDefinition controllerDefinition = new ControllerDefinition("theController", fakeController, null);

        ErrorsContext errorsContext = new DefaultErrorsContext();
        Validator validator = new DefaultValidator();
        validator.validate(controllerDefinition, errorsContext);
    }

}
