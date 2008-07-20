/*
 * Copyright (c) terms as published in http://waffle.codehaus.org/license.html
 */
package org.codehaus.waffle.servlet;

import org.codehaus.waffle.Constants;
import org.codehaus.waffle.WaffleException;
import org.codehaus.waffle.context.RequestLevelContainer;
import org.codehaus.waffle.controller.RubyController;
import org.codehaus.waffle.controller.ScriptedController;
import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ERB (rhtml) views support
 *
 * @author Michael Ward
 * @author Fabio Kung
 */
@SuppressWarnings("serial")
public class RhtmlServlet extends HttpServlet {
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String template = loadRhtml(request.getServletPath());

        Ruby runtime = RequestLevelContainer.get().getComponent(Ruby.class);
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

    private String loadRhtml(String fileName) {
        log("Loading...." + fileName);
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;

        try {
            inputStream = getServletContext().getResourceAsStream(fileName);
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
