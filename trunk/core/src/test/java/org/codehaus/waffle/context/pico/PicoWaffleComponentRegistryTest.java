package org.codehaus.waffle.context.pico;

import org.codehaus.waffle.WaffleComponentRegistry;
import org.codehaus.waffle.controller.DefaultControllerNameResolver;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.controller.DefaultControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerNameResolver;
import org.codehaus.waffle.action.*;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.DataBinder;
import org.codehaus.waffle.bind.DefaultBindErrorMessageResolver;
import org.codehaus.waffle.bind.OgnlDataBinder;
import org.codehaus.waffle.bind.OgnlTypeConverter;
import org.codehaus.waffle.context.AbstractContextContainerFactory;
import org.codehaus.waffle.context.ContextContainerFactory;
import org.codehaus.waffle.context.pico.PicoWaffleComponentRegistry;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.testmodel.StubControllerDefinitionFactory;
import org.codehaus.waffle.testmodel.StubActionMethodExecutor;
import org.codehaus.waffle.testmodel.StubActionMethodResponseHandler;
import org.codehaus.waffle.testmodel.StubArgumentResolver;
import org.codehaus.waffle.testmodel.StubBindErrorMessageResolver;
import org.codehaus.waffle.testmodel.StubContextContainerFactory;
import org.codehaus.waffle.testmodel.StubDataBinder;
import org.codehaus.waffle.testmodel.StubDispatchAssistant;
import org.codehaus.waffle.testmodel.StubMessageResources;
import org.codehaus.waffle.testmodel.StubMethodDefinitionFinder;
import org.codehaus.waffle.testmodel.StubValidator;
import org.codehaus.waffle.testmodel.StubViewDispatcher;
import org.codehaus.waffle.testmodel.StubViewResolver;
import org.codehaus.waffle.testmodel.StubControllerNameResolver;
import org.codehaus.waffle.validation.DefaultValidator;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.view.DefaultDispatchAssistant;
import org.codehaus.waffle.view.DefaultViewDispatcher;
import org.codehaus.waffle.view.DefaultViewResolver;
import org.codehaus.waffle.view.DispatchAssistant;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.view.ViewResolver;
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
        assertTrue(componentRegistry.getActionMethodExecutor() instanceof InterceptingActionMethodExecutor);
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
