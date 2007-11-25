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
import org.codehaus.waffle.testmodel.FakeControllerWithValidation;
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
    public void canValidateWithControllerValidation() throws Exception {
        FakeController fakeController = new FakeControllerWithValidation();
        Validator validator = new DefaultValidator(new SilentMonitor());
        assertValidation(validator, fakeController, null, DefaultValidatorConfiguration.DEFAULT_SUFFIX);
    }

    @Test
    public void canValidateWithControllerValidatorOfDefaultSuffix() throws Exception {
        FakeController fakeController = new FakeController();
        Validator validator = new DefaultValidator(new SilentMonitor());
        assertValidation(validator, fakeController, new FakeControllerValidator(), DefaultValidatorConfiguration.DEFAULT_SUFFIX);
    }
    
    @Test
    public void canValidateWithControllerValidatorOfCustomSuffix() throws Exception {
        FakeController fakeController = new FakeController();
        String suffix = "Check";
        Validator validator = new DefaultValidator(new DefaultValidatorConfiguration(suffix), new SilentMonitor());
        assertValidation(validator, fakeController, new FakeControllerValidator(), suffix);
    }

    private void assertValidation(Validator validator, final FakeController fakeController, final FakeControllerValidator fakeControllerValidator, final String suffix) throws NoSuchMethodException {
        // Mock ContextContainer
        final ContextContainer contextContainer = mockery.mock(ContextContainer.class);
        mockery.checking(new Expectations() {
            {                
                one(contextContainer).getComponentInstance("theController"+suffix);
                will(returnValue(fakeControllerValidator));
                if (fakeControllerValidator == null) {
                    one(contextContainer).getComponentInstance("theController");
                    will(returnValue(fakeController));
                }
            }
        });
        RequestLevelContainer.set(contextContainer);


        Method method = FakeController.class.getMethod("sayHello", String.class);
        MethodDefinition methodDefinition = new MethodDefinition(method);
        methodDefinition.addMethodArgument("foobar");

        ControllerDefinition controllerDefinition = new ControllerDefinition("theController", fakeController, methodDefinition);

        ErrorsContext errorsContext = new DefaultErrorsContext();
        validator.validate(controllerDefinition, errorsContext);

        if ( fakeControllerValidator != null ){
            assertSame(errorsContext, fakeControllerValidator.errorsContext);
            assertEquals("foobar", fakeControllerValidator.value);
        } else if ( fakeController instanceof FakeControllerWithValidation ){
            FakeControllerWithValidation fakeControllerWithValidation = (FakeControllerWithValidation) fakeController;
            assertSame(errorsContext, fakeControllerWithValidation.errorsContext);
            assertEquals("foobar", fakeControllerWithValidation.value);            
        }
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
