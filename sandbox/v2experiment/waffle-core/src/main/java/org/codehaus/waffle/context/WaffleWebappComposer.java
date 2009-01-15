package org.codehaus.waffle.context;

import org.picocontainer.web.WebappComposer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Characteristics;
import org.picocontainer.Parameter;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.view.DefaultViewDispatcher;
import org.codehaus.waffle.view.ViewResolver;
import org.codehaus.waffle.view.DefaultViewResolver;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.validation.DefaultValidator;
import org.codehaus.waffle.monitor.ActionMonitor;
import org.codehaus.waffle.monitor.SilentMonitor;
import org.codehaus.waffle.monitor.BindMonitor;
import org.codehaus.waffle.monitor.ControllerMonitor;
import org.codehaus.waffle.monitor.ServletMonitor;
import org.codehaus.waffle.monitor.ValidationMonitor;
import org.codehaus.waffle.monitor.ViewMonitor;
import org.codehaus.waffle.i18n.MessageResources;
import org.codehaus.waffle.i18n.DefaultMessageResources;
import org.codehaus.waffle.controller.ControllerDefinitionFactory;
import org.codehaus.waffle.controller.ControllerNameResolver;
import org.codehaus.waffle.controller.ContextPathControllerNameResolver;
import org.codehaus.waffle.controller.ContextControllerDefinitionFactory;
import org.codehaus.waffle.bind.BindErrorMessageResolver;
import org.codehaus.waffle.bind.ControllerDataBinder;
import org.codehaus.waffle.bind.ViewDataBinder;
import org.codehaus.waffle.bind.IntrospectingViewDataBinder;
import org.codehaus.waffle.bind.StringTransmuter;
import org.codehaus.waffle.bind.DefaultStringTransmuter;
import org.codehaus.waffle.bind.ValueConverterFinder;
import org.codehaus.waffle.bind.ognl.OgnlBindErrorMessageResolver;
import org.codehaus.waffle.bind.ognl.OgnlControllerDataBinder;
import org.codehaus.waffle.bind.ognl.OgnlValueConverterFinder;
import org.codehaus.waffle.action.ActionMethodExecutor;
import org.codehaus.waffle.action.InterceptingActionMethodExecutor;
import org.codehaus.waffle.action.ActionMethodResponseHandler;
import org.codehaus.waffle.action.DefaultActionMethodResponseHandler;
import org.codehaus.waffle.action.ArgumentResolver;
import org.codehaus.waffle.action.HierarchicalArgumentResolver;
import org.codehaus.waffle.action.MethodDefinitionFinder;
import org.codehaus.waffle.action.AnnotatedMethodDefinitionFinder;
import org.codehaus.waffle.action.MethodNameResolver;
import org.codehaus.waffle.action.RequestParameterMethodNameResolver;

import javax.servlet.ServletContext;
import java.util.Enumeration;

public class WaffleWebappComposer implements WebappComposer {

    private static final String REGISTER_KEY = "register:";
    private static final String REGISTER_NON_CACHING_KEY = "registerNonCaching:";

    private ParameterResolver parameterResolver;

    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        picoContainer.addComponent(servletContext);

        // add all known components
        picoContainer.addComponent(ActionMethodExecutor.class, locateComponentClass((Object) ActionMethodExecutor.class, (Class<?>) InterceptingActionMethodExecutor.class, servletContext));
        picoContainer.addComponent(ActionMethodResponseHandler.class, locateComponentClass((Object) ActionMethodResponseHandler.class, (Class<?>) DefaultActionMethodResponseHandler.class, servletContext));
        picoContainer.addComponent(ArgumentResolver.class, locateComponentClass((Object) ArgumentResolver.class, (Class<?>) HierarchicalArgumentResolver.class, servletContext));
        picoContainer.addComponent(MethodDefinitionFinder.class, locateComponentClass((Object) MethodDefinitionFinder.class, (Class<?>) AnnotatedMethodDefinitionFinder.class, servletContext));
        picoContainer.addComponent(MethodNameResolver.class, locateComponentClass((Object) MethodNameResolver.class, (Class<?>) RequestParameterMethodNameResolver.class, servletContext));
        picoContainer.addComponent(BindErrorMessageResolver.class, locateComponentClass((Object) BindErrorMessageResolver.class, (Class<?>) OgnlBindErrorMessageResolver.class, servletContext));
        picoContainer.addComponent(ControllerDataBinder.class, locateComponentClass((Object) ControllerDataBinder.class, (Class<?>) OgnlControllerDataBinder.class, servletContext));
        picoContainer.addComponent(ViewDataBinder.class, locateComponentClass((Object) ViewDataBinder.class, (Class<?>) IntrospectingViewDataBinder.class, servletContext));
        picoContainer.addComponent(StringTransmuter.class, locateComponentClass((Object) StringTransmuter.class, (Class<?>) DefaultStringTransmuter.class, servletContext));
        picoContainer.addComponent(ValueConverterFinder.class, locateComponentClass((Object) ValueConverterFinder.class, (Class<?>) OgnlValueConverterFinder.class, servletContext));
        picoContainer.addComponent(ControllerDefinitionFactory.class, locateComponentClass((Object) ControllerDefinitionFactory.class, (Class<?>) ContextControllerDefinitionFactory.class, servletContext));
        picoContainer.addComponent(ControllerNameResolver.class, locateComponentClass((Object) ControllerNameResolver.class, (Class<?>) ContextPathControllerNameResolver.class, servletContext));
        picoContainer.addComponent(MessageResources.class, locateComponentClass((Object) MessageResources.class, (Class<?>) DefaultMessageResources.class, servletContext));
        picoContainer.addComponent(ActionMonitor.class, locateComponentClass((Object) ActionMonitor.class, (Class<?>) SilentMonitor.class, servletContext));
        picoContainer.addComponent(BindMonitor.class, locateComponentClass((Object) BindMonitor.class, (Class<?>) SilentMonitor.class, servletContext));
        picoContainer.addComponent(ControllerMonitor.class, locateComponentClass((Object) ControllerMonitor.class, (Class<?>) SilentMonitor.class, servletContext));
        picoContainer.addComponent(ServletMonitor.class, locateComponentClass((Object) ServletMonitor.class, (Class<?>) SilentMonitor.class, servletContext));
        picoContainer.addComponent(ValidationMonitor.class, locateComponentClass((Object) ValidationMonitor.class, (Class<?>) SilentMonitor.class, servletContext));
        picoContainer.addComponent(ViewMonitor.class, locateComponentClass((Object) ViewMonitor.class, (Class<?>) SilentMonitor.class, servletContext));
        picoContainer.addComponent(Validator.class, locateComponentClass((Object) Validator.class, (Class<?>) DefaultValidator.class, servletContext));
        picoContainer.addComponent(ViewDispatcher.class, locateComponentClass((Object) ViewDispatcher.class, (Class<?>) DefaultViewDispatcher.class, servletContext));
        picoContainer.addComponent(ViewResolver.class, locateComponentClass((Object) ViewResolver.class, (Class<?>) DefaultViewResolver.class, servletContext));
        picoContainer.addComponent(ParameterResolver.class, locateComponentClass((Object) ParameterResolver.class, (Class<?>) ParameterResolver.class, servletContext));

        // register other components
        addOtherComponents(picoContainer, servletContext);

        parameterResolver = picoContainer.getComponent(ParameterResolver.class);

    }

    @SuppressWarnings("unchecked")
    protected void addOtherComponents(MutablePicoContainer picoContainer, ServletContext servletContext) {
        //noinspection unchecked
        Enumeration<String> enums = servletContext.getInitParameterNames();

        while (enums != null && enums.hasMoreElements()) {
            String name = enums.nextElement();

            if (name.startsWith(REGISTER_KEY)) {
                String key = name.split(REGISTER_KEY)[1];
                Class concreteClass = loadClass(servletContext.getInitParameter(name));

                picoContainer.addComponent(key, concreteClass);
            } else if (name.startsWith(REGISTER_NON_CACHING_KEY)) {
                String key = name.split(REGISTER_NON_CACHING_KEY)[1];
                Class concreteClass = loadClass(servletContext.getInitParameter(name));
                picoContainer.as(Characteristics.NO_CACHE).addComponent(key, concreteClass);
            }
        }
    }


    public void composeSession(MutablePicoContainer picoContainer) {
    }

    public void composeRequest(MutablePicoContainer picoContainer) {
    }


    /**
     * This method will locate the component Class to use.  Each of the components can be
     * overwritten by setting <code>context-param</code> in the applications <code>web.xml</code>.
     * <p/>
     * <code>
     * &lt;context-param&gt;
     * &lt;param-name&gt;org.codehaus.waffle.actions.ControllerDefinitionFactory&lt;/param-name&gt;
     * &lt;param-value&gt;org.myurl.FooBarControllerFactory&lt;/param-value&gt;
     * &lt;/context-param&gt;
     * </code>
     *
     * @param key            represents the component key which the implementation should be registered under.
     * @param defaultClass   represents the Class to use by default (when not over-written).
     * @param servletContext required to obtain the InitParameter defined for the web application.
     * @throws org.codehaus.waffle.WaffleException
     */
    protected Class<?> locateComponentClass(Object key, Class<?> defaultClass, ServletContext servletContext) throws WaffleException {
        String parameterName;
        if (key instanceof Class) {
            parameterName = ((Class<?>) key).getName();
        } else if (key instanceof String) {
            parameterName = (String) key;
        } else {
            return defaultClass;
        }

        String className = servletContext.getInitParameter(parameterName);

        if (className == null || className.length() == 0) {
            return defaultClass;
        } else {
            return loadClass(className);
        }
    }

    /**
     * Loads class for a given name
     *
     * @param className the Class name
     * @return The Class for the name
     * @throws WaffleException if class not found
     */
    private Class<?> loadClass(String className) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new WaffleException(e.getMessage(), e);
        }
    }

    protected Parameter[] picoParameters(Object... parameters) {
        if (parameters.length == 0) {
            return null; // pico expects a null when no parameters
        }

        Parameter[] picoParameters = new Parameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            picoParameters[i] = parameterResolver.resolve(parameters[i]);
        }
        return picoParameters;
    }



}
