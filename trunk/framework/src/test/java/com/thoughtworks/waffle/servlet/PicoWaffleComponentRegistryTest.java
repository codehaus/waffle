package com.thoughtworks.waffle.servlet;

import com.thoughtworks.waffle.WaffleComponentRegistry;
import com.thoughtworks.waffle.action.ControllerDefinitionFactory;
import com.thoughtworks.waffle.action.DefaultControllerDefinitionFactory;
import com.thoughtworks.waffle.action.DefaultControllerNameResolver;
import com.thoughtworks.waffle.action.ControllerNameResolver;
import com.thoughtworks.waffle.action.method.ActionMethodExecutor;
import com.thoughtworks.waffle.action.method.ActionMethodResponseHandler;
import com.thoughtworks.waffle.action.method.ArgumentResolver;
import com.thoughtworks.waffle.action.method.DefaultActionMethodExecutor;
import com.thoughtworks.waffle.action.method.DefaultActionMethodResponseHandler;
import com.thoughtworks.waffle.action.method.MethodDefinitionFinder;
import com.thoughtworks.waffle.bind.BindErrorMessageResolver;
import com.thoughtworks.waffle.bind.DataBinder;
import com.thoughtworks.waffle.bind.DefaultBindErrorMessageResolver;
import com.thoughtworks.waffle.bind.OgnlDataBinder;
import com.thoughtworks.waffle.bind.OgnlTypeConverter;
import com.thoughtworks.waffle.context.AbstractContextContainerFactory;
import com.thoughtworks.waffle.context.ContextContainerFactory;
import com.thoughtworks.waffle.i18n.DefaultMessageResources;
import com.thoughtworks.waffle.i18n.MessageResources;
import com.thoughtworks.waffle.testmodel.StubControllerDefinitionFactory;
import com.thoughtworks.waffle.testmodel.StubActionMethodExecutor;
import com.thoughtworks.waffle.testmodel.StubActionMethodResponseHandler;
import com.thoughtworks.waffle.testmodel.StubArgumentResolver;
import com.thoughtworks.waffle.testmodel.StubBindErrorMessageResolver;
import com.thoughtworks.waffle.testmodel.StubContextContainerFactory;
import com.thoughtworks.waffle.testmodel.StubDataBinder;
import com.thoughtworks.waffle.testmodel.StubDispatchAssistant;
import com.thoughtworks.waffle.testmodel.StubMessageResources;
import com.thoughtworks.waffle.testmodel.StubMethodDefinitionFinder;
import com.thoughtworks.waffle.testmodel.StubValidator;
import com.thoughtworks.waffle.testmodel.StubViewDispatcher;
import com.thoughtworks.waffle.testmodel.StubViewResolver;
import com.thoughtworks.waffle.testmodel.StubControllerNameResolver;
import com.thoughtworks.waffle.validation.DefaultValidator;
import com.thoughtworks.waffle.validation.Validator;
import com.thoughtworks.waffle.view.DefaultDispatchAssistant;
import com.thoughtworks.waffle.view.DefaultViewDispatcher;
import com.thoughtworks.waffle.view.DefaultViewResolver;
import com.thoughtworks.waffle.view.DispatchAssistant;
import com.thoughtworks.waffle.view.ViewDispatcher;
import com.thoughtworks.waffle.view.ViewResolver;
import ognl.DefaultTypeConverter;
import ognl.TypeConverter;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.picocontainer.MutablePicoContainer;

import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class PicoWaffleComponentRegistryTest extends MockObjectTestCase {
    @SuppressWarnings({"unchecked"})
    private static final Enumeration EMPTY_ENUMERATION = Collections.enumeration(Collections.EMPTY_LIST);

    public void testLocateComponentClassReturnDefault() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(String.class.getName()))
                .will(returnValue(null));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        Class clazz = PicoWaffleComponentRegistry.locateComponentClass(String.class, Integer.class, servletContext);

        assertEquals(clazz, Integer.class);
    }

    public void testLocateComponentClassReturnAlternate() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(String.class.getName()))
                .will(returnValue(BigDecimal.class.getName()));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();

        Class clazz = PicoWaffleComponentRegistry.locateComponentClass(String.class, Integer.class, servletContext);

        assertEquals(clazz, BigDecimal.class);
    }

    public void testDefaultRegistration() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getInitParameterNames")
                .will(returnValue(EMPTY_ENUMERATION));
        mockServletContext.expects(exactly(15))
                .method("getInitParameter")
                .will(returnValue(null));
        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        WaffleComponentRegistry componentRegistry = new PicoWaffleComponentRegistry(servletContext);

        assertTrue(componentRegistry.getControllerNameResolver() instanceof DefaultControllerNameResolver);
        assertTrue(componentRegistry.getControllerDefinitionFactory() instanceof DefaultControllerDefinitionFactory);
        assertTrue(componentRegistry.getContextContainerFactory() instanceof AbstractContextContainerFactory);
        assertTrue(componentRegistry.getBindErrorMessageResolver() instanceof DefaultBindErrorMessageResolver);
        assertTrue(componentRegistry.getDataBinder() instanceof OgnlDataBinder);
        assertTrue(componentRegistry.getDispatchAssistant() instanceof DefaultDispatchAssistant);
        assertTrue(componentRegistry.getActionMethodExecutor() instanceof DefaultActionMethodExecutor);
        assertTrue(componentRegistry.getActionMethodResponseHandler() instanceof DefaultActionMethodResponseHandler);
        assertTrue(componentRegistry.getMessageResources() instanceof DefaultMessageResources);
        assertTrue(componentRegistry.getViewDispatcher() instanceof DefaultViewDispatcher);
        assertTrue(componentRegistry.getTypeConverter() instanceof OgnlTypeConverter);
        assertTrue(componentRegistry.getViewResolver() instanceof DefaultViewResolver);
        assertTrue(componentRegistry.getValidator() instanceof DefaultValidator);
    }

    public void testAlternateRegistration() {
        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getInitParameterNames")
                .will(returnValue(EMPTY_ENUMERATION));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(ControllerNameResolver.class.getName()))
                .will(returnValue(StubControllerNameResolver.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(ArgumentResolver.class.getName()))
                .will(returnValue(StubArgumentResolver.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(BindErrorMessageResolver.class.getName()))
                .will(returnValue(StubBindErrorMessageResolver.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(DataBinder.class.getName()))
                .will(returnValue(StubDataBinder.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(DispatchAssistant.class.getName()))
                .will(returnValue(StubDispatchAssistant.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(ActionMethodResponseHandler.class.getName()))
                .will(returnValue(StubActionMethodResponseHandler.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(TypeConverter.class.getName()))
                .will(returnValue(DefaultTypeConverter.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(ViewDispatcher.class.getName()))
                .will(returnValue(StubViewDispatcher.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(ViewResolver.class.getName()))
                .will(returnValue(StubViewResolver.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(ControllerDefinitionFactory.class.getName()))
                .will(returnValue(StubControllerDefinitionFactory.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(ContextContainerFactory.class.getName()))
                .will(returnValue(StubContextContainerFactory.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(ActionMethodExecutor.class.getName()))
                .will(returnValue(StubActionMethodExecutor.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(Validator.class.getName()))
                .will(returnValue(StubValidator.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(MessageResources.class.getName()))
                .will(returnValue(StubMessageResources.class.getName()));
        mockServletContext.expects(once()).method("getInitParameter")
                .with(eq(MethodDefinitionFinder.class.getName()))
                .will(returnValue(StubMethodDefinitionFinder.class.getName()));

        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        WaffleComponentRegistry componentRegistry = new PicoWaffleComponentRegistry(servletContext);

        assertTrue(componentRegistry.getControllerNameResolver() instanceof StubControllerNameResolver);
        assertTrue(componentRegistry.getControllerDefinitionFactory() instanceof StubControllerDefinitionFactory);
        assertTrue(componentRegistry.getArgumentResolver() instanceof StubArgumentResolver);
        assertTrue(componentRegistry.getBindErrorMessageResolver() instanceof StubBindErrorMessageResolver);
        assertTrue(componentRegistry.getContextContainerFactory() instanceof StubContextContainerFactory);
        assertFalse(componentRegistry.getDataBinder() instanceof OgnlDataBinder);
        assertTrue(componentRegistry.getDispatchAssistant() instanceof StubDispatchAssistant);
        assertTrue(componentRegistry.getActionMethodExecutor() instanceof StubActionMethodExecutor);
        assertTrue(componentRegistry.getMethodDefinitionFinder() instanceof StubMethodDefinitionFinder);
        assertTrue(componentRegistry.getActionMethodResponseHandler() instanceof StubActionMethodResponseHandler);
        assertTrue(componentRegistry.getMessageResources() instanceof StubMessageResources);
        assertTrue(componentRegistry.getTypeConverter() instanceof DefaultTypeConverter);
        assertTrue(componentRegistry.getValidator() instanceof StubValidator);
        assertTrue(componentRegistry.getViewDispatcher() instanceof StubViewDispatcher);
        assertTrue(componentRegistry.getViewResolver() instanceof StubViewResolver);
    }

    public void testRegisterAdditionalComponents() {
        List<String> names = new ArrayList<String>();
        names.add("register:NameCanBeAnything");

        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getInitParameterNames")
                .will(returnValue(Collections.enumeration(names)));
        mockServletContext.expects(exactly(15))
                .method("getInitParameter")
                .will(returnValue(null));
        mockServletContext.expects(once())
                .method("getInitParameter")
                .with(eq("register:NameCanBeAnything"))
                .will(returnValue("java.util.ArrayList"));

        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        WaffleComponentRegistry componentRegistry = new PicoWaffleComponentRegistry(servletContext);

        List list = componentRegistry.locateByType(List.class);
        assertNotNull(list);
        assertSame(list, componentRegistry.locateByKey("NameCanBeAnything"));
    }

    public void testRegisterNonCachingCustomComponent() throws Exception {
        List<String> names = new ArrayList<String>();
        names.add("registerNonCaching:FooBar");

        Mock mockServletContext = mock(ServletContext.class);
        mockServletContext.expects(once())
                .method("getInitParameterNames")
                .will(returnValue(Collections.enumeration(names)));
        mockServletContext.expects(exactly(15))
                .method("getInitParameter")
                .will(returnValue(null));
        mockServletContext.expects(once())
                .method("getInitParameter")
                .with(eq("registerNonCaching:FooBar"))
                .will(returnValue("java.util.ArrayList"));

        ServletContext servletContext = (ServletContext) mockServletContext.proxy();
        WaffleComponentRegistry componentRegistry = new PicoWaffleComponentRegistry(servletContext);

        // get private pico field
        Field picoField = PicoWaffleComponentRegistry.class.getDeclaredField("picoContainer");
        picoField.setAccessible(true);
        MutablePicoContainer pico = (MutablePicoContainer) picoField.get(componentRegistry);

        assertNotSame(pico.getComponentInstanceOfType(List.class), pico.getComponentInstanceOfType(List.class));
        assertNotSame(pico.getComponentInstanceOfType(List.class), pico.getComponentInstance("FooBar"));
    }

}
