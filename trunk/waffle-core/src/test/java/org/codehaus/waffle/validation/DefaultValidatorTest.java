package org.codehaus.waffle.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;

import org.codehaus.waffle.action.MethodDefinition;
import org.codehaus.waffle.context.ContextContainer;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.testmodel.FakeController;
import org.codehaus.waffle.testmodel.FakeControllerValidator;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class DefaultValidatorTest {

    private Mockery mockery = new Mockery();

    @After
    public void tearDown() throws Exception {
        RequestLevelContainer.set(null);
    }

    @Test
    public void canValidateWithDefaultSuffix() throws Exception {
        Validator validator = new DefaultValidator(new SilentMonitor());
        assertValidator(validator, DefaultValidatorConfiguration.DEFAULT_SUFFIX);
    }
    
    @Test
    public void canValidateWithCustumSuffix() throws Exception {
        String suffix = "Check";
        Validator validator = new DefaultValidator(new DefaultValidatorConfiguration(suffix), new SilentMonitor());
        assertValidator(validator, suffix);
    }

    private void assertValidator(Validator validator, final String suffix) throws NoSuchMethodException {
        final FakeControllerValidator fakeControllerValidator = new FakeControllerValidator();

        // Mock ContextContainer
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponentInstance("theController"+suffix);
                will(returnValue(fakeControllerValidator));
            }
        });
        RequestLevelContainer.set(contextContainer);


        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("foobar");

        FakeController fakeController = new FakeController();
        ControllerDefinition controllerDefinition = new ControllerDefinition("theController", fakeController, methodDefinition);

        ErrorsContext errorsContext = new DefaultErrorsContext();
        validator.validate(controllerDefinition, errorsContext);

        assertSame(errorsContext, fakeControllerValidator.errorsContext);
        assertEquals("foobar", fakeControllerValidator.value);
    }

    @Test
    public void canValidateWhenControllerDefinitionHasANullMethodDefinition() {
        final FakeControllerValidator fakeControllerValidator = new FakeControllerValidator();

        // Mock ContextContainer
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {
                one(contextContainer).getComponentInstance("theControllerValidator");
                will(returnValue(fakeControllerValidator));
            }
        });
        RequestLevelContainer.set(contextContainer);

        FakeController fakeController = new FakeController();
        ControllerDefinition controllerDefinition = new ControllerDefinition("theController", fakeController, null);

        ErrorsContext errorsContext = new DefaultErrorsContext();
        Validator validator = new DefaultValidator(new SilentMonitor());
        validator.validate(controllerDefinition, errorsContext);
    }

}
