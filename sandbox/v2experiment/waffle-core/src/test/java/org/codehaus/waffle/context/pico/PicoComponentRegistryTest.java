package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.ComponentRegistry;
import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.action.DefaultActionMethodResponseHandler;
import org.codehaus.waffle.action.InterceptingActionMethodExecutor;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.action.RequestParameterMethodNameResolver;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.ControllerDataBinder;
import org.codehaus.waffle.bind.DefaultStringTransmuter;
import org.codehaus.waffle.bind.ViewDataBinder;
import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.bind.ognl.OgnlBindErrorMessageResolver;
import org.codehaus.waffle.bind.ognl.OgnlControllerDataBinder;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.controller.ContextControllerDefinitionFactory;
import org.codehaus.waffle.controller.ContextPathControllerNameResolver;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerNameResolver;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.monitor.AbstractWritingMonitor;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.monitor.ContextMonitor;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.codehaus.waffle.monitor.RegistrarMonitor;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.monitor.ValidationMonitor;
import org.codehaus.waffle.monitor.ViewMonitor;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.testmodel.StubActionMethodExecutor;
import org.codehaus.waffle.testmodel.StubActionMethodResponseHandler;
import org.codehaus.waffle.testmodel.StubArgumentResolver;
import org.codehaus.waffle.testmodel.StubBindErrorMessageResolver;
import org.codehaus.waffle.testmodel.StubControllerDefinitionFactory;
import org.codehaus.waffle.testmodel.StubControllerNameResolver;
import org.codehaus.waffle.testmodel.StubDataBinder;
import org.codehaus.waffle.testmodel.StubMessageResources;
import org.codehaus.waffle.testmodel.StubMethodDefinitionFinder;
import org.codehaus.waffle.testmodel.StubMethodNameResolver;
import org.codehaus.waffle.testmodel.StubMonitor;
import org.codehaus.waffle.testmodel.StubParameterResolver;
import org.codehaus.waffle.testmodel.StubRequestAttributeBinder;
import org.codehaus.waffle.testmodel.StubStringTransmuter;
import org.codehaus.waffle.testmodel.StubValidator;
import org.codehaus.waffle.testmodel.StubViewDispatcher;
import org.codehaus.waffle.testmodel.StubViewResolver;
import org.codehaus.waffle.validation.DefaultValidator;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.DefaultViewDispatcher;
import org.codehaus.waffle.view.DefaultViewResolver;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.view.ViewResolver;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * @author Michael Ward
 * @author Mauro Talevi
 */
@RunWith(JMock.class)
public class PicoComponentRegistryTest {

    private Mockery mockery = new Mockery();
    
    @SuppressWarnings({"unchecked"})
    private static final Enumeration EMPTY_ENUMERATION = Collections.enumeration(Collections.EMPTY_LIST);

    @Test
    public void canLocateComponentClassReturnDefault() {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getInitParameter(String.class.getName());
                will(returnValue(null));
            }
        });
        
        Class<?> clazz = PicoComponentRegistry.locateComponentClass(String.class, Integer.class, servletContext);

        assertEquals(clazz, Integer.class);
    }
    
    @Test
    public void canLocateComponentClassReturnAlternate() {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getInitParameter(String.class.getName());
                will(returnValue(BigDecimal.class.getName()));
            }
        });

        Class<?> clazz = PicoComponentRegistry.locateComponentClass(String.class, Integer.class, servletContext);

        assertEquals(clazz, BigDecimal.class);
    }

    @Test
    public void canHaveDefaultRegistration() {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getInitParameterNames();
                will(returnValue(EMPTY_ENUMERATION));
                exactly(26).of(servletContext).getInitParameter(with(any(String.class)));
            }
        });

        ComponentRegistry componentRegistry = new PicoComponentRegistry(servletContext);

        assertTrue(componentRegistry.getActionMethodExecutor() instanceof InterceptingActionMethodExecutor);
        assertTrue(componentRegistry.getActionMethodResponseHandler() instanceof DefaultActionMethodResponseHandler);
        assertTrue(componentRegistry.getActionMonitor() instanceof AbstractWritingMonitor);
        assertTrue(componentRegistry.getBindErrorMessageResolver() instanceof OgnlBindErrorMessageResolver);
        assertTrue(componentRegistry.getBindMonitor() instanceof AbstractWritingMonitor);
        assertTrue(componentRegistry.getContextContainerFactory() instanceof ContextContainerFactory);
        assertTrue(componentRegistry.getContextMonitor() instanceof AbstractWritingMonitor);
        assertTrue(componentRegistry.getControllerMonitor() instanceof AbstractWritingMonitor);
        assertTrue(componentRegistry.getControllerNameResolver() instanceof ContextPathControllerNameResolver);
        assertTrue(componentRegistry.getControllerDefinitionFactory() instanceof ContextControllerDefinitionFactory);
        assertTrue(componentRegistry.getControllerDataBinder() instanceof OgnlControllerDataBinder);
        assertTrue(componentRegistry.getMethodNameResolver() instanceof RequestParameterMethodNameResolver);
        assertTrue(componentRegistry.getMessageResources() instanceof DefaultMessageResources);
        assertTrue(componentRegistry.getRegistrarMonitor() instanceof AbstractWritingMonitor);
        assertTrue(componentRegistry.getServletMonitor() instanceof AbstractWritingMonitor);
        assertTrue(componentRegistry.getStringTransmuter() instanceof DefaultStringTransmuter);
        assertTrue(componentRegistry.getValueConverterFinder() instanceof OgnlValueConverterFinder);
        assertTrue(componentRegistry.getValidator() instanceof DefaultValidator);
        assertTrue(componentRegistry.getValidationMonitor() instanceof AbstractWritingMonitor);
        assertTrue(componentRegistry.getViewDispatcher() instanceof DefaultViewDispatcher);
        assertTrue(componentRegistry.getViewMonitor() instanceof AbstractWritingMonitor);
        assertTrue(componentRegistry.getViewResolver() instanceof DefaultViewResolver);
    }

    @Test
    public void canDoAlternateRegistration() {
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getInitParameterNames();
                will(returnValue(EMPTY_ENUMERATION));
                one(servletContext).getInitParameter(ActionMethodExecutor.class.getName());
                will(returnValue(StubActionMethodExecutor.class.getName()));
                one(servletContext).getInitParameter(Validator.class.getName());
                will(returnValue(StubValidator.class.getName()));
                one(servletContext).getInitParameter(MessageResources.class.getName());
                will(returnValue(StubMessageResources.class.getName()));
                one(servletContext).getInitParameter(MethodDefinitionFinder.class.getName());
                will(returnValue(StubMethodDefinitionFinder.class.getName()));
                one(servletContext).getInitParameter(MethodNameResolver.class.getName());
                will(returnValue(StubMethodNameResolver.class.getName()));
                one(servletContext).getInitParameter(ActionMethodResponseHandler.class.getName());
                will(returnValue(StubActionMethodResponseHandler.class.getName()));
                one(servletContext).getInitParameter(ActionMonitor.class.getName());
                will(returnValue(StubMonitor.class.getName()));
                one(servletContext).getInitParameter(ArgumentResolver.class.getName());
                will(returnValue(StubArgumentResolver.class.getName()));
                one(servletContext).getInitParameter(BindErrorMessageResolver.class.getName());
                will(returnValue(StubBindErrorMessageResolver.class.getName()));
                one(servletContext).getInitParameter(BindMonitor.class.getName());
                will(returnValue(StubMonitor.class.getName()));
                one(servletContext).getInitParameter(ContextContainerFactory.class.getName());
                will(returnValue(PicoContextContainerFactory.class.getName()));
                one(servletContext).getInitParameter(ContextMonitor.class.getName());
                will(returnValue(StubMonitor.class.getName()));
                one(servletContext).getInitParameter(ControllerDefinitionFactory.class.getName());
                will(returnValue(StubControllerDefinitionFactory.class.getName()));
                one(servletContext).getInitParameter(ControllerMonitor.class.getName());
                will(returnValue(StubMonitor.class.getName()));
                one(servletContext).getInitParameter(ControllerNameResolver.class.getName());
                will(returnValue(StubControllerNameResolver.class.getName()));
                one(servletContext).getInitParameter(ControllerDataBinder.class.getName());
                will(returnValue(StubDataBinder.class.getName()));
                one(servletContext).getInitParameter(RegistrarMonitor.class.getName());
                will(returnValue(StubMonitor.class.getName()));
                one(servletContext).getInitParameter(ViewDataBinder.class.getName());
                will(returnValue(StubRequestAttributeBinder.class.getName()));
                one(servletContext).getInitParameter(ServletMonitor.class.getName());
                will(returnValue(StubMonitor.class.getName()));
                one(servletContext).getInitParameter(StringTransmuter.class.getName());
                will(returnValue(StubStringTransmuter.class.getName()));
                one(servletContext).getInitParameter(ValueConverterFinder.class.getName());
                will(returnValue(OgnlValueConverterFinder.class.getName()));
                one(servletContext).getInitParameter(ValidationMonitor.class.getName());
                will(returnValue(StubMonitor.class.getName()));
                one(servletContext).getInitParameter(ViewDispatcher.class.getName());
                will(returnValue(StubViewDispatcher.class.getName()));
                one(servletContext).getInitParameter(ViewMonitor.class.getName());
                will(returnValue(StubMonitor.class.getName()));
                one(servletContext).getInitParameter(ViewResolver.class.getName());
                will(returnValue(StubViewResolver.class.getName()));
                one(servletContext).getInitParameter(ParameterResolver.class.getName());
                will(returnValue(StubParameterResolver.class.getName()));
            }
        });
        ComponentRegistry componentRegistry = new PicoComponentRegistry(servletContext);

        assertTrue(componentRegistry.getActionMethodExecutor() instanceof StubActionMethodExecutor);
        assertTrue(componentRegistry.getActionMethodResponseHandler() instanceof StubActionMethodResponseHandler);
        assertTrue(componentRegistry.getActionMonitor() instanceof StubMonitor);
        assertTrue(componentRegistry.getArgumentResolver() instanceof StubArgumentResolver);
        assertTrue(componentRegistry.getBindErrorMessageResolver() instanceof StubBindErrorMessageResolver);
        assertTrue(componentRegistry.getBindMonitor() instanceof StubMonitor);
        assertTrue(componentRegistry.getControllerNameResolver() instanceof StubControllerNameResolver);
        assertTrue(componentRegistry.getControllerDefinitionFactory() instanceof StubControllerDefinitionFactory);
        assertTrue(componentRegistry.getContextContainerFactory() instanceof PicoContextContainerFactory);
        assertFalse(componentRegistry.getControllerDataBinder() instanceof OgnlControllerDataBinder);
        assertTrue(componentRegistry.getMethodDefinitionFinder() instanceof StubMethodDefinitionFinder);
        assertTrue(componentRegistry.getMethodNameResolver() instanceof StubMethodNameResolver);
        assertTrue(componentRegistry.getMessageResources() instanceof StubMessageResources);
        assertTrue(componentRegistry.getRegistrarMonitor() instanceof StubMonitor);
        assertTrue(componentRegistry.getViewDataBinder() instanceof StubRequestAttributeBinder);
        assertTrue(componentRegistry.getServletMonitor() instanceof StubMonitor);
        assertTrue(componentRegistry.getValueConverterFinder() instanceof OgnlValueConverterFinder);
        assertTrue(componentRegistry.getValidator() instanceof StubValidator);
        assertTrue(componentRegistry.getValidationMonitor() instanceof StubMonitor);
        assertTrue(componentRegistry.getViewDispatcher() instanceof StubViewDispatcher);
        assertTrue(componentRegistry.getViewMonitor() instanceof StubMonitor);
        assertTrue(componentRegistry.getViewResolver() instanceof StubViewResolver);
    }

    @Test
    public void canRegisterAdditionalComponents() {
        final List<String> names = new ArrayList<String>();
        names.add("register:NameCanBeAnything");
        
        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getInitParameterNames();
                will(returnValue(Collections.enumeration(names)));
                exactly(26).of(servletContext).getInitParameter(with(any(String.class)));
                one(servletContext).getInitParameter("register:NameCanBeAnything");
                will(returnValue("java.util.ArrayList"));
            }
        });

        ComponentRegistry componentRegistry = new PicoComponentRegistry(servletContext);

        List<?> list = componentRegistry.locateByType(List.class);
        assertNotNull(list);
        assertSame(list, componentRegistry.locateByKey("NameCanBeAnything"));
    }

    @Test
    public void canRegisterNonCachingCustomComponent() throws Exception {
        final List<String> names = new ArrayList<String>();
        names.add("registerNonCaching:FooBar");

        // Mock ServletContext
        final ServletContext servletContext = mockery.mock(ServletContext.class);
        mockery.checking(new Expectations() {
            {
                one(servletContext).getInitParameterNames();
                will(returnValue(Collections.enumeration(names)));
                exactly(26).of(servletContext).getInitParameter(with(any(String.class)));
                one(servletContext).getInitParameter("registerNonCaching:FooBar");
                will(returnValue("java.util.ArrayList"));
            }
        });
        ComponentRegistry componentRegistry = new PicoComponentRegistry(servletContext);

        // get private pico field
        Field picoField = PicoComponentRegistry.class.getDeclaredField("picoContainer");
        picoField.setAccessible(true);
        MutablePicoContainer pico = (MutablePicoContainer) picoField.get(componentRegistry);

        assertNotSame(pico.getComponent(List.class), pico.getComponent(List.class));
        assertNotSame(pico.getComponent(List.class), pico.getComponent("FooBar"));
    }

}
