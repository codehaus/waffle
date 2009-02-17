/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.ruby.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.servlet.WaffleServlet;
import org.codehaus.waffle.ruby.controller.RubyController;
import org.codehaus.waffle.controller.ScriptedController;
import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.picocontainer.MutablePicoContainer;

/**
 * ERB (rhtml) views support
 *
 * @author Michael Ward
 * @author Fabio Kung
 */
@SuppressWarnings("serial")
public class RhtmlServlet extends HttpServlet {

    private static ThreadLocal<MutablePicoContainer> currentRequestContainer = new ThreadLocal<MutablePicoContainer>();
    private static ThreadLocal<MutablePicoContainer> currentSessionContainer = new ThreadLocal<MutablePicoContainer>();
    private static ThreadLocal<MutablePicoContainer> currentAppContainer = new ThreadLocal<MutablePicoContainer>();

    public static class ServletFilter extends WaffleServlet.ServletFilter {

        protected void setAppContainer(MutablePicoContainer container) {
            currentAppContainer.set(container);
            super.setAppContainer(container);
        }

        protected void setRequestContainer(MutablePicoContainer container) {
            currentRequestContainer.set(container);
            super.setRequestContainer(container);
        }

        protected void setSessionContainer(MutablePicoContainer container) {
            currentSessionContainer.set(container);
            super.setSessionContainer(container);

        }
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String template = loadRhtml(request.getServletPath());

        Ruby runtime = currentRequestContainer.get().getComponent(Ruby.class);
        RubyModule module = runtime.getClassFromPath("ERB");

        IRubyObject erb = (IRubyObject) JavaEmbedUtils.invokeMethod(runtime, module, "new",
                new Object[] {JavaEmbedUtils.javaToRuby(runtime, template)}, IRubyObject.class); // cache?

        // TODO: Test with a non-ruby controller
        Object controller = extractController(request);

        IRubyObject binding = (IRubyObject) JavaEmbedUtils.invokeMethod(runtime, controller, "send",
                new Object[]{runtime.newSymbol("binding")}, IRubyObject.class);

        String out = (String) JavaEmbedUtils.invokeMethod(runtime, erb, "result",
                new Object[] {binding}, String.class);

        response.getOutputStream().println(out);
        response.getOutputStream().flush();
    }

    private Object extractController(HttpServletRequest request) {
        Object controller = request.getAttribute(Constants.CONTROLLER_KEY);
        if(controller instanceof RubyController) {
            controller = ((ScriptedController) controller).getScriptObject();
        }
        return controller;
    }

    private String loadRhtml(String resource) {
        log("Loading...." + resource);
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;

        try {
            inputStream = getServletContext().getResourceAsStream(resource);
            if ( inputStream == null ){
                throw new IOException("Failed to find resource "+resource+" in servlet context");
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder script = new StringBuilder();
            String line = bufferedReader.readLine();

            while (line != null) {
                script.append(line).append("\n");
                line = bufferedReader.readLine();
            }

            return script.toString();
        } catch (IOException e) {
            throw new WaffleException(e);
        } finally {
            try {
                if(inputStream != null) inputStream.close();
                if(bufferedReader != null) bufferedReader.close();
            } catch (IOException ignore) {
                // ignore
            }
        }
    }
}
