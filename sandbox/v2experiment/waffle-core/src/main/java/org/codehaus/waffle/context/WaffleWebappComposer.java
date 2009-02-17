/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.context;

import org.picocontainer.web.WebappComposer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.codehaus.waffle.registrar.pico.ParameterResolver;
import org.codehaus.waffle.view.ViewDispatcher;
import org.codehaus.waffle.view.DefaultViewDispatcher;
import org.codehaus.waffle.view.ViewResolver;
import org.codehaus.waffle.view.DefaultViewResolver;
import org.codehaus.waffle.validation.Validator;
import org.codehaus.waffle.validation.DefaultValidator;
import org.codehaus.waffle.validation.ErrorsContext;
import org.codehaus.waffle.validation.DefaultErrorsContext;
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

/**
 * @author Michael Ward
 * @author Mauro Talevi
 * @author Paul Hammant
 */
public abstract class WaffleWebappComposer implements WebappComposer {

    private MutablePicoContainer appContainer;

    public void composeApplication(MutablePicoContainer picoContainer, ServletContext servletContext) {
        appContainer = picoContainer;

        picoContainer.addComponent(servletContext);

        // add all known components
        picoContainer.addComponent(ActionMethodExecutor.class, actionMethodExecutor());
        picoContainer.addComponent(ActionMethodResponseHandler.class, actionMethodResponseHandler());
        picoContainer.addComponent(ArgumentResolver.class, argumentResolver());
        picoContainer.addComponent(MethodDefinitionFinder.class, methodDefinitionFinder());
        picoContainer.addComponent(MethodNameResolver.class, methodNameResolver());
        picoContainer.addComponent(BindErrorMessageResolver.class, bindErrorMessageResolver());
        picoContainer.addComponent(ControllerDataBinder.class, controllerDataBinder());
        picoContainer.addComponent(ViewDataBinder.class, viewDataBinder());
        picoContainer.addComponent(StringTransmuter.class, stringTransmuter());
        picoContainer.addComponent(ValueConverterFinder.class, valueConverterFinder());
        picoContainer.addComponent(ControllerDefinitionFactory.class, controllerDefinitionFactory());
        picoContainer.addComponent(ControllerNameResolver.class, controllerNameResolver());
        picoContainer.addComponent(MessageResources.class, messageResources());
        picoContainer.addComponent(ActionMonitor.class, actionMonitor());
        picoContainer.addComponent(BindMonitor.class, bindMonitor());
        picoContainer.addComponent(ControllerMonitor.class, bindMonitor());
        picoContainer.addComponent(ServletMonitor.class, servletMonitor());
        picoContainer.addComponent(ValidationMonitor.class, validationMonitor());
        picoContainer.addComponent(ViewMonitor.class, viewMonitor());
        picoContainer.addComponent(Validator.class, validator());
        picoContainer.addComponent(ViewDispatcher.class, viewDispatcher());
        picoContainer.addComponent(ViewResolver.class, viewResolver());
        picoContainer.addComponent(ParameterResolver.class, parameterResolver());

    }

    protected Class<? extends ActionMethodExecutor> actionMethodExecutor() {
        return InterceptingActionMethodExecutor.class;
    }

    protected Class<? extends ActionMethodResponseHandler> actionMethodResponseHandler() {
        return DefaultActionMethodResponseHandler.class;
    }

    protected Class<? extends ArgumentResolver> argumentResolver() {
        return HierarchicalArgumentResolver.class;
    }

    protected Class<? extends MethodDefinitionFinder> methodDefinitionFinder() {
        return AnnotatedMethodDefinitionFinder.class;
    }

    protected Class<? extends MethodNameResolver> methodNameResolver() {
        return RequestParameterMethodNameResolver.class;
    }

    protected Class<? extends BindErrorMessageResolver> bindErrorMessageResolver() {
        return OgnlBindErrorMessageResolver.class;
    }

    protected Class<? extends ControllerDataBinder> controllerDataBinder() {
        return OgnlControllerDataBinder.class;
    }

    protected Class<? extends ViewDataBinder> viewDataBinder() {
        return IntrospectingViewDataBinder.class;
    }

    protected Class<? extends StringTransmuter> stringTransmuter() {
        return DefaultStringTransmuter.class;
    }

    protected Class<? extends ValueConverterFinder> valueConverterFinder() {
        return OgnlValueConverterFinder.class;
    }

    protected Class<? extends ControllerDefinitionFactory> controllerDefinitionFactory() {
        return ContextControllerDefinitionFactory.class;
    }

    protected Class<? extends ControllerNameResolver> controllerNameResolver() {
        return ContextPathControllerNameResolver.class;
    }

    protected Class<? extends MessageResources> messageResources() {
        return DefaultMessageResources.class;
    }

    protected Class<? extends ParameterResolver> parameterResolver() {
        return ParameterResolver.class;
    }

    protected Class<? extends ViewResolver> viewResolver() {
        return DefaultViewResolver.class;
    }

    protected Class<? extends ViewDispatcher> viewDispatcher() {
        return DefaultViewDispatcher.class;
    }

    protected Class<? extends Validator> validator() {
        return DefaultValidator.class;
    }

    protected Class<? extends ViewMonitor> viewMonitor() {
        return SilentMonitor.class;
    }

    protected Class<? extends ValidationMonitor> validationMonitor() {
        return SilentMonitor.class;
    }

    protected Class<? extends ServletMonitor> servletMonitor() {
        return SilentMonitor.class;
    }

    protected Class<? extends BindMonitor> bindMonitor() {
        return SilentMonitor.class;
    }

    protected Class<? extends ActionMonitor> actionMonitor() {
        return SilentMonitor.class;
    }


    public void composeSession(MutablePicoContainer picoContainer) {
        // nothing yet
    }

    public void composeRequest(MutablePicoContainer picoContainer) {
        picoContainer.addComponent(ErrorsContext.class, errorsContext());
    }

    protected Class<? extends ErrorsContext> errorsContext() {
        return DefaultErrorsContext.class;
    }

    protected Parameter[] picoParameters(Object... parameters) {
        if (parameters.length == 0) {
            return null; // pico expects a null when no parameters
        }

        Parameter[] picoParameters = new Parameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            picoParameters[i] = appContainer.getComponent(ParameterResolver.class).resolve(parameters[i]);
        }
        return picoParameters;
    }

}
