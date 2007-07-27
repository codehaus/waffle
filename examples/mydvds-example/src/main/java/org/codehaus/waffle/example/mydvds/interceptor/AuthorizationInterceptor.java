package org.codehaus.waffle.example.mydvds.interceptor;

import org.codehaus.waffle.action.intercept.InterceptorChain;
import org.codehaus.waffle.action.intercept.MethodInterceptor;
import org.codehaus.waffle.controller.ControllerDefinition;
import org.codehaus.waffle.example.mydvds.action.UsersController;
import org.codehaus.waffle.example.mydvds.model.Passport;
import org.codehaus.waffle.view.RedirectView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AuthorizationInterceptor implements MethodInterceptor {
    private final Set<Method> skippedMethods;

    private final Passport passport;

    public AuthorizationInterceptor(Passport passport) {
        this.passport = passport;
        this.skippedMethods = new HashSet<Method>();
        try {
            this.skippedMethods.add(UsersController.class.getMethod("login", String.class, String.class));
            this.skippedMethods.add(UsersController.class.getMethod("add", String.class, String.class, String.class));
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean accept(Method method) {
        return !this.skippedMethods.contains(method);
    }

    public Object intercept(ControllerDefinition controllerDefinition,
                            Method method,
                            InterceptorChain chain,
                            Object... arguments) throws IllegalAccessException, InvocationTargetException {
        // TODO logging
        System.out.println("Trying to authenticate ...");
        if (this.passport.isValid()) {
            System.out.println("Successful Authenticated");
            return chain.proceed(controllerDefinition, method, arguments);
        } else {
            System.out.println("Not Authenticated");
            return new RedirectView("users.waffle", controllerDefinition.getController(), new HashMap());
        }
    }

}
